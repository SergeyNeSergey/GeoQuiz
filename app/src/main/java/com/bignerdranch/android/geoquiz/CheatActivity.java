package com.bignerdranch.android.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CheatActivity extends AppCompatActivity {
    //объявляю ключи для объектов классов Intent и Bundle.
    private static final String EXTRA_ANSWER_IS_TRUE =
            "com.bignerdranch.android.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN =
            "com.bignerdranch.android.geoquiz.answer_shown";
    private static final String ANSWER_WAS_SHOWN = "AnswerWasShown";
    //объявляю переменную в которую записываю подсмотренный из класса MainActivity ответ
    private boolean mAnswerIsTrue;
    //объявляю переменную "был ли подсмотрен ответ" для записи в Bundle
    private boolean answerWasShown;
    //объявляю объект класса TextView
    private TextView mAnswerTextView;
    private TextView mAPIlevel;
    //Объявляю объект класса Button
    private Button mShowAnswerButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            answerWasShown = savedInstanceState.getBoolean(ANSWER_WAS_SHOWN);
        }
        setAnswerShownResult(answerWasShown);
        // Ссылаюсь на макет активности
        setContentView(R.layout.activity_cheat);
        // Записываю результат(верный ответ из класса MainActivity) полученный из интента в переменную
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
        //ссылаюсь на текстовое поле макета
        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);
        mAPIlevel = (TextView) findViewById(R.id.api_level);
        mAPIlevel.setText("API Level " + Build.VERSION.SDK_INT);
        //ссылаюсь на кнопку макета
        mShowAnswerButton = (Button) findViewById(R.id.show_answer_button);
        //задаю слушателя кнопке, при нажатии на которую в текстовом поле выводится наименование
        //кнопок true_button/false_button заданных в макете activity_main и вызываю метод setAnswerShownResult,
        //передав в него значение true. Кроме того, скрываю кнопку
        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAnswerIsTrue) {
                    mAnswerTextView.setText(R.string.true_button);
                } else {
                    mAnswerTextView.setText(R.string.false_button);
                }
                answerWasShown = true;
                setAnswerShownResult(answerWasShown);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    int cx = mShowAnswerButton.getWidth() / 2;
                    int cy = mShowAnswerButton.getHeight() / 2;
                    float radius = mShowAnswerButton.getWidth();
                    Animator anim = ViewAnimationUtils
                            .createCircularReveal(mShowAnswerButton, cx, cy, radius, 0);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mShowAnswerButton.setVisibility(View.INVISIBLE);
                        }
                    });
                    anim.start();
                } else {
                    mShowAnswerButton.setVisibility(View.INVISIBLE);
                }
            }

        });
    }

    //Сохраняю значения переменных в объект класса Bundle, передающийся в метод onCreate при запуске приложения
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(ANSWER_WAS_SHOWN, answerWasShown);

    }
    //"Конструктор" интента для вызова из сторонней активности
    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }

    // Метод  гетор был ли подсмотрен ответ для вызова в сторонней активности
    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    //Создаю метод, в котором создаю интент передающий значение был ли подсмотрен
    //ответ.
    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }
}