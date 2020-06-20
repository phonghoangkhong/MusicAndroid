package com.stp.music_player.PlayList.Adapter;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stp.music_player.PlayList.SongForPlayListFragment;
import com.stp.music_player.R;
import com.stp.music_player.SongList.dialog.SongInfoDialog;
import com.stp.music_player.database.AppDatabase;
import com.stp.music_player.model.Song;
import com.stp.music_player.model.SongPath;

import java.io.File;
import java.util.ArrayList;

public class SongForPlayListAdapter extends RecyclerView.Adapter<SongForPlayListAdapter.SongViewHolder> {

    ArrayList<SongPath> songListPath;
    Context context;
    int idPlaylist;//id cua playlist
    int idSong;// id của bài hát
    OnSongSelected onSongSelected;

    public SongForPlayListAdapter(ArrayList<SongPath> songList, Context context, SongForPlayListFragment onSongSelected, int idPlaylist) {
        this.songListPath = songList;
        this.context = context;
        this.idPlaylist = idPlaylist;
        this.onSongSelected = onSongSelected;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View songItem = inflater.inflate(R.layout.song_item_playlist, viewGroup, false);// thay bằng dấu +
        return new SongViewHolder(songItem, onSongSelected);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder songViewHolder, int i) {
        String songPath = songListPath.get(i).getPath();
        File songFile = new File(songPath);
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(songPath);
        String author = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        String title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);

        if (author == null) author = "Unknown";
        if (title == null) title = songFile.getName();

        songViewHolder.author.setText(author);
        songViewHolder.title.setText(title);
        byte[] art = mmr.getEmbeddedPicture();
        if (art != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(art, 0, art.length);
            songViewHolder.img.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 150, 150, true));
        } else {
            songViewHolder.img.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_launcher));
        }


    }

    @Override
    public int getItemCount() {
        return songListPath.size();
    }

    public class SongViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView img;
        TextView author, title;
        ImageButton btn_plus;
        OnSongSelected onSongSelected;

        public SongViewHolder(@NonNull View itemView, OnSongSelected onSongSelected) {
            super(itemView);
            this.onSongSelected = onSongSelected;
            itemView.setOnClickListener(this);

            img = itemView.findViewById(R.id.img_song);
            author = itemView.findViewById(R.id.text_author);
            title = itemView.findViewById(R.id.text_title);
            btn_plus = itemView.findViewById(R.id.btn_plus);
//click dấu cộng để thêm bài hát vào playlist
            btn_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // thêm bài hát vào playlist
                    SongPath songPath = songListPath.get(getAdapterPosition());
                    idSong = songPath.getId();
                    Song song = AppDatabase.getInstance(context).getSongById(String.valueOf(idSong));
                    AppDatabase.getInstance(context).insertSongIntoPlaylist(idSong, idPlaylist);
                    Toast.makeText(context,"Đã Thêm",Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onClick(View v) {
            onSongSelected.onSongClick(getAdapterPosition());
        }
    }

    public interface OnSongSelected {
        public void onSongClick(int pos);
    }

    public void deleteSong(SongPath song) {
        File songPath = new File(song.getPath());
        if (songPath.delete() && AppDatabase.getInstance(context).deleteSong(song.getId())) {
            songListPath.remove(song);
            this.notifyDataSetChanged();
            Toast.makeText(context, "Xóa bài hát thành công", Toast.LENGTH_SHORT).show();
        }
    }
}
