package com.example.process;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;
public class MainActivity extends AppCompatActivity {

    ProgressBar bar1;
    ProgressBar bar2;

    TextView msgWorking;
    TextView msgReturned;

    boolean isRunning = false;
    final int MAX_SEC = 30;
    int globalIntTest = 0;

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            String returnedValue = (String) msg.obj;

            msgReturned.append("\nreturned value: " + returnedValue);
            bar1.incrementProgressBy(1);

            if (bar1.getProgress() == bar1.getMax()) {
                msgWorking.setText("Done");
                bar1.setVisibility(ProgressBar.INVISIBLE);
                bar2.setVisibility(ProgressBar.INVISIBLE);
                isRunning = false;
            } else {
                msgWorking.setText("Working..." + bar1.getProgress());
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bar1 = findViewById(R.id.progress1);
        bar1.setMax(MAX_SEC);
        bar1.setProgress(0);

        bar2 = findViewById(R.id.progress2);
        msgWorking = findViewById(R.id.txtWorkProgress);
        msgReturned = findViewById(R.id.txtReturnedValues);
    }

    public void onStart() {
        super.onStart();
        isRunning = true;
        Thread background = new Thread(new Runnable() {
            public void run() {
                try {
                    for (int i = 0; i < MAX_SEC && isRunning; i++) {
                        Thread.sleep(1000);

                        Random rnd = new Random();
                        int localData = rnd.nextInt(101);
                        String data = "Data-" + globalIntTest + "-" + localData;
                        globalIntTest++;

                        Message msg = handler.obtainMessage(1, (String) data);

                        if (isRunning) {
                            handler.sendMessage(msg);
                        }
                    }
                } catch (Throwable t) {
                    isRunning = false;
                }
            }
        });
        background.start();
    }

    public void onStop() {
        super.onStop();
        isRunning = false;
    }
}