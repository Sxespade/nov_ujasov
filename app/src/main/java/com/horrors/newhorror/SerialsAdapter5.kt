package com.horrors.newhorror

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.horrors.newhorror.GlideImageLoader.GlideImageLoader
import com.horrors.newhorror.R.id
import com.horrors.newhorror.R.layout


class SerialsAdapter5(private val presenter: IUserListPresenter2) :
    RecyclerView.Adapter<SerialsAdapter5.ViewHolder>() {

    private var itemClickListener: OnItemClickListener? = null
    private val imageLoader = GlideImageLoader()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val userView: View = inflater.inflate(layout.item_user_for_top_spisok, parent, false)
        return ViewHolder(userView)
    }

    override fun getItemCount(): Int {
        return presenter.getCount()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.count = position
        holder.itemView.setOnClickListener { presenter.onItemClick(holder) }
        presenter.bindView(holder)
    }


    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }

    fun SetOnItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        SerialItemView2 {
        var textView: TextView
        var ocen: TextView
        var count: Int = 0
        var imgRec : ImageView? = null


        init {
            textView = itemView.findViewById<View>(id.textView) as TextView
            ocen = itemView.findViewById<View>(R.id.ocen) as TextView

//            imgRec = itemView.findViewById(id.rec) as ImageView
//
//            imgRec!!.setOnClickListener {
//                if (itemClickListener != null) {
//                    itemClickListener!!.onItemClick(it,adapterPosition)
//                }
//            }
        }


        override fun setLogin(text: String?) {
            textView.text = text
        }

        override fun loadAvatar(url: String?) {
        }

        override fun setOpis(url: String?) {
            ocen.text = url
        }

        override fun addFilm(url: String?) {

        }

        override fun setBtn(int: Int?) {

        }

        override fun setFavor(boolean: Boolean) {

        }

        override fun changeToGrey() {

        }

        override fun changeToClass() {
            TODO("Not yet implemented")
        }


        override fun getPos(): Int {
            return count
        }


    }

}

