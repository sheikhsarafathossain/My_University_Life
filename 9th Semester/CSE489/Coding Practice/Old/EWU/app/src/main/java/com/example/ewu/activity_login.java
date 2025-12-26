package com.example.ewu;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class activity_login extends AppCompatActivity {
    private Button btnSignup , btnReset , btnGo , btnExit;
    private EditText  etEmail , etPass  ;
    private CheckBox cbRemUser, cbRemLogin ;
    private TextView tvError;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //       create Java references
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        cbRemUser = findViewById(R.id.cbRemUser);
        cbRemLogin = findViewById(R.id.cbRemLogin);
        tvError = findViewById(R.id.tvError);

        btnSignup = findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("SignUp Button has been pressed");
                Intent intent = new Intent(activity_login.this ,activity_signup.class);
                startActivity(intent);
                finishAffinity();
            }

        });

        btnReset = findViewById(R.id.btnReset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Reset Button has been pressed");
            }
        });


        btnGo = findViewById(R.id.btnGo);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Go Button has been pressed");
                getFiledValues();
            }
        });


        btnExit = findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Exit Button has been Pressed");
                finish();
            }
        });

    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();

    }
    private void getFiledValues() {

        String email = etEmail.getText().toString().trim();
        String pass = etPass.getText().toString().trim();


        if (!isValidEmailAddress(email)) {
//                show error message

            tvError.setText("invalid email!");
            return;
        }


        if (pass.length() < 4) {
//                show error message

            tvError.setText("invalid password!");
            return;
        }

        boolean shouldRememberUser = cbRemUser.isChecked();
        boolean shouldRememberLogin = cbRemLogin.isChecked();

        System.out.println("Email: " + email);
        System.out.println("Pass: " + pass);
        System.out.println("Remember User: " + shouldRememberUser);
        System.out.println("Remember Login: " + shouldRememberLogin);

    }
}