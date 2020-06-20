package com.stp.music_player.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.stp.music_player.AuthorList.AuthorListFragment;
import com.stp.music_player.PersonalTab.CustomList;
import com.stp.music_player.PlayList.PlayListFragment;
import com.stp.music_player.R;
import com.stp.music_player.SongList.SongListFragment;
import com.stp.music_player.database.AppDatabase;
import com.stp.music_player.model.SongPath;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Personal extends Fragment {

    String[] title = {"Bài hát",
            "Playlist", "Ca sĩ"};
    Integer[][] img = {
            {R.drawable.img9, R.drawable.img5, R.drawable.img10},
            {R.drawable.img6, R.drawable.img0, R.drawable.img7},
            {R.drawable.img3, R.drawable.img2, R.drawable.img4}
    };
    Integer[] imageId = {
            R.drawable.ic1,
            R.drawable.playlist,
            R.drawable.ic_singer
    };
    ListView listView;

    public Personal() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);

        CustomList listAdapter = new CustomList(this.getContext(), title, imageId, img);
        listView = (ListView) view.findViewById(R.id.list);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {//i la position
                    case 0://click vao danh sach bai hat
                        ArrayList<SongPath> songList = AppDatabase.getInstance(getContext()).getSongList();
                        SongListFragment songListFragment = new SongListFragment();
                        FrameLayout frameLayout = (FrameLayout) getActivity().findViewById(R.id.replaceFragement);
                        frameLayout.setVisibility(View.GONE);
                        frameLayout = (FrameLayout) getActivity().findViewById(R.id.replaceFragement2);
                        frameLayout.setVisibility(View.VISIBLE);
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.replaceFragement2, songListFragment)
                                .addToBackStack(null)
                                .commit();
                        break;

                    case 1://click vao playlist
                        PlayListFragment playListFragment = new PlayListFragment();
                        FrameLayout frameLayout2 = (FrameLayout) getActivity().findViewById(R.id.replaceFragement);
                        frameLayout2.setVisibility(View.GONE);
                        frameLayout2 = (FrameLayout) getActivity().findViewById(R.id.replaceFragement2);
                        frameLayout2.setVisibility(View.VISIBLE);
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.replaceFragement2, playListFragment)
                                .addToBackStack(null)
                                .commit();
                        break;

                    case 2://click vao danh sach tac gia
                        AuthorListFragment authorListFragment = new AuthorListFragment();
                        FrameLayout frameLayout1 = (FrameLayout) getActivity().findViewById(R.id.replaceFragement);
                        frameLayout1.setVisibility(View.GONE);
                        frameLayout1 = (FrameLayout) getActivity().findViewById(R.id.replaceFragement2);
                        frameLayout1.setVisibility(View.VISIBLE);
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.replaceFragement2, authorListFragment)

                                .addToBackStack(null)
                                .commit();
                }
            }
        });
        return view;
    }

}

