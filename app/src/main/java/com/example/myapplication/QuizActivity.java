package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

public class QuizActivity extends AppCompatActivity {

    private static final int QUESTION_COUNT = 10;
    String name ="";

    private TextView questionText, questionProgress, previous;
    private RadioGroup optionsGroup;
    private RadioButton[] optionButtons = new RadioButton[4];
    private Button nextButton;

    private int currentQuestionIndex = 0;
    private int score = 0;
    private int[] selectedOptions = new int[QUESTION_COUNT];
    private boolean[] isScored = new boolean[QUESTION_COUNT];

    private final String[] questions = {
            "What's the main source of stress at FAST?",
            "Who 'designed' the university's website?",
            "What's the most frequent question on campus?",
            "Where does all the money go at FAST?",
            "What's the most common excuse for missing class?",
            "What’s FAST’s primary method of communication?",
            "What’s the average response time for emails?",
            "What's the most common subject students fail?",
            "Who’s the 'rarest' thing in the cafeteria?",
            "Slowest thing at FAST?"
    };

    private final String[][] options = {
            {"Assignments", "Group Projects", "Wi-Fi Issues", "Parking"},
            {"A time-traveling intern", "The IT Department", "No one knows", "Random CS grad"},
            {"Where’s my parking?", "When’s the next exam?", "What’s for lunch?", "Who’s making shitty timetables?"},
            {"Down the Drain", "Faculty Salaries", "Cafeteria Snacks", "Lecture Halls"},
            {"Traffic", "I overslept", "Forgot the time", "Better Call Saul(Youtube)"},
            {"DC Notice", "Bulletin Boards", "WhatsApp Groups", "The Professor’s Mood"},
            {"3 days", "1 week", "Year", "Never"},
            {"Advanced Math", "Group Projects", "Coffee Making", "Anything by Malik sb"},
            {"The Chef", "The Invisible Staff", "That One Stranger", "Delicious Food"},
            {"Course Registration", "Random cats", "GPA increase", "Wifi"}
    };

    private final int[] correctAnswers = {3, 2, 3, 0, 3, 0, 3, 3, 3, 0};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name = getIntent().getStringExtra("userName");
        setContentView(R.layout.activity_quiz);

        initializeViews();
        resetSelections();
        loadQuestion();

        nextButton.setOnClickListener(view -> handleNextQuestion());
        previous.setOnClickListener(view -> handlePreviousQuestion());
    }

    private void initializeViews() {
        questionText = findViewById(R.id.question_text);
        questionProgress = findViewById(R.id.progress);
        optionsGroup = findViewById(R.id.options_group);
        nextButton = findViewById(R.id.next_button);
        previous = findViewById(R.id.previous);

        optionButtons[0] = findViewById(R.id.option_1);
        optionButtons[1] = findViewById(R.id.option_2);
        optionButtons[2] = findViewById(R.id.option_3);
        optionButtons[3] = findViewById(R.id.option_4);

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
        optionsGroup.clearCheck();

        for (int i = 0; i < optionButtons.length; i++) {
            optionButtons[i].setText(options[currentQuestionIndex][i]);
            optionButtons[i].setChecked(false);
            optionButtons[i].setEnabled(true);
            optionButtons[i].setClickable(true);
        }

        resetOptionStyles();
        String res = (currentQuestionIndex + 1) + "/" + QUESTION_COUNT;
        questionProgress.setText(res);
        if (currentQuestionIndex == QUESTION_COUNT - 1) {
            nextButton.setText(R.string.finish);
        } else {
            nextButton.setText(R.string.next);
        }

        if (selectedOptions[currentQuestionIndex] != -1) {

            int selectedOption = selectedOptions[currentQuestionIndex];
            optionButtons[selectedOption].setChecked(true);
            highlightSelectedOption(selectedOption);

            for (int i = 0 ; i < 4 ; i++){
                optionButtons[i].setClickable(false);
            }
        }

    }

    private void selectOption(int optionIndex) {
        if (selectedOptions[currentQuestionIndex] != -1) return; // Prevent re-selection

        selectedOptions[currentQuestionIndex] = optionIndex;
        highlightSelectedOption(optionIndex);

        //disabling because its a quiz
        for (RadioButton button : optionButtons) {
            button.setEnabled(false);
            button.setClickable(false);
        }

        if (!isScored[currentQuestionIndex] && optionIndex == correctAnswers[currentQuestionIndex]) {
            score++;
            isScored[currentQuestionIndex] = true;
        }
    }

    private void handleNextQuestion() {


        // comment this if we want a redo of choice
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
        intent.putExtra("userName", name);

        startActivity(intent);
        finish();
    }

    private void highlightSelectedOption(int optionIndex) {
        resetOptionStyles();

        RadioButton selectedButton = optionButtons[optionIndex];

        int correctColor = Color.parseColor("#abd1c6");  // green for correct answer
        int incorrectColor = Color.parseColor("#FF6B6B"); // red for incorrect answer

        boolean isCorrect = (optionIndex == correctAnswers[currentQuestionIndex]);
        int backgroundColor = isCorrect ? correctColor : incorrectColor;

        setRadioButtonStyle(selectedButton, backgroundColor, Color.WHITE);
    }

    private void resetOptionStyles() {
        for (RadioButton button : optionButtons) {
            setRadioButtonStyle(button, Color.WHITE, Color.BLACK);
        }
    }

    private void setRadioButtonStyle(RadioButton button, int backgroundColor, int textColor) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(backgroundColor);
        drawable.setCornerRadius(50);
        button.setBackground(drawable);
        drawable.setCornerRadius(50);
        button.setTextColor(textColor);
    }
}
