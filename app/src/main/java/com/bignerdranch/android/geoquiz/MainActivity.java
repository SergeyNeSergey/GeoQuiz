package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    //Задаю ключи для объекта класса бандл.и класса интент

    private static final String TAG = "MainActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_CORRECT_ANSWERS = "correct";
    private static final String KEY_TOTAL_ANSWERS = "total";
    private static final String KEY_CHECK_ARRAY = "intArray";
    private static final String KEY_ARRAY_OF_CHEAT = "cheatArray";
    private static final String BOOLEAN_CHEAT = "bcheat";

    private static final int REQUEST_CODE_CHEAT = 0;
    // Объявляю кнопки тру/фолс и кнопку чит активности.
    Button trueButton;
    Button falseButton;
    private Button mCheatButton;
    //объявляю массив, заполненный нулями, длинной равной длинне массива с вопросами.
    private int[] checkIndex = {0, 0, 0, 0, 0, 0};
    //Объявляю индекс вопроса на который подсмотрели ответ
    private int [] cheatQuestionIndex= {0, 0, 0, 0, 0, 0};
    //Задаю переменную в которую записываю количество верных ответов
    private int correctAnswer = 0;
    //задаю переменную с общим колличеством ответов
    private int totalAnswer = 0;
    // объявляю вьюшку для вывода текста вопроса.
    private TextView mQuestionTextView;
    // Задаю массив с вопросами и верными ответами на них, через создание экземпляров вспомогательного
    //класса
    private Question[] mQuestionBank = new Question[]{

            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };
    // Задаю переменную индекса
    private int mCurrentIndex = 0;
    // Объявляю переменную для записи данных о том, был ли подсмотрен ответ.
    private boolean mIsCheater;

    //переопределяю метод он криейт и добавляю в него тестовые логи.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called ");


//ссылаюсь на макет.
        setContentView(R.layout.activity_main);
        // если объект класса бандл не нулевой(то есть приложение уже было запущенно), то заполняю
        //переменные класса сохраненными значениями
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            correctAnswer = savedInstanceState.getInt(KEY_CORRECT_ANSWERS, 0);
            totalAnswer = savedInstanceState.getInt(KEY_TOTAL_ANSWERS, 0);
            checkIndex = savedInstanceState.getIntArray(KEY_CHECK_ARRAY);
            cheatQuestionIndex = savedInstanceState.getIntArray(KEY_ARRAY_OF_CHEAT);
            mIsCheater = savedInstanceState.getBoolean(BOOLEAN_CHEAT,false);
        }
        // ссылаюсь на текстовое поле с вопросом и задаю ему слушателя. При нажатии на поле,
        //увеличиваю индекс вопроса на один и обновляю вопрос. В случае если вопрос последний
        //возвращаюсь в начало.
        TextView textView = findViewById(R.id.question_text_view);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mIsCheater = false;
                updateQuestion();
            }
        });
        mQuestionTextView = findViewById(R.id.question_text_view);
// ссылаюсь на кнопку тру, назначаю слушателя при нажатии на кнопку вызываю метод проверка вопроса
        trueButton = findViewById(R.id.true_button);
        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

// аналогично для кнопки фолс
        falseButton = findViewById(R.id.false_button);
        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);


            }
        });
        // ссылаюсь на кнопку нэкст, при нажатии на которую происходит последовательность действий
        //аналогичная нажатию на текст с вопросом.
        Button nextButton = findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });
        // ссылаюсь на кнопку прев при её нажатии смена вопроса и индекса происходит в обратной
        //последовательности.
        Button prevButton = findViewById(R.id.prev_button);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentIndex == 0) mCurrentIndex = mQuestionBank.length - 1;
                mCurrentIndex = (mCurrentIndex - 1);
                updateQuestion();
            }
        });
//Задаю кнопку чит активности, назначаю ей слушателя и создаю интент.
        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(MainActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
                cheatQuestionIndex[mCurrentIndex]=1;
            }
        });
        updateQuestion();
    }
//
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
    }

    // метод отключает кнопки, если элемент массива с индексом, равным индексу вопроса, равен единице.
    private void setEnableButton(int numberOfQuestion) {
        if (numberOfQuestion != 1) {
            trueButton.setEnabled(true);
            falseButton.setEnabled(true);
        } else {
            trueButton.setEnabled(false);
            falseButton.setEnabled(false);
        }
    }

    // переопределяю метод добавив в него логи.*
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    //аналогично*
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    //аналогично*
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    //Сохраняю значения переменных в объект класса Бандл, передающийся в метод криейт при запуске приложения
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putInt(KEY_CORRECT_ANSWERS, correctAnswer);
        savedInstanceState.putInt(KEY_TOTAL_ANSWERS, totalAnswer);
        savedInstanceState.putIntArray(KEY_CHECK_ARRAY, checkIndex);
        savedInstanceState.putIntArray(KEY_ARRAY_OF_CHEAT, cheatQuestionIndex);
        savedInstanceState.putBoolean(BOOLEAN_CHEAT,mIsCheater);

    }

    //аналогично*
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    //аналогично*
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    //обновляю вопрос. Вызываю метод сэт энэбл
    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
        setEnableButton(checkIndex[mCurrentIndex]);
    }

    // Проверяю конечный результат теста после того, как пользователь дал ответы на все вопросы
    //и вывожу его на экран через объект класса Тост
    private void checkResult(int questionTrue, int allQuestion) {
        double divider = (double) questionTrue / allQuestion;
        String message = "Correct answer=" + divider * 100 + "%";

        Toast finish = Toast.makeText(this, message, Toast.LENGTH_LONG);
        finish.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        finish.show();

    }

    // Проверяю каждый отдельный ответ, вывожу через "Тосты" результат на экран, ссылаясь на файл со
    //строковыми ресурсами. Выключаю кнопки тру и фолс, после каждого вызова метода.
    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;
        if (mIsCheater&&cheatQuestionIndex[mCurrentIndex]==1) {
            messageResId = R.string.judgment_toast;
        } else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }

        checkIndex[mCurrentIndex] = 1;

        if (messageResId == R.string.correct_toast) correctAnswer++;

        totalAnswer += checkIndex[mCurrentIndex];
        if (totalAnswer == checkIndex.length) checkResult(correctAnswer, totalAnswer);

        trueButton.setEnabled(false);
        falseButton.setEnabled(false);
        Toast answer = Toast.makeText(this, messageResId, Toast.LENGTH_SHORT);
        answer.setGravity(Gravity.TOP, 0, 0);
        answer.show();


    }
}
