package com.tanlocdev.flashlightapplication

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TutorialAdapter(
    private val mDataset: List<Tutorial>
) : RecyclerView.Adapter<TutorialAdapter.TutorialHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TutorialHolder {
        return TutorialHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_tutorial,
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: TutorialHolder, position: Int) {
        holder.bind(mDataset[position])
        Log.d("check",mDataset[position].kiTu.toString())
    }

    override fun getItemCount(): Int {
        return mDataset.size
    }

    inner class TutorialHolder(view: View) : RecyclerView.ViewHolder(view) {
        public val txtText=   view.findViewById<TextView>(R.id.txtText)
        public val txtMorseCode= view.findViewById<TextView>(R.id.txtMorseCode)
        fun bind(dataSlide: Tutorial) {
            txtText.text = dataSlide.kiTu.toString()
            txtMorseCode.text = dataSlide.morseCode
            Log.d("check",dataSlide.morseCode)
        }

    }
}