package com.sunayanpradhan.unixpaper.Activities

import android.app.AlertDialog
import android.app.DownloadManager
import android.app.WallpaperManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.omega_r.libs.omegaintentbuilder.OmegaIntentBuilder
import com.omega_r.libs.omegaintentbuilder.downloader.DownloadCallback
import com.omega_r.libs.omegaintentbuilder.handlers.ContextIntentHandler
import com.sunayanpradhan.unixpaper.R
import com.sunayanpradhan.unixpaper.databinding.ActivityDownloadBinding


class DownloadActivity : AppCompatActivity(), OnUserEarnedRewardListener {

    lateinit var binding: ActivityDownloadBinding

    private lateinit var decorView: View

    lateinit var url:String

    var mRewardedAd: RewardedAd? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)

        binding=DataBindingUtil.setContentView(this,R.layout.activity_download)

        supportActionBar?.hide()

        decorView = window.decorView



        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }



        val intent = intent
        url = intent.getStringExtra("photoUrl").toString()


        val dialogView = View.inflate(this, R.layout.loading_dialog, null)

        val builder = AlertDialog.Builder(this).setView(dialogView).create()

        builder.window?.setBackgroundDrawableResource(android.R.color.transparent)

        builder.setCancelable(false)



        binding.apply {

            Glide.with(applicationContext).load(url)
                .into(setImage)


            downloadButton.setOnClickListener {




                try {
                    val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                    val uri = Uri.parse(url)
                    val request = DownloadManager.Request(uri)
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    downloadManager.enqueue(request)

                    Snackbar.make(it,"Download Successful",Snackbar.LENGTH_LONG).show()


                } catch (e: java.lang.Exception) {




                }



            }



            setButton.setOnClickListener {

                binding.downloadButton.isEnabled=false

                binding.setButton.isEnabled=false

                binding.shareButton.isEnabled=false


                Snackbar.make(it,"Loading...",Snackbar.LENGTH_LONG).show()



                loadAds()


            }


            shareButton.setOnClickListener {


                OmegaIntentBuilder(this@DownloadActivity)
                    .share()
                    .filesUrls(url)
                    .download(object : DownloadCallback {
                        override fun onDownloaded(
                            success: Boolean,
                            contextIntentHandler: ContextIntentHandler
                        ) {
                            contextIntentHandler.startActivity()
                        }
                    })



            }




        }







    }

    private fun loadAds() {



        val adRequest = AdRequest.Builder().build()

        RewardedAd.load(
            this,
            "ca-app-pub-9490269980026122/7846512181",
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {



                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {


                    rewardedAd.show(this@DownloadActivity,this@DownloadActivity)




                }
            })



    }










    override fun onBackPressed() {
        super.onBackPressed()

        finish()


    }

    override fun onUserEarnedReward(p0: RewardItem) {


        val dialogView = View.inflate(this, R.layout.loading_dialog, null)

        val builder = AlertDialog.Builder(this).setView(dialogView).create()

        builder.window?.setBackgroundDrawableResource(android.R.color.transparent)

        builder.setCancelable(false)

        builder.show()

        Glide.with(applicationContext).asBitmap().load(url).listener(object :
            RequestListener<Bitmap> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Bitmap>?,
                isFirstResource: Boolean
            ): Boolean {

                Snackbar.make(this@DownloadActivity.findViewById(android.R.id.content),"Loading Failed",Snackbar.LENGTH_LONG).show()

                builder.dismiss()


                return false
            }

            override fun onResourceReady(
                resource: Bitmap?,
                model: Any?,
                target: Target<Bitmap>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {

                try {

                    WallpaperManager.getInstance(applicationContext).setBitmap(resource).apply {


                        builder.dismiss()


                        Snackbar.make(this@DownloadActivity.findViewById(android.R.id.content),"Wallpaper Set Successfully",Snackbar.LENGTH_LONG)
                            .addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>(){

                                override fun onDismissed(
                                    transientBottomBar: Snackbar?,
                                    event: Int
                                ) {
                                    super.onDismissed(transientBottomBar, event)


                                    finish()



                                }


                            })
                            .show()


                    }




                }
                catch (e:Exception){

//                            Toast.makeText(this@DownloadActivity, "Failed to Set Wallpaper", Toast.LENGTH_SHORT).show()

                    Snackbar.make(this@DownloadActivity.findViewById(android.R.id.content),"Loading Failed",Snackbar.LENGTH_LONG).show()


                    builder.dismiss()


                }

                return false
            }

        }).submit()




    }


}