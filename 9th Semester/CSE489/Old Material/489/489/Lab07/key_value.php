<?php
function connect_db(){
    global $dbc;
    
    $mysql_hostname = "localhost";
    $mysql_user = "_DB_USER_NAME_";
    $mysql_password = "_DB_PASSWORD_";
    $mysql_database = "_DB_NAME_";

    $dbc = mysqli_connect($mysql_hostname, $mysql_user, $mysql_password, $mysql_database) or die("Opps some thing went wrong 1");
    return $dbc;
}

#print_r($_REQUEST);

# action, student id, and semester must be set
if(isset($_REQUEST['action'])&&isset($_REQUEST['sid'])&&isset($_REQUEST['semester'])){
 
    $dbc = connect_db();    
    
    # remove all white spaces from both sides
    $action = trim($_REQUEST['action']);
    $std_id = trim($_REQUEST['sid']);
    $semester = trim($_REQUEST['semester']);
    

    # remove all special characters
    $action = mysqli_real_escape_string($dbc, $action);
    $std_id = mysqli_real_escape_string($dbc, $std_id);
    $semester = mysqli_real_escape_string($dbc, $semester);

    # remove all characters except numbers
    $std_id = preg_replace('/[^0-9]/', '', $std_id);     # correct 2019360123
    $semester = preg_replace('/[^0-9]/', '', $semester); # correct 20222

    $response = array("msg" => "Something went wrong");


    if(strlen($std_id)==10 && strlen($semester)==5){
        
        $table_name = "univ_key_values";

        if($action=="backup"){
            $key = trim($_REQUEST['key']);
            $value = trim($_REQUEST['value']);

            if(strlen($key)>=4 && strlen($value)>=4){
                $key = mysqli_real_escape_string($dbc, $key);
                $value = mysqli_real_escape_string($dbc, $value);
    
                $sql = "SELECT key_name FROM $table_name WHERE sid='$std_id' AND semester='$semester' AND key_name='$key'";
                $r = mysqli_query($dbc, $sql);
                if($r && mysqli_num_rows($r)==0){
                    $sql = "INSERT INTO $table_name (sid, semester, key_name, val) 
                            VALUES('$std_id', '$semester', '$key', '$value')";
                    $r = mysqli_query($dbc, $sql) or die(mysqli_error($dbc));
                    $response["msg"] = "New key-value created successfully";
                } else {
                    $sql = "UPDATE $table_name SET val='$value' WHERE sid='$std_id' AND semester='$semester' AND key_name='$key'";
                    if(mysqli_query($dbc, $sql)) {
                        $response["msg"] = "Key-value updated successfully";
                    } else {
                        $error = mysqli_error($dbc);
                        $response["msg"] = "Error: " . $sql . "<br>$error";
                    }
                }
            }
        }
        else if($action=="restore"){
            $sql = "SELECT * FROM $table_name WHERE sid='$std_id' AND semester='$semester'";
            $r = mysqli_query($dbc, $sql);
            if($r && mysqli_num_rows($r)>0){
                $classes = array();
                while(($row=mysqli_fetch_assoc($r))!=0){
                    $classes[] = array(
                        'key' => $row['key_name'],
                        'value' => $row['val']
                    );
                }
                $response = array("msg" => "OK", "key-value"=>$classes);
            }
        }
        else if($action=="remove"){
            $key = trim($_REQUEST['key']);
            $key = mysqli_real_escape_string($dbc, $key);
            $sql = "DELETE FROM $table_name WHERE sid='$std_id' AND semester='$semester' AND key_name='$key'";
            $r = mysqli_query($dbc, $sql);
            if($r){
                $response = array("msg" => "OK");
            }
        }
    }
    $json = json_encode($response,JSON_HEX_TAG | JSON_HEX_APOS | JSON_HEX_QUOT | JSON_HEX_AMP | JSON_UNESCAPED_UNICODE);
    echo $json;
} else {
    echo "You are not allowed to access.";
}

?>