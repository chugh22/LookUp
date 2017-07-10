package com.example.anirudh.lookup.models

import org.intellij.lang.annotations.Language

/**
 * Created by anirudh on 07/07/17.
 */
data class Example(var text :String , var registers : Array<String> , var translations :Array<Translation> ){
    data class Translation(var language: String , var text : String)
}