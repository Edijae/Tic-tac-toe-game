package com.samuel.tictac.viewmodels

import androidx.lifecycle.ViewModel
import com.samuel.tictac.models.Move
import javax.inject.Inject

class MainViewModel @Inject constructor() : ViewModel() {


    var lastPlayer: String = ""
    var nextPlayer: String = ""
    var moves = Array<Move?>(9) { null }
    var restored = false

    fun resetData() {
        moves = Array<Move?>(9) { null }
        restored = false
    }
}