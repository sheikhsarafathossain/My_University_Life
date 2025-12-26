package com.example.lab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class activity_login extends AppCompatActivity {
    private Button btnSignup, btnExit, btnGo,btnReset; // create button  login,
    private EditText etemail,etpassword;

    private CheckBox edRemLogin, edRemUser;
    private TextView tvError;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);





        // 1. create java references

        etemail = findViewById(R.id.emailidlogin);
        etpassword = findViewById(R.id.etpassword);
        edRemLogin = findViewById(R.id.edRemLogin);
        edRemUser = findViewById(R.id.edRemUser);
        tvError = findViewById(R.id.tvError);

        // Login button
        btnSignup = findViewById(R.id.btnSignup);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Signup Button has been pressed");
                Intent intent = new Intent(activity_login.this, activity_signup.class);
                startActivity(intent);

                finishAffinity();
            }
        });






        //exit button
        btnExit = findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Exit Button has been pressed");
                finish();
            }
        });

        //go button

        btnGo = findViewById(R.id.btnGo);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Go Button has been pressed");
                getFiledValues();
            }
        });

        //reset button

        btnReset =findViewById(R.id.btnReset);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Reset Button has been pressed");
            }
        });


    }


    private void getFiledValues(){
        String email = etemail.getText().toString().trim();
        String pass = etpassword.getText().toString().trim();


        if(!isValidEmailAddress(email)){
            //show error message
            tvError.setText("Email error!");
            return;
        }

        if(pass.length() < 4){
            //error
            tvError.setText("Password error!");
            return ;
        }


        boolean shouldRememberUser = edRemLogin.isChecked();
        boolean shouldRememberLogin = edRemUser.isChecked();


        System.out.println("Email: "+email);
        System.out.println("Password: "+pass);
        System.out.println("Remember User: "+shouldRememberUser);
        System.out.println("Remember Login: "+shouldRememberLogin);


    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }




}