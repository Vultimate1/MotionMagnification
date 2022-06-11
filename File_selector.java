package com.spy.motionmagnificationsinglescreen;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

public class File_selector extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerViewHolder recyclerViewAdapter;
    public String file_to_be_magnified;
    protected void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.file_selector);
        if (getIntent().getExtras()!=null) {
            file_to_be_magnified = (getIntent().getExtras().getString("path"));
            //Toast.makeText(getApplicationContext(), "selected file "+file_to_be_magnified, Toast.LENGTH_SHORT).show();
        }
       /* if (file_to_be_magnified=="") {
            Toast.makeText(getApplicationContext(), "selected nothing", Toast.LENGTH_SHORT).show();

        }   else {
            Toast.makeText(getApplicationContext(), "selected " + file_to_be_magnified, Toast.LENGTH_SHORT).show();

        }*/

        MaterialButton storageBtn = findViewById(R.id.files);
        Button goToMain = findViewById(R.id.backToMain);
        goToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (file_to_be_magnified!=null) {
                    Toast.makeText(getApplicationContext(), "selected " + file_to_be_magnified, Toast.LENGTH_SHORT).show();
                }   else {
                    Toast.makeText(getApplicationContext(), "selected nothing", Toast.LENGTH_SHORT).show();

                }*/
                Intent intent = new Intent(File_selector.this, MainActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                intent.putExtra("path", file_to_be_magnified);
                startActivity(intent);
                //main.magnified_video_path = file_to_be_magnified.getAbsolutePath();
            }
        });
        /*recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //if you can't scroll then add the following lines
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerViewAdapter = new RecyclerViewHolder(this);
        recyclerView.setAdapter(recyclerViewAdapter);*/

        storageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    Log.d("msg","going to the filelistactivity screen");
                    FileListActivity list_activity = new FileListActivity();
                    Intent intent = new Intent(File_selector.this, list_activity.getClass());
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                    Log.d("path ",path);
                    intent.putExtra("path", path);
                    startActivity(intent);
                    //file_to_be_magnified = list_activity.getFile_to_be_magnified();
                } else {
                    requestPermission();
                }
            }
        });
    }
    public String getFile_to_be_magnified() {return file_to_be_magnified;}

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(File_selector.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result== PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(File_selector.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(File_selector.this, "Storage permission is required, allow from settings", Toast.LENGTH_SHORT).show();
        }
        ActivityCompat.requestPermissions(File_selector.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 111);
    }
}
