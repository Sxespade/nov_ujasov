package com.horrors.newhorror.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.horrors.newhorror.IUserListPresenter
import com.horrors.newhorror.R
import com.horrors.newhorror.SerialItemView
import com.horrors.newhorror.SerialsAdapter
import com.horrors.newhorror.di.module.Serial
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_friend_display.*
import java.net.URLEncoder

class FriendDisplay : AppCompatActivity() {



    lateinit var avat: ImageView
    val database = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_display)

        avat = findViewById<ImageView>(R.id.avat)
    }

    override fun onStart() {
        super.onStart()

        var k = intent.extras!!["1"] as String
        var s = URLEncoder.encode(k, "utf-8")
        Picasso.with(applicationContext)
            .load("https://firebasestorage.googleapis.com/v0/b/goodhorror-11922.appspot.com/o/avatars%2F$s%2Favatar?alt=media")
            .into(avat)
        mail.text = k


        loadHisFilms(k)
    }

    private fun loadHisFilms(k:String) {
        val myRef = database.getReference("user").child(
            k.replace(".", "dot")
        ).child("films")


            myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    var serialList = mutableListOf<Serial>()
                  if (snapshot.value != null) {
                        var list =snapshot.value as HashMap<String,List<String>>

                      for (c in list.values) {
                          var serial = Serial("",c[0].toString(),c[1].toString(),c[2].toString(),c[3].toString(),c[4])
                          serialList.add(serial)
                      }

                      var presenter = UsersListPresenter()
                      presenter.users = serialList
                      var adapter = SerialsAdapter(presenter)
                      recycleDisp.adapter = adapter
                      initRec(recycleDisp)
                    }
                }
            })


    }


    inner class UsersListPresenter :
        IUserListPresenter {
        var users: List<Serial> = mutableListOf()

        override fun onItemClick(view: SerialItemView?) {
            val result = users[view!!.getPos()]
        }

        override fun bindView(view: SerialItemView?) {
            val user: Serial = users[view!!.getPos()]
            view.setLogin(user.title)
            view.loadAvatar(user.poster)
        }

        override fun getCount(): Int {
            return users.size
        }

        override fun op() {

        }
    }


    fun initRec(recycleView: RecyclerView) {
        Log.d("xxxxx", "onStart: " + "здесь ок4")
        recycleView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(applicationContext)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        val lp: ViewGroup.LayoutParams = recycleView.layoutParams
        lp.width = ViewGroup.LayoutParams.WRAP_CONTENT
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT
        recycleView.requestLayout()
        recycleView.layoutManager = layoutManager
    }



}