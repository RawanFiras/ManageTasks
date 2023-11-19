package edu.cs.birzeit.manegetasks;


import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AddTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        EditText taskEditText = findViewById(R.id.taskEditText);
        Button saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(view -> {
            String newTask = taskEditText.getText().toString();

            if (!newTask.isEmpty()) {
                // Create an Intent to send back the new task
                Intent resultIntent = new Intent();
                resultIntent.putExtra("newTask", newTask);
                setResult(RESULT_OK, resultIntent);

                // Finish the activity
                finish();
            }
        });
    }
}