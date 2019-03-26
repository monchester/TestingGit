package com.example.bookex1;

import android.content.DialogInterface;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.NumberFormat;


public class MainActivity extends AppCompatActivity {
    private EditText txtAmount;
    private EditText txtPeople;
    private EditText txtTipOther;
    private RadioGroup rdoGroupTips;
    private Button btnCalculate;
    private Button btnReset;

    private TextView txtTipAmount;
    private TextView txtTotalToPay;
    private TextView txtTipPerPerson;

    private int radioCheckedId = 1;

    final static NumberFormat formatter =
            NumberFormat.getCurrencyInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtAmount = (EditText)findViewById(R.id.txtAmount);

        txtAmount.requestFocus();

        txtPeople = (EditText)findViewById(R.id.txtPeople);
        txtTipOther = (EditText)findViewById(R.id.txtTipOther);
        rdoGroupTips = (RadioGroup)findViewById(R.id.RadioGroupTips);

        btnCalculate = (Button)findViewById(R.id.btnCalculate);
        btnCalculate.setEnabled(false);
        btnReset = (Button)findViewById(R.id.btnReset);

        txtTipAmount = (TextView)findViewById(R.id.txtTipMount);
        txtTotalToPay = (TextView)findViewById(R.id.txtTotalToPay);
        txtTipPerPerson = (TextView)findViewById(R.id.txtTipPerPerson);

        txtTipOther.setEnabled(false);

        rdoGroupTips.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioFiteen || checkedId == R.id.radioTwenty) {
                    txtTipOther.setEnabled(false);
                    btnCalculate.setEnabled(txtAmount.getText().length() > 0 && txtPeople.getText().length() > 0);
                }
                if (checkedId == R.id.radioOther){
                    txtTipOther.setEnabled(true);
                    txtTipOther.requestFocus();
                    btnCalculate.setEnabled(txtAmount.getText().length() > 0 && txtPeople.getText().length() > 0
                    && txtTipOther.getText().length() > 0);
                }
                radioCheckedId = checkedId;

            }
        });

        txtAmount.setOnKeyListener(mKeyListener);
        txtPeople.setOnKeyListener(mKeyListener);
        txtTipOther.setOnKeyListener(mKeyListener);
        btnCalculate.setOnClickListener(mClickListener);
        btnReset.setOnClickListener(mClickListener);

    }
    private View.OnClickListener mClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btnCalculate){
                calculate();
            } else {
                reset();
            }
        }
    };

    private void reset() {
        txtTipAmount.setText("");
        txtTipPerPerson.setText("");
        txtTotalToPay.setText("");
        txtPeople.setText("");
        txtAmount.setText("");
        txtTipOther.setText("");
        rdoGroupTips.clearCheck();
        rdoGroupTips.check(R.id.radioFiteen);
        txtAmount.requestFocus();
    }

    private void calculate() {
        Double billAmount = Double.parseDouble(txtAmount.getText().toString());
        Double totalPeople = Double.parseDouble(txtPeople.getText().toString());
        Double percentage = null;
        boolean isError = false;
        percentage = 15.0;

        if (billAmount < 1.0){
            showErrorAlert("Enter a valid total amount", txtAmount.getId());
            isError=true;
        }
        if (totalPeople<1.0){
            showErrorAlert("Enter a valid no. of people", txtPeople.getId());
            isError=true;
        }
        if (radioCheckedId == -1){
            radioCheckedId = rdoGroupTips.getCheckedRadioButtonId();
        }
        if (radioCheckedId == R.id.radioFiteen){
            percentage = 15.00;
        }else if (radioCheckedId == R.id.radioTwenty){
            percentage = 20.00;
        }else if (radioCheckedId == R.id.radioOther){
            percentage = Double.parseDouble(txtTipOther.getText().toString());
        }
        if (percentage<1.0){
            showErrorAlert("Enter a valid tip percentage",txtTipOther.getId());
            isError=true;
        }


        if (!isError){
         //   Double totalToPay = billAmount +
         double tipAmount = ((billAmount * percentage) / 100);
         double totalToPay = billAmount + tipAmount;
         double perPersonPays = totalToPay /totalPeople;

         txtTipAmount.setText(formatter.format(tipAmount));
         txtTotalToPay.setText(formatter.format(totalToPay));
         txtTipPerPerson.setText(formatter.format(perPersonPays));

        }

    }

    private void showErrorAlert(String errorMessage, final int fieldId) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(errorMessage)
                .setNeutralButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        findViewById(fieldId).requestFocus();
                    }
                }).show();
    }

    private View.OnKeyListener mKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            switch (v.getId()){
                case R.id.txtAmount:
                case R.id.txtPeople:
                    btnCalculate.setEnabled(txtAmount.getText().length()>0 && txtPeople.getText().length() > 0);
                    break;
                case R.id.txtTipOther:
                    btnCalculate.setEnabled(txtAmount.getText().length()>0 && txtPeople.getText().length() > 0
                    && txtTipOther.getText().length()>0);
                    break;
            }
            return false;
        }

    };
}
