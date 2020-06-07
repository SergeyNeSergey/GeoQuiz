package com.bignerdranch.android.geoquiz;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    private TextView mQuestionTextView;
    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };


    private int mCurrentIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.question_text_view);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
                                     });
        mQuestionTextView = findViewById(R.id.question_text_view);
        Button trueButton = findViewById(R.id.true_button);
        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });
        Button falseButton = findViewById(R.id.false_button);
        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);


            }
        });
        Button nextButton = findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });
        Button prevButton = findViewById(R.id.prev_button);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentIndex==0)  mCurrentIndex=mQuestionBank.length-1;
                mCurrentIndex = (mCurrentIndex - 1);
                updateQuestion();
            }
        });
        updateQuestion();}

    private void updateQuestion() {
            int question = mQuestionBank[mCurrentIndex].getTextResId();
            mQuestionTextView.setText(question);
        }
    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;
        if (userPressedTrue == answerIsTrue) {
            messageResId = R.string.correct_toast;
        } else {
            messageResId = R.string.incorrect_toast;
        }
        Toast answer =Toast.makeText(this, messageResId, Toast.LENGTH_SHORT);
        answer.setGravity(Gravity.TOP, 0, 0);
        answer.show();
    }
    }
