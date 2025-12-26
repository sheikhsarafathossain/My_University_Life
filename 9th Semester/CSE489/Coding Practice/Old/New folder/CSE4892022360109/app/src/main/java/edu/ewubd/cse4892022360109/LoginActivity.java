package edu.ewubd.cse4892022360109;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private Button btnSignup, btnExit, btnGo;
    private EditText etEmail, etPass;
    private CheckBox cbRemLogin, cbRemUser;
    private TextView tvError;
    private String existingUserId = "", existingPass = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        cbRemLogin = findViewById(R.id.cbRemLogin);
        cbRemUser = findViewById(R.id.cbRemUser);
        tvError = findViewById(R.id.tvError);

        Intent i = this.getIntent();
        if(i != null){
            if(i.hasExtra("_USER_ID_")){
                existingUserId = i.getStringExtra("_USER_ID_");
            }
            if(i.hasExtra("_PASS_")){
                existingPass = i.getStringExtra("_PASS_");
            }
            if(i.hasExtra("_USER_REMEM_")){
                boolean isUserRemembered = i.getBooleanExtra("_USER_REMEM_", false);
                if(isUserRemembered) {
                    etEmail.setText(existingUserId);
                    cbRemUser.setChecked(true);
                }
            }
        }
        btnSignup = findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Login Button has been pressed");
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
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
        String email = etEmail.getText().toString().trim();
        String pass = etPass.getText().toString().trim();
        if(!isValidEmailAddress(email)){
            // show error message
            tvError.setText("Invalid Email address");
            return;
        }
        if(pass.length() < 4){
            // show error message
            tvError.setText("Password must have at least 4 digits.");
            return;
        }
        boolean shouldRememberUser = cbRemUser.isChecked();
        boolean shouldRememberLogin = cbRemLogin.isChecked();

        System.out.println("Email: "+email);
        System.out.println("Pass: "+pass);
        System.out.println("Remember User: "+shouldRememberUser);
        System.out.println("Remember Login: "+shouldRememberLogin);
        if(!email.equals(existingUserId)){
            Toast.makeText(this, "User id didn't match", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!pass.equals(existingPass)){
            Toast.makeText(this, "Password didn't match", Toast.LENGTH_SHORT).show();
            return;
        }
        // Declare & initiate shared preference here
        SharedPreferences sp = this.getSharedPreferences("my_pr", MODE_PRIVATE);
        SharedPreferences.Editor spEdit = sp.edit();
        // then, store the user&/remember login information here
        spEdit.putBoolean("_LOGIN_REMEM_", shouldRememberLogin);
        spEdit.putBoolean("_USER_REMEM_", shouldRememberUser);
        spEdit.apply();
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