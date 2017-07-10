package com.example.anirudh.lookup;

import android.app.Activity;
import android.content.Intent;
import android.icu.text.SelectFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.example.anirudh.lookup.Api.SingeltonApi;
import com.example.anirudh.lookup.Interface.RemoveCallBack;
import com.example.anirudh.lookup.Views.LookUp;
import com.example.anirudh.lookup.models.Word;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TextSelectionActivity extends Activity {

    public static final String TAG ="TextSelectionActivity";
    View view ;
    String selectedText ;
    FrameLayout frameLayout ;
    RemoveCallBack removeCallBack ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_selection);
        frameLayout = (FrameLayout)findViewById(R.id.activity_text_call) ;

        selectedText = getIntent().getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT).toString() ;
        removeCallBack = new RemoveCallBack() {
            @Override
            public void onViewRemoved() {
                finish();
                Log.d(TAG, "onViewRemoved: ");
            }
        };
        LookUp lookUp = new LookUp(this) ;
        view = lookUp.addtoWindowManager(selectedText , removeCallBack) ;
        frameLayout.addView(view);

    }
}
