package com.ewucse2022360109.labexampractice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignUp extends AppCompatActivity {
    Button btnExit, btnGo, btnLogin;
    EditText etName, etEmail,etPass, etRePass;
    CheckBox ckRemLogin, ckRemUser;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        sp = getSharedPreferences("my_pr", MODE_PRIVATE);

        btnExit = findViewById(R.id.btnExit);
        btnGo = findViewById(R.id.btnGo);
        btnLogin = findViewById(R.id.btnLogin);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        etRePass = findViewById(R.id.etRePass);
        ckRemLogin = findViewById(R.id.ckRemLogin);
        ckRemUser = findViewById(R.id.ckRemUser);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Login Pressed");
                Intent i = new Intent(SignUp.this,Login.class);
                startActivity(i);
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validation();
                System.out.println("hello world");
                Toast.makeText(SignUp.this, "Shared preferences insert Successfully", Toast.LENGTH_SHORT).show();
            }
        });








    }

    private void validation(){
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String pass = etPass.getText().toString().trim();

        SharedPreferences.Editor prEditor = sp.edit();

        prEditor.putString("username",name);
        prEditor.putString("email",email);
        prEditor.putString("pass",pass);
        prEditor.apply();
        System.out.println(name);
        System.out.println(email);
        System.out.println(pass);
    }
}