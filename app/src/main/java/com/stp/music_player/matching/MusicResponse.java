package com.stp.music_player.matching;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MusicResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("song_name")
    @Expose
    private String songName;

    public Boolean getSuccess() {
        return success;
    }

    public String getSongName() {
        return songName;
    }
}
