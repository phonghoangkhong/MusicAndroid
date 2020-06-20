package com.stp.music_player;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;


import android.app.NotificationManager;
import android.content.IntentFilter;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.tabs.TabLayout;
import com.stp.music_player.BroadcaseReceiver.NotiBroadcast;
import com.stp.music_player.database.AppDatabase;
import com.stp.music_player.database.MusicSQLiteOpenHelper;
import com.stp.music_player.model.Song;
import com.stp.music_player.fragment.Matching;
import com.stp.music_player.fragment.Personal;
import com.stp.music_player.fragment.Playing;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final String MEDIA_PATH = new String(Environment.getExternalStorageDirectory().toString() + "/Music");
    private static final int PERMISSION_REQUEST_CODE = 10001;
    ViewPager viewPager;
    TabLayout tabLayout;
    public Personal mPersonal;
    public Playing mPlaying;
    public Matching mMatching;
    public  static  ArrayList<String> arrayListSong ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();

        ArrayList<String> authorList = AppDatabase.getInstance(this).getAuthorList();
        arrayListSong = new ArrayList<>();
        String path = "android.resource://" + getPackageName() + "/" + R.raw.reality;
        arrayListSong.add(path);
        String path2 = "android.resource://" + getPackageName() + "/" + R.raw.senorita;
        arrayListSong.add(path2);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(R.mipmap.song_list_96);
        tabLayout.getTabAt(1).setIcon(R.mipmap.headphones_96);

    }

    private void setupViewPager(ViewPager viewPager) {
        mMatching = new Matching();
        mPersonal = new Personal();
        mPlaying = new Playing();
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(mPersonal, "");
        adapter.addFrag(mPlaying, "");
      
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.replaceFragement);
        frameLayout.setVisibility(View.VISIBLE);
        frameLayout = (FrameLayout) findViewById(R.id.replaceFragement2);
        frameLayout.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    @Override
    protected void onStart() {
        super.onStart();
        RegisterBroadcastReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(notiBroadcast);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    NotiBroadcast notiBroadcast;
    private void RegisterBroadcastReceiver(){
        notiBroadcast = new NotiBroadcast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Playing.ACTION_PLAY);
        intentFilter.addAction(Playing.ACTION_NEXT);
        intentFilter.addAction(Playing.ACTION_PAUSE);
        intentFilter.addAction(Playing.ACTION_PREVIOUS);
        registerReceiver(notiBroadcast,intentFilter);
    }

    public void checkPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            requestPermissions(permission, PERMISSION_REQUEST_CODE);
        }
    }

}