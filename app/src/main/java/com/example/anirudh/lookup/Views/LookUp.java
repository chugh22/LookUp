package com.example.anirudh.lookup.Views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.media.Image;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anirudh.lookup.Adapters.DefinitionAdapter;
import com.example.anirudh.lookup.Api.SingeltonApi;
import com.example.anirudh.lookup.DataBase.DatabaseHelper;
import com.example.anirudh.lookup.DataBase.HistoryTable;
import com.example.anirudh.lookup.Interface.RemoveCallBack;
import com.example.anirudh.lookup.MainActivity;
import com.example.anirudh.lookup.R;
import com.example.anirudh.lookup.TextSelectionActivity;
import com.example.anirudh.lookup.WordDefinitionAct;
import com.example.anirudh.lookup.models.LexAndDef;
import com.example.anirudh.lookup.models.LexicalEntry;
import com.example.anirudh.lookup.models.Word;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by anirudh on 08/07/17.
 */

public class LookUp extends View {
public static final String TAG = "LookUp";
    private Context mContext ;
    View view ;
    TextView tvWord ;
    ImageButton ivClear ;
    RecyclerView rvDefinitionDialog ;
    ArrayList<LexAndDef> LexDefAl ;
    DefinitionAdapter adapter ;
    DatabaseHelper databaseHelper ;
    public LookUp(Context context) {
        super(context);
        mContext = context ;
    }
    public View addtoWindowManager(final String wordSearch , final RemoveCallBack removeCallBack){
        Log.d(TAG, "addtoWindowManager: ");
        LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = li.inflate(R.layout.dialog_layout , null) ;
        tvWord = (TextView) view.findViewById(R.id.tvWordDialog);
        ivClear = (ImageButton)view.findViewById(R.id.ibClearDialog) ;
        rvDefinitionDialog = (RecyclerView)view.findViewById(R.id.rvDefinitionsDialog) ;
        LexDefAl = new ArrayList<>();
        rvDefinitionDialog.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new DefinitionAdapter(LexDefAl , mContext) ;
        rvDefinitionDialog.setAdapter(adapter);
        databaseHelper = new DatabaseHelper(mContext) ;
        final SQLiteDatabase db = databaseHelper.getWritableDatabase() ;
        tvWord.setText(wordSearch);
        Typeface myfont = Typeface.createFromAsset(mContext.getAssets() ,"my_cursive_font.ttf" ) ;
        tvWord.setTypeface(myfont);
        ivClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                removeCallBack.onViewRemoved();
            }
        });
        final ProgressDialog pd  ;
        pd = ProgressDialog.show(
                mContext ,
                "Loading" ,"Please Wait ...",
                true ,
                false) ;

        SingeltonApi.build().getDictionaryApi().getDefination(
                wordSearch ,
                MainActivity.sourceLanguage ,
                MainActivity.Dictionary_App_Id,
                MainActivity.Dictionary_App_Key).enqueue(new Callback<Word>() {
            @Override
            public void onResponse(Call<Word> call, Response<Word> response) {
                if(pd != null)
                    pd.dismiss();

                Log.d(TAG, "onResponse: ");

                if(response.body() != null){

                    LexicalEntry[] lexes = response.body()
                            .getResults()[0]
                            .getLexicalEntries() ;
                    int count = 0 ;
                    String example = "No example" ;
                    for(LexicalEntry entry : lexes){
                        if(count>4){
                            break ;
                        }
                         example = "No example" ;
                        if(entry.getEntries()[0].getSenses()[0].getExamples() != null){
                           example = entry.getEntries()[0].getSenses()[0].getExamples()[0].getText();
                        }
                        LexAndDef lexAndDef = new LexAndDef(
                                entry.getLexicalCategory() ,
                                entry.getEntries()[0].getSenses()[0].getDefinitions()[0],
                                example
                        );
                        LexDefAl.add(lexAndDef) ;

                        count++;
                    }
                    HistoryTable.addHistory(db , wordSearch ,
                            response.body().getResults()[0].getLexicalEntries()[0].getLexicalCategory() ,
                            response.body().getResults()[0].getLexicalEntries()[0].getEntries()[0].getSenses()[0].getDefinitions()[0] ,
                           example ,
                            0
                            );
                    adapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(
                            mContext,
                            "Word Not Found",
                            Toast.LENGTH_SHORT
                    ).show();
                }

            }

            @Override
            public void onFailure(Call<Word> call, Throwable t) {
                Toast.makeText(mContext, "OnFailure : Window Manager", Toast.LENGTH_SHORT).show();
            }
        });
        return view ;
    }
}
