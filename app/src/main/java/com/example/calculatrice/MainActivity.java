package com.example.calculatrice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private String number1, number2, operator;
    private String result = "0";

    private boolean EmptyOrNull(String value){
        return value == null || value.isEmpty();
    }

    private void displayResultAndOperation(){
        TextView operationTextView = findViewById(R.id.operation);
        TextView resultTextView = findViewById(R.id.result);

        String operationText = "";

        if(number1 != null){
            operationText += number1;

            if(operator != null){
                operationText += " " + operator + " ";

                if(number2 != null){
                    operationText += number2;
                }
            }
        }

        operationTextView.setText(operationText);
        resultTextView.setText(result);

    }

    private void numberClickHandler(@NonNull Button button){
        String number = button.getText().toString();

        if(operator == null || operator.isEmpty()){
            if(number1 == null) {
                number1 = number;
            }else{
                number1 += number;
            }
        }else{
            if(number2 == null) {
                number2 = number;
            }else{
                number2 += number;
            }
        }

        displayResultAndOperation();
    }

    private void numbersListeners(){
        Button[] numbers = new Button[]{
                findViewById(R.id.button0),
                findViewById(R.id.button1),
                findViewById(R.id.button2),
                findViewById(R.id.button3),
                findViewById(R.id.button4),
                findViewById(R.id.button5),
                findViewById(R.id.button6),
                findViewById(R.id.button7),
                findViewById(R.id.button8),
                findViewById(R.id.button9)
        };

        for (int i = 0; i < numbers.length; i++) {
            Button button = numbers[i];

            button.setOnClickListener(view -> {
                numberClickHandler(button);
            });
        }
    }

    private void calculateResult(){
        if(!EmptyOrNull(number1) && !EmptyOrNull(operator) && !EmptyOrNull(number2)){
            double number1Double = Double.parseDouble(number1);
            double number2Double = Double.parseDouble(number2);

            switch (operator){
                case "+": {
                    result = (number1Double + number2Double) + "";
                    break;
                }
                case "-": {
                    result = (number1Double - number2Double) + "";
                    break;
                }
                case "*": {
                    result = (number1Double * number2Double) + "";
                    break;
                }
                case "/": {
                    result = (number1Double / number2Double) + "";
                    break;
                }
                default: break;
            }

            number1 = result;
            number2 = null;
        }
    }

    private void operatorClickHandler(@NonNull Button button){
        String operator = button.getText().toString();

        if(this.operator != null && !this.operator.isEmpty()){
            calculateResult();
        }

        this.operator = operator;

        displayResultAndOperation();
    }

    private void operatorsListeners(){
        Button[] operators = new Button[]{
                findViewById(R.id.addition),
                findViewById(R.id.division),
                findViewById(R.id.multiply),
                findViewById(R.id.substraction)
        };

        for (int i = 0; i < operators.length; i++) {
            Button button = operators[i];

            button.setOnClickListener(view -> {
                operatorClickHandler(button);
            });
        }
    }

    private void egalOperatorListener(){
        Button button = findViewById(R.id.equal);

        button.setOnClickListener(view -> {
            calculateResult();

            displayResultAndOperation();
        });
    }

    private void percentOperatorHandler(){
        Button button = findViewById(R.id.percent);

        button.setOnClickListener(view -> {
            if(!EmptyOrNull(number2)){
                double number2Double = Double.parseDouble(number2);

                number2 = (number2Double / 100) + "";
            }else if(!EmptyOrNull(number1)){
                double number1Double = Double.parseDouble(number1);

                number1 = (number1Double / 100) + "";
            }

            displayResultAndOperation();
        });
    }

    private void plusMinusListener(){
        Button button = findViewById(R.id.plusMinus);

        button.setOnClickListener(view -> {
            if(!EmptyOrNull(number2)) {
                double number2Double = Double.parseDouble(number2);

                number2 = (-number2Double) + "";
            }else if(!EmptyOrNull(number1)){
                double number1Double = Double.parseDouble(number1);

                number1 = (-number1Double) + "";
            }

            displayResultAndOperation();
        });
    }

    private void clearListener(){
        Button button = findViewById(R.id.clear);

        button.setOnClickListener(view -> {
            number1 = null;
            operator = null;
            number2 = null;
            result = "0";

            displayResultAndOperation();
        });
    }

    private void eraseListener(){
        ImageButton button = findViewById(R.id.erase);

        button.setOnClickListener(view -> {
            if(!EmptyOrNull(number2)){
                number2 = number2.substring(0, number2.length() - 1);
            }else if(!EmptyOrNull(operator)){
                operator = operator.substring(0, operator.length() - 1);
            }else if(!EmptyOrNull(number1)){
                number1 = number1.substring(0, number1.length() - 1);
            }

            if(!EmptyOrNull(result)){
                calculateResult();
            }

            displayResultAndOperation();
        });
    }

    private void commaListener(){
        Button button = findViewById(R.id.comma);

        button.setOnClickListener(view -> {
            if(!EmptyOrNull(number2)){
                if(!number2.contains(".")) {
                    number2 += ".";
                }
            }else if(EmptyOrNull(operator) && !EmptyOrNull(number1)){
                if(!number1.contains(".")){
                    number1 += ".";
                }
            }

            displayResultAndOperation();
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        numbersListeners();

        egalOperatorListener();

        percentOperatorHandler();

        plusMinusListener();

        clearListener();

        eraseListener();

        commaListener();

        operatorsListeners();
    }
}