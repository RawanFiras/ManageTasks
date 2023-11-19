package edu.cs.birzeit.manegetasks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String PREF_NAME = "TaskManagerPrefs";
    private static final String TASK_LIST_KEY = "taskList";

    public static ArrayList<String> taskList;
    public static ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView taskListView = findViewById(R.id.taskListView);
        Button addButton = findViewById(R.id.addButton);

        loadTasksFromSharedPreferences();


        adapter = new ArrayAdapter<>(this, R.layout.list_item_task, R.id.taskTextView, taskList);

        taskListView.setAdapter(adapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch the AddTaskActivity for adding a new task
                Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
                startActivityForResult(intent, 1);
            }
        });


        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBox = view.findViewById(R.id.taskCheckBox);


                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // If the checkbox is checked, remove the task from the list
                        if (checkBox.isChecked()) {
                            taskList.remove(position);
                            adapter.notifyDataSetChanged();
                            saveTasksToSharedPreferences();
                        }
                    }
                });

                checkBox.setChecked(!checkBox.isChecked());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 1 && resultCode == RESULT_OK) {

            String newTask = data.getStringExtra("newTask");

            if (newTask != null && !newTask.isEmpty()) {
                taskList.add(newTask);
                adapter.notifyDataSetChanged();
                saveTasksToSharedPreferences();
            }
        }
    }

    private void loadTasksFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String jsonTasks = sharedPreferences.getString(TASK_LIST_KEY, null);

        if (jsonTasks != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            taskList = gson.fromJson(jsonTasks, type);
        } else {
            taskList = new ArrayList<>();
        }
    }

    private void saveTasksToSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String jsonTasks = gson.toJson(taskList);

        editor.putString(TASK_LIST_KEY, jsonTasks);
        editor.apply();
    }
}
