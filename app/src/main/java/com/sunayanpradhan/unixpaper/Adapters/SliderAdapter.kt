package com.sunayanpradhan.unixpaper.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sunayanpradhan.unixpaper.Activities.CategoriesActivity
import com.sunayanpradhan.unixpaper.Models.SliderModel
import com.sunayanpradhan.unixpaper.R

class SliderAdapter(var list:ArrayList<SliderModel>, var context: Context):RecyclerView.Adapter<SliderAdapter.ViewHolder>() {

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

        val sliderImage:ImageView= itemView.findViewById(R.id.slider_image)




    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(context).inflate(R.layout.slider_layout,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem=list[position]



//        Glide.with(context.applicationContext)
//            .load(currentItem.sliderImage)
//            .placeholder(R.drawable.placeholder)
//            .error(R.drawable.placeholder)
//            .into(holder.sliderImage)


        holder.sliderImage.setImageResource(currentItem.sliderImage)


        holder.itemView.setOnClickListener {

            val intent= Intent(context, CategoriesActivity::class.java)

            intent.putExtra("categoryId",currentItem.sliderId)

            context.startActivity(intent)
        }



    }

    override fun getItemCount(): Int {
        return list.size
    }



}