package com.horrors.newhorror.GlideImageLoader

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.horrors.newhorror.mvp.view.image.IImageLoader

class GlideImageLoader : IImageLoader<ImageView?> {

    override fun loadImage(url: String?, container: ImageView?) {
        Glide.with(container!!.context).load(url).into(container)
    }
}
