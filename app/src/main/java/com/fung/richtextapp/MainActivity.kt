package com.fung.richtextapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fung.entity.SpecialStyle
import com.fung.richtextapp.utils.FileUtils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val gson = Gson()
        val entity = gson.fromJson(FileUtils.getJson(this,"style.json"),SpecialStyle::class.java)
        richTv.specialSymbolList = entity.stemSpecialSymbol
        val entity2 = gson.fromJson(FileUtils.getJson(this,"mulLineStyle.json"),SpecialStyle::class.java)
        richTv2.specialSymbolList = entity2.stemSpecialSymbol
    }
}