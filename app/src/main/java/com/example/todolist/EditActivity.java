package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    EditText updateText;
    Button saveUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        updateText = findViewById(R.id.updateItem);
        saveUpdate = findViewById(R.id.buttonSave);

        getSupportActionBar().setTitle("Edit Item");


        updateText.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));

        saveUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = new Intent();
                result.putExtra(MainActivity.KEY_ITEM_TEXT,updateText.getText().toString());
                result.putExtra(MainActivity.KEY_ITEM_POSITION, getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));
                setResult(RESULT_OK,result);
                finish();
            }
        });


    }
}