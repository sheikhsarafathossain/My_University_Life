package com.example.calculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView historyRecyclerView;
    private HistoryAdapter historyAdapter;
    private Button closeButton;

    private Queue<String> historyQueue;
    private static final String PREF_NAME = "calculator_history";
    private static final String HISTORY_KEY_PREFIX = "calculation_";
    private static final int MAX_HISTORY_SIZE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyRecyclerView = findViewById(R.id.history_recycler_view);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        closeButton = findViewById(R.id.close_history_button);
        closeButton.setOnClickListener(v -> finish());

        historyQueue = new LinkedList<>();
        loadHistory();

        historyAdapter = new HistoryAdapter(new ArrayList<>(historyQueue));
        historyRecyclerView.setAdapter(historyAdapter);
    }

    private void loadHistory() {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        int count = prefs.getInt("history_count", 0);
        for (int i = 0; i < count; i++) {
            String calculation = prefs.getString(HISTORY_KEY_PREFIX + i, null);
            if (calculation != null) {
                historyQueue.offer(calculation);
            }
        }
    }

    private static class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

        private final List<String> historyItems;

        public HistoryAdapter(List<String> historyItems) {
            this.historyItems = historyItems;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.historyTextView.setText(historyItems.get(historyItems.size() - 1 - position)); // Show latest first
        }

        @Override
        public int getItemCount() {
            return historyItems.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public TextView historyTextView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                historyTextView = itemView.findViewById(android.R.id.text1);
                historyTextView.setTextColor(0xFFFFFFFF); // Set text color
                historyTextView.setPadding(16, 16, 16, 16);
                historyTextView.setTextSize(18);
            }
        }
    }
}