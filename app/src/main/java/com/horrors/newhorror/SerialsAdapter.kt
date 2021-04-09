package com.horrors.newhorror

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.horrors.newhorror.GlideImageLoader.GlideImageLoader
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SerialsAdapter(private val presenter: IUserListPresenter) :
    RecyclerView.Adapter<SerialsAdapter.ViewHolder>() {

    private val imageLoader = GlideImageLoader()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val userView: View = inflater.inflate(R.layout.item_user, parent, false)
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
        var textStar: TextView


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
            val database = FirebaseDatabase.getInstance()
            var ref = database.getReference("film_titles")
                .child(textView.text.toString())
            ref.addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value != null) {
                        var list = snapshot.value as MutableList<String>
                        val result = java.lang.String.format("%.2f", list[4].toDouble())
                        textStar.text = result
                    }
                }
            })
        }

        override fun setTextStar(url: String?) {
            textStar.text = url
        }

        override fun getPos(): Int {
            return count
        }


        init {
            textView = itemView.findViewById<View>(R.id.textView) as TextView
            xxx = itemView.findViewById<View>(R.id.xxx) as ImageView
            textStar = itemView.findViewById(R.id.textStar) as TextView





        }
    }

}