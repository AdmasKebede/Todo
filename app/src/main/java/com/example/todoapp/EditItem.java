package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class EditItem extends AppCompatActivity {

    String oldItem,newItem;
    EditText userInput;
    Button save;
    String FILE_NAME = "TodoList";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        oldItem = getIntent().getStringExtra("OLD_ITEM");
        userInput = (EditText) findViewById(R.id.userEditInput);
        save = (Button) findViewById(R.id.save);



        userInput.setText(oldItem);
        ArrayList<String> list = readFromFile();


        for(int i=0;i<list.size();i++){
            Log.d("List=============>",list.get(i));
        }
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newItem= userInput.getText().toString();
                Intent intent = new Intent(EditItem.this, MainActivity.class);
                list.set(list.indexOf(oldItem),newItem);
                writeToFile(list);
                startActivity(intent);
            }
        });


    }

    public ArrayList<String> readFromFile(){
        JSONArray newData = new JSONArray();
        File file = new File(EditItem.this.getFilesDir(),FILE_NAME);
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
    public void writeToFile(ArrayList<String> arrayList){
        JSONArray jsonArray = new JSONArray();
        for(int i=0;i<arrayList.size();i++){
            jsonArray.put(arrayList.get(i));
        }

        String userString=jsonArray.toString();
        File file = new File(EditItem.this.getFilesDir(),FILE_NAME);
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
}