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

import com.stp.music_player.R;
import com.stp.music_player.SongList.dialog.SongInfoDialog;
import com.stp.music_player.database.AppDatabase;
import com.stp.music_player.model.SongPath;

import java.io.File;
import java.util.ArrayList;

public class SongInPlayListAdapter extends RecyclerView.Adapter<SongInPlayListAdapter.SongViewHolder> {

    ArrayList<SongPath> songListPath;
    Context context;
    OnSongSelected onSongSelected;

    public SongInPlayListAdapter(ArrayList<SongPath> songList, Context context, OnSongSelected onSongSelected) {
        this.songListPath = songList;
        this.context = context;
        this.onSongSelected = onSongSelected;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View songItem = inflater.inflate(R.layout.song_item, viewGroup, false);
        return new SongViewHolder(songItem, onSongSelected);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder songViewHolder, int i) {
        String song = songListPath.get(i).getPath();
        File songFile = new File(song);
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(song);
        String author = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        String title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);

        if(author == null)  author = "Unknown";
        if(title == null)   title = songFile.getName();

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
        ImageButton btnOpt;
        OnSongSelected onSongSelected;

        public SongViewHolder(@NonNull View itemView, OnSongSelected onSongSelected) {
            super(itemView);
            this.onSongSelected = onSongSelected;
            itemView.setOnClickListener(this);

            img = itemView.findViewById(R.id.img_song);
            author = itemView.findViewById(R.id.text_author);
            title = itemView.findViewById(R.id.text_title);
            btnOpt = itemView.findViewById(R.id.btn_opt);

            btnOpt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    String[] opt = {"Chi tiết bài hát", "Thêm vào Playlist", "Xóa"};
                    builder.setItems(opt, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    SongInfoDialog songInfo = new SongInfoDialog(context, songListPath.get(getAdapterPosition()).getPath());
                                    songInfo.show();
                                    break;
                                case 1:

                                    break;
                                case 2:
                                    deleteSong(songListPath.get(getAdapterPosition()));
                                    break;
                            }
                        }
                    });

                    AlertDialog optDialog = builder.create();
                    optDialog.show();
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
        if (songPath.delete() && AppDatabase.getInstance(context).deleteSong(song.getId())){
            songListPath.remove(song);
            this.notifyDataSetChanged();
            Toast.makeText(context, "Xóa bài hát thành công", Toast.LENGTH_SHORT).show();
        }
    }
}
