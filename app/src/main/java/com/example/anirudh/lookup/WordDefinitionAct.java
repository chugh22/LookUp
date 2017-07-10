package com.example.anirudh.lookup;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anirudh.lookup.Adapters.DefinitionAdapter;
import com.example.anirudh.lookup.Api.SingeltonApi;
import com.example.anirudh.lookup.models.LexAndDef;
import com.example.anirudh.lookup.models.Word;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WordDefinitionAct extends AppCompatActivity {

    public static final String TAG = "WordActivity" ;
    TextView tvWord ;
    RecyclerView rvDefinitionList ;
    MediaPlayer mediaPlayer ;
    ImageButton  ibSpeaker  , ibTranslations;
    CheckBox ibStar ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_word_definition);
        Typeface myfont = Typeface.createFromAsset(getAssets() ,"my_cursive_font.ttf" ) ;

        tvWord = (TextView)findViewById(R.id.tvWord) ;
        tvWord.setTypeface(myfont);
        ibStar = (CheckBox)findViewById(R.id.imageButtonStar) ;
        ibTranslations = (ImageButton)findViewById(R.id.imageButtonTranslate) ;
        final String word = getIntent().getStringExtra("word") ;

        ibSpeaker = (ImageButton)findViewById(R.id.imageButtonSpeaker) ;
        final ArrayList<LexAndDef> lexAndDefs = new ArrayList<>() ;
        final DefinitionAdapter adapter = new DefinitionAdapter(lexAndDefs ,this) ;
        mediaPlayer = new MediaPlayer() ;
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC) ;
        ibStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tvWord.setText(word);

        rvDefinitionList = (RecyclerView)findViewById(R.id.rvListDefinitions) ;
        rvDefinitionList.setLayoutManager(new LinearLayoutManager(this));

        for(int i = 0 ;i<getIntent().getIntExtra("count" ,0) ; i++){

            LexAndDef defObj = new LexAndDef(
                    getIntent().getStringExtra("LexicalCategory"+i),
                    getIntent().getStringExtra("definition"+i),
                    getIntent().getStringExtra("examples"+i)
            );
            lexAndDefs.add(defObj) ;
        }
        final String audioUrl = getIntent().getStringExtra("audioUrl") ;
        ibSpeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(audioUrl != null) {
                        new Player().execute(audioUrl);
                    }else{
                        Toast.makeText(
                                WordDefinitionAct.this,
                                "Audio not available",
                                Toast.LENGTH_SHORT
                        ).show();
                    }

            }
        });

        rvDefinitionList.setAdapter(adapter);

        ibTranslations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingeltonApi.build().getTranslationApi().getTranslations(
                        "en" ,
                        word ,
                        "es" ,
                        MainActivity.Dictionary_App_Id ,
                        MainActivity.Dictionary_App_Key).
                        enqueue(new Callback<Word>() {
                            @Override
                            public void onResponse(Call<Word> call, Response<Word> response) {

                                Log.d(TAG, "onResponse: " + response.body());
                                if (response.body() != null) {
                                    for (int i = 0; i < lexAndDefs.size(); i++) {
                                        if (response.body().getResults()[0].getLexicalEntries()[i]
                                                .getSenses()[0].
                                                getExamples() != null) {
                                            lexAndDefs.get(i).
                                                    setExamples(response.body().getResults()[0].getLexicalEntries()[i].getEntries()[0]
                                                            .getSenses()[0].getExamples()[0].getTranslations()[0].getText());
                                        } else {
                                            lexAndDefs.get(i).setExamples("No translations found");
                                        }
                                    }
                                    adapter.notifyDataSetChanged();
                                }else{
                                    Toast.makeText(WordDefinitionAct.this, "No translations found", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Word> call, Throwable t) {
                                Toast.makeText(WordDefinitionAct.this, "On failure Translation", Toast.LENGTH_SHORT).show();
                            }
                        });


            }
        });

    }

    class Player extends AsyncTask<String , Void , Boolean>{

        private ProgressDialog progress ;
        @Override
        protected Boolean doInBackground(String... params) {

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                }
            });
            try {
                mediaPlayer.setDataSource(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }catch (IllegalStateException e){
                e.printStackTrace();
            }
            try {
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }

        public Player() {
            this.progress = new ProgressDialog(WordDefinitionAct.this) ;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progress.setMessage("Buffering ....");
            this.progress.show() ;
        }

        @Override
        protected void onPostExecute(Boolean b) {
            super.onPostExecute(b);
            if(this.progress.isShowing()) {
                this.progress.cancel();
                this.progress.dismiss();
            }
            if(mediaPlayer.isPlaying()){
                mediaPlayer.reset();
            }
            mediaPlayer.start();

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mediaPlayer!=null){
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null ;
        }
    }
}
