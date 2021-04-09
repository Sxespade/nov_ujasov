package com.horrors.newhorror.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.horrors.newhorror.*
import com.horrors.newhorror.di.module.Serial
import com.horrors.newhorror.SerialsAdapter6
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers.io
import kotlinx.android.synthetic.main.fragment_friends.*
import kotlinx.android.synthetic.main.fragment_main.recycleView1
import moxy.MvpAppCompatFragment
import java.lang.IllegalStateException
import java.net.URLEncoder

class FriendsFragment : MvpAppCompatFragment() {



    fun getInstance(): FriendsFragment? {
        return FriendsFragment()
    }

    var presenter = UsersListPresenter()
    var serialsAdapterr = SerialsAdapter6(presenter)
    lateinit var friendMail: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_friends, container, false)
    }

    override fun onStart() {
        super.onStart()

        if (Singleton.instance.mail == null) {
            button.isEnabled = false
        }

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("emails")

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value != null) {
                    var list = snapshot.value as MutableList<String>
                    if (list.contains(Singleton.instance.mail.toString())) {
                        list.remove(Singleton.instance.mail.toString())
                    }
                    try {
                    var adapter = ArrayAdapter(requireActivity(), R.layout.item_for_adapter, list)
                    friends_adapter.setAdapter(adapter)} catch (c: IllegalStateException) {}
                }
            }
        })


        if (Singleton.instance.mail != null) {
            button.setOnClickListener {
                val ref = database.getReference("user")
                    .child(Singleton.instance.mail.toString().replace(".", "dot"))
                    .child("friends")

                ref.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        var list = mutableListOf<String>()
                        if (snapshot.value != null && friends_adapter.text.toString() != "") {
                            list = snapshot.value as MutableList<String>
                            if (!list.contains(friends_adapter.text.toString())) {
                                list.add(friends_adapter.text.toString())
                            }
                            ref.setValue(list)
                        } else if (friends_adapter.text.toString() != "") {
                            list = mutableListOf<String>()
                            list.add(friends_adapter.text.toString())
                            ref.setValue(list)
                        } else if (snapshot.value != null) {
                            list = snapshot.value as MutableList<String>
                        }

                        var serialList = mutableListOf<Serial>()
                        for (serial in list) {
                            friendMail = serial
                            var s = URLEncoder.encode(serial, "utf-8")
                            var serial1 = Serial(
                                "1",
                                serial,
                                "https://firebasestorage.googleapis.com/v0/b/goodhorror-11922.appspot.com/o/avatars%2F$s%2Favatar?alt=media&token=040f8ca5-eafa-44aa-80bd-5079b6bc7aa0",
                                "1",
                                "1", ""
                            )
                            serialList.add(serial1)
                        }

                        var usersListPresenter1 = UsersListPresenter()
                        usersListPresenter1.users = serialList
                        serialsAdapterr = SerialsAdapter6(usersListPresenter1)
                        recycleView1.adapter = serialsAdapterr
                        initRec(recycleView1)


                    }
                })

            }
        }

        loadFriendsAdapter()



        serialsAdapterr.SetOnItemClickListener(object :
            SerialsAdapter6.OnItemClickListener {
            override fun onItemClick(view: View?, position: Int) {
                serialsAdapterr.notifyDataSetChanged()
                loadFriendsAdapter()
            }
        })


    }


    private fun loadFriendsAdapter() {

        val database = FirebaseDatabase.getInstance()
        var myRef = database.getReference("user")
            .child(Singleton.instance.mail.toString().replace(".", "dot"))
            .child("friends")


        Observable.create<List<String>> {
            myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value != null) {

                        var list = snapshot.value as MutableList<String>
                        it.onNext(list)
                    }
                }
            })
        }.subscribeOn(io()).observeOn(AndroidSchedulers.mainThread()).subscribe( {
            var list = it
            var serialList = mutableListOf<Serial>()


            for (serial in list) {

                var s = URLEncoder.encode(serial, "utf-8")
                friendMail = serial
                var serial = Serial(
                    "1",
                    serial,
                    "https://firebasestorage.googleapis.com/v0/b/goodhorror-11922.appspot.com/o/avatars%2F$s%2Favatar?alt=media&token=040f8ca5-eafa-44aa-80bd-5079b6bc7aa0",
                    "1",
                    "1",""
                )
                serialList.add(serial)

                var usersListPresenter = UsersListPresenter()
                usersListPresenter.users = serialList
                serialsAdapterr = SerialsAdapter6(usersListPresenter)
                recycleView1.adapter = serialsAdapterr
                initRec(recycleView1)


            }
        },{})
    }

        inner class UsersListPresenter :
            IUserListPresenter2 {
            var users: List<Serial> = mutableListOf()

            override fun bindView(view: SerialItemView2?) {
                val user: Serial = users[view!!.getPos()]
                view.setLogin(user.title)
                view.loadAvatar(user.poster)
            }

            override fun onItemClick(view: SerialItemView2?) {
                val result = users[view!!.getPos()]
                var intent = Intent(requireContext(),FriendDisplay::class.java)
                intent.putExtra("1",users[view!!.getPos()].title)
                startActivity(intent)
//            Singleton.instance.serial = result
//            router.replaceScreen(Screens.SerialScreen())
//            Singleton.instance.serial = result
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
            val layoutManager = LinearLayoutManager(activity)
            layoutManager.orientation = LinearLayoutManager.VERTICAL
            val lp: ViewGroup.LayoutParams = recycleView.layoutParams
            lp.width = ViewGroup.LayoutParams.WRAP_CONTENT
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT
            recycleView.requestLayout()
            recycleView.layoutManager = layoutManager
        }
    }


