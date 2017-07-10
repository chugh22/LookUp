package com.example.anirudh.lookup.Api;

import com.example.anirudh.lookup.MainActivity;
import com.example.anirudh.lookup.models.Word;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

/**
 * Created by anirudh on 06/07/17.
 */

public interface DictionaryApi {


    @GET("entries/{sourceLanguage}/{word_id}")
    Call<Word> getDefination(
            @Path("word_id") String word
            , @Path("sourceLanguage") String sourceLanguage
            , @Header("app_id") String appId
            , @Header("app_key") String appKey
    ) ;

}
