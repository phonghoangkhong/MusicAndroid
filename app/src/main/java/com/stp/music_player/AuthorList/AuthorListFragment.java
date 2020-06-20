package com.stp.music_player.AuthorList;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stp.music_player.AuthorList.adapter.AuthorListAdapter;
import com.stp.music_player.R;
import com.stp.music_player.database.AppDatabase;
import com.stp.music_player.model.SongPath;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AuthorListFragment extends Fragment implements AuthorListAdapter.OnAuthorSelected {

    RecyclerView mAuthorRv;
    private ArrayList<String> mAuthorList;
    AuthorListAdapter adapter;

    public AuthorListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_author_list, container, false);;

        mAuthorRv = v.findViewById(R.id.list_author);
        mAuthorList = AppDatabase.getInstance(getContext()).getAuthorList();


        adapter = new AuthorListAdapter(mAuthorList, getContext(), this);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(RecyclerView.VERTICAL);

        mAuthorRv.setLayoutManager(manager);
        mAuthorRv.setAdapter(adapter);

        return v;
    }

    @Override
    public void onAuthorClicked(int pos) {
        String artist = mAuthorList.get(pos);
        ArrayList<SongPath> songList = AppDatabase.getInstance(getContext()).getSongListByArtist(artist);

        SubSongListFragment subSongListFragment = new SubSongListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("artist", artist);
        bundle.putParcelableArrayList("list", songList);
        subSongListFragment.setArguments(bundle);

//        FrameLayout frameLayout = (FrameLayout)getActivity().findViewById(R.id.author_Frame);
//        frameLayout.setVisibility(View.GONE);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.author_Frame, subSongListFragment)
                .addToBackStack(null)
                .commit();

    }
}
