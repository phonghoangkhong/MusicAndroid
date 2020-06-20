package com.stp.music_player.PlayList;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.stp.music_player.PlayList.Adapter.PlayListAdapter;
import com.stp.music_player.R;
import com.stp.music_player.SongList.SongListFragment;
import com.stp.music_player.database.AppDatabase;
import com.stp.music_player.database.MusicSQLiteOpenHelper;
import com.stp.music_player.database.Playlist;
import com.stp.music_player.fragment.Personal;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayListFragment extends Fragment {
    int idPlaylist_creat = 0;
    int idPlaylist_exist = 0;
    TextView textViewBack;
    ListView listViewALbum;
    ArrayList<Playlist> albumList;
    PlayListAdapter Play_listAdapter;
    Button mButton_create;
    Context context;

    public PlayListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_play_list, container, false);
        context = getContext();
        mButton_create = view.findViewById(R.id.btn_create);
        listViewALbum = view.findViewById(R.id.lv_Album);
        textViewBack = view.findViewById(R.id.tv_back_to_personal);
        GetPlayList();
//click để tạo 1 play list mới
        mButton_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowPopup_dialog();
            }
        });
        textViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Personal personal = new Personal();
                FrameLayout frameLayout = (FrameLayout) getActivity().findViewById(R.id.replaceFragement);
                frameLayout.setVisibility(View.GONE);
                frameLayout = (FrameLayout) getActivity().findViewById(R.id.replaceFragement2);
                frameLayout.setVisibility(View.VISIBLE);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.replaceFragement2, personal)
                        .addToBackStack(null)
                        .commit();
            }
        });
// click vào 1 playlist đã tồn tại
        onClickItemPlayList(listViewALbum);
        return view;
    }

    public void GetPlayList() {
//        if(albumList.size() > 0) albumList.clear();
        albumList = AppDatabase.getInstance(context).getPlaylistList();
        Play_listAdapter = new PlayListAdapter(context, R.layout.line_playlist, albumList);
        listViewALbum.setAdapter(Play_listAdapter);
    }

    // show dialog để tạo playlist
    public void ShowPopup_dialog() {

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.pop_up_dialog);
        final EditText editText = dialog.findViewById(R.id.edt_name);
        Button button_agree = dialog.findViewById(R.id.btn_agree);
        Button button_cancel = dialog.findViewById(R.id.btn_cancel);
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        button_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String namePlaylist = editText.getText().toString();
                if (namePlaylist == "") {
                    Toast.makeText(context, "Vui lòng nhập tên Playlist", Toast.LENGTH_SHORT).show();
                } else {
                    Playlist playlist = new Playlist(namePlaylist);
                    AppDatabase.getInstance(getContext()).insertPlaylist(playlist);
                    playlist.setPlaylistId(AppDatabase.getInstance(context).getPlaylistList().size());
                    idPlaylist_creat = playlist.getPlaylistId();//id chưa tự tăng

                    Toast.makeText(context, "Đã tạo Playlist thành công", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    // chuyển đến trang danh sách bài hát để chọn
                    SongForPlayListFragment songListFragment = new SongForPlayListFragment(idPlaylist_creat);
                    FrameLayout frameLayout = (FrameLayout) getActivity().findViewById(R.id.replaceFragement);
                    frameLayout.setVisibility(View.GONE);
                    frameLayout = (FrameLayout) getActivity().findViewById(R.id.replaceFragement2);
                    frameLayout.setVisibility(View.VISIBLE);
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.replaceFragement2, songListFragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });
        dialog.show();
    }

    //click vào 1 playlist đã tồn tại
    public void onClickItemPlayList(final ListView listView) {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Playlist pl = (Playlist) listView.getAdapter().getItem(i);
                idPlaylist_exist = pl.getPlaylistId();//dang có lỗi

                SongInPlayListFragment songListFragment = new SongInPlayListFragment(idPlaylist_exist);

                FrameLayout frameLayout = (FrameLayout) getActivity().findViewById(R.id.replaceFragement);
                frameLayout.setVisibility(View.GONE);
                frameLayout = (FrameLayout) getActivity().findViewById(R.id.replaceFragement2);
                frameLayout.setVisibility(View.VISIBLE);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.replaceFragement2, songListFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });

    }

}
