package com.example.myapplication;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ImageView mImageView;
    private Button mLoadButton;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = findViewById(R.id.image_view);
        mLoadButton = findViewById(R.id.load_image_button);
        mProgressBar = findViewById(R.id.progress_bar);

        // Set the click listener for the button
        mLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show progress and disable button to prevent multiple clicks
                mProgressBar.setVisibility(View.VISIBLE);
                mLoadButton.setEnabled(false);

                // Start a new thread to perform the network operation
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // This runs on the WORKER THREAD
                        final Bitmap bitmap = loadImageFromNetwork("http://example.com/image.png");

                        // Post the result back to the UI thread
                        mImageView.post(new Runnable() {
                            @Override
                            public void run() {
                                // This runs on the UI THREAD
                                mImageView.setImageBitmap(bitmap);
                                mProgressBar.setVisibility(View.GONE);
                                mLoadButton.setEnabled(true);
                            }
                        });
                    }
                }).start();
            }
        });
    }

    /**
     * Simulates a network call to download an image.
     * In a real app, this would use an HTTP client. For a real network call,
     * you would also need to add the INTERNET permission to your AndroidManifest.xml.
     * <uses-permission android:name="android.permission.INTERNET" />
     */
    private Bitmap loadImageFromNetwork(String url) {
        try {
            // Simulate a 3-second network delay
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Instead of a real network call, we'll decode a sample image
        // from our app's resources.
        // Make sure you have an image named 'placeholder_image.png' in res/drawable
        return BitmapFactory.decodeResource(getResources(), R.drawable.placeholder_image);
    }
}