package com.horrors.newhorror

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.horrors.newhorror.GlideImageLoader.GlideImageLoader
import com.horrors.newhorror.R.id
import com.horrors.newhorror.R.layout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class SerialsAdapter6(private val presenter: IUserListPresenter2) :
    RecyclerView.Adapter<SerialsAdapter6.ViewHolder>() {

    private var itemClickListener: OnItemClickListener? = null
    private val imageLoader = GlideImageLoader()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val userView: View = inflater.inflate(layout.item_for_spisok_friends, parent, false)
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
        var name: TextView
        var count: Int = 0
        var iks: ImageView
        var xxx: ImageView


        init {
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("user")
                .child(Singleton.instance.mail.toString().replace(".","dot")).child("friends")

            name = itemView.findViewById(id.name) as TextView
            iks = itemView.findViewById<View>(id.iks) as ImageView
            xxx = itemView.findViewById(id.xxx)

            iks.setOnClickListener {
                if (itemClickListener != null) {
                    itemClickListener!!.onItemClick(it, adapterPosition)
                }
                myRef.addListenerForSingleValueEvent(object: ValueEventListener{
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.value != null) {
                            var list = snapshot.value as MutableList<String>
                            list.remove(name.text.toString())
                            myRef.setValue(list)
                        }
                    }
                })
                itemView.isGone = true
            }

        }


        override fun setLogin(text: String?) {
            name.text = text
        }

        override fun loadAvatar(url: String?) {
            imageLoader.loadImage(
                url,
                xxx
            )
        }

        override fun setOpis(url: String?) {
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

