package com.example.mynoteapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends ArrayAdapter {
    public ArrayList<MyNote> notes;
    public ArrayList<MyNote> search_notes;
    TextView tvTitle, tvEditTime, tvTag;
    LayoutInflater inflater;
    String search_type = "";
    public MyAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        notes = (ArrayList<MyNote>) objects;
        search_notes = (ArrayList<MyNote>) objects;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return search_notes.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        MyNote note = search_notes.get(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_notes, parent, false);

        tvTitle = convertView.findViewById(R.id.tvTitle);
        tvTitle.setText(note.getTitle());

        tvEditTime = convertView.findViewById(R.id.tvEditTime);
        tvEditTime.setText(note.getEdit_time());

        tvTag = convertView.findViewById(R.id.tvTag);
        tvTag.setText(note.getTag());

        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<MyNote> FilteredList = new ArrayList<MyNote>();


                if (constraint == null || constraint.length() == 0) {
                    results.count = notes.size();
                    results.values = notes;
                }
                else {
                    constraint = constraint.toString().toLowerCase();
                    if (search_type.equals("Title")) {
                        for (int i = 0; i < notes.size(); i++) {
                            String data = notes.get(i).getTitle();
                            if (data.toLowerCase().contains(constraint)) {
                                FilteredList.add(new MyNote(notes.get(i).getTitle(), notes.get(i).getTag(), notes.get(i).getEdit_time(), notes.get(i).getContent(), notes.get(i).getAlignment()));
                            }
                        }
                    }
                    else if (search_type.equals("Tag")) {
                        for (int i = 0; i < notes.size(); i++) {
                            String data = notes.get(i).getTag();
                            if (data.toLowerCase().contains(constraint)) {
                                FilteredList.add(new MyNote(notes.get(i).getTitle(), notes.get(i).getTag(), notes.get(i).getEdit_time(), notes.get(i).getContent(), notes.get(i).getAlignment()));
                            }
                        }
                    }

                    results.count = FilteredList.size();
                    results.values = FilteredList;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                search_notes = (ArrayList<MyNote>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    public void getSearchType(String search_type) {
        this.search_type = search_type;
    }

}
