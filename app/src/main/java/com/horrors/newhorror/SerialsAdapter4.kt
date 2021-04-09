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
import com.google.firebase.database.FirebaseDatabase


class SerialsAdapter4(private val presenter: IUserListPresenter2) :
    RecyclerView.Adapter<SerialsAdapter4.ViewHolder>() {

    private var itemClickListener4: OnItemClickListener? = null
    private var itemClickListener: OnItemClickListener? = null
    private var itemClickListener2: OnItemClickListener2? = null
    private var itemClickListener3: OnItemListener? = null
    private val imageLoader = GlideImageLoader()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val userView: View = inflater.inflate(layout.item_for_spisok, parent, false)
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

    interface OnItemClickListener2 {
        fun onItemClick(view: View?, position: Int)
    }

    interface OnItemListener {
        fun onItem(view: View?, position: Int)
    }

    fun SetOnItemListener(itemClickListener: OnItemListener) {
        this.itemClickListener3 = itemClickListener3

    }

    fun SetOnItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    fun SetOnItemClickListener2(itemClickListener2: OnItemClickListener2) {
        this.itemClickListener2 = itemClickListener2
    }




    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        SerialItemView2 {
        var textView: TextView
        var xxx: ImageView
        var count: Int = 0
        var iks: ImageView? = null
        var rec: ImageView? = null
        var bool: Boolean = false
        val database = FirebaseDatabase.getInstance()

        init {
            textView = itemView.findViewById<View>(id.textView) as TextView
            xxx = itemView.findViewById<View>(id.xxx) as ImageView
            rec = itemView.findViewById<View>(id.rec) as ImageView
            iks = itemView.findViewById(id.iks) as ImageView



            iks!!.setOnClickListener {
                if (itemClickListener != null) {
                    itemClickListener!!.onItemClick(it, adapterPosition)
                }
            }

            xxx.setOnClickListener {
                changeToClass()
            }

            textView.setOnClickListener { changeToGrey() }


            rec!!.setOnClickListener {
                if (itemClickListener2 != null) {
                    itemClickListener2!!.onItemClick(it, adapterPosition)
                }
                var myRef = database.getReference("User")
                    .child(Singleton.instance.mail.toString().replace(".","dot"))
                    .child("films")

            }

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

        fun changeClass() {
            rec!!.setImageResource(R.drawable.classs)
        }

        override fun setOpis(url: String?) {
        }

        override fun addFilm(url: String?) {

        }

        override fun setBtn(int: Int?) {

        }

        override fun setFavor(boolean: Boolean) {
            if (boolean) {
                rec!!.setImageResource(R.drawable.classs)
            }
        }

        override fun changeToGrey() {
            rec!!.setImageResource(R.drawable.class_grey)
        }

        override fun changeToClass() {
            rec!!.setImageResource(R.drawable.classs)
        }


        override fun getPos(): Int {
            return count
        }


    }

}

