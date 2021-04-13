package com.example.mynoteapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NoteDetails extends AppCompatActivity {
    EditText et_title, et_tag, et_content;
    TextView tv_edit_time;
    ImageButton btn_align_left, btn_align_justify, btn_align_right, btn_edit, btn_check, btn_delete;
    List<Object> notes = new ArrayList<>();
    int note_position;
    String alignment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
        Intent intent = getIntent();
        String title = intent.getStringExtra("Title");
        String tag = intent.getStringExtra("Tag");
        String edit_time = intent.getStringExtra("EditTime");
        String content = intent.getStringExtra("Content");
        note_position = intent.getIntExtra("Position", 0);
        alignment = intent.getStringExtra("Alignment");

        et_title = findViewById(R.id.title_detail);
        et_tag = findViewById(R.id.tag_detail);
        et_content = findViewById(R.id.content_detail);
        tv_edit_time = findViewById(R.id.last_modified);

        et_title.setText(title);
        et_tag.setText(tag);
        et_content.setText(content);
        tv_edit_time.setText(edit_time);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (alignment.equals("Left")) {
                et_content.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                Spannable spannableString = new SpannableStringBuilder(et_content.getText());
                et_content.setText(spannableString);
            }
            else if (alignment.equals("Justify")) {
                et_content.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                Spannable spannableString = new SpannableStringBuilder(et_content.getText());
                et_content.setText(spannableString);
            }
            else if (alignment.equals("Right")) {
                et_content.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                Spannable spannableString = new SpannableStringBuilder(et_content.getText());
                et_content.setText(spannableString);
            }
        }

        btn_align_left = findViewById(R.id.align_left);
        btn_align_justify = findViewById(R.id.align_justify);
        btn_align_right = findViewById(R.id.align_right);

        //Align left button
        btn_align_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    et_content.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                    Spannable spannableString = new SpannableStringBuilder(et_content.getText());
                    et_content.setText(spannableString);
                    alignment = "Left";
                }
            }
        });

        //Align justify button
        btn_align_justify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    et_content.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    Spannable spannableString = new SpannableStringBuilder(et_content.getText());
                    et_content.setText(spannableString);
                    alignment = "Justify";
                }
            }
        });

        //Align right button
        btn_align_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    et_content.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                    Spannable spannableString = new SpannableStringBuilder(et_content.getText());
                    et_content.setText(spannableString);
                    alignment = "Right";
                }
            }
        });

        btn_edit = findViewById(R.id.btn_edit);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_content.isEnabled() == true)
                    et_content.setEnabled(false);
                else et_content.setEnabled(true);

                if (et_title.isEnabled() == true)
                    et_title.setEnabled(false);
                else et_title.setEnabled(true);

                if (et_tag.isEnabled() == true)
                    et_tag.setEnabled(false);
                else et_tag.setEnabled(true);
            }
        });

        btn_check = findViewById(R.id.btn_check);
        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cur_title = et_title.getText().toString();
                String cur_tag = et_tag.getText().toString();
                String cur_content = et_content.getText().toString();

                if (!cur_title.equals(title) || !cur_tag.equals(tag) || !cur_content.equals(content) || !alignment.equals("")) {
                    SharedPreferences sharedPreferences = getSharedPreferences("notes", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Gson gson = new Gson();
                    String json = sharedPreferences.getString("list_notes", null);
                    Type type = new TypeToken<ArrayList<MyNote>>() {}.getType();
                    notes = gson.fromJson(json, type);

                    MyNote temp = new MyNote();
                    temp.setTitle(et_title.getText().toString());
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    String edit_time = sdf.format(new Date());
                    temp.setEdit_time(edit_time);
                    temp.setTag(et_tag.getText().toString());
                    temp.setContent(et_content.getText().toString());
                    temp.setAlignment(alignment);

                    notes.set(note_position, temp);
                    json = gson.toJson(notes);
                    editor.putString("list_notes", json);
                    editor.apply();

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("Edit","OK");
                    setResult(100, returnIntent);
                }
                else {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("Edit","No");
                    setResult(100, returnIntent);
                }
                finish();
            }
        });

        btn_delete = findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NoteDetails.this);
                builder.setTitle("Delete note?");
                builder.setMessage("Are you sure to delete this note?");
                builder.setNegativeButton("Cancel", null);
                builder.setPositiveButton("Confirm", new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sharedPreferences = getSharedPreferences("notes", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        Gson gson = new Gson();
                        String json = sharedPreferences.getString("list_notes", null);
                        Type type = new TypeToken<ArrayList<MyNote>>() {}.getType();
                        notes = gson.fromJson(json, type);
                        notes.remove(note_position);
                        json = gson.toJson(notes);
                        editor.putString("list_notes", json);
                        editor.apply();
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("Delete","OK");
                        setResult(101, returnIntent);
                        finish();
                    }
                });
                builder.show();
            }
        });
    }
}