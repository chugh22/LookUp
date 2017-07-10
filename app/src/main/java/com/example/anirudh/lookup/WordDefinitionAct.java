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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anirudh.lookup.Adapters.DefinitionAdapter;
import com.example.anirudh.lookup.models.LexAndDef;

import java.io.IOException;
import java.util.ArrayList;

public class WordDefinitionAct extends AppCompatActivity {

    public static final String TAG = "WordActivity" ;
    TextView tvWord ;
    RecyclerView rvDefinitionList ;
    MediaPlayer mediaPlayer ;
    ImageButton ibStar , ibSpeaker ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_word_definition);
        Typeface myfont = Typeface.createFromAsset(getAssets() ,"my_cursive_font.ttf" ) ;

        tvWord = (TextView)findViewById(R.id.tvWord) ;
        tvWord.setTypeface(myfont);
        ibStar = (ImageButton)findViewById(R.id.imageButtonStar) ;
        ibStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ibStar.setImageDrawable(getDrawable(R.drawable.ic_star_filled));
                ibStar.setBackgroundResource(R.drawable.ic_star_filled);
            }
        });
        ibSpeaker = (ImageButton)findViewById(R.id.imageButtonSpeaker) ;
        ArrayList<LexAndDef> lexAndDefs = new ArrayList<>() ;
        DefinitionAdapter adapter = new DefinitionAdapter(lexAndDefs ,this) ;
        mediaPlayer = new MediaPlayer() ;
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC) ;

        String word = getIntent().getStringExtra("word") ;
        tvWord.setText(word);

        rvDefinitionList = (RecyclerView)findViewById(R.id.rvListDefinitions) ;
        rvDefinitionList.setLayoutManager(new LinearLayoutManager(this));

        for(int i =0 ;i<getIntent().getIntExtra("count" ,0) ; i++){

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
