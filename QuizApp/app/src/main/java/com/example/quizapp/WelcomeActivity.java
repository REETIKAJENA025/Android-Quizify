package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Initialize views
        Button startBtn = findViewById(R.id.startBtn);
        RadioGroup categoryGroup = findViewById(R.id.categoryGroup);

        startBtn.setOnClickListener(v -> {
            // Get selected category
            int selectedId = categoryGroup.getCheckedRadioButtonId();

            if (selectedId == -1) {
                Toast.makeText(this, "Please select a category!", Toast.LENGTH_SHORT).show();
            } else {
                RadioButton selectedRadio = findViewById(selectedId);
                String category = selectedRadio.getText().toString();

                // Pass category to MainActivity
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                intent.putExtra("category", category);
                startActivity(intent);
                finish();
            }
        });
    }
}
