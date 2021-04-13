package com.example.mynoteapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NoteDialog.NoteDialogListener {
    ImageButton add_btn;
    ListView listView;
    EditText search_field;
    List<Object> notes = new ArrayList<>();
    MyAdapter aa;
    String[] filter_item = {"Title", "Tag"};
    String search_type = "";
    Spinner search_filter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search_filter = findViewById(R.id.search_type);
        ArrayAdapter aa1 = new ArrayAdapter(this, R.layout.spinner, filter_item);
        aa1.setDropDownViewResource(R.layout.spinner_dropdown);
        search_filter.setAdapter(aa1);
        search_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                search_type = search_filter.getSelectedItem().toString();
                aa.getSearchType(search_type);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        loadData();
        listView = findViewById(R.id.list_node);
        search_field = findViewById(R.id.search_field);

        add_btn = findViewById(R.id.addNoteButton);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        aa = new MyAdapter(this, R.layout.list_notes, notes);
        listView.setAdapter(aa);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyNote note = (MyNote) aa.search_notes.get(position);
                Intent intent = new Intent(view.getContext(), NoteDetails.class);
                intent.putExtra("Title", note.getTitle());
                intent.putExtra("EditTime", note.getEdit_time());
                intent.putExtra("Tag", note.getTag());
                intent.putExtra("Content", note.getContent());
                intent.putExtra("Position", position);
                intent.putExtra("Alignment", note.getAlignment());
                startActivityForResult(intent, 100);
            }
        });

        search_field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                aa.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void openDialog() {
        NoteDialog noteDialog = new NoteDialog();

        noteDialog.show(getSupportFragmentManager(), "note dialog");
    }

    @Override
    public void setNote(String title, String tag) {
        MyNote new_note = new MyNote();
        new_note.setTitle(title);
        new_note.setTag(tag);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String edit_time = sdf.format(new Date());
        new_note.setEdit_time(edit_time);
        new_note.setContent("");
        new_note.setAlignment("");
        notes.add(new_note);
        aa.notifyDataSetChanged();

        SharedPreferences sharedPreferences = getSharedPreferences("notes", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(notes);
        editor.putString("list_notes", json);
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("notes", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("list_notes", null);
        Type type = new TypeToken<ArrayList<MyNote>>() {}.getType();
        notes = gson.fromJson(json, type);
        if (notes == null) {
            notes = new ArrayList<>();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            if (resultCode == 100) {
                String isEdit = data.getStringExtra("Edit");
                if (isEdit.equals("OK")) {
                    notes.clear();
                    aa.notifyDataSetChanged();
                    loadData();
                    aa = new MyAdapter(this, R.layout.list_notes, notes);
                    listView.setAdapter(aa);
                }
                else if (isEdit.equals("No")) {};
            }
            else if (resultCode == 101) {
                notes.clear();
                aa.notifyDataSetChanged();
                loadData();
                aa = new MyAdapter(this, R.layout.list_notes, notes);
                listView.setAdapter(aa);
            }
        }
    }

}