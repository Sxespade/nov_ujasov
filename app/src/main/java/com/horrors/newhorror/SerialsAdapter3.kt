package com.horrors.newhorror

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.horrors.newhorror.GlideImageLoader.GlideImageLoader
import com.horrors.newhorror.R.id
import com.horrors.newhorror.R.layout
import ru.terrakok.cicerone.Router
import javax.inject.Inject


class SerialsAdapter3(private val presenter: IUserListPresenter2) :
    RecyclerView.Adapter<SerialsAdapter3.ViewHolder>() {

    @Inject lateinit var router: Router

    private var itemClickListener: OnItemClickListener? = null
    private val imageLoader = GlideImageLoader()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val userView: View = inflater.inflate(layout.item_for_serach_film, parent, false)
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
        var opisanie: TextView
        var xxx: ImageView
        var count: Int = 0
        var button: Button


        init {


            textView = itemView.findViewById<View>(id.textView) as TextView
            xxx = itemView.findViewById<View>(id.xxx) as ImageView
            button = itemView.findViewById<View>(id.btnAdd) as Button
            opisanie = itemView.findViewById<View>(id.opisanie) as TextView

            button.setOnClickListener {
                if (itemClickListener != null) {
                    itemClickListener!!.onItemClick(it,adapterPosition)
                }
            };
        }


        override fun setLogin(text: String?) {
            textView.text = text
        }

        override fun loadAvatar(url: String?) {
            imageLoader.loadImage(
                url,
                xxx
            )
        }

        override fun setOpis(url: String?) {
            opisanie.text = url
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

