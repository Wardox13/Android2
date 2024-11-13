package com.example.agps3android2;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button trueButton;
    private Button falseButton;
    private Button nextButton;
    private Button promptButton;
    private TextView questionTextView;
    private TextView scoreTextView; // Pole tekstowe do wyświetlania wyniku po zakończeniu quizu
    private static final String QUIZ_TAG = "ZADANIE";
    private static final String KEY_CURRENT_INDEX = "currentIndex";
    public static final String KEY_EXTRA_ANSWER = "pl.edu.pb.wi.quiz.correctAnswer";
    private static final int REQUEST_CODE_PROMPT = 0;
    private boolean answerWasShown = false;

    private Question[] questions = new Question[] {
            new Question(R.string.q_activity, true),
            new Question(R.string.q_find_resources, false),
            new Question(R.string.q_listener, true),
            new Question(R.string.q_resources, true),
            new Question(R.string.q_version, false),
    };

    private int currentIndex = 0;
    private int score = 0;
    private boolean[] answeredQuestions;

    private void checkAnswerCorrectness(boolean userAnswer) {
        if (!answeredQuestions[currentIndex]) {
            boolean correctAnswer = questions[currentIndex].isTrueAnswer();
            int resultMessageId = 0;
            if (answerWasShown) {
                resultMessageId = R.string.answer_was_shown;
            }else {
                if (userAnswer == correctAnswer) {
                    resultMessageId = R.string.correct_answer;
                    score++;
                } else {
                    resultMessageId = R.string.incorrect_answer;
                }
            }
            answeredQuestions[currentIndex] = true;
            Toast.makeText(this, resultMessageId, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.already_answered, Toast.LENGTH_SHORT).show();
        }
    }

    private void setNextQuestion() {
        if (currentIndex == 0) {
            answeredQuestions = new boolean[questions.length]; // Resetowanie tablicy przy nowym quizie
        }

        if (currentIndex < questions.length) {
            questionTextView.setText(questions[currentIndex].getQuestionId());
        } else {
            // Wyświetlenie wyniku po zakończeniu quizu
            questionTextView.setText(R.string.quiz_finished);
            scoreTextView.setText("Wynik końcowy: " + score + "/" + questions.length);
            trueButton.setEnabled(false);
            falseButton.setEnabled(false);
            nextButton.setEnabled(false);
            promptButton.setEnabled(false);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(QUIZ_TAG, "Wywołana zostałą metoda: onSaveInstanceState");
        outState.putInt(KEY_CURRENT_INDEX, currentIndex);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(QUIZ_TAG,"Used onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(QUIZ_TAG,"Used onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(QUIZ_TAG,"Used onDestroy");

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(QUIZ_TAG,"Used onStart");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(QUIZ_TAG,"Used onStop");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK){ return; }
        if (resultCode != REQUEST_CODE_PROMPT){
            if (data == null){return;}
            answerWasShown = data.getBooleanExtra(PromptActivity.KEY_EXTRA_ANSWER_SHOWN, false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(QUIZ_TAG,"Used onCreate");
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null){
            currentIndex = savedInstanceState.getInt(KEY_CURRENT_INDEX);
        }

        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        nextButton = findViewById(R.id.next_button);
        questionTextView = findViewById(R.id.question_text_view);
        scoreTextView = findViewById(R.id.score_text_view);
        promptButton = findViewById(R.id.hint_button);

        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswerCorrectness(true);
            }
        });

        promptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PromptActivity.class);
                boolean correctAnswer = questions[currentIndex].isTrueAnswer();
                intent.putExtra(KEY_EXTRA_ANSWER, correctAnswer);
                startActivityForResult(intent, REQUEST_CODE_PROMPT);
            }
        });

        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswerCorrectness(false);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex = (currentIndex + 1);
                answerWasShown = false;
                setNextQuestion();
            }
        });
        setNextQuestion();
    }
}
