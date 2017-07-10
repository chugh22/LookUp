package com.example.anirudh.lookup;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.anirudh.lookup.Adapters.HistoryAdapter;
import com.example.anirudh.lookup.DataBase.DatabaseHelper;
import com.example.anirudh.lookup.DataBase.HistoryTable;
import com.example.anirudh.lookup.models.HistoryModel;

import java.util.ArrayList;

public class StarredActivity extends AppCompatActivity {

    TextView textView ;
    RecyclerView rv ;
    ArrayList<HistoryModel> list ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starred);
        textView = (TextView)findViewById(R.id.textView) ;
        Typeface myfont = Typeface.createFromAsset(getAssets() ,"my_cursive_font.ttf" ) ;
        textView.setTypeface(myfont);
        rv = (RecyclerView)findViewById(R.id.rvStarredWords) ;
        list = new ArrayList<>() ;
        DatabaseHelper dbhelper = new DatabaseHelper(this) ;
        SQLiteDatabase db = dbhelper.getWritableDatabase() ;

        rv.setLayoutManager(new LinearLayoutManager(this));

        list = HistoryTable.getStarred(db) ;
        HistoryAdapter adapter = new HistoryAdapter(list , this) ;
        adapter.setOnStarClickListener(new HistoryAdapter.onStarClickedListener() {
            @Override
            public void onStarClicked(boolean isStar, int pos) {
                //do nothing
            }
        });
        rv.setAdapter(adapter);

    }
}
