package com.horrors.newhorror.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.horrors.newhorror.*
import com.horrors.newhorror.di.module.Serial
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers.newThread
import kotlinx.android.synthetic.main.fragment_adminka.*
import moxy.MvpAppCompatFragment


class AdminFragment : MvpAppCompatFragment() {

    private var presenter = UsersListPresenter()
    private var serialsAdapter4 = SerialsAdapter4(presenter)
    private val RC_SIGN_IN = 40404
    private val TAG = "GoogleAuth"
    val database = FirebaseDatabase.getInstance()
    var listtt = mutableMapOf<String,List<String>>()

    fun getInstance(): AdminFragment? {
        return AdminFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            com.horrors.newhorror.R.layout.fragment_adminka,
            container,
            false
        )
    }

    private var googleSignInClient: GoogleSignInClient? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)


    }


    override fun onStart() {
        super.onStart()




        extended_fab.setOnClickListener {
            if (Singleton.instance.mail == null) {
                Toast.makeText(requireContext(), "Вы не зарегистрированы!", Toast.LENGTH_SHORT).show()
            } else {
            val intent = Intent(context, LoadFilmInsert::class.java)
            startActivity(intent)}
        }

        checkUserLoginIn()
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("user")
            .child(Singleton.instance.mail.toString().replace(".", "dot"))
            .child("films")

        listtt = mutableMapOf<String, List<String>>()


        Observable.create<Map<String, List<String>>> {
            myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value != null) {
                        var map = snapshot.value as HashMap<String, List<String>>
                        for (c in map.keys) {
                            var myRef2 = database.getReference("user")
                                .child(Singleton.instance.mail.toString().replace(".", "dot"))
                                .child("films").child(c)

                            myRef2.addListenerForSingleValueEvent((object : ValueEventListener {
                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }

                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.value != null) {
                                        var list = snapshot.value as ArrayList<String>
                                        listtt[c] = list
                                        it.onNext(listtt)
                                    }
                                }
                            }))
                        }
                    }
                }

            })
        }.subscribeOn(newThread()).subscribe({
            Log.d("55555", "onDataChange: " + it.values)
            var listSerial = mutableListOf<Serial>()
            for (c in listtt.values) {
                var serial = Serial("", c[0], c[1], c[2], c[3],c[4])
                listSerial.add(serial)
            }

            var usersListPresenter = UsersListPresenter()
            usersListPresenter.users = listSerial
            var presenter = usersListPresenter
            var serialsAdapter4 = SerialsAdapter4(presenter)
            films.adapter = serialsAdapter4
            initRec(films)





            serialsAdapter4.SetOnItemClickListener2(object : SerialsAdapter4.OnItemClickListener2 {
                override fun onItemClick(view: View?, position: Int) {
                    var list = mutableListOf<String>()
                    for (c in listtt.keys) {
                        list.add(c)
                    }
                    checkClassiki(database, list, position, view)
                }
            })


            serialsAdapter4.SetOnItemClickListener(object: SerialsAdapter4.OnItemClickListener {
                override fun onItemClick(view: View?, position: Int) {
                    recursDel(database, position)
                    onStart()
                }
            })


        }, {})



    }

    private fun recursDel(
        database: FirebaseDatabase,
        position: Int
    ) {
        var list = mutableListOf<String>()
        for (c in listtt.keys) {
            list.add(c)
        }
        var myRef3 = database.getReference("user")
            .child(Singleton.instance.mail.toString().replace(".", "dot"))
            .child("films")


        myRef3.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value != null) {
                    var map = snapshot.value as MutableMap<String, List<String>>
                    map.remove(list[position])
                    myRef3.setValue(map)


                    var listSerial1 = mutableListOf<Serial>()
                    for (c in map.values) {
                        var serial1 = Serial("", c[0], c[1], c[2], c[3],c[4])
                        listSerial1.add(serial1)
                    }

                    var usersListPresenter1 = UsersListPresenter()
                    usersListPresenter1.users = listSerial1
                    var presenter = usersListPresenter1
                    var serialsAdapter41 = SerialsAdapter4(presenter)
                    films.adapter = serialsAdapter41
                    initRec(films)

                }
            }
        })
    }


    private fun checkClassiki(
        database: FirebaseDatabase,
        list: MutableList<String>,
        position: Int,
        view: View?
    ) {
        var myRef3 = database.getReference("user")
            .child(Singleton.instance.mail.toString().replace(".", "dot"))
            .child("films")
            .child(list[position])

        myRef3.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value != null) {
                    var listok = snapshot.value as ArrayList<String>
                    Log.d("66666", "onItemClick: " + listok)
                    if (listok!![3] == "false") {
                        listok[3] = "true"
                        view!!.setBackgroundResource(R.drawable.classs)
                        var rec = view.findViewById<ImageView>(R.id.rec)
                        rec.setImageResource(R.drawable.classs)
                    } else if (listok[3] == "true") {
                        listok[3] = "false"
                        var rec = view!!.findViewById<ImageView>(R.id.rec)
                        rec.setImageResource(R.drawable.class_grey)
                    }
                    var myRef2 =
                        database.getReference("user").child(
                            Singleton.instance.mail.toString().replace(".", "dot")
                        )
                            .child("films").child(listok[0])
                    myRef2.setValue(listok)
                }
            }
        })
    }

    private fun aboutClass(serialsAdapter4: SerialsAdapter4) {
        serialsAdapter4.SetOnItemListener(object : SerialsAdapter4.OnItemListener {
            override fun onItem(view: View?, position: Int) {
            }
        })
    }


    private fun checkUserLoginIn() {
        // Проверим, входил ли пользователь в это приложение через Google
        // Проверим, входил ли пользователь в это приложение через Google
        val account = GoogleSignIn.getLastSignedInAccount(requireActivity())
        if (account != null) {
            // Пользователь уже входил, сделаем кнопку недоступной
//            disableSign()
            // Обновим почтовый адрес этого пользователя и выведем его на экран
            Singleton.instance.mail = account.email
            updateUI(account.email)
        }
    }


    private fun signIn() {
        val signInIntent = googleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            // Когда сюда возвращается Task, результаты аутентификафии уже
            // готовы.
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }


    private fun signOut() {
        googleSignInClient!!.signOut()
            .addOnCompleteListener(requireActivity()) {
                updateUI("email")
//                enableSign()
            }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account =
                completedTask.getResult(ApiException::class.java)
            // Регистрация прошла успешно
//            disableSign()
            Singleton.instance.mail = account!!.email
            updateUI(account!!.email)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure
            // reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }

    // Обновляем данные о пользователе на экране
    private fun updateUI(idToken: String?) {
    }


//    private fun enableSign() {
//        sign.isEnabled = true
//        sing_out_button.isEnabled = false
//    }
//
//    private fun disableSign() {
//        sign.isEnabled = false
//        sing_out_button.isEnabled = true
//    }
//

    inner class UsersListPresenter :
        IUserListPresenter2 {
        var users = mutableListOf<Serial>()

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

            if (user.favor == "true") {view.changeToClass()}
            else {view.changeToGrey()}
        }

        override fun getCount(): Int {
            return users.size
        }

//        override fun delete(view: SerialItemView2?) {
//            users.removeAt(view!!.getPos())
//        }

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




