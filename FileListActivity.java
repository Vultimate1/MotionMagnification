package com.spy.motionmagnificationsinglescreen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;

public class FileListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerViewHolder recyclerViewAdapter;
    String file_to_be_magnified;
    boolean only_show_videos;
    public FileListActivity() {
        this.recyclerView = recyclerView;
        this.recyclerViewAdapter = recyclerViewAdapter;
        this.file_to_be_magnified = file_to_be_magnified;
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.files_list);
        Log.d("msg","in the filelistactivity screen");

        RecyclerView view = findViewById(R.id.recycler_view);
        recyclerView = view;
        TextView noFilesText = findViewById(R.id.no_files);
        String path = getIntent().getStringExtra("path");
        Button backToSelector = findViewById(R.id.back_to_selector);
        //if (path.endsWith("mp4") || path.endsWith("avi")) {
        File root = new File(path);
        //}
        File[] filesAndFolders = root.listFiles();
        if (filesAndFolders==null || filesAndFolders.length==0) {
            noFilesText.setVisibility(View.VISIBLE);
            return;
        }
        noFilesText.setVisibility(View.INVISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter = new RecyclerViewHolder(getApplicationContext(), filesAndFolders);
        recyclerView.setAdapter(recyclerViewAdapter);
        if (recyclerViewAdapter.file_to_be_magnified!=null) {
            Log.d("file to magnify ", file_to_be_magnified);
            //Toast.makeText(this.getApplicationContext(), file_to_be_magnified, Toast.LENGTH_SHORT).show();
            file_to_be_magnified = recyclerViewAdapter.getFile_to_be_magnified().getAbsolutePath();
        }
        only_show_videos = recyclerViewAdapter.isOnly_videos_shown();

        backToSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //selector.file_to_be_magnified = getFile_to_be_magnified();
                Intent intent = new Intent(FileListActivity.this, File_selector.class);
                String path = file_to_be_magnified;
                intent.putExtra("path", recyclerViewAdapter.getFile_to_be_magnified().getAbsolutePath());
                //Toast.makeText(getApplicationContext(), "path "+path, Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }
    public String getFile_to_be_magnified() {return file_to_be_magnified;}

}
