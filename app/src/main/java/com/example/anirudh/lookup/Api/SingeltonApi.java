package com.example.anirudh.lookup.Api;

import com.example.anirudh.lookup.MainActivity;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by anirudh on 07/07/17.
 */

public class SingeltonApi {
    private static SingeltonApi singeltonApi ;
    private DictionaryApi dictionaryApi ;
    private TranslationApi translationApi ;
    private SingeltonApi(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.Dictionary_BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build() ;
        dictionaryApi= retrofit.create(DictionaryApi.class);
        translationApi = retrofit.create(TranslationApi.class) ;

    }

    public TranslationApi getTranslationApi() {
        return translationApi;
    }

    public DictionaryApi getDictionaryApi() {
        return dictionaryApi;
    }

    public static SingeltonApi build(){
        if(singeltonApi == null){
            singeltonApi = new SingeltonApi() ;
        }
        return singeltonApi ;
    }

}
