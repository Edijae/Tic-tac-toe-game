package com.samuel.tictac.utilis

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.samuel.tictac.models.Move

class StorageUtils {
    companion object {
        private val PREF_NAME = "tictac"
        private val PLAYER = "player"
        private val MOVES = "moves"
        private val gson = Gson()
        private val type = object : TypeToken<Array<Move?>>() {}.type

        fun storeData(context: Context, player: String, moves: Array<Move?>) {
            val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            pref.edit()
                .putString(PLAYER, player)
                .putString(MOVES, gson.toJson(moves, type))
                .apply()
        }

        fun getLastPlayer(context: Context): String {
            return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getString(PLAYER, "")!!
        }

        fun getMoves(context: Context): Array<Move?> {
            val json = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getString(MOVES, "")!!
            Log.e("TAG", "getMoves: json $json")
            return if (json.isEmpty()) {
                Array(9) { null }
            } else {
                gson.fromJson(json, type)
            }
        }

        fun clearGameData(context: Context) {
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit().clear().apply()
        }
    }
}