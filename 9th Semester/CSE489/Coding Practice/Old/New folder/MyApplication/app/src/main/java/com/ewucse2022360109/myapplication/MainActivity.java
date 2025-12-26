package com.ewucse2022360109.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPhoneHome, etPhoneOffice;
    private Button btSave, btCancel;
    private ImageView userImage;
    private static final int GALLERY_REQUEST_CODE = 100;
    private String photoBase64 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhoneHome = findViewById(R.id.etPhoneHome);
        etPhoneOffice = findViewById(R.id.etPhoneOffice);
        btSave = findViewById(R.id.btSave);
        btCancel = findViewById(R.id.btCancel);
        userImage = findViewById(R.id.userImage);


        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String phoneHome = etPhoneHome.getText().toString().trim();
                String phoneOffice = etPhoneOffice.getText().toString().trim();

                if (name.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Fill Up Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (name.length() < 5) {
                    Toast.makeText(MainActivity.this, "Name must have at least 5 letters", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (int i = 0; i < name.length(); i++) {
                    char c = name.charAt(i);
                    if (!Character.isLetter(c) && c != ' ') {
                        Toast.makeText(MainActivity.this, "Invalid Name (only letters allowed)", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                if (!isValidEmailAddress(email)) {
                    Toast.makeText(MainActivity.this, "Invalid Email Address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (phoneHome.isEmpty() || phoneOffice.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Fill Up Phone Numbers", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (int i = 0; i < phoneHome.length(); i++) {
                    if (!Character.isDigit(phoneHome.charAt(i))) {
                        Toast.makeText(MainActivity.this, "Home Phone contains invalid character", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                for (int i = 0; i < phoneOffice.length(); i++) {
                    if (!Character.isDigit(phoneOffice.charAt(i))) {
                        Toast.makeText(MainActivity.this, "Office Phone contains invalid character", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                if (phoneHome.length() != 11 || phoneOffice.length() != 11) {
                    Toast.makeText(MainActivity.this, "Phone Number must be 11 digits", Toast.LENGTH_SHORT).show();
                    return;
                }

                // FIXED 1: Added validation to ensure a photo is selected.
                if (photoBase64 == null) {
                    Toast.makeText(MainActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
                    return;
                }

                MyDatabase dbHelper = new MyDatabase(MainActivity.this);
                dbHelper.insertForm(name, email, phoneHome, phoneOffice, photoBase64);
                Toast.makeText(MainActivity.this, "Contact saved successfully!", Toast.LENGTH_SHORT).show();
                showSavedContact();
            }
        });

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, GALLERY_REQUEST_CODE);
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Form fillup is incomplete (Cancel is clicked)", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        showSavedContact();
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern p = Pattern.compile(ePattern);
        Matcher m = p.matcher(email);
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
                userImage.setImageBitmap(bitmap);
                photoBase64 = encodeImage(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Image not found", Toast.LENGTH_SHORT).show();
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

    private void showSavedContact() {
        MyDatabase dbHelper = new MyDatabase(this);
        Cursor cursor = dbHelper.showcontact();

        if (cursor != null && cursor.moveToFirst()) {
            try {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                String phoneHome = cursor.getString(cursor.getColumnIndexOrThrow("phone_home"));
                String phoneOffice = cursor.getString(cursor.getColumnIndexOrThrow("phone_office"));
                String col_userimage = cursor.getString(cursor.getColumnIndexOrThrow("userimage"));

                etName.setText(name);
                etEmail.setText(email);
                etPhoneHome.setText(phoneHome);
                etPhoneOffice.setText(phoneOffice);

                // FIXED 2: Check if the image string from the database is valid before decoding.
                if (col_userimage != null) {
                    userImage.setImageBitmap(decodeImage(col_userimage));
                    photoBase64 = col_userimage; // Also update the current photoBase64 variable
                }

            } finally {
                cursor.close();
            }
        }
    }
}