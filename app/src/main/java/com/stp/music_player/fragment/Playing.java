package com.stp.music_player.fragment;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.stp.music_player.BroadcaseReceiver.NotiBroadcast;
import com.stp.music_player.MainActivity;
import com.stp.music_player.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

/**
 * A simple {@link Fragment} subclass.
 */
public class Playing extends Fragment {
    public static final String ACTION_PREVIOUS = "ACTION_PREVIOUS";
    public static final String ACTION_NEXT = "ACTION_NEXT";
    public static final String ACTION_PAUSE = "ACTION_PAUSE";
    public static final String ACTION_PLAY = "ACTION_PLAY";

    private ImageView imageView;
    public static int isPlayMusic = 0;
    public static int isRepeat = 0;
    public static int isShuffle = 0;
    public MediaPlayer mMediaPlayer;
    private ImageButton btnPlay;
    private ImageButton btnNext;
    private ImageButton btnBack;
    private ImageButton btnShuffle;
    private ImageButton btnRepeat;
    private SeekBar songSeekBar;
    private TextView tvTitle;
    private TextView tvArtist;
    private TextView tvTimeStart;
    private TextView tvTimeEnd;
    private int length = 0;
    private NotificationManager notificationManager = null;
    public ArrayList<String> arrayListSong;
    private CountDownTimer countDownTimer = null;
    public int songPosition = 0;
    public String path;
    public MediaMetadataRetriever mediaMetadataRetriever;
    public Bitmap bitmapImageSong = null;
    private LinearLayout linearLayout;


