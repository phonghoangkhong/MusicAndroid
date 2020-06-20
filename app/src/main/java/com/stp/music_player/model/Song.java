package com.stp.music_player.model;

public class Song {
    private int songId;
    private String songName;
    private String author;
    private String link, genre, album, duration;

    public Song(int songId, String songName, String author, String link) {
        this.songId = songId;
        this.songName = songName;
        this.author = author;
        this.link = link;
    }

    public Song(String songName, String author, String link, String genre, String album, String duration) {
        this.songName = songName;
        this.author = author;
        this.link = link;
        this.genre = genre;
        this.album = album;
        this.duration = duration;
    }

    public Song(String songName, String author, String link) {
        this.songName = songName;
        this.author = author;
        this.link = link;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getDuration() {
        int min, sec;
        try{
            int dur = Integer.parseInt(duration);
            min = dur / 60000;
            sec = (dur % 60000) / 1000;

        }catch(Exception e){
            return "Unknown";
        }
        return String.format("%02d:%02d", min, sec);
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Song{" +
                "songName='" + songName + '\'' +
                ", author='" + author + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
