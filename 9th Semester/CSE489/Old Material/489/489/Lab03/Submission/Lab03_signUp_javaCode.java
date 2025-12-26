package com.ewubd.Cse489_1_ShoebKhandaker_2022_3_60_125;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignUp extends AppCompatActivity {

    private EditText etName, etMail, etPhone, etPass,etConPass, RememberLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        etName = findViewById(R.id.etName);
        etMail = findViewById(R.id.etMail);
        etPhone = findViewById(R.id.etPhone);
        etPass = findViewById(R.id.etPass);
        etConPass = findViewById(R.id.etConPass);
        etName = findViewById(R.id.etName);





        Button btnHaveAccount = findViewById(R.id.btnHaveAccount);
        Button btnExit = findViewById(R.id.btnExit);
        Button btnSingup = findViewById(R.id.btnSignup);

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("btnHaveAccount not implemented yet");
            }
        });

        btnSingup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("btnSingup topped");

                String name = etName.getText().toString();
                String Email = etMail.getText().toString();
                String Phone = etPhone.getText().toString();
                String pass = etPass.getText().toString();
                String conPass = etConPass.getText().toString();

                System.out.println("name" + name);
                System.out.println("pass" + Email);
                System.out.println("Phone" + Phone);
                System.out.println("pass" + pass);
                System.out.println("conPass" + conPass);

            }
        });
    }

}