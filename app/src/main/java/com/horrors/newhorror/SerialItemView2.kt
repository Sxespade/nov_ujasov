package com.horrors.newhorror

import com.horrors.myapplication.di.module.IItemView

interface SerialItemView2: IItemView {
    fun setLogin(text: String?)
    fun loadAvatar(url: String?)
    fun setOpis(url: String?)
    fun addFilm(url: String?)
    fun setBtn(int: Int?)
    fun setFavor(boolean: Boolean)
    fun changeToGrey()
    fun changeToClass()
}