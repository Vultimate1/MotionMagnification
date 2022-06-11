package com.spy.motionmagnificationsinglescreen;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaExtractor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.spy.motionmagnificationsinglescreen.databinding.ActivityMainBinding;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.navigation.ui.AppBarConfiguration;


import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    VideoView magnified_video_view;
    VideoView original_video_view;

    EditText path_edittext;

    EditText k_val;
    EditText video_width;

    Button select_video;
    Button play_mag_video;
    Button play_original_video;
    public int k;
    public String video_path;
    public int width_val;
    //public VideoView magnified_video;
    public String magnified_video_path;
    MediaExtractor magnified_video_extractor;
    boolean isSelected=false;
    //File_selector selector = new File_selector();
    List<Video> videos = new ArrayList<Video>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        final Fragment magfrag = new MagFrag();
        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        } else {
            Log.d("message", "Yay, python started!");
        }
        /*if (OpenCVLoader.initDebug()) {
            Log.d("Check", "OpenCv configured successfully");
            //OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_11, mCallback);
        } else {
            Log.d("Check", "OpenCv doesn’t configured successfully");
        }*/
        Uri collection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collection = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        }

        String[] projection = new String[] {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.SIZE
        };
        String selection = MediaStore.Video.Media.DURATION +
                " >= ?";

        setContentView(R.layout.activity_main);

        Python pyObj = Python.getInstance();
        PyObject magnification = pyObj.getModule("magnify");
        PyObject animation = pyObj.getModule("Mp4_Format_Sergei");
        //Button ToMainScreeen =(Button)findViewById(R.id.gotomainscreen);

        k_val = findViewById(R.id.k_val);
        k = Integer.valueOf(String.valueOf(k_val.getText()));

        video_width = findViewById(R.id.vid_width);
        width_val = Integer.valueOf(String.valueOf(video_width.getText()));

        path_edittext = findViewById(R.id.videolink);
        original_video_view = findViewById(R.id.original_video_view);
        play_original_video = findViewById(R.id.play_original_video);
        video_path = String.valueOf(path_edittext.getText());
        //video_path = "C://Users//srira//Desktop//phone-videos-temp-3-29//20220326_205759.mp4";
        //video_path = "C:/Users/srira/AndroidStudioProjects/MotionMagnificationSingleScreen/app/src/main/res/raw/loaded_cart.mp4";
        //video_path = "android.resource://com.example.motionmagnificationsinglescreen/"+R.raw.baby;
        //video_path = "android.resource://" + getPackageName() + "/" + R.raw.camera;
        //video_path = "android.resource://com.example.motionmagnificationsinglescreen/"+R.raw.camouflage;
        //video_path = "C:\\Users\\srira\\Documents\\MATLAB\\Matlab\\baby.avi";
        //video_path = "https://techslides.com/demos/sample-videos/small.mp4";
        //video_path = "https://media.geeksforgeeks.org/wp-content/uploads/20201217192146/Screenrecorder-2020-12-17-19-17-36-828.mp4?_=1";
///Users/aasemjs/AndroidStudioProjects/VideoPlayDemo/app/src/main/res/raw/trial.MOV
        magnified_video_view = findViewById(R.id.magnified_video_view);
        select_video = findViewById(R.id.select_video);
        play_mag_video = findViewById(R.id.play_magnified_video);
        select_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
// create a FragmentTransaction to begin the transaction and replace the Fragment
                Intent intent = new Intent(MainActivity.this, File_selector.class);
