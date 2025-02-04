package com.example.firstapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private TextView textViewExp;
    private EditText etDisplay;
    private String currentNumber = "0";
    private String operator = "";
    private int previousOperatorPrior = 1;
    private double firstNumber = 0;
    private String expression = "";
    private Map<String, Integer> priority = new HashMap<String, Integer>() {{
        put("+", 1);
        put("-", 1);
        put("*", 2);
        put("/", 2);
    }};
    private Stack<String> stack_operator = new Stack<>();
    private Stack<Double> stack_num = new Stack<>();
    private boolean changeOperator = false; // Check if previous is already click operator


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etDisplay = findViewById(R.id.etDisplay);
        textViewExp = findViewById(R.id.textViewExpression);
    }

    // Clear button Click
    public void onClearClick(View view) {
        currentNumber = "0";
        operator = "";
        firstNumber = 0;
        expression = "";
        etDisplay.setText(currentNumber);
        textViewExp.setText(expression);
        stack_num.clear();
        stack_operator.clear();
    }

    // Operator click +, -, x,...
    public void onOperatorClick(View view) {
        Button button = (Button) view;
        operator = button.getText().toString();

        // Check if click operator more times
        if (changeOperator) {
            stack_operator.pop();
            stack_operator.push(operator);
            expression = expression.substring(0, expression.length() - 1) + operator;
            textViewExp.setText(expression);
            return;
        }

        changeOperator = true;
        firstNumber = Double.parseDouble(currentNumber);
        stack_num.push(firstNumber);

        // Calculate when click operator
        if (stack_operator.size() >= 1 && stack_num.size() >= 2 && priority.get(stack_operator.peek()) >= priority.get(operator)) {

            while (!stack_operator.isEmpty() && !stack_num.isEmpty()) {
                double firstVal = stack_num.pop();
                double secondVal = stack_num.pop();
                double res = 0;
                switch (stack_operator.peek()) {
                    case "+":
                        res += secondVal + firstVal;
                        break;
                    case "-":
                        res += secondVal - firstVal;
                        break;
                    case "*":
                        res += secondVal * firstVal;
                        break;
                    case "/":
                        res += secondVal / firstVal;
                        break;
                }
                stack_operator.pop();
                stack_num.push(res);
            }

            double res = stack_num.peek();
            int int_res = (int) res;
            currentNumber = res % int_res == 0 ? String.valueOf(int_res) : String.valueOf(res);
            etDisplay.setText(currentNumber);
        }

        stack_operator.push(operator);
        expression += operator;
        textViewExp.setText(expression);
        currentNumber = "0"; // Reset current number after click operator
    }

    // Number click 0, 1, 2,...
    public void onNumberClick(View view) {
        Button button = (Button) view;
        String number = button.getText().toString();

        if (currentNumber.equals("0")) {
            currentNumber = number;
        } else {
            currentNumber += number;
        }

        expression += number;
        changeOperator = false;
        Display();
    }

    // Change sign
    public void onSignNumberClick(View view) {
        expression = expression.substring(0, expression.lastIndexOf(currentNumber));
        double number = Double.parseDouble(currentNumber);

        if (number <= 0) {
            currentNumber = currentNumber.substring(1);
        } else {
            currentNumber = '-' + currentNumber;
        }
        expression += currentNumber;

        Display();
    }

    // Modulo click
    public void onModClick(View view) {
        expression = expression.substring(0, expression.lastIndexOf(currentNumber));
        double val = Double.parseDouble(currentNumber) / 100;
        currentNumber = String.valueOf(val);
        expression += currentNumber;

        Display();
    }

    // Equal click
    public void onEqualClick(View view) {
        if (!priority.containsKey(expression.substring(expression.length() - 1))) {
            stack_num.push(Double.parseDouble(currentNumber));
        }

        while (!stack_operator.isEmpty() && stack_num.size() >= 2) {
            double firstVal = stack_num.pop();
            double secondVal = stack_num.pop();
            double res = 0;
            switch (stack_operator.peek()) {
                case "+":
                    res += secondVal + firstVal;
                    break;
                case "-":
                    res += secondVal - firstVal;
                    break;
                case "*":
                    res += secondVal * firstVal;
                    break;
                case "/":
                    res += secondVal / firstVal;
                    break;
            }
            stack_operator.pop();
            stack_num.push(res);
        }

        double res = stack_num.peek();
        int int_res = (int) res;
        currentNumber = res % int_res == 0 ? String.valueOf(int_res) : String.valueOf(res);
        stack_operator.clear();
        stack_num.clear();
        expression = currentNumber;
        Display();
    }

    // Display to View
    public void Display() {
        textViewExp.setText(expression);
        etDisplay.setText(currentNumber);
    }
}