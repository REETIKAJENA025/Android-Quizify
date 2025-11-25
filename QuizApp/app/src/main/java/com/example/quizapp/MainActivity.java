package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import android.view.animation.AlphaAnimation;
import android.content.Intent;
import android.os.CountDownTimer;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    TextView questionText, scoreText, timerText;
    RadioGroup optionsGroup;
    RadioButton option1, option2, option3, option4;
    Button nextBtn;
    ProgressBar progressBar;

    int score = 0;
    int currentQuestion = 0;
    ArrayList<Integer> order;
    CountDownTimer countDownTimer;

    final long QUESTION_TIME_MS = 12_000; // 12 seconds per question
    final long TICK_MS = 1000;

    String category = "GK";
    String[][][] allOptions;
    String[][] allQuestions;
    int[][] allCorrectAnswers;

    String[] questions;
    String[][] options;
    int[] correctAnswers;

    final int QUIZ_SIZE = 7; // ✅ only 7 random questions per play

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ---- Link UI ----
        questionText = findViewById(R.id.questionText);
        scoreText = findViewById(R.id.scoreText);
        timerText = findViewById(R.id.timerText);
        optionsGroup = findViewById(R.id.optionsGroup);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        nextBtn = findViewById(R.id.nextBtn);
        progressBar = findViewById(R.id.progressBar);

        // ---- Receive category from WelcomeActivity ----
        category = getIntent().getStringExtra("category");
        if (category == null) category = "GK";

        setupAllQuestions();
        loadCategoryQuestions();

        // ---- Shuffle order and pick only 7 random questions ----
        order = new ArrayList<>();
        for (int i = 0; i < questions.length; i++) order.add(i);
        Collections.shuffle(order);
        if (order.size() > QUIZ_SIZE) order = new ArrayList<>(order.subList(0, QUIZ_SIZE));

        progressBar.setMax(order.size());
        loadQuestion();

        nextBtn.setOnClickListener(v -> {
            if (optionsGroup.getCheckedRadioButtonId() == -1) {
                Toast.makeText(MainActivity.this, "Please select an option!", Toast.LENGTH_SHORT).show();
                return;
            }

            stopTimer();
            boolean correct = checkAnswerAndUpdateScore();
            int qIndex = order.get(currentQuestion);

            if (correct) {
                Toast.makeText(MainActivity.this, "✅ Correct!", Toast.LENGTH_SHORT).show();
            } else {
                String rightAnswer = options[qIndex][correctAnswers[qIndex]];
                Toast.makeText(MainActivity.this, "❌ Wrong! Correct: " + rightAnswer, Toast.LENGTH_SHORT).show();
            }

            currentQuestion++;
            if (currentQuestion < order.size()) {
                loadQuestion();
                animateQuestion();
            } else {
                goToResult();
            }
        });
    }

    // -----------------------------
    // QUESTION SETUP
    // -----------------------------
    void setupAllQuestions() {
        // ---- GK ----
        String[] gkQuestions = {
                "What is the capital of France?", "Who developed Java?", "Which planet is known as the Red Planet?",
                "What is the national animal of India?", "Who is known as the father of computers?",
                "What is the largest ocean on Earth?", "In which year did India get independence?",
                "Which continent is known as the Dark Continent?", "Who wrote the national anthem of India?",
                "What is the tallest mountain in the world?", "Which country is called the Land of the Rising Sun?",
                "Who was the first President of India?", "What is the largest desert in the world?",
                "Which animal is known as the Ship of the Desert?", "What is the currency of Japan?",
                "Who invented the telephone?", "Which planet is closest to the Sun?",
                "What is the smallest country in the world?", "Who painted the Mona Lisa?",
                "Which is the longest river in the world?"
        };
        String[][] gkOptions = {
                {"Paris", "London", "Rome", "Berlin"},
                {"Elon Musk", "James Gosling", "Bill Gates", "Mark Zuckerberg"},
                {"Venus", "Mars", "Jupiter", "Saturn"},
                {"Peacock", "Tiger", "Lion", "Elephant"},
                {"Charles Babbage", "Albert Einstein", "Isaac Newton", "Alan Turing"},
                {"Pacific Ocean", "Atlantic Ocean", "Indian Ocean", "Arctic Ocean"},
                {"1945", "1947", "1950", "1960"},
                {"Asia", "Africa", "Europe", "Australia"},
                {"Rabindranath Tagore", "Mahatma Gandhi", "Jawaharlal Nehru", "Sardar Patel"},
                {"K2", "Everest", "Kilimanjaro", "Denali"},
                {"China", "Japan", "Thailand", "Korea"},
                {"Dr. Rajendra Prasad", "Jawaharlal Nehru", "S. Radhakrishnan", "APJ Abdul Kalam"},
                {"Sahara", "Gobi", "Thar", "Arabian"},
                {"Camel", "Horse", "Elephant", "Donkey"},
                {"Yen", "Won", "Peso", "Euro"},
                {"Alexander Graham Bell", "Thomas Edison", "Nikola Tesla", "Newton"},
                {"Mercury", "Venus", "Earth", "Mars"},
                {"Vatican City", "Monaco", "Nauru", "Malta"},
                {"Leonardo da Vinci", "Pablo Picasso", "Vincent van Gogh", "Michelangelo"},
                {"Nile", "Amazon", "Yangtze", "Ganga"}
        };
        int[] gkCorrect = {0, 1, 1, 1, 0, 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        // ---- Math ----
        String[] mathQuestions = {
                "What is 5 + 3?", "What is 9 × 9?", "What is the square root of 64?", "What is 12 ÷ 4?",
                "What is 15 - 7?", "What is the cube of 3?", "What is 10% of 200?", "What is 7²?",
                "What is 100 ÷ 25?", "What is the value of π (approx)?", "What is 8 × 6?", "What is ½ of 50?",
                "What is 3³?", "What is 45 + 55?", "What is 11 × 11?", "What is 20% of 150?", "What is 14 - 6?",
                "What is 60 ÷ 5?", "What is 25 + 75?", "What is the square of 12?"
        };
        String[][] mathOptions = {
                {"5", "6", "7", "8"}, {"72", "81", "99", "90"}, {"6", "7", "8", "9"}, {"2", "3", "4", "5"},
                {"5", "7", "8", "9"}, {"9", "27", "18", "30"}, {"10", "15", "20", "25"}, {"42", "49", "56", "64"},
                {"3", "4", "5", "6"}, {"3.14", "2.71", "1.41", "1.73"}, {"36", "42", "48", "54"},
                {"10", "20", "25", "30"}, {"9", "18", "27", "36"}, {"80", "90", "100", "110"},
                {"100", "110", "121", "132"}, {"20", "25", "30", "35"}, {"6", "8", "10", "12"},
                {"10", "12", "15", "20"}, {"80", "90", "100", "110"}, {"124", "144", "164", "100"}
        };
        int[] mathCorrect = {3, 1, 2, 2, 2, 1, 2, 1, 1, 0, 1, 2, 2, 2, 2, 1, 1, 1, 2, 1};

        // ---- Science ----
        String[] scienceQuestions = {
                "What is H₂O commonly known as?", "Which gas do plants release during photosynthesis?",
                "What planet is known for its rings?", "What part of the cell contains DNA?",
                "What is the powerhouse of the cell?", "Which organ purifies blood in the human body?",
                "Which force keeps us on the ground?", "What is the boiling point of water?",
                "Which planet is closest to the Sun?", "Which organ helps us breathe?",
                "What is the main gas in the Earth's atmosphere?", "What vitamin do we get from sunlight?",
                "What is the human body's largest organ?", "Which blood cells fight infections?",
                "Which metal is liquid at room temperature?", "What gas do humans exhale?",
                "Which planet is called the Blue Planet?", "What part of the plant makes food?",
                "Which organ pumps blood in the body?", "Which part of the eye controls light entry?"
        };
        String[][] scienceOptions = {
                {"Hydrogen", "Oxygen", "Water", "Carbon Dioxide"},
                {"Oxygen", "Carbon Dioxide", "Nitrogen", "Hydrogen"},
                {"Mars", "Jupiter", "Saturn", "Neptune"},
                {"Nucleus", "Cytoplasm", "Mitochondria", "Ribosome"},
                {"Heart", "Mitochondria", "Lungs", "Kidney"},
                {"Kidney", "Liver", "Heart", "Brain"},
                {"Magnetism", "Gravity", "Air Pressure", "Friction"},
                {"90°C", "95°C", "100°C", "105°C"},
                {"Mercury", "Venus", "Earth", "Mars"},
                {"Heart", "Lungs", "Kidneys", "Brain"},
                {"Oxygen", "Nitrogen", "Carbon Dioxide", "Hydrogen"},
                {"A", "B", "C", "D"},
                {"Skin", "Heart", "Liver", "Brain"},
                {"Red", "White", "Platelets", "Plasma"},
                {"Mercury", "Gold", "Silver", "Iron"},
                {"Oxygen", "Carbon Dioxide", "Nitrogen", "Helium"},
                {"Earth", "Mars", "Neptune", "Venus"},
                {"Root", "Stem", "Leaf", "Flower"},
                {"Heart", "Lungs", "Liver", "Kidneys"},
                {"Cornea", "Pupil", "Lens", "Retina"}
        };
        int[] scienceCorrect = {2, 0, 2, 0, 1, 0, 1, 2, 0, 1, 1, 2, 0, 1, 0, 1, 0, 2, 0, 1};

        allQuestions = new String[][]{gkQuestions, mathQuestions, scienceQuestions};
        allOptions = new String[][][]{gkOptions, mathOptions, scienceOptions};
        allCorrectAnswers = new int[][]{gkCorrect, mathCorrect, scienceCorrect};
    }

    // -----------------------------
    // CATEGORY HANDLER
    // -----------------------------
    void loadCategoryQuestions() {
        String lower = category.toLowerCase();
        int index;
        if (lower.contains("math")) index = 1;
        else if (lower.contains("science")) index = 2;
        else index = 0;
        questions = allQuestions[index];
        options = allOptions[index];
        correctAnswers = allCorrectAnswers[index];
    }

    // -----------------------------
    // QUESTION DISPLAY
    // -----------------------------
    void loadQuestion() {
        progressBar.setProgress(currentQuestion + 1);
        optionsGroup.clearCheck();
        int qIndex = order.get(currentQuestion);
        questionText.setText(questions[qIndex]);
        option1.setText(options[qIndex][0]);
        option2.setText(options[qIndex][1]);
        option3.setText(options[qIndex][2]);
        option4.setText(options[qIndex][3]);
        updateScoreText();
        startTimer();
    }

    void startTimer() {
        stopTimer();
        countDownTimer = new CountDownTimer(QUESTION_TIME_MS, TICK_MS) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerText.setText(String.format("00:%02d", millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                timerText.setText("00:00");
                Toast.makeText(MainActivity.this, "⏰ Time's up!", Toast.LENGTH_SHORT).show();
                int qIndex = order.get(currentQuestion);
                String rightAnswer = options[qIndex][correctAnswers[qIndex]];
                Toast.makeText(MainActivity.this, "Correct: " + rightAnswer, Toast.LENGTH_SHORT).show();
                currentQuestion++;
                if (currentQuestion < order.size()) {
                    loadQuestion();
                    animateQuestion();
                } else goToResult();
            }
        }.start();
    }

    void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    boolean checkAnswerAndUpdateScore() {
        int selectedId = optionsGroup.getCheckedRadioButtonId();
        int selectedIndex = -1;
        if (selectedId == R.id.option1) selectedIndex = 0;
        else if (selectedId == R.id.option2) selectedIndex = 1;
        else if (selectedId == R.id.option3) selectedIndex = 2;
        else if (selectedId == R.id.option4) selectedIndex = 3;

        int qIndex = order.get(currentQuestion);
        if (selectedIndex == correctAnswers[qIndex]) {
            score++;
            updateScoreText();
            return true;
        }
        updateScoreText();
        return false;
    }

    void updateScoreText() {
        int total = order.size();
        int percent = (int) Math.round(((double) score * 100) / total);
        scoreText.setText("Score: " + score + "/" + total + " (" + percent + "%)");
    }

    void animateQuestion() {
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(350);
        questionText.startAnimation(fadeIn);
    }

    void goToResult() {
        stopTimer();
        Intent i = new Intent(MainActivity.this, ResultActivity.class);
        i.putExtra("score", score);
        i.putExtra("total", order.size()); // ✅ show 7
        i.putExtra("category", category);
        startActivity(i);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
    }
}
