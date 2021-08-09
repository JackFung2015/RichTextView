package com.fung.richtextapp.utils

import android.content.Context
import android.content.res.AssetManager
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class FileUtils {
    companion object {
        fun getJson(context: Context, fileName: String?): String? {
            val stringBuilder = StringBuilder()
            try {
                val assetManager: AssetManager = context.assets
                val bf = BufferedReader(InputStreamReader(assetManager.open(fileName!!)))
                var line: String?
                while (bf.readLine().also { line = it } != null) {
                    stringBuilder.append(line)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return stringBuilder.toString()
        }
    }

}