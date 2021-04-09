package com.horrors.newhorror

import com.horrors.myapplication.di.module.IItemView

interface SerialItemView: IItemView {
    fun setLogin(text: String?)
    fun loadAvatar(url: String?)
    fun setOpis(url: String?)
    fun setTextStar(url: String?)
}