package com.spy.motionmagnificationsinglescreen;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

import static com.spy.motionmagnificationsinglescreen.Method.load_Directory_Files;

public class SplashScreen extends AppCompatActivity {
    private File storage;
    private String[] allpath;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allpath = StorageUtil.getStorageDirectories(this);
        for (String path : allpath) {
            storage = new File(path);
            load_Directory_Files(storage);
        }
        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
        startActivity(intent);
    }
}
