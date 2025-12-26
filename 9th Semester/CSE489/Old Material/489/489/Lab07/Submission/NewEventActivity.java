package com.ewubd.Cse489_1_ShoebKhandaker_2022_3_60_125;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.message.BasicNameValuePair;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewEventActivity extends AppCompatActivity {
    private String eventID = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        EditText etTitle = findViewById(R.id.etTitle);
        EditText etVenue = findViewById(R.id.etVenue);
        EditText etDateTime = findViewById(R.id.etDateTime);
        EditText etNumParticipants = findViewById(R.id.etNumParticipants);
        EditText etDescription = findViewById(R.id.etDescription);
        RadioButton rbOnline = findViewById(R.id.rbOnline);
        RadioButton rbOffline = findViewById(R.id.rbOffline);

        Button btnCancel = findViewById(R.id.btnCancel);
        Button btnSave = findViewById(R.id.btnSave);

        Intent i = this.getIntent();

        if(i != null && i.hasExtra("eventId")){
            eventID = i.getStringExtra("eventId");
            String title = i.getStringExtra("title");
            String venue = i.getStringExtra("venue");
            long datetime = i.getLongExtra("datetime", 0);
            int numParticipants = i.getIntExtra("numParticipants", 0);
            String description = i.getStringExtra("description");

            etTitle.setText(title);
            etVenue.setText(venue);
            try {
                etDateTime.setText(Util.getInstance().convertMillisecondsToDate(datetime, "dd-MM-yyyy HH:mm aaa"));
            }catch (Exception e){
                e.printStackTrace();
            }
            etNumParticipants.setText("" + numParticipants); // as the value of numParticipants an int, need to convert it into string !!!
            etDescription.setText(description);
        }


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("btnLogin tapped");
                String title = etTitle.getText().toString().trim();
                String venue = etVenue.getText().toString().trim();
                String dt = etDateTime.getText().toString();
                String np = etNumParticipants.getText().toString();
                String description = etDescription.getText().toString();

                int numParticipants = 0;

                try {
                    numParticipants = Integer.parseInt(np);
                }
                catch (Exception e){}


                if(title.length() < 8){
                    Toast.makeText(NewEventActivity.this, "Title must have 8 cletters", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(venue.length() < 8){
                    Toast.makeText(NewEventActivity.this, "Title must have 8 cletters", Toast.LENGTH_SHORT).show();
                    return;
                }
                // datetime needs to be converted
                long datetime = 0;

                try{
                    datetime = Util.getInstance().convertDateToMilliseconds(dt, "dd-MM-yyyy hh:mm a");
                }
                catch (Exception e){}


                if(numParticipants <= 0){
                    Toast.makeText(NewEventActivity.this, "Invalid no of participants", Toast.LENGTH_SHORT).show();
                    return;
                }
                // write code to store data in SQLite
                EventDB db = new EventDB(NewEventActivity.this);
                if(eventID.isEmpty()){
                    eventID = title + System.nanoTime();
                    db.insertEvent(eventID, title, venue, datetime, numParticipants, description);
                }else{
                    db.updateEvent(eventID, title, venue, datetime, numParticipants, description);
                }

                db.close(); // must close the database
                storeDataToRemoteDB(eventID, title, venue, datetime, numParticipants, description);

                finish();
            }
        });





    }

    private void storeDataToRemoteDB(String eventID, String title, String venue, long datetime, int numParticipants, String description    ){
        try{
            JSONObject jo  = new JSONObject();

            jo.put("title", title);
            jo.put("venue", venue);
            jo.put("dateTime", datetime);
            jo.put("numParticipants", numParticipants);
            jo.put("description", description);

            String value = jo.toString();

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("sid", "2022-3-60-125));
            params.add(new BasicNameValuePair("semester", "2025-1"));
            params.add(new BasicNameValuePair("key", eventID));
            params.add(new BasicNameValuePair("value", value));
            params.add(new BasicNameValuePair("action", "backup"));

            Handler h = new Handler();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String dataFromServer = RemoteAccess.getInstance().makeHttpRequest(params);
                    if(dataFromServer != null){
                      try{
                          JSONObject json  = new JSONObject(dataFromServer);
                          if(json.has("msg")){
                              String msg = json.getString("msg");
                              h.post(new Runnable() {
                                  @Override
                                  public void run() {
                                      Toast.makeText(NewEventActivity.this, msg, Toast.LENGTH_LONG).show();
                                  }
                              });
                              return;
                          }
                      }catch(Exception e){}
                    }
                    System.out.println("Something went wrong");
                }
            }).start();




        }catch(Exception r){}
    }
}