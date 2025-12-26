package com.ewucse2022360109.contactform;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import android.database.Cursor;

import android.net.Uri;
import android.util.Base64;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    Button btnCancel, btnSave;
    TextView tvName, tvEmail, tvPhoneHome, tvPhoneOffice;
    EditText etName, etEmail, etPhoneHome, etPhoneOffice;
    ImageView ivPhoto;
    private static final int GALLERY_REQUEST_CODE = 100;
    private String photoBase64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhoneHome = findViewById(R.id.etPhoneHome);
        etPhoneOffice = findViewById(R.id.etPhoneOffice);
        ivPhoto = findViewById(R.id.ivPhoto);
        btnCancel = findViewById(R.id.btnCancel);
        btnSave = findViewById(R.id.btnSave);

        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhoneHome = findViewById(R.id.tvPhoneHome);
        tvPhoneOffice = findViewById(R.id.tvPhoneOffice);

        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, GALLERY_REQUEST_CODE);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        showSavedContact();
    }

    private void validation(){
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phoneHome = etPhoneHome.getText().toString().trim();
        String phoneOffice = etPhoneOffice.getText().toString().trim();
        String photo = photoBase64;

        if(name.length()<5){
            Toast.makeText(this,"Invalid Name",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!isValidEmailAddress(email)){
            Toast.makeText(this,"Invalid Email",Toast.LENGTH_SHORT).show();
            return;
        }
        if(phoneHome.length()<11){
            Toast.makeText(this,"Invalid Phone Number Home", Toast.LENGTH_SHORT).show();
            return;
        }
        if(phoneOffice.length()<11){
            Toast.makeText(this,"Invalid Phone Number Office", Toast.LENGTH_SHORT).show();
            return;
        }
        if(photo == null){
            Toast.makeText(this, "Invalid Photo", Toast.LENGTH_SHORT).show();
            return;
        }

        Database dbHelper = new Database(this);
        dbHelper.insertContact(name, email, phoneHome, phoneOffice, photo);

        Toast.makeText(this, "Contact Saved Successfully!", Toast.LENGTH_LONG).show();
        showSavedContact();
    }

    private void showSavedContact() {
        Database dbHelper = new Database(this);
        Cursor cursor = dbHelper.getContact();

        if (cursor != null && cursor.moveToFirst()) {
            try {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                String phoneHome = cursor.getString(cursor.getColumnIndexOrThrow("phone_home"));
                String phoneOffice = cursor.getString(cursor.getColumnIndexOrThrow("phone_office"));
                String photo = cursor.getString(cursor.getColumnIndexOrThrow("photo"));

                etName.setText(name);
                etEmail.setText(email);
                etPhoneHome.setText(phoneHome);
                etPhoneOffice.setText(phoneOffice);

                if (photo != null) {
                    ivPhoto.setImageBitmap(decodeImage(photo));
                }
            } finally {
                cursor.close();
            }
        }
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(selectedImage);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                ivPhoto.setImageBitmap(bitmap);
                photoBase64 = encodeImage(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private String encodeImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private Bitmap decodeImage(String base64String) {
        byte[] imageBytes = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }
}
