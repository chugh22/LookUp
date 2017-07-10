package com.example.anirudh.lookup.DataBase;

/**
 * Created by anirudh on 09/07/17.
 */
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.anirudh.lookup.models.HistoryModel;
import com.example.anirudh.lookup.models.LexAndDef;

import java.util.ArrayList;

import static com.example.anirudh.lookup.DataBase.DataBaseUtils.* ;
public class HistoryTable {
public static final String TAG = "History Table : " ;
    public static final String TABLE_NAME = "history" ;

    interface Columns{
        String COL_ID = "id" ;
        String COL_LEX = "lex" ;
        String COL_DEFINITION = "def" ;
        String COL_EXAMPLES = "examples" ;
        String COL_WORD = "word" ;
        String COL_IS_STAR = "star" ;
    }
    public static final String CMD_CREATE_TABLE =
            CREATE_TABLE +
                    TABLE_NAME + LBR +
                    Columns.COL_ID + TYPE_INT_PK_AI +COMMA+
                    Columns.COL_WORD + TYPE_TEXT + COMMA +
                    Columns.COL_LEX + TYPE_TEXT + COMMA +
                    Columns.COL_EXAMPLES + TYPE_TEXT + COMMA +
                    Columns.COL_DEFINITION + TYPE_TEXT + COMMA +
                    Columns.COL_IS_STAR + TYPE_INTEGER  +
                    RBR +SEMI ;

    public static ArrayList<HistoryModel> getHistory(SQLiteDatabase db){
        ArrayList<HistoryModel> lists  = new ArrayList<>();
        Cursor c = db.query(
                TABLE_NAME ,
                new String[]{
                        Columns.COL_WORD ,
                        Columns.COL_LEX ,
                        Columns.COL_DEFINITION ,
                        Columns.COL_EXAMPLES ,
                        Columns.COL_IS_STAR
                } ,
                null ,
                null ,
                null,
                null ,
                Columns.COL_ID + " DESC"

        )  ;
        int[] indexes = new int[5] ;
        indexes[0] = c.getColumnIndex(Columns.COL_WORD) ;
        indexes[1] = c.getColumnIndex(Columns.COL_LEX) ;
        indexes[2] = c.getColumnIndex(Columns.COL_DEFINITION) ;
        indexes[3] = c.getColumnIndex(Columns.COL_EXAMPLES) ;
        indexes[4] = c.getColumnIndex(Columns.COL_IS_STAR) ;
        while(c.moveToNext()){
            lists.add(
                    new HistoryModel(
                            c.getString(indexes[0]) ,
                            c.getString(indexes[1]) ,
                            c.getString(indexes[2]) ,
                            c.getString(indexes[3]) ,
                            c.getInt(indexes[4])
                    )) ;
        }
        Log.d(TAG, "getHistory: " + lists.size());
        return lists ;
    }

    public static void addHistory(SQLiteDatabase db ,
                           @NonNull String word ,
                           @NonNull String lex ,
                           @NonNull String definition ,
                           String examples , int isStar){


        ContentValues contentValues = new ContentValues() ;
        contentValues.put(Columns.COL_WORD , word);
        contentValues.put(Columns.COL_LEX , lex);
        contentValues.put(Columns.COL_DEFINITION , definition);
        contentValues.put(Columns.COL_EXAMPLES , examples);
        contentValues.put(Columns.COL_IS_STAR , isStar);
        db.insert(
                TABLE_NAME ,
                null ,
                contentValues
        ) ;

    }
    public static void updateStar(SQLiteDatabase db , String word , boolean isStar){
//        db.execSQL("UPDATE "+TABLE_NAME +
//        " SET " + Columns.COL_IS_STAR + " = "+(isStar?1:0) + " WHERE " + Columns.COL_WORD + "=" + word );
        ContentValues contentValues = new ContentValues() ;
        contentValues.put(Columns.COL_IS_STAR , isStar?1:0);
        db.update(TABLE_NAME , contentValues , Columns.COL_WORD + "= ?" , new String[]{word}) ;

    }

}
