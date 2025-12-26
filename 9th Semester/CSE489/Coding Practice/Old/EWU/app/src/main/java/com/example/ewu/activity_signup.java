package com.example.ewu;

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

    private Button btnLogin, btnExit , btnGo;
    private EditText etName , etEmail , etPass , etRePass ;
    private CheckBox cbRemUser, cbRemLogin ;
    private TextView tvError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

//       create Java references
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        etRePass = findViewById(R.id.etRePass);
        cbRemUser = findViewById(R.id.cbRemUser);
        cbRemLogin = findViewById(R.id.cbRemLogin);
        tvError = findViewById(R.id.tvError);



        btnLogin = findViewById(R.id.btnlogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Login Button has been pressed");
                Intent intent = new Intent(activity_signup.this, activity_login.class);
                startActivity(intent);
                finishAffinity();

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

        btnGo = findViewById(R.id.btnGo);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Go Button has been Pressed");
                getFiledValues();
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
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String pass = etPass.getText().toString().trim();
        String rePass = etRePass.getText().toString().trim();



        if (name.length() < 5) {
//              show error message
            tvError.setText("Name Error !");
            return;
        }


        if (!isValidEmailAddress(email)) {
//                show error message

            tvError.setText("Email error! ");
            return;
        }


        if (pass.length() < 4) {
            Toast.makeText(this, "Password is too short", Toast.LENGTH_SHORT).show();
//                show error message
            tvError.setText("Password error! ");
            return;
        }


        if (!pass.equals(rePass)) {
            Toast.makeText(this, "Password Error", Toast.LENGTH_SHORT).show();
//                show error message
            tvError.setText("Password did not matched! ");
            return;
        }



        boolean shouldRememberUser = cbRemUser.isChecked();
        boolean shouldRememberLogin = cbRemLogin.isChecked();

        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Pass: " + pass);
        System.out.println("Remember User: " + shouldRememberUser);
        System.out.println("Remember Login: " + shouldRememberLogin);

//        store all information in storage

//

    }
}