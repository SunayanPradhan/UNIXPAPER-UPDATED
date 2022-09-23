package com.sunayanpradhan.unixpaper.Models

data class SliderModel(
    var sliderId:String,
    var sliderImage:Int,
)
{
    constructor():this(
        "",
        0
    )

}
