package com.example.androidflier.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidflier.R
import com.example.androidflier.adapter.holder.TabViewHolder
import com.example.androidflier.model.Tab

class TabCardAdapter(var listTab: List<Tab> = mutableListOf()) :
    RecyclerView.Adapter<TabViewHolder>(), TabViewHolder.Selectable {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): TabViewHolder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.tab_layout, parent, false)
        return TabViewHolder(inflate, this)
    }

    override fun onBindViewHolder(p0: TabViewHolder, p1: Int) {
        p0.bind(listTab[p1])
    }

    override fun getItemCount(): Int {
        return listTab.size
    }

    override fun changeSelectedItem(tab: Tab) {
        listTab.forEach {
            if (tab.id == it.id) {
                it.selected = tab.selected
            } else {
                it.selected = false
            }
        }

        notifyDataSetChanged()
    }
}