package com.stp.music_player.fragment;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.stp.music_player.R;
import com.stp.music_player.matching.IMatching;
import com.stp.music_player.matching.MusicResponse;
import com.stp.music_player.matching.NetworkClient;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * A simple {@link Fragment} subclass.
 */
public class Matching extends Fragment {
    private static final String LOG_TAG = "AudioRecording";
    public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;
    private static final int BIT_RATE = 16;
    private static final int SAMPLE_RATE = 44100;

    private static String mFileName = null;
    private View matchingView;
    private ImageButton btn_record;
    private MediaRecorder mMediaRecorder;
    private TextView tv_songName;
    private TextView tv_bottom_guide;
    private ObjectAnimator anim;
    Context mContext;

    public Matching() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        matchingView = inflater.inflate(R.layout.fragment_matching, container, false);
        btn_record = (ImageButton) matchingView.findViewById(R.id.btn_record);
        tv_songName = (TextView) matchingView.findViewById(R.id.tv_discovered_song_name);
        tv_bottom_guide = (TextView) matchingView.findViewById(R.id.tv_bottom_guide);
        matchingView.findViewById(R.id.tv_discovered_song_name).setSelected(true);
        matchingView.findViewById(R.id.tv_discovered_song_name).setVisibility(View.GONE);

        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/AudioRecording.mp3";
        Log.d(LOG_TAG, "File name: " + mFileName);
        mContext = getContext();
        btn_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CheckPermissions())
                {
                    tv_bottom_guide.setVisibility(View.INVISIBLE);
                    tv_songName.setVisibility(View.GONE);
                    tv_bottom_guide.setText("Listening...");
                    tv_bottom_guide.setVisibility(View.VISIBLE);
                    runningAnimation();
                    record();

                }
                else {
                    RequestPermissions();
                }
            }
        });
        return matchingView;
    }

    private void getSongName() {
        Retrofit retrofit = NetworkClient.getRetrofitClient();
        IMatching iMatching = retrofit.create(IMatching.class);
        File file = new File(mFileName);
        RequestBody mp3File = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part uploadFile = MultipartBody.Part.createFormData("data", file.getName(), mp3File);
        Call<MusicResponse> call = iMatching.getSongName(uploadFile);
        call.enqueue(new Callback<MusicResponse>() {
            @Override
            public void onResponse(Call<MusicResponse> call, Response<MusicResponse> response) {
                if (response.body() != null) {
                    MusicResponse res = response.body();
                    if (!res.getSuccess())
                    {
                        Toast.makeText(mContext, "Poor network connection, can't connect to server!", Toast.LENGTH_LONG).show();
                        tv_bottom_guide.setVisibility(View.GONE);
                        tv_bottom_guide.setText("Tap to identify");
                        tv_bottom_guide.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        tv_songName.setText(res.getSongName());
                        tv_songName.setVisibility(View.VISIBLE);
                        tv_bottom_guide.setVisibility(View.GONE);
                        tv_bottom_guide.setText("Tap to identify");
                        tv_bottom_guide.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(mContext, "Could not connect to server. Please try again later.", Toast.LENGTH_LONG).show();
                Log.d(LOG_TAG, "onFailure: " + t.getMessage());
                tv_bottom_guide.setVisibility(View.GONE);
                tv_bottom_guide.setText("Tap to identify");
                tv_bottom_guide.setVisibility(View.VISIBLE);
            }
        });
    }

    private void record()
    {
        tv_songName.setVisibility(View.GONE);
        btn_record.setClickable(false);
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mMediaRecorder.setAudioEncodingBitRate(BIT_RATE*SAMPLE_RATE);
        mMediaRecorder.setAudioSamplingRate(SAMPLE_RATE);
        mMediaRecorder.setAudioChannels(2);
        mMediaRecorder.setOutputFile(mFileName);

        try {
            mMediaRecorder.prepare();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        mMediaRecorder.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mMediaRecorder.stop();
                mMediaRecorder.reset();
                mMediaRecorder.release();
                mMediaRecorder = null;
                btn_record.setClickable(true);
                anim.removeAllListeners();
                anim.end();
                anim.cancel();
                tv_bottom_guide.setTextColor(Color.parseColor("#a9a9a9"));
//                Toast.makeText(mContext, "Sending record to server", Toast.LENGTH_LONG).show();

                tv_bottom_guide.setVisibility(View.GONE);
                tv_bottom_guide.setText("Wait for server respond");
                tv_bottom_guide.setVisibility(View.VISIBLE);
                getSongName();
            }
        },10000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_AUDIO_PERMISSION_CODE:
                if (grantResults.length> 0) {
                    boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean permissionToStore = grantResults[1] ==  PackageManager.PERMISSION_GRANTED;
                    if (permissionToRecord && permissionToStore) {
                        Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(),"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean CheckPermissions() {
        int result = ContextCompat.checkSelfPermission(getContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getContext(), RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void RequestPermissions() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, REQUEST_AUDIO_PERMISSION_CODE);
    }

    private void runningAnimation() {
        anim = ObjectAnimator.ofInt(tv_bottom_guide, "textColor", Color.WHITE, Color.RED, Color.WHITE);
        anim.setDuration(3000);
        anim.setEvaluator(new ArgbEvaluator());
        anim.setRepeatMode(ValueAnimator.REVERSE);
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.start();
    }
}
