package com.sunayanpradhan.unixpaper.Activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sunayanpradhan.unixpaper.Adapters.SliderAdapter
import com.sunayanpradhan.unixpaper.R
import com.sunayanpradhan.unixpaper.Adapters.WallpaperAdapter
import com.sunayanpradhan.unixpaper.Models.SliderModel
import com.sunayanpradhan.unixpaper.databinding.ActivityMainBinding
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    lateinit var firebaseDatabase: FirebaseDatabase

    lateinit var wallpaperList: ArrayList<String>

    lateinit var sliderHandler: Handler

    lateinit var isList:ArrayList<SliderModel>

    lateinit var sliderAdapter: SliderAdapter
    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding= DataBindingUtil.setContentView(this, R.layout.activity_main)


        firebaseDatabase= FirebaseDatabase.getInstance()

        wallpaperList= ArrayList()

        isList= ArrayList()

        sliderHandler=Handler()

        sliderAdapter= SliderAdapter(isList,this)

        binding.apply {


//            firebaseDatabase.reference.child("unixpaper_images").child("slider")
//                .addListenerForSingleValueEvent(object : ValueEventListener{
//                    @SuppressLint("NotifyDataSetChanged")
//                    override fun onDataChange(snapshot: DataSnapshot) {
//
//                        isList.clear()
//
//                        if (snapshot.exists()){
//
//                            for (dataSnapshot in snapshot.children){
//
//
//                                val data=dataSnapshot.getValue(SliderModel::class.java)
//
//                                data?.sliderId=dataSnapshot.key.toString()
//
//                                isList.add(data!!)
//
//                            }
//
//
//                            sliderAdapter.notifyDataSetChanged()
//
//                            isViewImage.adapter=sliderAdapter
//
//
//                        }
//
//
//
//
//                    }
//
//                    override fun onCancelled(error: DatabaseError) {
//
//
//
//                    }
//
//
//                })



            isList.add(SliderModel("creative",R.drawable.creative))

            isList.add(SliderModel("editorschoice",R.drawable.editors_choice))

            isList.add(SliderModel("lifestyle",R.drawable.lifestyle))

            isList.add(SliderModel("quietcalm",R.drawable.quiet_calm))

            isList.add(SliderModel("topcharts",R.drawable.top_charts))

            isList.add(SliderModel("trending",R.drawable.trending))

            sliderAdapter.notifyDataSetChanged()

            isViewImage.adapter=sliderAdapter



            isViewImage.clipChildren=false

            isViewImage.clipToPadding=false

            isViewImage.offscreenPageLimit=3

            isViewImage.getChildAt(0).overScrollMode= RecyclerView.OVER_SCROLL_NEVER

            val compositePageTransformer= CompositePageTransformer()

            compositePageTransformer.addTransformer(MarginPageTransformer(40))

            compositePageTransformer.addTransformer(object : ViewPager2.PageTransformer{
                override fun transformPage(page: View, position: Float) {

                    val r= 1- abs(position)

                    page.scaleY=0.85f+r*0.15f


                }

            })

            isViewImage.setPageTransformer(compositePageTransformer)


            isViewImage.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)


                    sliderHandler.removeCallbacks(sliderRunnable)

                    sliderHandler.postDelayed(sliderRunnable,5000)

                    if (position==isList.size-2){

                        isViewImage.post(runnable)

                    }




                }


            })





        }




        binding.nature.setOnClickListener {

            val intent= Intent(this, CategoriesActivity::class.java)

            intent.putExtra("categoryId","nature")

            startActivity(intent)


        }

        binding.wildlife.setOnClickListener {

            val intent= Intent(this, CategoriesActivity::class.java)

            intent.putExtra("categoryId","wildlife")

            startActivity(intent)


        }

        binding.neonArt.setOnClickListener {

            val intent= Intent(this, CategoriesActivity::class.java)

            intent.putExtra("categoryId","neonart")

            startActivity(intent)


        }

        binding.vehicles.setOnClickListener {

            val intent= Intent(this, CategoriesActivity::class.java)

            intent.putExtra("categoryId","vehicles")

            startActivity(intent)


        }


        val layoutManager= GridLayoutManager(this,2)

        val adapter= WallpaperAdapter(wallpaperList,this)

        binding.wallpaperRv.layoutManager=layoutManager


        binding.wallpaperRv.showShimmerAdapter()

        firebaseDatabase.reference.child("unixpaper_images").child("home")
            .addListenerForSingleValueEvent(object : ValueEventListener{
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {

                    wallpaperList.clear()

                    if (snapshot.exists()){

                        for (dataSnapshot in snapshot.children){

                            val data=dataSnapshot.value.toString()

                            wallpaperList.add(data)


                        }

                        adapter.notifyDataSetChanged()

                        binding.wallpaperRv.adapter=adapter

                        binding.wallpaperRv.hideShimmerAdapter()

                    }




                }

                override fun onCancelled(error: DatabaseError) {



                }


            })





    }



    val sliderRunnable= Runnable { binding.isViewImage.currentItem = binding.isViewImage.currentItem+1 }

    val runnable= Runnable {

        isList.addAll(isList)

        sliderAdapter.notifyDataSetChanged()
    }

    override fun onPause() {
        super.onPause()

        sliderHandler.removeCallbacks(sliderRunnable)


    }

    override fun onResume() {
        super.onResume()

        sliderHandler.postDelayed(sliderRunnable,5000)


    }

    override fun onBackPressed() {

        if (backPressedTime + 2000 > System.currentTimeMillis()) {

            finishAffinity()
        }

        else {


            Snackbar.make(this.findViewById(android.R.id.content),"Double Press To Exit",Snackbar.LENGTH_LONG).show()



        }
        backPressedTime = System.currentTimeMillis()


    }


}