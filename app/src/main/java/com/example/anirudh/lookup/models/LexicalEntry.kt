package com.example.anirudh.lookup.models



/**
 * Created by anirudh on 06/07/17.
 */
data class LexicalEntry(var language:String , var lexicalCategory : String ,var entries : Array<DefinitionEntry>
                        ,var pronunciations:Array<Phonetics> , var senses : Array<Sense>)