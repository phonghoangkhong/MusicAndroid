package com.stp.music_player.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MusicSQLiteOpenHelper extends SQLiteOpenHelper {


    public static final String SONG_TABLE = "song_table";
    public static final String SONG_ID = "songId";
    public static final String SONG_NAME = "songName";
    public static final String AUTHOR = "author";
    public static final String LINK = "link";
    public static final String ALBUM = "album";
    public static final String GENRE = "genre";
    public static final String DURATION = "duration";

    public static final String PLAYLIST_TABLE = "playlist_table";
    public static final String PLAYLIST_ID = "playlistId";
    public static final String PLAYLIST_NAME = "playlistName";
    public static final String ID = "id";
    public static final String SONG_PLAYLIST_TABLE = "song_playlist_table";
    private static final String DATABASE_NAME = "musicPlayer.db";
    private static final int DATABASE_VERSION = 1;


    public MusicSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createSongTable = String.format
                ("CREATE table %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
                SONG_TABLE, SONG_ID, SONG_NAME, AUTHOR, LINK, ALBUM, GENRE, DURATION);

        String createPlaylistTable = String.format("CREATE table %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT)",
                PLAYLIST_TABLE, PLAYLIST_ID, PLAYLIST_NAME);
        String createSongPlaylistTable = String.format("CREATE table %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s TEXT)",
                SONG_PLAYLIST_TABLE, ID, SONG_ID, PLAYLIST_ID,LINK);
        sqLiteDatabase.execSQL(createSongTable);
        sqLiteDatabase.execSQL(createPlaylistTable);
        sqLiteDatabase.execSQL(createSongPlaylistTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String DROP_SONG_TABLE = String.format("DROP TABLE IF EXIST %s", SONG_TABLE);
        sqLiteDatabase.execSQL(DROP_SONG_TABLE);
        String DROP_PLAYLIST_TABLE = String.format("DROP TABLE IF EXIST %s", PLAYLIST_TABLE);
        sqLiteDatabase.execSQL(DROP_PLAYLIST_TABLE);
        String DROP_SONG_PLAYLIST_TABLE = String.format("DROP TABLE IF EXIST %s", SONG_PLAYLIST_TABLE);
        sqLiteDatabase.execSQL(DROP_SONG_PLAYLIST_TABLE);
    }
}
