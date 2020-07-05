package com.example.todolist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 30;

    RecyclerView recyclerView;
    Button addItem;
    EditText typeText;
    LinkedList<String> items;
    AdapterItems adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.RecyclerView);
        addItem = findViewById(R.id.buttonAdd);
        typeText = findViewById(R.id.editAdd);

        loadItems();


        AdapterItems.OnLongClickListener OnLongClickListener = new AdapterItems.OnLongClickListener(){
            @Override
            public void onItemLongClick(final int position) {

                AlertDialog.Builder confirmDialog = new AlertDialog.Builder(MainActivity.this);
                confirmDialog.setMessage("REMOVE FROM LIST?");
                confirmDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        items.remove(position);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, "Item was remove", Toast.LENGTH_SHORT).show();
                        saveItems();
                    }
                });

                confirmDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("onClick NO", "Not Remove element");
                    }
                });

                confirmDialog.create().show();
            }
        };

        AdapterItems.OnClickListener onClickListener = new AdapterItems.OnClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent editActivity = new Intent(MainActivity.this, EditActivity.class);
                editActivity.putExtra(KEY_ITEM_TEXT,items.get(position));
                editActivity.putExtra(KEY_ITEM_POSITION,position);
                startActivityForResult(editActivity,EDIT_TEXT_CODE);
            }
        };

        adapter = new AdapterItems(this,items, OnLongClickListener,onClickListener);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(typeText.getText().toString().length() != 0){
                    items.addLast(typeText.getText().toString());
                    typeText.setText("");
                    Toast.makeText(MainActivity.this, "Item was added to List", Toast.LENGTH_SHORT).show();
                    saveItems();
                }
                adapter.notifyDataSetChanged();
            }
        });



        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback
                (ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT |
                        ItemTouchHelper.DOWN | ItemTouchHelper.UP,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT ) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {

                // Get the from and to positions.
                int from = viewHolder.getAdapterPosition();
                int to = target.getAdapterPosition();

                // Swap the items and notify the adapter.
                Collections.swap(items, from, to);
                adapter.notifyItemMoved(from, to);
                saveItems();
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder,
                                 int direction) {
                items.remove(viewHolder.getAdapterPosition());
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                Toast.makeText(MainActivity.this, "Item was removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }

        });
        // Attached the helper to the Recyclerview.
        helper.attachToRecyclerView(recyclerView);

    }

    private File getDataFile(){
        return new File(getFilesDir(),"data.txt");
    }

    private void loadItems(){
        try {
            items = new LinkedList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity","Error loading data",e);
            items = new LinkedList<>();
        }
    }

    private void saveItems(){
        try {
            FileUtils.writeLines(getDataFile(),items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {

            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);

            items.set(position,itemText);
            adapter.notifyDataSetChanged();
            saveItems();

            Toast.makeText(this, "Item has been updated", Toast.LENGTH_SHORT).show();

        } else {
            Log.w("MainActivity", "Unknown call");
        }
    }
}