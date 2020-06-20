package com.stp.music_player.AuthorList.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.stp.music_player.PlayList.PlayListFragment;
import com.stp.music_player.database.AppDatabase;
import com.stp.music_player.model.SongPath;
import com.stp.music_player.R;
import com.stp.music_player.SongList.dialog.SongInfoDialog;

import java.io.File;
import java.util.ArrayList;

public class SongByAuthorAdapter extends RecyclerView.Adapter<SongByAuthorAdapter.SongViewHolder> {

    ArrayList<SongPath> mSongList;
    Context mContext;
    OnSongSelected onSongSelected;
    FragmentManager fm;

    public SongByAuthorAdapter(ArrayList<SongPath> mSongList, Context mContext,
                               OnSongSelected onSongSelected, FragmentManager fm) {
        this.mSongList = mSongList;
        this.mContext = mContext;
        this.onSongSelected = onSongSelected;
        this.fm = fm;

    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.song_item, parent, false);

        return new SongViewHolder(view, onSongSelected);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        String song = mSongList.get(position).getPath();
        File songFile = new File(song);
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(song);
        String author = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        String title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);

        if(author == null)  author = "Unknown";
        if(title == null)   title = songFile.getName();

        holder.author.setText(author);
        holder.title.setText(title);
        byte[] art = mmr.getEmbeddedPicture();
        if (art != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(art, 0, art.length);
            holder.img.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 150, 150, true));
        } else {
            holder.img.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_launcher));
        }
    }

    @Override
    public int getItemCount() {
        return (mSongList != null) ? mSongList.size() : 0;
    }

    public class SongViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView img;
        TextView author, title;
        ImageButton btnOpt;
        SongByAuthorAdapter.OnSongSelected onSongSelected;

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
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                    String[] opt = {"Chi tiết bài hát", "Thêm vào Playlist", "Xóa"};
                    builder.setItems(opt, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    SongInfoDialog songInfo = new SongInfoDialog(mContext, mSongList.get(getAdapterPosition()).getPath());
                                    songInfo.show();
                                    break;
                                case 1:
                                    PlayListFragment playListFragment = new PlayListFragment();
                                    fm.beginTransaction()
                                            .replace(R.id.replaceFragement2, playListFragment)
                                            .addToBackStack(null)
                                            .commit();
                                    break;
                                case 2:
                                    deleteSong(mSongList.get(getAdapterPosition()));
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
        public void onClick(View view) {
            onSongSelected.onSongClick(getAdapterPosition());
        }
    }

    public interface OnSongSelected {
        public void onSongClick(int pos);
    }

    public void deleteSong(SongPath song) {
        File songPath = new File(song.getPath());
        if (songPath.delete() && AppDatabase.getInstance(mContext).deleteSong(song.getId())){
            mSongList.remove(song);
            this.notifyDataSetChanged();
            Toast.makeText(mContext, "Xóa bài hát thành công", Toast.LENGTH_SHORT).show();
        }
    }

}
