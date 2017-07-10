package com.example.anirudh.lookup.Api;

import com.example.anirudh.lookup.models.Word;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

/**
 * Created by anirudh on 10/07/17.
 */

public interface TranslationApi {

    @GET("/entries/{sourceLang}/{wordId}/translations={targetLang}")
    Call<Word> getTranslations(@Path("sourceLang") String sourcelang , @Path("wordId") String word , @Path("targetLang") String lang ,
                @Header("app_id") String appId
            , @Header("app_key") String appKey) ;
}
