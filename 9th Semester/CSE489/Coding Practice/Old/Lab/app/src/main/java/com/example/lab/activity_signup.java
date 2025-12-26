package com.example.lab;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class activity_signup extends AppCompatActivity {

    private Button btnLogin, btnExit, btnGo; // create button  login,
    private EditText etName, etEmail,etPass,etRePass;
    private CheckBox edRemLogin, edRemUser;

    private TextView tvError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // 1. create java references
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etemail);
        etPass = findViewById(R.id.etpassword);
        etRePass = findViewById(R.id.etrepass);
        edRemLogin = findViewById(R.id.edRemLogin);
        edRemUser = findViewById(R.id.edRemUser);
        tvError = findViewById(R.id.tvError);



        // Login button
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Login Button has been pressed");

                Intent intent = new Intent(activity_signup.this, activity_login.class);
                startActivity(intent);

                finishAffinity(); // to remove everything from the stack of memory

            }
        });
        //exit button
        btnExit = findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Exit Button has been pressed");
                //close app
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

    }
    private void getFiledValues(){
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String pass = etPass.getText().toString().trim();
        String rePass = etRePass.getText().toString().trim();

        if(name.length() < 5){
            //show error message
            Toast.makeText(this, "Invalid Name", Toast.LENGTH_SHORT).show();
            tvError.setText("Name error!");

            return;
        }

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

        if(!pass.equals(rePass)){
            //error
            tvError.setText("Password not matched error!");
            return ;
        }

        boolean shouldRememberUser = edRemLogin.isChecked();
        boolean shouldRememberLogin = edRemUser.isChecked();


        System.out.println("Name: "+name);
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



