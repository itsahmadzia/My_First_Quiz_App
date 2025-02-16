package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class QuizActivity extends AppCompatActivity {

    private static final int QUESTION_COUNT = 10;

    private TextView questionText, questionProgress;
    private Button[] optionButtons = new Button[4];
    private Button nextButton, prevButton;

    private int currentQuestionIndex = 0;
    private int score = 0;
    private int[] selectedOptions = new int[QUESTION_COUNT];
    private boolean[] isScored = new boolean[QUESTION_COUNT];

    private final String[] questions = {
            "What is the capital of France?", "Who developed Java?", "What is 2 + 2?",
            "Which planet is known as the Red Planet?", "What is the largest ocean on Earth?",
            "Who wrote 'Hamlet'?", "What is the boiling point of water?",
            "Which element has the chemical symbol O?", "Who painted the Mona Lisa?",
            "What is the currency of Japan?"
    };

    private final String[][] options = {
            {"Paris", "London", "Berlin", "Rome"},
            {"James Gosling", "Dennis Ritchie", "Bjarne Stroustrup", "Guido van Rossum"},
            {"3", "4", "5", "6"},
            {"Venus", "Mars", "Jupiter", "Saturn"},
            {"Atlantic", "Indian", "Pacific", "Arctic"},
            {"Shakespeare", "Charles Dickens", "Mark Twain", "Jane Austen"},
            {"50°C", "100°C", "150°C", "200°C"},
            {"Oxygen", "Gold", "Silver", "Iron"},
            {"Da Vinci", "Van Gogh", "Picasso", "Michelangelo"},
            {"Yen", "Dollar", "Euro", "Rupee"}
    };

    private final int[] correctAnswers = {0, 0, 1, 1, 2, 0, 1, 0, 0, 0};

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        initializeViews();
        resetSelections();
        loadQuestion();

        nextButton.setOnClickListener(view -> handleNextQuestion());
        prevButton.setOnClickListener(view -> handlePreviousQuestion());
    }

    private void initializeViews() {
        questionText = findViewById(R.id.question_text);
        questionProgress = findViewById(R.id.question_progress);

        optionButtons[0] = findViewById(R.id.option_1);
        optionButtons[1] = findViewById(R.id.option_2);
        optionButtons[2] = findViewById(R.id.option_3);
        optionButtons[3] = findViewById(R.id.option_4);
        nextButton = findViewById(R.id.next_button);
        prevButton = findViewById(R.id.previous_button);

        for (int i = 0; i < optionButtons.length; i++) {
            final int index = i;
            optionButtons[i].setOnClickListener(view -> selectOption(index));
        }
    }

    private void resetSelections() {
        for (int i = 0; i < QUESTION_COUNT; i++) {
            selectedOptions[i] = -1;
            isScored[i] = false;
        }
    }

    private void loadQuestion() {
        questionText.setText(questions[currentQuestionIndex]);
        for (int i = 0; i < optionButtons.length; i++) {
            optionButtons[i].setText(options[currentQuestionIndex][i]);
        }

        resetOptionStyles();
        questionProgress.setText((currentQuestionIndex + 1) + "/" + QUESTION_COUNT);

        if (selectedOptions[currentQuestionIndex] != -1) {
            highlightSelectedOption(selectedOptions[currentQuestionIndex]);
        }
    }

    private void selectOption(int optionIndex) {
        if (selectedOptions[currentQuestionIndex] != -1) return;

        selectedOptions[currentQuestionIndex] = optionIndex;
        highlightSelectedOption(optionIndex);

        if (!isScored[currentQuestionIndex] && optionIndex == correctAnswers[currentQuestionIndex]) {
            score++;
            isScored[currentQuestionIndex] = true;
        }
    }

    private void handleNextQuestion() {
        if (selectedOptions[currentQuestionIndex] == -1) {
            Toast.makeText(this, "Please select an option", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentQuestionIndex < QUESTION_COUNT - 1) {
            currentQuestionIndex++;
            loadQuestion();
        } else {
            goToResults();
        }
    }

    private void handlePreviousQuestion() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            loadQuestion();
        }
    }

    private void goToResults() {
        Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
        intent.putExtra("finalScore", score);
        startActivity(intent);
        finish();
    }

    private void highlightSelectedOption(int optionIndex) {
        resetOptionStyles();

        Button selectedButton = optionButtons[optionIndex];

        int correctColor = Color.parseColor("#4CAF50");  // Soft green for correct answers
        int incorrectColor = Color.parseColor("#FF6B6B"); // Soft red for incorrect answers

        boolean isCorrect = (optionIndex == correctAnswers[currentQuestionIndex]);
        int backgroundColor = isCorrect ? correctColor : incorrectColor;
        String tick = isCorrect ? "✔" : "❌";

        setButtonStyle(selectedButton, backgroundColor, Color.WHITE, tick);
    }

    private void resetOptionStyles() {
        for (Button button : optionButtons) {
            setButtonStyle(button, Color.LTGRAY, Color.BLACK, "");
        }
    }

    private void setButtonStyle(Button button, int backgroundColor, int textColor, String tick) {
        button.setBackgroundColor(backgroundColor);
        button.setTextColor(textColor);
        button.setText(button.getText().toString() + " " + tick);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(backgroundColor);
        drawable.setCornerRadius(20);
        button.setBackground(drawable);
    }
}
