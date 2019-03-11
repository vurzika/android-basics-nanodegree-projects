package com.example.android.winterolympicsquiz;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    // controls required to check correct answers
    @BindView(R.id.chamonix_radio_button)
    RadioButton mCorrectAnswerOneRadioButton;
    @BindView(R.id.speed_skating_radio_button)
    RadioButton mCorrectAnswerTwoRadioButton;
    @BindView(R.id.answer_edit_text)
    EditText mQuestionThreeAnswerField;
    @BindView(R.id.norway_radio_button)
    RadioButton mCorrectAnswerFourRadioButton;
    @BindView(R.id.australia_checkbox)
    CheckBox mQuestionFiveAustraliaCheckBox;
    @BindView(R.id.new_zeland_checkbox)
    CheckBox mQuestionFiveNewZealandCheckBox;
    @BindView(R.id.south_africa_checkbox)
    CheckBox mQuestionFiveSouthAfricaCheckBox;
    @BindView(R.id.argentina_checkbox)
    CheckBox mQuestionFiveArgentinaCheckBox;
    @BindView(R.id.the_jamaican_bobsled_team_radio_button)
    RadioButton mCorrectAnswerSixRadioButton;
    @BindView(R.id.calgary_radio_button)
    RadioButton mCorrectAnswerSevenRadioButton;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }

    public void submit(View view) {

        int score = 0;

        //Figure out if the first question is answered correctly
        if (mCorrectAnswerOneRadioButton.isChecked()) {
            score++;
        }

        //Figure out if the second question is answered correctly
        if (mCorrectAnswerTwoRadioButton.isChecked()) {
            score++;
        }

        //Figure out if the third question is answered correctly
        //making it lowercase to handle all upper/lower case combinations
        String thirdQuestionAnswer = mQuestionThreeAnswerField.getText().toString().toLowerCase();
        if ("curling".equals(thirdQuestionAnswer)) {
            score++;
        }

        //Figure out if the fourth question is answered correctly
        if (mCorrectAnswerFourRadioButton.isChecked()) {
            score++;
        }

        //Fifth question
        //Figure out if Australia check box is checked
        boolean australiaIsChecked = mQuestionFiveAustraliaCheckBox.isChecked();

        //Figure out if New Zealand check box is checked
        boolean newZealandIsChecked = mQuestionFiveNewZealandCheckBox.isChecked();

        //Figure out if South Africa check box is not checked
        boolean southAfricaIsNotChecked = !mQuestionFiveSouthAfricaCheckBox.isChecked();

        //Figure out if Argentina check box is not checked
        boolean argentinaIsNotChecked = !mQuestionFiveArgentinaCheckBox.isChecked();

        //Checking if the fifth question is answered correctly
        if (australiaIsChecked && newZealandIsChecked && southAfricaIsNotChecked && argentinaIsNotChecked) {
            score++;
        }

        //Figure out if the sixth question is answered correctly
        if (mCorrectAnswerSixRadioButton.isChecked()) {
            score++;
        }

        //Figure out if the seventh question is answered correctly
        if (mCorrectAnswerSevenRadioButton.isChecked()) {
            score++;
        }

        // Hide existing toast in case it is still displayed
        if (mToast != null) {
            mToast.cancel();
        }

        mToast = createStyledToast(this, getResultMessage(score), Toast.LENGTH_LONG);
        mToast.show();
    }

    private String getResultMessage(int score) {
        if (score == 0) {
            return getString(R.string.message_result_bad, score);
        } else if (score <= 3) {
            return getString(R.string.message_result_better, score);
        } else if (score <= 6) {
            return getString(R.string.message_result_good, score);
        } else {
            return getString(R.string.message_result_perfect, score);
        }
    }

    private Toast createStyledToast(Context context, String message, int duration) {
        // creating custom Toast as it can not be customized via style, see
        // https://developer.android.com/guide/topics/ui/notifiers/toasts.html#CustomToastView
        // https://stackoverflow.com/questions/16909476/how-to-customize-toast-in-android

        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_styled,
                findViewById(R.id.styled_toast_container));

        TextView text = layout.findViewById(R.id.text);
        text.setText(message);

        Toast toast = new Toast(context);
        toast.setDuration(duration);
        toast.setView(layout);
        return toast;
    }
}