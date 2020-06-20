package com.stp.music_player.SongList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.tabs.TabLayout;
import com.stp.music_player.R;
import com.stp.music_player.SongList.adapter.SongListAdapter;
import com.stp.music_player.ViewPagerAdapter;
import com.stp.music_player.database.AppDatabase;
import com.stp.music_player.database.MusicSQLiteOpenHelper;
import com.stp.music_player.fragment.Playing;
import com.stp.music_player.model.Song;
import com.stp.music_player.model.SongPath;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A simple {@link Fragment} subclass.
 */
public class SongListFragment extends Fragment implements  SongListAdapter.OnSongSelected, View.OnClickListener{

    SharedPreferences sharedPreferences;
    private final String TAG = "hey";
    private static String MY_PREFERENCES = "my_preferences";
    private final String MEDIA_PATH = new String(Environment.getExternalStorageDirectory().toString() + "/Music");
    private ImageButton mBtnShuffle, mBtnRefresh;
    private RecyclerView songListRV;
    private Context mContext;
    private ArrayList<SongPath> songListPath;
    private SongListAdapter adapter;
    private MediaMetadataRetriever mmr;

    public SongListFragment() {
        // Required empty public constructor
    }


    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_song_list, container, false);
        mContext = getContext();
        songListRV = v.findViewById(R.id.rv_songList);
        mBtnRefresh = v.findViewById(R.id.btn_refresh);
        mBtnShuffle = v.findViewById(R.id.btn_shuffle);

        final SharedPreferences reader = getActivity().getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        final boolean first = reader.getBoolean("is_first", true);
        Log.d(TAG, "onCreate: " + first);
        if(first){
            MusicSQLiteOpenHelper musicSQLiteOpenHelper = new MusicSQLiteOpenHelper(getContext());
            final SharedPreferences.Editor editor = reader.edit();
            editor.putBoolean("is_first", false);
            editor.commit();
            setUpDatabase();
        }

        songListPath = AppDatabase.getInstance(getContext()).getSongList();

        LinearLayoutManager manager =new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        songListRV.setLayoutManager(manager);

        adapter = new SongListAdapter(songListPath, mContext,
                        this, getActivity().getSupportFragmentManager());
        songListRV.setAdapter(adapter);

        mBtnShuffle.setOnClickListener(this);
        mBtnRefresh.setOnClickListener(this);

        return v;
    }

    @Override
    public void onSongClick(int pos) {
        TabLayout tabLayout = getActivity().findViewById(R.id.tab_layout);
        TabLayout.Tab tab = tabLayout.getTabAt(1);
        Playing playingFragment = (Playing) ViewPagerAdapter.getFrament(1);
        playingFragment.setsongList(getSongList(), pos);
        tab.select();
    }

    public ArrayList<String> getSongList(){
        ArrayList<String> arr = new ArrayList<>();
        for(int i=0; i<songListPath.size(); i++){
            arr.add(songListPath.get(i).getPath());
        }
        return arr;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_shuffle:
                Collections.shuffle(songListPath);
                adapter = new SongListAdapter(songListPath, mContext, this, getActivity().getSupportFragmentManager());
                songListRV.setAdapter(adapter);
                adapter.shuffleList();
                adapter.notifyItemRangeChanged(0, songListPath.size());
                break;
            case R.id.btn_refresh:
                AppDatabase.getInstance(getContext()).deleteAllSong();
                setUpDatabase();
                songListPath = AppDatabase.getInstance(getContext()).getSongList();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.detach(this);
                ft.attach(this);
                ft.commit();
                adapter.notifyDataSetChanged();
                break;
        }
    }

    public void sortSongList(){
        Collections.sort(songListPath, new Comparator<SongPath>() {
            @Override
            public int compare(SongPath song, SongPath t1) {
                return song.getPath().compareTo(t1.getPath());
            }
        });
    }

    public void setSongList(ArrayList<SongPath> songPath){
        this.songListPath = songPath;
    }

    public void setUpDatabase(){
        File home = new File(MEDIA_PATH);
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        Song song = null;

        for(File file: home.listFiles()){
            mmr.setDataSource(file.getPath());
            String title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            String author = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            String path = file.getPath();
            String album = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            String genre = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);
            String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            if(title == null)   title = file.getName();
            if(author == null)  author = "Unknown";
            if(genre == null)  author = "Unknown";
            if(album == null)  author = "Unknown";

            song = new Song(title, author, path, genre, album, duration);

            AppDatabase.getInstance(getContext()).insertSong(song);
        }
    }

}
