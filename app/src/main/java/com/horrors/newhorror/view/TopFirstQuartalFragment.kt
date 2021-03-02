package com.horrors.newhorror.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.horrors.newhorror.IUserListPresenter2
import com.horrors.newhorror.R
import com.horrors.newhorror.SerialItemView2
import com.horrors.newhorror.SerialsAdapter5
import com.horrors.newhorror.di.module.Serial
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers.io
import kotlinx.android.synthetic.main.fragment_quartal_1_2021.*
import moxy.MvpAppCompatFragment
import java.util.*

class TopFirstQuartalFragment : MvpAppCompatFragment() {


    var syncToken = Object()


    fun getInstance(): TopFirstQuartalFragment? {
        return TopFirstQuartalFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_quartal_1_2021, container, false)
    }

    override fun onStart() {
        super.onStart()


        drw.openDrawer(GravityCompat.START)
        ccc.setOnClickListener { drw.openDrawer(GravityCompat.START) }


        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("film_titles")


        Observable.create<List<String>> {
            myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value != null) {
                        var map = snapshot.value as HashMap<String, String>
                        Log.d("ddddd", "onDataChange: " + map.keys)
                        var list = mutableListOf<String>()
                        for (mutableEntry in map.keys) {
                            list.add(mutableEntry)
                        }
                        it.onNext(list)
                    }
                }
            })
        }.subscribeOn(io()).subscribe({
            var list = it
            var listSerial2 = mutableListOf<Serial>()



            var thread = Thread(Runnable {
                for (s in list) {
                    val myRef = database.getReference("film_titles").child(s)
                    myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.value != null) {
                                var list = snapshot.value as MutableList<String>
                                var d = "%.2f".format(list[4].toDouble())
                                var serial = Serial("1", s, "1", d, "false", "")
                                Log.d("010101", "onDataChange: " + list[4])
                                listSerial2.add(serial)

                            }
                        }
                    })

                }
            })

            thread.start()


            val thread2 = Thread(Runnable {
                Thread.sleep(300)
                var presenter = UsersListPresenter()
                Collections.sort(listSerial2, MyComparator())
                presenter.users = listSerial2
                var adapter = SerialsAdapter5(presenter)
                recyclerFilms.post(Runnable {
                    recyclerFilms.adapter = adapter
                    initRec(recyclerFilms)
                })

            })
            thread2.start()


        }, {})

    }


    internal class MyComparator : Comparator<Serial> {
        override fun compare(o1: Serial, o2: Serial): Int {
            var i1 = o1.opisanie.toDouble() * 100
            var i2 = o2.opisanie.toDouble() * 100
            return i2.toInt().compareTo(i1.toInt())
        }
    }

    private fun initRec(recycleView: RecyclerView) {
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


    inner class UsersListPresenter :
        IUserListPresenter2 {
        var users: List<Serial> = mutableListOf()

        override fun onItemClick(view: SerialItemView2?) {
            val result = users[view!!.getPos()]
//            Singleton.instance.serial = result
//            router.replaceScreen(Screens.SerialScreen())
//            Singleton.instance.serial = result
        }

        override fun bindView(view: SerialItemView2?) {
            val user: Serial = users[view!!.getPos()]
            view.setLogin(user.title)
            view.loadAvatar(user.poster)
            Log.d("020202", "bindView: " + user.opisanie)
            view.setOpis(user.opisanie)
        }

        override fun getCount(): Int {
            return users.size
        }

        override fun op() {

        }
    }


}





