package com.example.calculatrice.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.calculatrice.R;
import com.example.calculatrice.adapters.HistoryAdapter;
import com.example.calculatrice.db.DBHandler;
import com.example.calculatrice.db.Operation;

import java.util.ArrayList;

public class CalculatorFragment extends Fragment {

    private String number1, number2, operator;
    private String result = "0";
    private View view;
    private boolean historyShown = true;

    private boolean EmptyOrNull(String value){
        return value == null || value.isEmpty();
    }

    private void displayResultAndOperation(){
        TextView operationTextView = (TextView) view.findViewById(R.id.operation);
        TextView resultTextView = (TextView) view.findViewById(R.id.result);

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
                (Button) view.findViewById(R.id.button0),
                (Button) view.findViewById(R.id.button1),
                (Button) view.findViewById(R.id.button2),
                (Button) view.findViewById(R.id.button3),
                (Button) view.findViewById(R.id.button4),
                (Button) view.findViewById(R.id.button5),
                (Button) view.findViewById(R.id.button6),
                (Button) view.findViewById(R.id.button7),
                (Button) view.findViewById(R.id.button8),
                (Button) view.findViewById(R.id.button9)
        };

        for (int i = 0; i < numbers.length; i++) {
            Button button = numbers[i];

            button.setOnClickListener(view2 -> {
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
                (Button) view.findViewById(R.id.addition),
                (Button) view.findViewById(R.id.division),
                (Button) view.findViewById(R.id.multiply),
                (Button) view.findViewById(R.id.substraction)
        };

        for (int i = 0; i < operators.length; i++) {
            Button button = operators[i];

            button.setOnClickListener(view -> {
                operatorClickHandler(button);
            });
        }
    }

    private void egalOperatorListener(){
        Button button = (Button) view.findViewById(R.id.equal);

        button.setOnClickListener(view -> {

            try{
                DBHandler db = new DBHandler(this.getContext());

                String savedNumber1 = number1, savedNumber2 = number2, savedOperator = operator;

                calculateResult();

                Operation operation = new Operation(-1, savedNumber1, savedNumber2, savedOperator, result);

                long id = db.insertOperation(operation);

                if(id != -1){
                    setHistoryData();
                }
            }catch (Exception exception){

            }finally {
                displayResultAndOperation();
            }

        });
    }

    private void percentOperatorHandler(){
        Button button = (Button) view.findViewById(R.id.percent);

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
        Button button = (Button) view.findViewById(R.id.plusMinus);

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
        Button button = (Button) view.findViewById(R.id.clear);

        button.setOnClickListener(view -> {
            number1 = null;
            operator = null;
            number2 = null;
            result = "0";

            displayResultAndOperation();
        });
    }

    private void eraseListener(){
        ImageButton button = (ImageButton) view.findViewById(R.id.erase);

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
        Button button = (Button) view.findViewById(R.id.comma);

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

    private void historyButtonListener(){
        ImageButton button = (ImageButton) view.findViewById(R.id.history_button);

        button.setOnClickListener(view1 -> {
            toggleHistory();
        });
    }

    private void toggleHistory(){
        try{
            FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.history_container);

            float value = this.getActivity().getResources().getDisplayMetrics().heightPixels / 2;

            AnimatorSet set = new AnimatorSet();

            // Using property animation
            ObjectAnimator animation = ObjectAnimator.ofFloat(
                    frameLayout,
                    "translationY",
                    historyShown ? value - 160 : value * 2,
                    historyShown ? value * 2 : value - 160
            );

            animation.setDuration(500);

            set.play(animation);

            set.start();

            historyShown = !historyShown;

        }catch (Exception exception){

        }

    }

    private void initHistory(){
        FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.history_container);

        float height = this.getActivity().getResources().getDisplayMetrics().heightPixels / 2;

        ViewGroup.LayoutParams layoutParams = frameLayout.getLayoutParams();

        layoutParams.height = (int) height;

        frameLayout.setLayoutParams(layoutParams);

        setHistoryData();
    }

    private void itemSelectedHandler(HistoryAdapter historyAdapter, int index){
        try{
            Operation operation = historyAdapter.getItem(index);

            if(operation != null){
                TextView operationTextView = (TextView) view.findViewById(R.id.operation);

                number1 = operation.getNumber1();
                number2 = operation.getNumber2();
                operator = operation.getOperator();
                result = operation.getResult();

                Double number2Double = Double.parseDouble(number2);

                if(number2Double < 0 && operator.equals("+")){
                    operator = "-";

                    number2 = String.valueOf(Math.abs(number2Double));
                }

                operationTextView.setText(number1 + " " + operator + " " + number2);

                calculateResult();
            }

        }catch (Exception exception){
        }

    }

    public void setHistoryData(){
        try {
            DBHandler db = new DBHandler(this.getContext());

            ArrayList<Operation> operations = db.getOperations();

            ListView listView = (ListView) view.findViewById(R.id.history);

            HistoryAdapter historyAdapter = new HistoryAdapter(this.getContext(), operations, this);

            listView.setAdapter(historyAdapter);

            listView.setOnItemClickListener((adapterView, view, index, l) -> itemSelectedHandler(historyAdapter, index));

        }catch (Exception exception){
        }
    }

    public CalculatorFragment() {
        // Required empty public constructor
    }

    public static CalculatorFragment newInstance() {
        CalculatorFragment fragment = new CalculatorFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_calculator, container, false);

        numbersListeners();

        egalOperatorListener();

        percentOperatorHandler();

        plusMinusListener();

        clearListener();

        eraseListener();

        commaListener();

        operatorsListeners();

        initHistory();

        historyButtonListener();

        toggleHistory();

        return view;
    }
}