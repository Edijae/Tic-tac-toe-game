package com.samuel.tictac.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.samuel.tictac.databinding.ItemCellBinding
import com.samuel.tictac.models.Move
import com.samuel.tictac.utilis.Constants

class GridAdapter(val playerListener: PlayerListener, val height: Int, val moves: Array<Move?>) :
    RecyclerView.Adapter<GridAdapter.GridViewHolder>() {

    private var gameComplete = false
    private var xMoves = 0
    private var oMoves = 0;

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        return GridViewHolder(
            ItemCellBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        val move = moves[position]
        move?.let {
            var image = ""
            image = if (it.prayer == Constants.PLAYER_O) {
                Constants.O_MARK
            } else {
                Constants.X_MARK
            }
            Glide.with(holder.binding.imageView)
                .load(image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.binding.imageView)
        }
    }

    fun updateMoves() {
        moves.filterNotNull().forEach {
            if (it.prayer == Constants.PLAYER_X) {
                xMoves++
            } else {
                oMoves++
            }
        }
    }

    fun addPlayerMove(position: Int, player: String) {
        val move = Move(player, position / 3, position % 3)
        val totalMoves: Int = if (player == Constants.PLAYER_X) {
            xMoves++
            xMoves
        } else {
            oMoves++
            oMoves
        }
        moves[position] = move
        notifyItemChanged(position)

        if (totalMoves >= 3) {
            if (isAWin(moves.filter { it != null && it.prayer == player })) {
                gameComplete = true
                playerListener.win(player)
            } else if ((xMoves + oMoves) == moves.size) {
                gameComplete = true
                playerListener.draw()
            }
        }
    }

    private fun isAWin(indices: List<Move?>): Boolean {
        var isAWin = false
        val sub = HashMap<Int, Int>()
        val add = HashMap<Int, Int>()
        val rows = HashMap<Int, Int>()
        val columns = HashMap<Int, Int>()

        for (element in indices) {
            val subDiff = element!!.rowPos - element.colPos
            val addDiff = element.rowPos + element.colPos
            val subCount = (sub[subDiff] ?: 0) + 1
            val addCount = (add[addDiff] ?: 0) + 1
            val rowCount = (rows[element.rowPos] ?: 0) + 1
            val columnsCount = (columns[element.colPos] ?: 0) + 1

            if (rowCount == 3 || columnsCount == 3 || (rowCount != 3 && columnsCount != 3
                        && (subCount == 3 || addCount == 3))
            ) {
                isAWin = true
                break
            } else {
                sub[subDiff] = subCount
                add[addDiff] = addCount
                rows[element.rowPos] = rowCount
                columns[element.colPos] = columnsCount
            }
        }
        return isAWin
    }

    override fun getItemCount(): Int {
        return moves.size
    }

    fun gameCompleted(): Boolean {
        return gameComplete
    }


    inner class GridViewHolder(val binding: ItemCellBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.layoutParams.height = height
            binding.root.setOnClickListener {
                if (moves[adapterPosition] == null) {
                    val player = playerListener.getCurrentPlayer()
                    playerListener.switchPlayers()
                    addPlayerMove(adapterPosition, player)
                }
            }
        }
    }
}


interface PlayerListener {
    fun getCurrentPlayer(): String
    fun switchPlayers()
    fun win(name: String)
    fun draw()
}