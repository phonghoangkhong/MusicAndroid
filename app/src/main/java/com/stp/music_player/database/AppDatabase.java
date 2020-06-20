package com.stp.music_player.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.stp.music_player.model.SongPath;
import com.stp.music_player.model.Song;

import java.util.ArrayList;

public class AppDatabase {
    MusicSQLiteOpenHelper mMusicSQLiteOpenHelper;
    private static AppDatabase instance;

    private AppDatabase(Context context) {
        mMusicSQLiteOpenHelper = new MusicSQLiteOpenHelper(context);
    }

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new AppDatabase(context);
        }
        return instance;
    }

    public void insertSong(Song song) {
        SQLiteDatabase sqLiteDatabase = instance.mMusicSQLiteOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MusicSQLiteOpenHelper.SONG_NAME, song.getSongName());
        contentValues.put(MusicSQLiteOpenHelper.AUTHOR, song.getAuthor());
        contentValues.put(MusicSQLiteOpenHelper.LINK, song.getLink());
        contentValues.put(MusicSQLiteOpenHelper.GENRE, song.getGenre());
        contentValues.put(MusicSQLiteOpenHelper.ALBUM, song.getAlbum());
        contentValues.put(MusicSQLiteOpenHelper.DURATION, song.getDuration());
        sqLiteDatabase.insert(MusicSQLiteOpenHelper.SONG_TABLE, null, contentValues);
    }

    public void insertPlaylist(Playlist playlist) {
        SQLiteDatabase sqLiteDatabase = instance.mMusicSQLiteOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MusicSQLiteOpenHelper.PLAYLIST_NAME, playlist.getPlaylistName());
        sqLiteDatabase.insert(MusicSQLiteOpenHelper.PLAYLIST_TABLE, null, contentValues);
    }

    public void insertSongIntoPlaylist(int songId, int playlistId) {

        SQLiteDatabase sqLiteDatabase = instance.mMusicSQLiteOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MusicSQLiteOpenHelper.SONG_ID, songId);
        contentValues.put(MusicSQLiteOpenHelper.PLAYLIST_ID, playlistId);
        sqLiteDatabase.insert(MusicSQLiteOpenHelper.SONG_PLAYLIST_TABLE, null, contentValues);
    }

    public ArrayList<SongPath> getSongList() {
        SQLiteDatabase sqLiteDatabase = instance.mMusicSQLiteOpenHelper.getReadableDatabase();
        ArrayList<SongPath> retArray = new ArrayList<>();
        String[] projection = {MusicSQLiteOpenHelper.SONG_ID, MusicSQLiteOpenHelper.SONG_NAME, MusicSQLiteOpenHelper.AUTHOR,
                MusicSQLiteOpenHelper.LINK};
        Cursor cursor = sqLiteDatabase.query(MusicSQLiteOpenHelper.SONG_TABLE, projection, null, null,
                null, null, null);
        while (cursor.moveToNext()) {
            int songId = cursor.getInt(0);
//            String songName = cursor.getString(1);
//            String author = cursor.getString(2);
            String link = cursor.getString(3);
            retArray.add(new SongPath(link, songId));
        }
        cursor.close();
        return retArray;
    }

    public ArrayList<Playlist> getPlaylistList() {
        SQLiteDatabase sqLiteDatabase = instance.mMusicSQLiteOpenHelper.getReadableDatabase();
        ArrayList<Playlist> retArray = new ArrayList<>();
        String[] projection = {MusicSQLiteOpenHelper.PLAYLIST_ID, MusicSQLiteOpenHelper.PLAYLIST_NAME};
        Cursor cursor = sqLiteDatabase.query(MusicSQLiteOpenHelper.PLAYLIST_TABLE, projection, null, null,
                null, null, null);
        while (cursor.moveToNext()) {
            int playlistId = cursor.getInt(0);
            String playlistName = cursor.getString(1);
            retArray.add(new Playlist(playlistId, playlistName));
        }
        cursor.close();
        return retArray;
    }

    public ArrayList<String> getAuthorList() {
        SQLiteDatabase sqLiteDatabase = instance.mMusicSQLiteOpenHelper.getReadableDatabase();
        ArrayList<String> retArray = new ArrayList<>();

        String[] projection = {MusicSQLiteOpenHelper.SONG_ID, MusicSQLiteOpenHelper.AUTHOR};
        Cursor cursor = sqLiteDatabase.query(true, MusicSQLiteOpenHelper.SONG_TABLE, projection, null, null,
                MusicSQLiteOpenHelper.AUTHOR, null, null, null);
        while (cursor.moveToNext()) {
            String author = cursor.getString(1);
            retArray.add(author);
        }
        cursor.close();
        return retArray;
    }

    public Song getSongById(String songId) {
        SQLiteDatabase sqLiteDatabase = instance.mMusicSQLiteOpenHelper.getReadableDatabase();
        String[] projection = {MusicSQLiteOpenHelper.SONG_ID, MusicSQLiteOpenHelper.SONG_NAME, MusicSQLiteOpenHelper.AUTHOR, MusicSQLiteOpenHelper.LINK};
        String whereClause = MusicSQLiteOpenHelper.SONG_ID + " = ?";
        String[] whereArgs = new String[]{songId};
        Cursor cursor = sqLiteDatabase.query(MusicSQLiteOpenHelper.SONG_TABLE, projection, whereClause, whereArgs, null, null, null);
        Song retSong = null;
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String author = cursor.getString(2);
            String link = cursor.getString(3);
            retSong = new Song(id, name, author, link);
        }
        cursor.close();
        return retSong;
    }

    public ArrayList<Song> getPlaylistById(String playlistId) {
        SQLiteDatabase sqLiteDatabase = instance.mMusicSQLiteOpenHelper.getReadableDatabase();
        ArrayList<Song> retArray = new ArrayList<>();
        String[] projection = {MusicSQLiteOpenHelper.SONG_ID};
        String whereClause = MusicSQLiteOpenHelper.PLAYLIST_ID + " = ?";
        Log.d("Get playlist", "getPlaylistById: " + whereClause);
        String[] whereArgs = new String[]{playlistId};
        Cursor cursor = sqLiteDatabase.query(MusicSQLiteOpenHelper.SONG_PLAYLIST_TABLE, projection,
                whereClause, whereArgs, null, null, null);
        while (cursor.moveToNext()) {
            String id = String.valueOf(cursor.getInt(0));
            retArray.add(getSongById(id));
        }
        cursor.close();
        return retArray;
    }

    public ArrayList<SongPath> getSongListByArtist(String author) {
        ArrayList<SongPath> retArr = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = instance.mMusicSQLiteOpenHelper.getReadableDatabase();

        String[] projection = {MusicSQLiteOpenHelper.SONG_ID, MusicSQLiteOpenHelper.LINK, MusicSQLiteOpenHelper.AUTHOR};
        Cursor cursor = sqLiteDatabase.query(MusicSQLiteOpenHelper.SONG_TABLE,
                projection, null,
                null, null, null, null);

        while (cursor.moveToNext()) {
            String songAuthor = cursor.getString(2);
            if (author.equals(songAuthor)) {
                int id = cursor.getInt(0);
                String path = cursor.getString(1);
                retArr.add(new SongPath(path, id));
            }
        }
        return retArr;
    }


    public ArrayList<String> getSongPathList() {
        SQLiteDatabase sqLiteDatabase = instance.mMusicSQLiteOpenHelper.getReadableDatabase();
        ArrayList<String> retArray = new ArrayList<>();
        String[] projection = {MusicSQLiteOpenHelper.SONG_ID, MusicSQLiteOpenHelper.SONG_NAME, MusicSQLiteOpenHelper.AUTHOR,
                MusicSQLiteOpenHelper.LINK};
        Cursor cursor = sqLiteDatabase.query(MusicSQLiteOpenHelper.SONG_TABLE, projection, null, null,
                null, null, null);
        while (cursor.moveToNext()) {
            String link = cursor.getString(3);
            retArray.add(link);
        }
        return retArray;
    }

    public boolean deleteSong(int id) {
        SQLiteDatabase sqLiteDatabase = instance.mMusicSQLiteOpenHelper.getWritableDatabase();

        return sqLiteDatabase.delete(MusicSQLiteOpenHelper.SONG_TABLE,
                MusicSQLiteOpenHelper.SONG_ID + "=" + id, null) > 0;
    }

    public void deleteAllSong() {
        SQLiteDatabase sqLiteDatabase = instance.mMusicSQLiteOpenHelper.getWritableDatabase();
        sqLiteDatabase.delete(MusicSQLiteOpenHelper.SONG_TABLE, null, null);
    }
    public boolean deletePlayList(int id){
        SQLiteDatabase sqLiteDatabase = instance.mMusicSQLiteOpenHelper.getWritableDatabase();

        return sqLiteDatabase.delete(MusicSQLiteOpenHelper.PLAYLIST_TABLE,
                MusicSQLiteOpenHelper.PLAYLIST_ID + "=" + id,  null) > 0;
    }

}
