package com.bignerdranch.android.geoquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {
    //объявляю ключи для класса интент и бандл.
    private static final String EXTRA_ANSWER_IS_TRUE =
            "com.bignerdranch.android.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN =
            "com.bignerdranch.android.geoquiz.answer_shown";
    private static final String ANSWER_WAS_SHOWN = "AnswerWasShown";
    //объявляю переменную в которую записываю подсмотренный из класса МайнАктивити ответ
    private boolean mAnswerIsTrue;
    //объявляю переменную "был ли подсмотрен ответ" для записи в бандл"
    private boolean answerWasShown;
    //объявляю объект класса ТекстВью
    private TextView mAnswerTextView;
    //Объявляю объект класса Батн
    private Button mShowAnswerButton;
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
// Переопределяю метод он криейт.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            answerWasShown=savedInstanceState.getBoolean(ANSWER_WAS_SHOWN);
        }
        setAnswerShownResult(answerWasShown);
        // Ссылаюсь на макет активности
        setContentView(R.layout.activity_cheat);
        // Записываю результат(верный ответ из класса МайнАктивити) полученный из интента в переменную
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
        //ссылаюсь на текстовое поле макета активити чит
        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);
        //ссылаюсь на кнопку макета активитичит
        mShowAnswerButton = (Button) findViewById(R.id.show_answer_button);
        //задаю слушателя кнопке, при нажатии на которую в текстовом поле выводится наименование
        //кнопок тру/или фэлс заданных в макете активити майн и вызываю метод ансверШоуРезульт,
        //передав в него значение тру.
        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAnswerIsTrue) {
                    mAnswerTextView.setText(R.string.true_button);
                } else {
                    mAnswerTextView.setText(R.string.false_button);
                }
                answerWasShown=true;
                setAnswerShownResult(answerWasShown);
            }

        });
    }
    //Сохраняю значения переменных в объект класса Бандл, передающийся в метод криейт при запуске приложения
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(ANSWER_WAS_SHOWN,answerWasShown);

    }
    //Создаю метод ансвер Шоу результ, в котором создаю интент передающий значение был ли подсмотрен
    //ответ.
        private void setAnswerShownResult(boolean isAnswerShown) {
            Intent data = new Intent();
            data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
            setResult(RESULT_OK, data);
    }
}