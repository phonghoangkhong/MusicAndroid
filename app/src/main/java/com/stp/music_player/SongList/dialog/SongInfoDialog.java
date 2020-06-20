package com.stp.music_player.SongList.dialog;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.widget.TextView;

import com.stp.music_player.R;
import com.stp.music_player.model.Song;

import java.io.File;

public class SongInfoDialog extends Dialog {

    private String songPath;
    private Context context;
    private TextView artist, title, album, genre, duration;

    public SongInfoDialog(Context context, String song) {
        super(context);
        this.context = context;
        this.songPath = song;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song_info);

        artist = findViewById(R.id.song_artist);
        title = findViewById(R.id.song_title);
        album = findViewById(R.id.song_album);
        duration = findViewById(R.id.song_duration);
        genre = findViewById(R.id.song_genre);
        Song song = getInfo(songPath);

        artist.setText(song.getAuthor());
        title.setText(song.getSongName());
        album.setText(song.getAlbum());
        duration.setText(song.getDuration());
        genre.setText(song.getGenre());
    }

    public Song getInfo(String path){
        Song song = null;
        File songFile = new File(path);
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(path);
        String author = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        String title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        String album = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
        String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        String genre = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);
        if(author == null)  author = "Unknown";
        if(title == null)   title = songFile.getName();
        if(album == null)  album = "Unknown";
        if(duration == null)  duration = "Unknown";
        if(genre == null)  genre = "Unknown";

        song = new Song(title, author, path, genre, album, duration);

        return song;
    }

}

