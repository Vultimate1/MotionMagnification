package com.spy.motionmagnificationsinglescreen;


import android.media.Image;
import android.media.MediaExtractor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

//import androidx.navigation.fragment.NavHostFragment;
import com.chaquo.python.PyObject;
import com.chaquo.python.android.AndroidPlatform;
import com.chaquo.python.Python;
//import org.opencv.android.OpenCVLoader;
//import org.opencv.imgproc.Imgproc;
//import org.opencv.video.Video;

import com.spy.motionmagnificationsinglescreen.databinding.MagfragBinding;

import java.io.IOException;

public class MagFrag extends Fragment {

    private MagfragBinding binding;

    EditText k_val;
    EditText video_width;
    VideoView magnified_video_view;
    Button magnify;
    Button play_mag_video;

    public int k;
    public String video_path;
    public int width_val;
    //public VideoView magnified_video;
    public String magnified_video_path;
    MediaExtractor magnified_video_extractor;

    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState, Image[] images
    ) {
        View view = inflater.inflate(R.layout.magfrag,container,false);
        binding = MagfragBinding.inflate(inflater, container, false);
        return binding.getRoot();
        //return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.magnify.findViewById(R.id.magnify).setVisibility(View.VISIBLE);
        binding.magnify.findViewById(R.id.magnified_video_view).setVisibility(View.VISIBLE);
        binding.magnify.findViewById(R.id.play_magnified_video).setVisibility(View.VISIBLE);
        binding.magnify.findViewById(R.id.vid_width).setVisibility(View.VISIBLE);
        binding.magnify.findViewById(R.id.videolink).setVisibility(View.VISIBLE);
        binding.magnify.findViewById(R.id.play_video).setVisibility(View.VISIBLE);
        binding.magnify.findViewById(R.id.k_val).setVisibility(View.VISIBLE);
        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this.getContext()));
        }
        /*if (OpenCVLoader.initDebug()) {
            Log.d("Check","OpenCv configured successfully");
        } else{
            Log.d("Check","OpenCv doesn’t configured successfully");
        }*/
        k_val = binding.kVal.findViewById(R.id.k_val);
        k = Integer.valueOf(String.valueOf(k_val.getText()));


        video_width = binding.vidWidth.findViewById(R.id.vid_width);
        width_val = Integer.valueOf(String.valueOf(video_width.getText()));


        magnified_video_view = binding.magnifiedVideoView.findViewById(R.id.magnified_video_view);
        magnify = binding.magnify.findViewById(R.id.magnify);
        play_mag_video = binding.playMagnifiedVideo.findViewById(R.id.play_magnified_video);
        magnified_video_extractor = new MediaExtractor();
        Python pyObj = Python.getInstance();
        PyObject magnification = pyObj.getModule("magnify");
        PyObject animation = pyObj.getModule("Mp4_Format_Sergei");
        magnified_video_extractor = new MediaExtractor();
        try {
            magnified_video_extractor.setDataSource("mag_vid");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //VideoView magnified_vid_image = (ImageView) view.findViewById(R.id.magnified_video_view);
        binding.playMagnifiedVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*MediaController m = new MediaController(view.getContext());
                binding.magnifiedVideoView.setMediaController(m);
                Uri u = Uri.parse(video_path);
                binding.magnifiedVideoView.setVideoURI(u);
                binding.magnifiedVideoView.start();*/
                Log.d("video title", video_path);
                create_animation(binding.magnifiedVideoView, magnification, animation, video_path, k, width_val);
            }
        });


    }

    public void create_animation(VideoView view, PyObject mag_obj, PyObject vid_creator, String video_path, int mag_level, int width) {
        //images=Create_Animation(result,frameCount,vidpath)
        //result = magnify_motions_2d(input_data, k, width)
        //input_data=np.sum(input_data,axis=3)
        //input_data = np.empty((frameCount, frameHeight, frameWidth,3), np.dtype('double'))
        //frameCount = int(cap.get(cv2.CAP_PROP_FRAME_COUNT))
        //frameWidth = int(cap.get(cv2.CAP_PROP_FRAME_WIDTH))
        //frameHeight = int(cap.get(cv2.CAP_PROP_FRAME_HEIGHT))
        //view.setMediaController(new MediaController(view.getContext()));

        //Uri uri = Uri.parse(video_path);
        //view.setVideoURI(uri);
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
        magnified_video_path = vid_creator.callAttr("create_animations", width_val, k, video_path).toJava(String.class);
        MediaController m = new MediaController(view.getContext());
        binding.magnifiedVideoView.setMediaController(m);
        Uri u = Uri.parse(magnified_video_path);
        binding.magnifiedVideoView.setVideoURI(u);
        binding.magnifiedVideoView.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
