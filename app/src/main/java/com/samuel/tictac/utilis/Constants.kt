package com.samuel.tictac.utilis

import android.Manifest

class Constants {
    companion object {
        const val PLAYER_X = "X"
        const val PLAYER_O = "O"
        const val X_MARK = "https://d.michd.me/aa-lab/x_mark.png"
        const val O_MARK = "https://d.michd.me/aa-lab/o_mark.png"

        //
        val STORAGE_PERMISSIONS = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }
}