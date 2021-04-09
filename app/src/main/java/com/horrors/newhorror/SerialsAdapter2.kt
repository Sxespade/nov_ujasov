package com.horrors.newhorror

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.horrors.newhorror.GlideImageLoader.GlideImageLoader

class SerialsAdapter2(private val presenter: IUserListPresenter) :
    RecyclerView.Adapter<SerialsAdapter2.ViewHolder>() {

    private val imageLoader = GlideImageLoader()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val userView: View = inflater.inflate(R.layout.item_for_serach_film, parent, false)
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

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        SerialItemView {
        var textView: TextView
        var xxx: ImageView
        var count: Int = 0
        var opisanie: TextView
        var ocen: TextView


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
            ocen.text = url
        }

        override fun setTextStar(url: String?) {

        }

        override fun getPos(): Int {
            return count
        }



        init {
            textView = itemView.findViewById<View>(R.id.textView) as TextView
            xxx = itemView.findViewById<View>(R.id.xxx) as ImageView
            opisanie = itemView.findViewById<View>(R.id.opisanie) as TextView
            ocen = itemView.findViewById<View>(R.id.ocen) as TextView
        }
    }

}