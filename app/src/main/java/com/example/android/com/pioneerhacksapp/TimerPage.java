package com.example.android.com.pioneerhacksapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class TimerPage extends AppCompatActivity {

    private EditText mEditTextInput;
    private TextView mTextViewCountDown;
    private Button mButtonSet;
    private Button mButtonStart;
    private Button mButtonGiveUp;
    private Button home;

    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;

    private long mStartTimeInMillis;
    private long mTimeLeftInMillis = mStartTimeInMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_page);

        home = (Button) findViewById(R.id.homeButton);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });


        mEditTextInput = findViewById(R.id.minutes);

        mTextViewCountDown = findViewById(R.id.clock);

        mButtonSet = findViewById(R.id.buttonSet);
        mButtonStart = findViewById(R.id.buttonStart);
        mButtonGiveUp = findViewById(R.id.buttonGiveUp);

        mButtonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = mEditTextInput.getText().toString();
                if(input.length() == 0){
                    Toast.makeText(TimerPage.this, "Cannot Leave Field Blank", Toast.LENGTH_SHORT).show();
                    return;
                }
                long millisInput = Long.parseLong(input) * 60000;
                if(millisInput == 0){
                    Toast.makeText(TimerPage.this, "Please Enter Positive Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                setTime(millisInput);
                mEditTextInput.setText("");
            }
        });

        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTimerRunning){
                    pauseTimer();
                }else {
                    startTimer();
                }
            }
        });
        updateCountDownText();

        mButtonGiveUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });
        updateCountDownText();
    }

    private void setTime(long milliseconds){
        mStartTimeInMillis = milliseconds;
        resetTimer();
    }

    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                mButtonStart.setVisibility(View.VISIBLE);
                mButtonGiveUp.setVisibility(View.INVISIBLE);
            }

        }.start();
        mTimerRunning = true;
        mButtonGiveUp.setVisibility(View.INVISIBLE);
        Toast toast = Toast.makeText(this, R.string.buildmessage, Toast.LENGTH_LONG);
        toast.show();
        mEditTextInput.setVisibility(View.INVISIBLE);
        mButtonSet.setVisibility(View.INVISIBLE);
        mButtonStart.setText("pause");
    }
    private void pauseTimer(){
        mCountDownTimer.cancel();
        mTimerRunning = false;
        mButtonStart.setText("build");
        mButtonGiveUp.setVisibility(View.VISIBLE);
        mButtonStart.setVisibility(View.INVISIBLE);
        Toast toast1 = Toast.makeText(this, R.string.resetMessage, Toast.LENGTH_LONG);
        toast1.show();
        RelativeLayout currentLayout = (RelativeLayout) findViewById(R.id.container);
        currentLayout.setBackgroundColor(Color.RED);
    }
    private void resetTimer(){
        mTimeLeftInMillis = mStartTimeInMillis;
        updateCountDownText();
        mButtonGiveUp.setVisibility(View.INVISIBLE);
        mButtonStart.setVisibility(View.VISIBLE);
        mEditTextInput.setVisibility(View.VISIBLE);
        mButtonSet.setVisibility(View.VISIBLE);

    }
    private void updateCountDownText(){
        int hours = (int) ((mTimeLeftInMillis)/1000)/3600;
        int minutes = (int) ((mTimeLeftInMillis / 1000) % 3600)/ 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted;
        if(hours > 0){
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%d:%02d:%02d", hours, minutes, seconds);
        }else{
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%02d:%02d", minutes, seconds);
        }

        mTextViewCountDown.setText(timeLeftFormatted);

    }

}
