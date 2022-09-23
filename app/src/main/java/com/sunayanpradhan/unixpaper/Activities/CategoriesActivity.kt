package com.sunayanpradhan.unixpaper.Activities

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sunayanpradhan.unixpaper.Adapters.WallpaperAdapter
import com.sunayanpradhan.unixpaper.R
import com.sunayanpradhan.unixpaper.databinding.ActivityCategoriesBinding

class CategoriesActivity : AppCompatActivity() {

    lateinit var binding: ActivityCategoriesBinding

    lateinit var firebaseDatabase: FirebaseDatabase

    lateinit var wallpaperList:ArrayList<String>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)

        binding=DataBindingUtil.setContentView(this,R.layout.activity_categories)


        firebaseDatabase=FirebaseDatabase.getInstance()

        wallpaperList= ArrayList()

        val intent=intent

        val categoryId=intent.getStringExtra("categoryId").toString()



        binding.apply {

            when(categoryId){

                "nature"->{

                    categoryText.text="Nature"

                }

                "wildlife"->{

                    categoryText.text="Wildlife"

                }

                "neonart"->{

                    categoryText.text="Neon & Art"

                }

                "vehicles"->{

                    categoryText.text="Vehicles"

                }

                "trending"->{

                    categoryText.text="Trending"

                }

                "creative"->{

                    categoryText.text="Creative"

                }
                "editorschoice"->{

                    categoryText.text="Editors' Choice"

                }
                "lifestyle"->{

                    categoryText.text="Lifestyle"

                }
                "topcharts"->{

                    categoryText.text="Top Charts"

                }
                "quietcalm"->{

                    categoryText.text="Quiet & Calm"

                }






            }


        }



        val layoutManager= GridLayoutManager(this,2)

        val adapter= WallpaperAdapter(wallpaperList,this)

        binding.categoryRv.layoutManager=layoutManager

        binding.categoryRv.showShimmerAdapter()


        firebaseDatabase.reference.child("unixpaper_images").child(categoryId)
            .addListenerForSingleValueEvent(object :ValueEventListener{
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {

                    wallpaperList.clear()

                    if (snapshot.exists()){

                        for (dataSnapshot in snapshot.children){

                            val data=dataSnapshot.value.toString()


                            wallpaperList.add(data)

                        }

                        adapter.notifyDataSetChanged()


                        binding.categoryRv.adapter=adapter

                        binding.categoryRv.hideShimmerAdapter()


                    }



                }

                override fun onCancelled(error: DatabaseError) {

                }


            })







    }
}