package com.stp.music_player.AuthorList;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.stp.music_player.AuthorList.adapter.SongByAuthorAdapter;
import com.stp.music_player.R;
import com.stp.music_player.ViewPagerAdapter;
import com.stp.music_player.fragment.Playing;
import com.stp.music_player.model.SongPath;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class  SubSongListFragment extends Fragment implements SongByAuthorAdapter.OnSongSelected {

    String artist;
    ArrayList<SongPath> mSongList;
    RecyclerView mRecyclerView;
    TextView mTitleTV;
    SongByAuthorAdapter adapter;
    Context mContext;
    public SubSongListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sub_song_list, container, false);
        mContext = getContext();
        mRecyclerView = view.findViewById(R.id.rv_song_by_author);
        mTitleTV = view.findViewById(R.id.tv_title);
        getData();

        mTitleTV.setText(artist);
        adapter = new SongByAuthorAdapter(mSongList, mContext,
                this, getActivity().getSupportFragmentManager());

        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(RecyclerView.VERTICAL);

        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(adapter);
        return view;
    }

    public void getData() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            this.artist = bundle.getString("artist");
            this.mSongList = (ArrayList<SongPath>) bundle.getSerializable("list");
        }

    }

    @Override
    public void onSongClick(int pos) {
        TabLayout tabLayout = getActivity().findViewById(R.id.tab_layout);
        TabLayout.Tab tab = tabLayout.getTabAt(1);
        Playing playingFragment = (Playing) ViewPagerAdapter.getFrament(1);
        playingFragment.setsongList(convert(), pos);
        tab.select();
    }

    public ArrayList<String> convert(){
        ArrayList<String> arr = new ArrayList<>();
        for(int i=0; i<mSongList.size(); i++){
            arr.add(mSongList.get(i).getPath());
        }
        return arr;
    }
}
