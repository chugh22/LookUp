package com.example.anirudh.lookup;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.anirudh.lookup.Adapters.HistoryAdapter;
import com.example.anirudh.lookup.DataBase.DataBaseUtils;
import com.example.anirudh.lookup.DataBase.DatabaseHelper;
import com.example.anirudh.lookup.DataBase.HistoryTable;
import com.example.anirudh.lookup.models.HistoryModel;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
public static final String TAG ="HISTORY ACTIVITY : " ;
    SQLiteDatabase db ;
    DatabaseHelper dbhelper ;
    RecyclerView rvHistory ;
    ArrayList<HistoryModel> list ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarHistory) ;
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setTitle("History");
        list = new ArrayList<>() ;
        dbhelper = new DatabaseHelper(this) ;
        db = dbhelper.getWritableDatabase() ;
        rvHistory = (RecyclerView)findViewById(R.id.rvHistory) ;
        rvHistory.setLayoutManager(new LinearLayoutManager(this));

        list = HistoryTable.getHistory(db) ;
        HistoryAdapter adapter = new HistoryAdapter(list , this) ;
        adapter.setOnStarClickListener(new HistoryAdapter.onStarClickedListener() {
            @Override
            public void onStarClicked(boolean isStar, int pos) {

                HistoryTable.updateStar(db , list.get(pos).getWord() , isStar);
            }
        });
        Log.d(TAG, "onCreate: " + list.size());
        rvHistory.setAdapter(adapter);

    }
}
