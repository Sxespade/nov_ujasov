package com.horrors.newhorror.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.horrors.newhorror.*
import com.horrors.newhorror.GlideImageLoader.GlideImageLoader
import com.horrors.newhorror.di.module.Serial
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers.io
import kotlinx.android.synthetic.main.activity_load_film_insert.*
import kotlinx.android.synthetic.main.item_for_serach_film.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import ru.terrakok.cicerone.Router
import java.net.URLEncoder
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap


class LoadFilmInsert : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    private val imageLoader = GlideImageLoader()

    @Inject
    lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_film_insert)
        App.instance.appComponent.inject(this)

        val myRef = database.getReference("film_titles")

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value != null) {
                    var map = snapshot.value as HashMap<String, List<String>>
                    var list = mutableListOf<String>()
                    for (c in map.keys) {
                        list.add(c)
                    }
                    var adapter =
                        ArrayAdapter<String>(applicationContext, R.layout.item_for_adapter, list)
                    textInput.setAdapter(adapter)
                }
            }
        })
    }


    override fun onStart() {
        super.onStart()

//        addFilm("Поворот не туда: Наследие(2021)")


        alarm.visibility = View.GONE
        textInput2.visibility = View.GONE
        button5.visibility = View.GONE
        picture.visibility = View.GONE
        button7.visibility = View.GONE

        if (Singleton.instance.mail == null) {
            button3.isEnabled = false
        }


        button3.setOnClickListener {


            Log.d("klklkl", "onStart: " + textInput.text.toString())

            if (textInput.text.toString() != "") {
                Log.d("xzxz", "onCreate: ")
                var textFilm = textInput.text.toString()


                val myRef1 = database.getReference("film_titles").child(textFilm)

                myRef1.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.value != null) {
                            snapshotNotNull(snapshot, textFilm)
                        } else {
                            textInput2.isVisible = true
                            button3.isVisible = false
                            button5.isVisible = true

                            yandexGo()
                        }
                    }

                })
            }
        }

    }

    private fun yandexGo() {

        button5.setOnClickListener {
            if (textInput != null) {
                hideKeyboard()
                Log.d("TAGllllllllllllll", "yandexGo: ")
                var doc: Document
                val calendar = Calendar.getInstance()
                var str = ""
                var str2 = ""
                var str3 = ""
                Observable.create<String> {
                    var s: String = textInput.text.toString()
                    val y: String = textInput2.text.toString()
                    s = URLEncoder.encode(s, "utf-8")
                    doc = Jsoup.connect("https://www.kinopoisk.ru/index.php?kp_query=$s+$y").get()
                    val title = doc.select("p.name")[0].text()

                    Log.d("sssssss", "yandexGo: " + "https://www.kinopoisk.ru/index.php?kp_query=$s+$y")

                    val title2 = doc.select("div.info")[0].text()
                    str3 = title2
                    if (title2.contains("ужасы") && (title2.contains("2019") || title2.contains("2020") || title2.contains("2021"))) {
                    str = title
                    Log.d("TAG222", "aaa: $title")
                    val string =
                        "https://yandex.ru/images/search?text=$title%20постер%20обложка&size=large&iorient=vertical&from=tabbar"
                    val doc = Jsoup.connect(string).get()
                    it.onNext(getNumberOfPicture(doc, 0)) } else {it.onNext("1")}
                }.subscribeOn(io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
                    Glide.with(applicationContext).load("https://" + it).override(120, 180)
                        .centerCrop()
                        .into(picture)
                    str2 = "https://$it"
                    picture.isVisible = true
                    button7.isVisible = true


                    if (it == "1") {alarm.isVisible = true
                        Glide.with(applicationContext).load(R.drawable.emm).override(120, 180)
                            .centerCrop()
                            .into(picture)
                        button7.isEnabled = false

                    } else {button7.isEnabled = true}


                    button7.setOnClickListener {
                        val myRef = database.getReference("film_titles")
                        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.value != null) {
                                    var map = snapshot.value as HashMap<String, List<String>>

                                    var list = mutableListOf<String>()
                                    list.add(skobkiOnYear(str)!!)
                                    list.add(str2)
                                    list.add("Описание в разработке...")
                                    list.add("false")
                                    list.add("0")
                                    list.add("0")
                                    list.add("0")


                                    map[skobkiOnYear(str)!!] = list

                                    myRef.setValue(map)
                                    val myRef2 = database.getReference("user")
                                        .child(
                                            Singleton.instance.mail.toString()
                                                .replace(".", "dot")
                                        ).child("films")

                                    myRef2.addListenerForSingleValueEvent(object :
                                        ValueEventListener {
                                        override fun onCancelled(error: DatabaseError) {
                                            TODO("Not yet implemented")
                                        }

                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            if (snapshot.value != null) {
                                                var map =
                                                    snapshot.value as HashMap<String, List<String>>
                                                map[skobkiOnYear(str)!!] = list
                                                myRef2.setValue(map)
                                            } else {
                                                var map =
                                                    snapshot.value as HashMap<String, List<String>>
                                                map[skobkiOnYear(str)!!] = list
                                                myRef2.setValue(map)
                                            }
                                        }
                                    })
                                }
                            }
                        })
                    }

                }, {})

            }
        }

    }


    private fun getNumberOfPicture(doc2: Document, l: Int): String? {
        val str2 = doc2.select("a[href].serp-item__link")[l]
        val str3 = "https://yandex.ru" + str2.attr("href")
        var j = 0
        for (i in 2 until str3.length - 2) {
            if ("" + str3.toCharArray()[i] + str3.toCharArray()[i + 1] + str3.toCharArray()[i + 2] == "jpg" || "" + str3.toCharArray()[i] + str3.toCharArray()[i + 1] + str3.toCharArray()[i + 2] == "png"
            ) {
                j = i + 2
                break
            }
        }
        var str4 = ""
        for (k in 72..j) {
            str4 = str4 + str3.toCharArray()[k]
        }
        val str5 = str4.replace("%2F", "/")
        return str5
    }


    private fun skobkiOnYear(title: String): String? {
        var str2 = StringBuilder()
        val chars = title.toCharArray()
        for (i in title.indices) {
            str2.append(chars[i])
            if (i == title.length - 5) {
                str2 = StringBuilder("$str2(")
            }
        }
        val str3 = "$str2)"
        Log.d("TAG9", "initBtnSendAction: $str3")
        return str3
    }


    private fun snapshotNotNull(
        snapshot: DataSnapshot,
        textFilm: String
    ) {
        var list = snapshot.value as ArrayList<String>
        var listOfSerial = mutableListOf<Serial>()
        var serial =
            Serial("1", list[0], list[1], list[2], "false", list[4])
        listOfSerial.add(serial)
        var presenter = UsersListPresenter()
        presenter.users = listOfSerial
        var adapter = SerialsAdapter3(presenter)
        recyclForFind.adapter = adapter
        initRec(recyclForFind)

        adapter.SetOnItemClickListener(object :
            SerialsAdapter3.OnItemClickListener {
            override fun onItemClick(view: View?, position: Int) {
                val myRef1 = database.getReference("user")
                    .child(
                        Singleton.instance.mail.toString()
                            .replace(".", "dot")
                    )
                    .child("films")
                    .child(textFilm)
                myRef1.setValue(list)
                var intent =
                    Intent(this@LoadFilmInsert, MainActivity::class.java)
                intent.putExtra("2", "2")
                startActivity(intent)
            }

        })
    }


    private fun addFilm(s: String) {
        val myRef1 = database.getReference("film_titles")

        myRef1.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value != null) {
                    var map = snapshot.value as HashMap<String, List<String>>


                    val myRef = database.getReference("film_titles/")
                    var list = mutableListOf<String>()
                    list.add(textInput.text.toString())
                    list.add("1")
                    list.add("1")
                    list.add("false")
                    list.add("0")
                    list.add("0")
                    list.add("0")

                    map.put(s, list)
                    myRef.setValue(map)
                } else {
                    var map = mutableMapOf<String, List<String>>()


                    val myRef = database.getReference("film_titles/")
                    var list = mutableListOf<String>()
                    list.add(textInput.text.toString())
                    list.add("1")
                    list.add("1")
                    list.add("false")
                    list.add("0")
                    list.add("0")
                    list.add("0")

                    map.put(s, list)
                    myRef.setValue(map)
                }
            }
        })


    }


    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(textInput.windowToken, 0)
    }


    fun initRec(recycleView: RecyclerView) {
        Log.d("xxxxx", "onStart: " + "здесь ок4")
        recycleView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
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

        init {
            var listBut = mutableListOf<Button>()
            var button = btnAdd
            for (user in users) {
                listBut.add(button)
            }
        }


        override fun onItemClick(view: SerialItemView2?) {
            val user = users[view!!.getPos()]
        }

        override fun bindView(view: SerialItemView2?) {
            val user: Serial = users[view!!.getPos()]
            view.setLogin(user.title)
            view.loadAvatar(user.poster)
            view.setOpis(user.opisanie)
        }

        override fun getCount(): Int {
            return users.size
        }

        override fun op() {
//
        }
    }


}