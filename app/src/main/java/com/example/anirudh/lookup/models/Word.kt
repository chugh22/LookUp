package com.example.anirudh.lookup.models

/**
 * Created by anirudh on 06/07/17.
 */
data class Word(var results : Array <Results>){
    data class Results(var id : String , var language : String, var lexicalEntries : Array<LexicalEntry>)
}