// replace the FrameLayout with new Fragment
                startActivity(intent);
                Bundle newbundle = getIntent().getExtras();
                if (newbundle!=null) {
                    video_path = newbundle.getString("path");
                }//video_path = selector.getFile_to_be_magnified().getAbsolutePath();
            }
        });
        //VideoView magnified_vid_image = (ImageView) view.findViewById(R.id.magnified_video_view);
        play_original_video.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View view) {
                if (getIntent().getExtras().getString("path")!=null) {

                    video_path = getIntent().getExtras().getString("path");
                    //Toast.makeText(getApplicationContext(), "intent is not null", Toast.LENGTH_LONG).show();
                    isSelected = true;
                }

                if (isSelected) {
//                    String mainpath[] = video_path.split("/storage/emulated/0/");

//                    video_path = mainpath[1];
//                    video_path = MainActivity.this.getFilesDir().toString()+video_path;

                    Log.d("selected ", "true");
                    //video_path = selector.file_to_be_magnified.getAbsolutePath();

                    path_edittext.setText(video_path);
                    Toast.makeText(getApplicationContext(), video_path, Toast.LENGTH_SHORT).show();
                    Log.d("video_title", video_path.toString());

                    original_video_view.setVisibility(View.VISIBLE);
                    MediaController m = new MediaController(getApplicationContext());
                    original_video_view.setMediaController(m);
                    m.setAnchorView(m);
                    Uri u = Uri.fromFile(new File(video_path));
                    if (video_path.endsWith(".avi")) {
                        Intent i = new Intent();
                        i.setAction(Intent.ACTION_VIEW);
                        String type = "video/avi";
                        i.setDataAndType(u, type);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplicationContext().startActivity(i);
                    }
                    //Uri u = Uri.parse(video_path);
                    original_video_view.setVideoURI(u);
                    //original_video_view.setVideoPath(video_path);
                    original_video_view.requestFocus();
                    original_video_view.start();
                    if (original_video_view.isPlaying()){
                        magnified_video_view.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No video selected. Please select video.", Toast.LENGTH_SHORT);
                    //Log.d("selected ", "false");
                }
                return true;
            }
        });

        play_mag_video.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {
                if (getIntent().getExtras().getString("path")!=null) {
                    video_path = getIntent().getExtras().getString("path");
                    isSelected = true;
                }
                if (isSelected || video_path!="") {
                    /*ContentValues values = new ContentValues();
                    values.put(MediaStore.Video.Media.TITLE, "Title1");
                    values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
                    values.put(MediaStore.Video.Media.DATA, video_path);
                    Uri uri = cr.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);*/
                    Uri u = Uri.fromFile(new File(video_path));
                    //Toast.makeText(getApplicationContext(), "No video selected. Please select video.", Toast.LENGTH_SHORT);
                    //MediaController m = new MediaController(view.getContext());
                    //binding.magnifiedVideoView.setMediaController(m);
                    //Uri u = Uri.parse(video_path);
                    //binding.magnifiedVideoView.setVideoURI(u);
                    //binding.magnifiedVideoView.start();
                    create_animation(magnified_video_view, magnification, animation, video_path, k, width_val);
                }
                return true;
            }

        });
        /*ToMainScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.gotomainscreen).setVisibility(View.INVISIBLE);


                androidx.fragment.app.FragmentManager fm = getSupportFragmentManager();
                androidx.fragment.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, magfrag);
                fragmentTransaction.commit();
            }
        });*/
    }


    public void create_animation(VideoView view, PyObject mag_obj, PyObject vid_creator, String video_path, int mag_level, int width) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        video_path = MainActivity.this.getFilesDir().toString()+video_path;

        String mainpath[] = video_path.split("com.example.motionmagnificationappsinglescreen/files/");
        video_path = mainpath[1];
        Uri temp = Uri.fromFile(new File(video_path));

        Log.d("version", String.valueOf(Build.VERSION.SDK_INT));
        if (Build.VERSION.SDK_INT >= 14)
            retriever.setDataSource(getApplicationContext(), temp);
        else
            retriever.setDataSource(video_path);
        retriever.setDataSource(getApplicationContext(), temp);
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        String framecount = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_FRAME_COUNT);
        String framewidth = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
        String frameheight = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);

        long timeInMillisec = Long.parseLong(time);
        retriever.release();
        int duration= (int) ((timeInMillisec)*1000);
        int frames = Integer.parseInt(framecount);
        int Framewidth = Integer.parseInt(framewidth);
        int Frameheight = Integer.parseInt(frameheight);

        //MediaRecorder.VideoSource videoSource = MediaRecorder.VideoSource(video_path);
        //int frameRate = 24; //may be default
        /*try {
            //Adjust data source as per the requirement if file, URI, etc.
            Log.println(Log.VERBOSE, "media", "before setting data source");
            magnified_video_extractor.setDataSource(video_path);
            Log.println(Log.VERBOSE, "media", "after setting data source");

            int numTracks = magnified_video_extractor.getTrackCount();
            for (int i = 0; i < numTracks; ++i) {
                MediaFormat format = magnified_video_extractor.getTrackFormat(i);
                String mime = format.getString(MediaFormat.KEY_MIME);
                if (format.containsKey(MediaFormat.KEY_FRAME_RATE)) {
                    frameRate = format.getInteger(MediaFormat.KEY_FRAME_RATE);
                }
            }
        } catch (IOException e) {
            Log.d("exception", "not able to create media extractor");
            e.printStackTrace();
        }*/
//        double[][][] input_data = new double[frameCount][frameheight][framewidth];
//        double[][][] result = mag_obj.callAttr("magnify_motions_2d", input_data, k, width).toJava(double[][][].class);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // yourMethod();
            }
        }, 5000);
        original_video_view.setVisibility(View.INVISIBLE);
        magnified_video_view.setVisibility(View.VISIBLE);

        magnified_video_path = vid_creator.callAttr("Create_Animation", frames, Framewidth, Frameheight, width, mag_level, video_path).toString();
        Log.d("magnified title ", magnified_video_path);

        Uri u = Uri.fromFile(new File(magnified_video_path));


        MediaController m = new MediaController(getBaseContext());
        m.setAnchorView(m);
        magnified_video_view.setMediaController(m);
        magnified_video_view.setVideoURI(u);
        magnified_video_view.requestFocus();

        magnified_video_view.start();
        if (magnified_video_view.isPlaying()){
            original_video_view.setVisibility(View.GONE);
        }
        //magnified_video_view.loadUrl(magnified_video_path);
        /*Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        String type="video/mp4";
        intent.setDataAndType(Uri.parse(video_path), type);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);*/
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = MediaStore.MediaColumns.DATA;
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
}


    /*public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu., menu);
        return true;
    }*/

    /*public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    //@Override
    //public boolean onSupportNavigateUp() {
    //    NavController navController = .findNavController(this, R.id.nav_host_fragment_content_main);
    //    return NavigationUI.navigateUp(navController, appBarConfiguration)
    //            || super.onSupportNavigateUp();
    //}
    /*private void loadFragment(android.app.Fragment fragment) {
// create a FragmentManager
        FragmentManager fm = getFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.ActivityMain, fragment);
        fragmentTransaction.commit(); // save the changes
    }*/


