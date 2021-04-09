package com.horrors.myapplication.di.module


interface IListPresenter<V : IItemView?> {
    fun onItemClick(view: V)
    fun bindView(view: V)
    fun getCount(): Int
    fun op()
}