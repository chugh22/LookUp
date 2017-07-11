package com.example.anirudh.lookup;

import com.example.anirudh.lookup.models.LexAndDef;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by anirudh on 10/07/17.
 */

public class StaticData {
    public static ArrayList<LexAndDef> wordOfDay = new ArrayList<>() ;

    private void setData(){
       wordOfDay.add(new LexAndDef("noun" , "instantiate" , "to provide an instance of or concrete evidence in support of (a theory, concept, claim, or the like).")) ;

    }
    public String[] getTodaysWord(){
        String[] strings = new String[2] ;

        return strings ;
    }
}
