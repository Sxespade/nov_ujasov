package com.horrors.newhorror.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.horrors.newhorror.*
import com.horrors.newhorror.di.module.Serial
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers.io
import kotlinx.android.synthetic.main.fragment_main.*
import moxy.MvpAppCompatFragment


class MainFragment : MvpAppCompatFragment() {

    private var googleSignInClient: GoogleSignInClient? = null
    val database = FirebaseDatabase.getInstance()

    fun getInstance(): MainFragment? {
        return MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onStart() {
        super.onStart()

        bestRec.isVisible = true
        yourRec.isVisible = false
        friendsRec.isVisible = false
        val database = FirebaseDatabase.getInstance()

        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        loadMyFilms(database)
        loadFilmsOfMyFriends()
        loadPopularFilms()

    }

    private fun loadMyFilms(database: FirebaseDatabase) {

        val ref = database.getReference("user").child(
            Singleton.instance.mail.toString()
                .replace(".", "dot")
        ).child("films")

        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value != null) {

                    try {
                        friendsRec.isVisible = true
                    } catch (e: java.lang.IllegalStateException) {}


                    var map = snapshot.value as MutableMap<String,List<String>>
                    var listSerial = mutableListOf<Serial>()

                    for (k in map.values.toMutableList()) {
                        var serial = Serial("",k[0],k[1],k[2],k[3],k[4])
                        if (k[3] == "true") {
                        listSerial.add(serial)}
                    }

                    var presenter = UsersListPresenter()
                    presenter.users = listSerial
                    var adapter = SerialsAdapter(presenter)
                    try {
                        recycleView1.adapter = adapter
                        initRec(recycleView1)
                    } catch (e: IllegalStateException) {
                    }


                }
            }
        })

    }


    private fun loadPopularFilms() {
        var ref = database.getReference("film_titles")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value != null) {
                    try {
                        bestRec.isVisible = true
                    } catch (e: java.lang.IllegalStateException) {
                    }

                    var map = snapshot.value as HashMap<String, List<String>>
                    var list = mutableListOf<List<String>>()
                    for (m in map.values) {
                        list.add(m)
                    }

                    var serialList = mutableListOf<Serial>()

                    for (m in list) {
                        if (m[4].toDouble() > 0) {
                            var serial = Serial("1", m[0], m[1], m[2], m[3],m[4])
                            serialList.add(serial)
                        }
                    }

                    var presenter = UsersListPresenter()
                    presenter.users = serialList
                    var adapter = SerialsAdapter(presenter)

                    try {
                        bestrecom.adapter = adapter
                        initRec(bestrecom)
                    } catch (e: IllegalStateException) {
                    }

                }
            }
        })
    }


    private fun loadFilmsOfMyFriends() {
        var ref = database.getReference("user")
            .child(Singleton.instance.mail.toString().replace(".", "dot"))
            .child("friends")


        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.d("sssss", "onCancelled: " + error)
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.value != null) {

                    try {
                        recFriends.isVisible = true
                    } catch (e: java.lang.IllegalStateException) {}

                    var list = snapshot.value as ArrayList<String>
                    Log.d("66666", "onDataChange: " + list)
                    var listList = mutableListOf<List<String>>()


                    for (c in list) {
                        var ref = database.getReference("user").child(c.replace(".", "dot"))
                            .child("films")


                        Observable.create<MutableList<List<String>>> {
                            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onCancelled(error: DatabaseError) {
                                }

                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.value != null) {
                                        var map = snapshot.value as HashMap<String, List<String>>
                                        Log.d("77777", "onDataChange: " + map.values)
                                        try {
                                            friendsRec.isVisible = true
                                        } catch (e: IllegalStateException) {
                                        }

                                        for (c in map.values) {
                                            listList.add(c)
                                            Log.d("222222", "onDataChange: " + listList)
                                        }
                                        it.onNext(listList)
                                    }
                                }
                            })
                        }.subscribeOn(io()).subscribe({
                            Log.d("33333", "onDataChange: " + it)
                            var serialList = mutableListOf<Serial>()
                            it.shuffle()
                            Log.d("99999", "onDataChange: " + listList)
                            for (s in it) {
                                var serial = Serial("1", s[0], s[1], s[2], s[3], s[4])
                                serialList.add(serial)
                            }
                            var presenter = UsersListPresenter()
                            presenter.users = serialList
                            var adap = SerialsAdapter(presenter)
                            recFriends.adapter = adap
                            initRec(recFriends)
                        }, {})


                    }
                }
            }
        })
    }


    inner class UsersListPresenter :
        IUserListPresenter {
        var users: List<Serial> = mutableListOf()

        override fun onItemClick(view: SerialItemView?) {
            val result = users[view!!.getPos()]
            var intent = Intent(requireContext(), AboutFilm::class.java)
            intent.putExtra("1", users[view!!.getPos()].title)
            intent.putExtra("2", users[view!!.getPos()].poster)
            intent.putExtra("3", users[view!!.getPos()].opisanie)
            intent.putExtra("4",  java.lang.String.format("%.2f", users[view!!.getPos()].commonStar   .toDouble()))
            startActivity(intent)
//            Singleton.instance.serial = result
//            router.replaceScreen(Screens.SerialScreen())
//            Singleton.instance.serial = result
        }

        override fun bindView(view: SerialItemView?) {
            val user: Serial = users[view!!.getPos()]
            view.setLogin(user.title)
            view.loadAvatar(user.poster)
            view.setOpis("")
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
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recycleView.requestLayout()
        recycleView.layoutManager = layoutManager
    }


}