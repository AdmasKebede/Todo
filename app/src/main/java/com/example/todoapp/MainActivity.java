package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button addItem;
    EditText userInput;
    ListView listView;
    String oldItem, newItem;
    ArrayList<String> todoList = new ArrayList<>();
    String FILE_NAME = "TodoList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addItem = (Button) findViewById(R.id.addItem);
        userInput = (EditText) findViewById(R.id.todoInput);
        listView = (ListView) findViewById(R.id.listView);
        oldItem = getIntent().getStringExtra("OLD_ITEM");
        newItem = getIntent().getStringExtra("NEW_ITEM");

        todoList = readFromFile();
        ArrayAdapter adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.list_view, todoList);
        if(!todoList.isEmpty()) {listView.setAdapter(adapter);}

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userInput.getText().toString() != null) {
                    todoList.add(userInput.getText().toString());
                    writeToFile(todoList);
                    userInput.setText("");
                    listView.setAdapter(adapter);
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EditItem.class);
                intent.putExtra("OLD_ITEM", todoList.get(position));
                startActivity(intent);

            }
        });


    }
    public void writeToFile(ArrayList<String> arrayList){
        JSONArray jsonArray = new JSONArray();
        for(int i=0;i<arrayList.size();i++){
            jsonArray.put(arrayList.get(i));
        }

        String userString=jsonArray.toString();
        File file = new File(MainActivity.this.getFilesDir(),FILE_NAME);
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(userString);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public ArrayList<String> readFromFile(){
        JSONArray newData = new JSONArray();
        File file = new File(MainActivity.this.getFilesDir(),FILE_NAME);
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuilder stringBuilder = new StringBuilder();
        try {

            String line = bufferedReader.readLine();
            while (line != null){
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            newData = new JSONArray(stringBuilder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<String> todoList = new ArrayList<>();
        for(int i=0;i<newData.length();i++){
            try {
                todoList.add(newData.get(i).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return todoList;
    }


}

























