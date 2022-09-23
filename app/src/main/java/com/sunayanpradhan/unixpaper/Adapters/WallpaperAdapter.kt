package com.sunayanpradhan.unixpaper.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sunayanpradhan.unixpaper.Activities.DownloadActivity
import com.sunayanpradhan.unixpaper.R

class WallpaperAdapter(var list:ArrayList<String>,var context: Context):RecyclerView.Adapter<WallpaperAdapter.ViewHolder>() {


    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

         val designImage:ImageView= itemView.findViewById(R.id.design_image)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.design_layout,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem= list[position]


        Glide.with(context.applicationContext)
            .load(currentItem)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .into(holder.designImage)


        holder.itemView.setOnClickListener {

            val intent= Intent(context, DownloadActivity::class.java)

            intent.putExtra("photoUrl",currentItem)

            context.startActivity(intent)

        }



    }

    override fun getItemCount(): Int {

        return list.size
    }
}