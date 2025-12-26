package edu.ewubd.cse4892022360109;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.message.BasicNameValuePair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText etDate;
    private Button btnExit, btnReport, btnSave;
    private ListView lvAttendance;
    private CustomListAdapter cla;
    private String[] students = {"Student 1", "Student 2", "Student 3", "Student 4", "Student 5"};
    private ArrayList<StudentAttendance> stdAttendances = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etDate = findViewById(R.id.etDate);
        btnExit = findViewById(R.id.btnExit);
        btnReport = findViewById(R.id.btnReport);
        btnSave = findViewById(R.id.btnSave);
        lvAttendance = findViewById(R.id.lvAttendance);
        for(String s: students){
            stdAttendances.add(new StudentAttendance(s, false, ""));
        }
        cla = new CustomListAdapter(this, stdAttendances);
        lvAttendance.setAdapter(cla);

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
            }
        });
        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Write code to show report activity
                String[] keys = {"action", "sid", "semester"};
                        String[] values = {"restore", "2022-3-60-109", "2025-2"};
                        httpRequest(keys, values);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAttendance();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();


    }


    private void saveAttendance(){
        // write code to get info of attendance and save


        String _date = etDate.getText().toString().trim();
        String course = "CSE489-01";
        long dateTime = convertToDatetime(_date);
        if(dateTime > 0){
            JSONArray json = new JSONArray();
            AttendanceDB adb = new AttendanceDB(this);
            for(int i=0; i<stdAttendances.size(); i++){
                StudentAttendance sa = cla.getItem(i);
                View row = lvAttendance.getChildAt(i);
                EditText etRemarks = row.findViewById(R.id.etRemarks);
                sa.remarks = etRemarks.getText().toString();
                try{
                    adb.insert(sa, dateTime, course);
                } catch (Exception e){
                    adb.update(sa, dateTime, course);
                }
                json.put(course+";"+sa.toString());
            }
            adb.close();
            String uniqueKey = "2022-3-60-109"+dateTime;
            String rowValue = json.toString();
            String keys[] = {"action", "sid", "semester", "key", "value"};
            String values[] = {"backup", "2022-3-60-109", "2025-2", uniqueKey, rowValue};
            httpRequest(keys, values);

            Toast.makeText(this, "Attendance has been stored successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Invalid date format", Toast.LENGTH_SHORT).show();
        }
    }
    private long convertToDatetime(String date){



        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d = sdf.parse(date);
            return d.getTime();
        }catch (Exception e){}
        return -1;
    }


    private void httpRequest(final String keys[], final String values[]) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                List<NameValuePair> params = new ArrayList<>();
                for (int i = 0; i < keys.length; i++) {
                    params.add(new BasicNameValuePair(keys[i], values[i]));
                }
                String url = "https://www.muthosoft.com/univ/cse489/key_value.php";
                try {
                    String data = RemoteAccess.getInstance().makeHttpRequest(url, "POST", params);
                    return data;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String data) {
                System.out.println(data);
                if (data != null) {
                    try {
                        JSONObject jObj = new JSONObject(data);
                        if(jObj.has("msg")){
                            String msg = jObj.getString("msg");
                            if(msg.equals("OK")){
                                if(jObj.has("key-value")){
                                    JSONArray keyValues = jObj.getJSONArray("key-value");
                                    String _date = etDate.getText().toString().trim();
                                    long dateTime = convertToDatetime(_date);
                                    String myStudentid = "2022-3-60-109";
                                    String uniqueKey = myStudentid+dateTime;
                                    System.out.println(uniqueKey);
                                    for (int i=0;i< keyValues.length();i++){
                                        JSONObject keyValue = keyValues.getJSONObject(i);
                                        String key = keyValue.getString("key");
                                        String value = keyValue.getString("value");
                                        if(key.equals(uniqueKey)){

                                            JSONArray attendanceInfo = new JSONArray(value);


                                            stdAttendances.clear();
                                            for(int j=0;j<attendanceInfo.length();j++){
                                                System.out.println(attendanceInfo.getString(j));

                                                String[] stdAttInfo = attendanceInfo.getString(j).split(";");
                                                System.out.println(stdAttInfo.length);
                                                String course = stdAttInfo[0];
                                                String name = stdAttInfo[1];
                                                boolean status = Boolean.parseBoolean(stdAttInfo[2]);
                                                String remarks = "";
                                                if(stdAttInfo.length>3)
                                                    remarks = stdAttInfo[3];
                                                StudentAttendance sa = new StudentAttendance(name, status, remarks);
                                                stdAttendances.add(sa);
                                            }
                                            System.out.println(stdAttendances.size());
                                            cla.notifyDataSetChanged();

                                            break;
                                        }
                                    }
                                }
                            }else{
                                Toast.makeText(getApplicationContext(), msg,Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(),"Something went wrong", Toast.LENGTH_SHORT).show();

                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }.execute();
    }






}