package com.example.guru_oneulmaru

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.data_list_item.view.*
import java.util.*

class WeatherAdapter(val context: Context, val itemCheck:(Weather)->Unit)//weatheradapter
    :RecyclerView.Adapter<WeatherAdapter.ViewHolder>(){
    private var items = ArrayList<Weather>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val itemView: View = inflater.inflate(R.layout.data_list_item, viewGroup, false)
        return ViewHolder(itemView, itemCheck)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item: Weather = items[position]
        viewHolder.setItem(item)
    }

    override fun getItemCount(): Int {
        return items.count()//아이템 수
    }

    fun setItems(items: ArrayList<Weather>) {
        this.items = items//아이템 선언
    }

    fun addItem(item: Weather) {
        items.add(item)
    }

    inner class ViewHolder(itemView: View, itemCheck: (Weather) -> Unit)
        : RecyclerView.ViewHolder(itemView) {
        fun setItem(item: Weather) {
            itemView.txtTemp.text = item.temp//온도
            itemView.txtTime.text = item.time//날씨
            itemView.imgWeather.setImageResource(item.photo)
            itemView.setOnClickListener { itemCheck(item) }
        }
    }
}