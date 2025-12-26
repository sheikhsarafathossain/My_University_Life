package com.ewucse2022360109.labexampractice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Login extends AppCompatActivity {
    SharedPreferences sp;
    EditText etEmail, etPass;
    Button btnGo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        btnGo = findViewById(R.id.btnGo);


        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVerify();
            }
        });






    }

    private void getVerify(){
        String email, pass;
        email = etEmail.getText().toString().trim();
        pass = etPass.getText().toString().trim();

        sp = this.getSharedPreferences("my_pr", MODE_PRIVATE);

        String sp_name = sp.getString("name","");
        String sp_email = sp.getString("email","");
        String sp_pass = sp.getString("pass","");

        if(email.equals(sp_email) && pass.equals(sp_pass)){
            Toast.makeText(this,"Login Successful",Toast.LENGTH_LONG).show();
            Intent i = new Intent(Login.this, MainActivity.class);
            startActivity(i);
            finishAffinity();
        }
        else{
            Toast.makeText(this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
        }
    }
}