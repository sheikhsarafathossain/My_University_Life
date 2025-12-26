package com.example.calculator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Calculator_activity extends AppCompatActivity implements View.OnClickListener {

    private TextView resultText;
    private Button historyButton;
    private Button deleteButton;
    private Button button7;
    private Button button8;
    private Button button9;
    private Button buttonDivide;
    private Button button4;
    private Button button5;
    private Button button6;
    private Button buttonMultiply;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button buttonSubtract;
    private Button buttonDecimal;
    private Button button0;
    private Button buttonEquals;
    private Button buttonAdd;

    private String currentInput = "";
    private Queue<String> historyQueue;
    private static final String PREF_NAME = "calculator_history";
    private static final String HISTORY_KEY_PREFIX = "calculation_";
    private static final int MAX_HISTORY_SIZE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        resultText = findViewById(R.id.result_text);
        historyButton = findViewById(R.id.history);
        deleteButton = findViewById(R.id.delete_button);
        button7 = findViewById(R.id.button_7);
        button8 = findViewById(R.id.button_8);
        button9 = findViewById(R.id.button_9);
        buttonDivide = findViewById(R.id.button_divide);
        button4 = findViewById(R.id.button_4);
        button5 = findViewById(R.id.button_5);
        button6 = findViewById(R.id.button_6);
        buttonMultiply = findViewById(R.id.button_multiply);
        button1 = findViewById(R.id.button_1);
        button2 = findViewById(R.id.button_2);
        button3 = findViewById(R.id.button_3);
        buttonSubtract = findViewById(R.id.button_subtract);
        buttonDecimal = findViewById(R.id.button_decimal);
        button0 = findViewById(R.id.button_0);
        buttonEquals = findViewById(R.id.button_equals);
        buttonAdd = findViewById(R.id.button_add);

        historyButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        buttonDivide.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        buttonMultiply.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        buttonSubtract.setOnClickListener(this);
        buttonDecimal.setOnClickListener(this);
        button0.setOnClickListener(this);
        buttonEquals.setOnClickListener(this);
        buttonAdd.setOnClickListener(this);

        historyQueue = new LinkedList<>();
        loadHistory();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.button_0) {
            appendToInput("0");
        } else if (id == R.id.button_1) {
            appendToInput("1");
        } else if (id == R.id.button_2) {
            appendToInput("2");
        } else if (id == R.id.button_3) {
            appendToInput("3");
        } else if (id == R.id.button_4) {
            appendToInput("4");
        } else if (id == R.id.button_5) {
            appendToInput("5");
        } else if (id == R.id.button_6) {
            appendToInput("6");
        } else if (id == R.id.button_7) {
            appendToInput("7");
        } else if (id == R.id.button_8) {
            appendToInput("8");
        } else if (id == R.id.button_9) {
            appendToInput("9");
        } else if (id == R.id.button_decimal) {
            if (!currentInput.contains(".")) {
                appendToInput(".");
            }
        } else if (id == R.id.button_add) {
            appendToInput("+");
        } else if (id == R.id.button_subtract) {
            appendToInput("-");
        } else if (id == R.id.button_multiply) {
            appendToInput("×");
        } else if (id == R.id.button_divide) {
            appendToInput("÷");
        } else if (id == R.id.delete_button) {
            if (!currentInput.isEmpty()) {
                currentInput = currentInput.substring(0, currentInput.length() - 1);
                resultText.setText(currentInput);
            }
        } else if (id == R.id.button_equals) {
            evaluateExpression();
        } else if (id == R.id.history) {
            Intent intent = new Intent(this, HistoryActivity.class);
            startActivity(intent);
        }
    }

    private void appendToInput(String value) {
        currentInput += value;
        resultText.setText(currentInput);
    }

    private void evaluateExpression() {
        try {
            String expression = currentInput.replace('×', '*').replace('÷', '/');
            double result = evaluate(expression);
            String resultString = formatResult(result);
            resultText.setText(resultString);
            addToHistory(currentInput + " = " + resultString);
            currentInput = resultString;
        } catch (Exception e) {
            Toast.makeText(this, "Error in expression", Toast.LENGTH_SHORT).show();
        }
    }

    private String formatResult(double result) {
        if (result == (long) result) {
            return String.format("%d", (long) result);
        } else {
            return String.format("%.4f", result);
        }
    }

    private double evaluate(String expression) {
        String[] parts = expression.split("(?<=[-+*/])|(?=[-+*/])");
        List<String> tokens = new ArrayList<>();
        for (String part : parts) {
            if (!part.isEmpty()) {
                tokens.add(part.trim());
            }
        }

        if (tokens.isEmpty()) {
            return 0;
        }

        double result = Double.parseDouble(tokens.get(0));

        for (int i = 1; i < tokens.size(); i += 2) {
            String operator = tokens.get(i);
            double operand = Double.parseDouble(tokens.get(i + 1));
            switch (operator) {
                case "+":
                    result += operand;
                    break;
                case "-":
                    result -= operand;
                    break;
                case "*":
                    result *= operand;
                    break;
                case "/":
                    if (operand != 0) {
                        result /= operand;
                    } else {
                        throw new ArithmeticException("Division by zero");
                    }
                    break;
            }
        }
        return result;
    }

    private void addToHistory(String calculation) {
        historyQueue.offer(calculation);
        if (historyQueue.size() > MAX_HISTORY_SIZE) {
            historyQueue.poll();
        }
        saveHistory();
    }

    private void saveHistory() {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        List<String> historyList = new ArrayList<>(historyQueue);
        for (int i = 0; i < historyList.size(); i++) {
            editor.putString(HISTORY_KEY_PREFIX + i, historyList.get(i));
        }
        editor.putInt("history_count", historyList.size());
        editor.apply();
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
}