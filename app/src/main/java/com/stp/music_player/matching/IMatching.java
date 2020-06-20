package com.stp.music_player.matching;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface IMatching {
    @Multipart
    @POST("mobile_upload")
    Call<MusicResponse> getSongName(@Part MultipartBody.Part file);
}
