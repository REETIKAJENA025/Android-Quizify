package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import nl.dionsegijn.konfetti.xml.KonfettiView;
import nl.dionsegijn.konfetti.core.Party;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.Position;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.core.models.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ResultActivity extends AppCompatActivity {

    private TextView scoreText, percentText, messageText;
    private Button playAgainBtn;
    private KonfettiView konfettiView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // UI Elements
        scoreText = findViewById(R.id.scoreText);
        percentText = findViewById(R.id.percentText);
        messageText = findViewById(R.id.messageText);
        playAgainBtn = findViewById(R.id.playAgainBtn);
        konfettiView = findViewById(R.id.konfettiView);

        // Get data from Intent
        int score = getIntent().getIntExtra("score", 0);
        int total = getIntent().getIntExtra("total", 0);
        String category = getIntent().getStringExtra("category");

        // Calculate and show score
        scoreText.setText("Your Score: " + score + "/" + total);
        int percent = (total == 0) ? 0 : (int) Math.round((score * 100.0) / total);
        percentText.setText(percent + "%");

        // Show a message based on score
        if (percent == 100) {
            messageText.setText("🏆 Perfect! You're a genius in " + category + "!");
        } else if (percent >= 80) {
            messageText.setText("🎉 Great job in " + category + "! Almost perfect!");
        } else if (percent >= 50) {
            messageText.setText("🙂 Nice attempt in " + category + " — keep practicing!");
        } else {
            messageText.setText("😅 Don’t worry! Try again and improve in " + category + "!");
        }

        // 🎊 Show confetti animation only for 50% or higher
        if (percent >= 50) {
            konfettiView.setVisibility(View.VISIBLE);

            int[] colorArray = getResources().getIntArray(R.array.konfetti_colors);
            List<Integer> colors = new ArrayList<>();
            for (int c : colorArray) colors.add(c);

            EmitterConfig emitterConfig = new Emitter(5, TimeUnit.SECONDS).max(500);

            Party party = new PartyFactory(emitterConfig)
                    .spread(360)
                    .position(new Position.Relative(0.5, 0.0))
                    .sizes(new Size(12, 5f, 0.2f))
                    .shapes(Shape.Square.INSTANCE, Shape.Circle.INSTANCE)
                    .colors(colors)
                    .fadeOutEnabled(true)
                    .timeToLive(3000)
                    .build();

            konfettiView.start(party);
        } else {
            konfettiView.setVisibility(View.GONE);
        }

        // 🔁 Play Again button -> back to Home screen
        playAgainBtn.setOnClickListener(v -> {
            Intent i = new Intent(ResultActivity.this, WelcomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();

        });
    }
}
