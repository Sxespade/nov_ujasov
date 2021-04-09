package com.horrors.newhorror.mvp.view.image

interface IImageLoader<T> {
    fun loadImage(url: String?, container: T?)
}