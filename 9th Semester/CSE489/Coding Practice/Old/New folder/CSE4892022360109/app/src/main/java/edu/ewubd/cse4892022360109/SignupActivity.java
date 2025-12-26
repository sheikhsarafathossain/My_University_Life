package edu.ewubd.cse4892022360109;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {
    private Button btnLogin, btnExit, btnGo;
    private EditText etName, etEmail, etPass, etRePass;
    private CheckBox cbRemLogin, cbRemUser;
    private TextView tvError;

    // declare the shared preference here
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // open shared preference file here
        sp = this.getSharedPreferences("my_pr", MODE_PRIVATE);
        // check if profile already exists
        String userId = sp.getString("_USER_ID_", "");
        // if exists, then
        if(!userId.isEmpty()) {
            boolean isLoginRemembered = sp.getBoolean("_LOGIN_REMEM_", false);
            // check if login is remembered
            if(isLoginRemembered){
                // Move to home page from here
                Intent i = new Intent(this, MainActivity.class);
                i.putExtra("_USER_ID_", userId);
                startActivity(i);
                finishAffinity();
            }
            // Otherwise, Move to login page from here
            else {
                // get if user id is remembered
                // get password
                // send user id & password to login page
                boolean isUserRemembered = sp.getBoolean("_USER_REMEM_", false);
                String pass = sp.getString("_PASS_", "");
                Intent i = new Intent(this, LoginActivity.class);
                i.putExtra("_USER_ID_", userId);
                i.putExtra("_USER_REMEM_", isUserRemembered);
                i.putExtra("_PASS_", pass);
                startActivity(i);
                finishAffinity();
            }
            return;
        }
        setContentView(R.layout.activity_signup);


        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        etRePass = findViewById(R.id.etRePass);
        cbRemLogin = findViewById(R.id.cbRemLogin);
        cbRemUser = findViewById(R.id.cbRemUser);
        tvError = findViewById(R.id.tvError);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Login Button has been pressed");
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });
        btnExit = findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Exit Button has been pressed");
                finish();
            }
        });
        btnGo = findViewById(R.id.btnGo);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Go Button has been pressed");
                getFiledValues();
            }
        });
    }
    private void getFiledValues(){
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String pass = etPass.getText().toString().trim();
        String rePass = etRePass.getText().toString().trim();
        if(name.length() < 5){
            // show error message
            Toast.makeText(this, "Invalid name", Toast.LENGTH_SHORT).show();
            tvError.setText("User name must have at least 5 characters.");
            return;
        }
        if(!isValidEmailAddress(email)){
            // show error message
            Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show();
            tvError.setText("Invalid Email address");
            return;
        }
        if(pass.length() < 4){
            // show error message
            Toast.makeText(this, "Invalid Password", Toast.LENGTH_SHORT).show();
            tvError.setText("Password must have at least 4 digits.");
            return;
        }
        if(!pass.equals(rePass)){
            // show error message
            Toast.makeText(this, "Invalid Re-Password", Toast.LENGTH_SHORT).show();
            tvError.setText("Re-Password didn't match.");
            return;
        }
        boolean shouldRememberUser = cbRemUser.isChecked();
        boolean shouldRememberLogin = cbRemLogin.isChecked();

        System.out.println("Name: "+name);
        System.out.println("Email: "+email);
        System.out.println("Pass: "+pass);
        System.out.println("Remember User: "+shouldRememberUser);
        System.out.println("Remember Login: "+shouldRememberLogin);

        // Store all information in shared preference here
        SharedPreferences.Editor prEdit = sp.edit();
        prEdit.putString("_USER_ID_", email);
        prEdit.putString("_PASS_", pass);
        prEdit.putBoolean("_LOGIN_REMEM_", shouldRememberLogin);
        prEdit.putBoolean("_USER_REMEM_", shouldRememberUser);
        prEdit.apply();
        // Move to home page
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("_USER_ID_", email);
        startActivity(i);
        finishAffinity();
    }
    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
}