    public Playing() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Play", "onCreatePlaying");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Play", "onPausePlaying");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Play", "onResumePlaying");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("Play", "onStartPlaying");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Play","onDestroyPlaying");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("Play", "OnAcCre");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_playing, container, false);
        Log.d("Play", "onCreateView");
        arrayListSong = MainActivity.arrayListSong;
        isPlayMusic = 0;
        isShuffle = 0;
        isRepeat = 0;
        imageView = (ImageView) view.findViewById(R.id.imageView);
        tvTitle = view.findViewById(R.id.tv_name_song);
        tvArtist = view.findViewById(R.id.tv_name_artist);
        tvTimeStart = view.findViewById(R.id.tv_time_start);
        tvTimeEnd = view.findViewById(R.id.tv_time_end);
        imageView.setImageResource(R.drawable.rose);
        songSeekBar = view.findViewById(R.id.seekBar);
        path = arrayListSong.get(songPosition);
        btnPlay = view.findViewById(R.id.btn_play);
        btnNext = view.findViewById(R.id.btn_next);
        btnBack = view.findViewById(R.id.btn_back);
        btnRepeat = (ImageButton) view.findViewById(R.id.btn_repeat);
        btnShuffle = (ImageButton) view.findViewById(R.id.btn_shuffle);
        tvTitle.setText("Reality");//default
        tvArtist.setText("Lost Prequencies");//default
        linearLayout = view.findViewById(R.id.constraintLayout);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickButtonPlay();

            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickButtonNext();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickButtonBack();
            }
        });
        btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickButtonRepeat();
            }
        });

        btnShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickButtonShuffle();
            }
        });


        songSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (mMediaPlayer != null && b) {
                    //mMediaPlayer.seekTo(i * 1000);
                    if (mMediaPlayer != null && b) {
                        //mMediaPlayer.seekTo(i * 1000);

                        mMediaPlayer.pause();
                        songSeekBar.setProgress(i);

                        length = i;
                        countDownTimer.cancel();
                        mMediaPlayer = new MediaPlayer();
                        try {
                            mMediaPlayer.setDataSource(getContext(), Uri.parse(path));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        try {
                            mMediaPlayer.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        playMusic(path);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mMediaPlayer.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return view;
    }

    public void onClickButtonRepeat() {

        if (isRepeat == 0) {
            int icon = R.drawable.ic_repeat_one;
            btnRepeat.setImageDrawable(ContextCompat.getDrawable(getContext(), icon));
            Toast.makeText(getContext(),"Repeat one",Toast.LENGTH_SHORT);
            isRepeat = 1;
        } else if (isRepeat == 1) {
            int icon = R.drawable.ic_repeat;
            btnRepeat.setImageDrawable(ContextCompat.getDrawable(getContext(), icon));
            Toast.makeText(getContext(),"Repeat all",Toast.LENGTH_SHORT);
            isRepeat = 2;
        } else {
            int icon = R.drawable.ic_no_repeat;
            btnRepeat.setImageDrawable(ContextCompat.getDrawable(getContext(), icon));
            Toast.makeText(getContext(),"No repeat",Toast.LENGTH_SHORT);
            isRepeat = 0;
        }

    }

    public void onClickButtonShuffle() {
        if (isShuffle == 0) {
            int icon = R.drawable.ic_shuffle;
            btnShuffle.setImageDrawable(ContextCompat.getDrawable(getContext(), icon));
            Toast.makeText(getContext(),"Suffle on",Toast.LENGTH_SHORT);
            isShuffle = 1;
        } else {
            int icon = R.drawable.ic_no_shuffle;
            btnShuffle.setImageDrawable(ContextCompat.getDrawable(getContext(), icon));
            Toast.makeText(getContext(),"Suffle off",Toast.LENGTH_SHORT);
            isShuffle = 1;
            isShuffle = 0;
        }
    }

    public void onClickButtonPlay() {
        if (isPlayMusic == 1) {
            int icon = R.drawable.ic_play;
            btnPlay.setImageDrawable(ContextCompat.getDrawable(getContext(), icon));
            isPlayMusic = 0;
            mMediaPlayer.pause();
            length = mMediaPlayer.getCurrentPosition();
            countDownTimer.cancel();
            pushNotification();
        } else {
            int icon = R.drawable.ic_pause;
            btnPlay.setImageDrawable(ContextCompat.getDrawable(getContext(), icon));
            mMediaPlayer = new MediaPlayer();
            try {
                mMediaPlayer.setDataSource(getContext(), Uri.parse(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mMediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            playMusic(path);

        }
    }

    public void onClickButtonNext() {
        length = 0;
        songPosition++;
        if(isRepeat == 1){
            songPosition--;
        }else{
            final int min = 0;
            final int max = arrayListSong.size()-1;
            final int random = new Random().nextInt((max - min) + 1) + min;
            if(isShuffle == 1){
                songPosition = random;
            }

            if(songPosition>=arrayListSong.size()){
                if(isRepeat ==2){
                    songPosition = 0;
                }else{
                    songPosition=arrayListSong.size()-1;
                }

            }
        }

        if(mMediaPlayer != null){
            mMediaPlayer.stop();
            countDownTimer.cancel();
        }
        path = arrayListSong.get(songPosition);
        playMusic(path);
    }

    public void onClickButtonBack() {
        songPosition--;
        if (isRepeat == 1) {
            songPosition++;
        }else{
            final int min = 0;
            final int max = arrayListSong.size()-1;
            final int random = new Random().nextInt((max - min) + 1) + min;
            if(isShuffle == 1){
                songPosition = random;
            }
            if(songPosition<0){
                if(isRepeat ==2){
                    songPosition = arrayListSong.size()-1;
                }else{
                    songPosition = 0;
                }

            }
        }

        if(mMediaPlayer != null){
            mMediaPlayer.stop();
            countDownTimer.cancel();
        }
        path = arrayListSong.get(songPosition);
        playMusic(path);
    }

    public void playMusic(String pathSong) {
        mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(getContext(), Uri.parse(path));
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(getContext(), Uri.parse(pathSong));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        songSeekBar.setProgress(0);
        songSeekBar.setMax(mMediaPlayer.getDuration());
        Log.d("Process", mMediaPlayer.getDuration() + "");
        isPlayMusic = 1;
        btnPlay.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_pause));
        setInfor(mediaMetadataRetriever);
        pushNotification();

    }

    public void setInfor(MediaMetadataRetriever mediaMetadataRetriever) {
        File song = new File (path);

        String titleSong =
                mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        if(titleSong == null)   titleSong = song.getName();
        tvTitle.setText(titleSong);

        String titleArtist =
                mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        if(titleArtist == null)   titleArtist = "Unknown";
        tvArtist.setText(titleArtist);

        byte rawArt[];
        rawArt = mediaMetadataRetriever.getEmbeddedPicture();
        bitmapImageSong = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        if (rawArt == null) {
            imageView.setImageResource(R.drawable.rose);
        } else {
            bitmapImageSong = BitmapFactory.decodeByteArray(rawArt, 0, rawArt.length, options);
            imageView.setImageBitmap(bitmapImageSong);
        }

        String time = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        int mm = Integer.parseInt(time) / 60000;
        int ss = (Integer.parseInt(time) - mm * 60000) / 1000;
        final String stringTimeMax = String.format("%02d:%02d", mm, ss);
        tvTimeEnd.setText(stringTimeMax);
        int timeTemp = mMediaPlayer.getDuration() / 200;
        songSeekBar.setProgress(timeTemp);
        mMediaPlayer.seekTo(length);
        mMediaPlayer.start();
        countDownTimer = new CountDownTimer(mMediaPlayer.getDuration() - length, 1000) {
            public void onTick(long millisUntilFinished) {
                long timeEnd = mMediaPlayer.getDuration() - millisUntilFinished;
                int minute = (int) (timeEnd / 60000);
                int second = (int) (timeEnd - minute * 60000) / 1000;
                String m;
                String s;
                if (minute < 10) {
                    m = "0" + minute;
                } else m = minute + "";
                if (second < 10) {
                    s = "0" + second;
                } else s = second + "";
                tvTimeStart.setText(String.format("%02d:%02d", minute, second));
                songSeekBar.setProgress((int) timeEnd);
            }

            public void onFinish() {
                onClickButtonNext();
            }

        };
        countDownTimer.start();
    }

    public void pushNotification() {
        String titleSong =
                mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        String titleArtist =
                mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        String channelId = "musicServiceChannelID";
        notificationManager = null;
        NotificationChannel notificationChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(channelId, "Music Service Name",
                    NotificationManager.IMPORTANCE_DEFAULT);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            notificationManager =
                    getActivity().getSystemService(NotificationManager.class);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(notificationChannel);
        }
        RemoteViews remoteViews = new RemoteViews(getActivity().getPackageName(), R.layout.notification_music);
        remoteViews.setTextViewText(R.id.title, titleSong);
        remoteViews.setTextViewText(R.id.singer, titleArtist);
        if (isPlayMusic == 0) {//neu dang chay thi set nut pause
            remoteViews.setImageViewResource(R.id.btn_pause, R.drawable.ic_play);
        } else {
            remoteViews.setImageViewResource(R.id.btn_pause, R.drawable.ic_pause);
        }
        remoteViews.setInt(R.id.notification_view, "setBackgroundColor", Color.CYAN);
        setListener(remoteViews);
        byte rawArt[];
        rawArt = mediaMetadataRetriever.getEmbeddedPicture();
        bitmapImageSong = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        if (rawArt != null) {
            bitmapImageSong = BitmapFactory.decodeByteArray(rawArt, 0, rawArt.length, options);
            remoteViews.setImageViewBitmap(R.id.img1, bitmapImageSong);
        }else{
            remoteViews.setImageViewResource(R.id.img1, R.drawable.rose);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getContext(), channelId);
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setCustomContentView(remoteViews)
                .setColor(getResources().getColor(R.color.colorAccent))
                .setOnlyAlertOnce(true)
                .setOngoing(true)
                .setAutoCancel(false);
        notificationManager.notify(200, notificationBuilder.build());
    }

    public void setListener(RemoteViews view) {
        //pending intent
        Intent previousIntent = new Intent();
        previousIntent.setAction(ACTION_PREVIOUS);
        PendingIntent prePendingIntent = PendingIntent.getBroadcast(getContext(), 101, previousIntent, FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btn_pre, prePendingIntent);
//      pause Intent
        Intent pauseIntent = new Intent();
        pauseIntent.setAction(ACTION_PAUSE);
        PendingIntent pausePendingIntent = PendingIntent.getBroadcast(getContext(), 102, pauseIntent, FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btn_pause, pausePendingIntent);
        //next Intent
        Intent nextIntent = new Intent();
        nextIntent.setAction(ACTION_NEXT);
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(getContext(), 103, nextIntent, FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btn_next_in_notifi, nextPendingIntent);
        //play Intent
        Intent playIntent = new Intent();
        playIntent.setAction(ACTION_PLAY);
        PendingIntent playPendingIntent = PendingIntent.getBroadcast(getContext(), 104, playIntent, FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btn_pause, playPendingIntent);
    }

    public void setsongList(ArrayList<String> songList, int pos) {
        this.arrayListSong = songList;
        this.songPosition = pos;
        path = songList.get(pos);
        Log.d("hey", "setsongList: " + arrayListSong.size());
        if (isPlayMusic == 1) {
            mMediaPlayer.stop();
            countDownTimer.cancel();
        }
        playMusic(path);
    }

}