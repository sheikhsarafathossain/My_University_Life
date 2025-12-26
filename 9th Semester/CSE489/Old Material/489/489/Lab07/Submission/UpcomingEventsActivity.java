package com.ewubd.Cse489_1_ShoebKhandaker_2022_3_60_125;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.message.BasicNameValuePair;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UpcomingEventsActivity extends AppCompatActivity {
    private ArrayList<Event> eventsList = new ArrayList<>();
    private ListView listViewEvents;
    private CustomEventAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_events);

        listViewEvents = findViewById(R.id.listViewEvents);
        adapter = new CustomEventAdapter(this, eventsList);
        listViewEvents.setAdapter(adapter);

        Button btnExit = findViewById(R.id.btnExit);
        Button btnAddNew = findViewById(R.id.btnAddNew);


        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UpcomingEventsActivity.this, NewEventActivity.class);
                startActivity(i);
            }
        });
        listViewEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event e = eventsList.get(position);
                Intent i = new Intent(UpcomingEventsActivity.this, NewEventActivity.class);
                i.putExtra("eventId", e.eventId);
                i.putExtra("title", e.title);
                i.putExtra("venue", e.venue);
                i.putExtra("datetime", e.datetime);
                i.putExtra("numParticipants", e.numParticipants);
                i.putExtra("description", e.description);
                startActivity(i);
            }
        });
        listViewEvents.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Event e = eventsList.get(position);
                showConsentDialog(e);
                return true;
            }
        });
    }

    private void showConsentDialog(Event e){
        new AlertDialog.Builder(this)
                .setTitle("Consent Required")
                .setMessage("Do you give consent to proceed?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EventDB db = new EventDB(UpcomingEventsActivity.this);
                        db.deleteEvent(e.eventId);

                        deleteDataFromRemoteDB(e.eventId);

                        eventsList.remove(e);
                        adapter.notifyDataSetChanged();

                        Toast.makeText(getApplicationContext(), "Event has been deleted successfully", Toast.LENGTH_SHORT);
                        dialog.dismiss();
                    }
                })
                .setNeutralButton("Cancel", null)
                .show();
    }

    @Override
    public void onStart(){
        super.onStart();
        this.loadDataFromSQLite();
        this.loadDataFromRemoteServer();
    }

    private void loadDataFromSQLite(){
        eventsList.clear(); // need to clear the list so that the multiplication of the list does not show
        EventDB db = new EventDB(this);
        Cursor cur = db.selectEvents("SELECT * FROM events ORDER BY datetime DESC");
        if(cur != null){
            while(cur.moveToNext()){
                String eventId = cur.getString(0);
                String title = cur.getString(1);
                String venue = cur.getString(2);
                long datetime = cur.getLong(3);
                int numParticipants = cur.getInt(4);
                String description = cur.getString(5);

                Event e = new Event(eventId, title, venue, datetime, numParticipants, description);
                System.out.println(e);

                eventsList.add(e);
            }
        }
        adapter.notifyDataSetChanged();
    }



    private void loadDataFromRemoteServer() {
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("sid", "2022-3-60-125"));
            params.add(new BasicNameValuePair("semester", "2025-1"));
            params.add(new BasicNameValuePair("action", "restore"));

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String dataFromServer = RemoteAccess.getInstance().makeHttpRequest(params);

                        if (dataFromServer != null && !dataFromServer.trim().isEmpty()) {
                            JSONObject json = new JSONObject(dataFromServer);
                            if (json.has("msg") && json.getString("msg").equals("OK")) {
                                // Get the key-value array from the response
                                JSONArray rows = json.getJSONArray("key-value");
                                EventDB db = new EventDB(UpcomingEventsActivity.this);

                                for (int i = 0; i < rows.length(); i++) {
                                    JSONObject keyValue = rows.getJSONObject(i);
                                    String eventID = keyValue.getString("key");
                                    String value = keyValue.getString("value");

                                    // Now, parse the 'value' string, which is itself a JSON string
                                    JSONObject eventJson = new JSONObject(value); // Parsing the nested JSON string

                                    String title = eventJson.optString("title", "No title available");
                                    String venue = eventJson.optString("venue", "Unknown venue");
                                    long datetime = eventJson.optLong("dateTime", System.currentTimeMillis());
                                    int numParticipants = eventJson.optInt("numParticipants", 0);
                                    String description = eventJson.optString("description", "No description");

                                    // Insert or update event in the database
                                    if (doesEventExist(eventID)) {
                                        db.updateEvent(eventID, title, venue, datetime, numParticipants, description);
                                    } else {
                                        db.insertEvent(eventID, title, venue, datetime, numParticipants, description);
                                    }
                                }
                                db.close();

                                // Update UI on the main thread after database operations
                                Handler mainHandler = new Handler(Looper.getMainLooper());
                                mainHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        loadDataFromSQLite();  // Update UI or trigger data refresh
                                    }
                                });
                            } else {
                                System.out.println("Error: " + json.getString("msg"));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();  // Log error
                    }
                }
            }).start();

        } catch (Exception r) {
            r.printStackTrace();  // Log error
        }
    }

    private  boolean doesEventExist(String eventID){
        for(Event e : eventsList){
            if(e.eventId.equals(eventID))return true;
        }

        return false;

    }


    private void deleteDataFromRemoteDB(String eventID ){
        try{


            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("sid", "2022-3-60-125"));
            params.add(new BasicNameValuePair("semester", "2025-1"));
            params.add(new BasicNameValuePair("key", eventID));
            params.add(new BasicNameValuePair("action", "remove"));

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
                                        Toast.makeText(UpcomingEventsActivity.this, msg, Toast.LENGTH_LONG).show();
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