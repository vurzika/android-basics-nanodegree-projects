package com.example.android.scorekeeperapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    /**
     * Tracks a score for Mom
     */
    private int scoreMom = 0;

    /**
     * Tracks a score for Dad
     */
    private int scoreDad = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Increase the score for Mom by 1 point.
     */
    public void addOneToMom(View v) {
        scoreMom = scoreMom + 1;
        displayForMom(scoreMom);
    }

    /**
     * Increase the score for Mom by 3 points.
     */
    public void addThreeToMom(View v) {
        scoreMom = scoreMom + 3;
        displayForMom(scoreMom);
    }

    /**
     * Increase the score for Mom by 5 points.
     */
    public void addFiveToMom(View v) {
        scoreMom = scoreMom + 5;
        displayForMom(scoreMom);
    }

    /**
     * Increase the score for Dad B by 1 point.
     */
    public void addOneToDad(View v) {
        scoreDad = scoreDad + 1;
        displayForDad(scoreDad);
    }

    /**
     * Increase the score for Dad by 3 points.
     */
    public void addThreeToDad(View v) {
        scoreDad = scoreDad + 3;
        displayForDad(scoreDad);
    }

    /**
     * Increase the score for Dad by 5 points.
     */
    public void addFiveToDad(View v) {
        scoreDad = scoreDad + 5;
        displayForDad(scoreDad);
    }

    /**
     * Displays the given score for Mom.
     */
    private void displayForMom(int score) {
        TextView scoreView = findViewById(R.id.mom_score);
        scoreView.setText(String.valueOf(score));
    }

    /**
     * Displays the given score for Dad.
     */
    private void displayForDad(int score) {
        TextView scoreView = findViewById(R.id.dad_score);
        scoreView.setText(String.valueOf(score));
    }

    /**
     * Resets score for Mom and Dad back to zero.
     */
    public void setScoreToZero(View view) {
        scoreMom = 0;
        scoreDad = 0;
        displayForMom(scoreMom);
        displayForDad(scoreDad);
    }
}