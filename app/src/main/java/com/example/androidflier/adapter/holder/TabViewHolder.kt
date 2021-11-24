package com.example.androidflier.adapter.holder

import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidflier.R
import com.example.androidflier.adapter.TabCardAdapter
import com.example.androidflier.model.Tab

class TabViewHolder(itemView: View, val adapter: TabCardAdapter) : RecyclerView.ViewHolder(itemView),
    View.OnClickListener {
    private var textView: TextView = itemView.findViewById(R.id.tab_layout_text)
    private lateinit var tab: Tab

    fun bind(tab: Tab) {
        this.tab = tab
        Log.d("TabViewHolder", "$tab")
        itemView.setOnClickListener(this)
        textView.text = tab.title

        val findViewById = itemView.findViewById<CardView>(R.id.tab_cardView)

        if (tab.selected) {
            findViewById.setCardBackgroundColor(Color.BLUE)
        } else {
            findViewById.setCardBackgroundColor(Color.GREEN)
        }
    }

    override fun onClick(v: View?) {
        tab.selected = !tab.selected
        adapter.selectTab(tab)
    }

    interface TabSelectable {
        fun withTab(tab: Tab)
    }
}

