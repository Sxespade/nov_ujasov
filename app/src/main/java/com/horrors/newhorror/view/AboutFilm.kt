package com.horrors.newhorror.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import com.horrors.newhorror.GlideImageLoader.GlideImageLoader
import com.horrors.newhorror.R
import com.horrors.newhorror.Singleton
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_about_film.*

class AboutFilm : AppCompatActivity() {

    private val imageLoader = GlideImageLoader()
    val database = FirebaseDatabase.getInstance()

    var name = ""
    var count = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_film)
        vote.isVisible = false
    }

    override fun onStart() {
        super.onStart()
        name = intent!!.extras!!["1"].toString()
        var url = intent!!.extras!!["2"].toString()
        var opis = intent!!.extras!!["3"].toString()
        var comStar = intent!!.extras!!["4"].toString()
        zvezda.text = comStar
        titleFilm.text = name
        imageLoader.loadImage(
            url,
            poster
        )
        opisanie.text = opis


        loadOcenkaEsliEst()



        checkOchenka()

    }

    private fun loadOcenkaEsliEst() {
//        var ref = database.getReference("user")
//            .child(Singleton.instance.mail.toString().replace(".", "dot"))
//            .child("film").child(name)


        var ref2 = database.getReference("user")
            .child(Singleton.instance.mail.toString().replace(".", "dot"))
            .child(name + "-ocenka")

        ref2.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
             if (snapshot.value != null) {
                 Log.d("54545", "onDataChange: ")
                 var list = snapshot.value as MutableList<String>
                 when (list[1].toInt()) {
                     1 -> starCount1()
                     2 -> starCount2()
                     3 -> starCount3()
                     4 -> starCount4()
                     5 -> starCount5()
                     6 -> starCount6()
                     7 -> starCount7()
                     8 -> starCount8()
                     9 -> starCount9()
                     10 -> starCount10()
                 }
             }
            }
        })


    }

    private fun checkOchenka() {

        val database = FirebaseDatabase.getInstance()
        var ref1 = database.getReference("film_titles")
            .child(name)
        ref1.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value != null) {
                    var list = snapshot.value as MutableList<String>
                    val result = java.lang.String.format("%.2f", list[4].toDouble())
                    zvezda.text = result.toString()
                }
            }
        })




        var ref = database.getReference("user")
            .child(Singleton.instance.mail.toString().replace(".", "dot"))
            .child("film").child(name)

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value != null) {
                    var list = snapshot.value as MutableList<String>
                    var ref = database.getReference("user")
                        .child(Singleton.instance.mail.toString().replace(".", "dot"))
                        .child(name + "-ocenka")


                    ref.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.value != null) {
                                var list1 = snapshot.value as MutableList<String>
                                if (list[1] != "0") {
                                    if (list[5] != "0" || list1[1] != null) {
                                        closeStar()
                                    }
                                } else {

                                    dvijSoZvezdami()
                                    dvijSUtverjdeniem()

                                }
                            } else {
                                dvijSoZvezdami()
                                dvijSUtverjdeniem()
                            }
                        }


                    })
                } else {
                    var ref = database.getReference("user")
                        .child(Singleton.instance.mail.toString().replace(".", "dot"))
                        .child(name + "-ocneka")

                    ref.addListenerForSingleValueEvent(object: ValueEventListener{
                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.value == null) {
                                dvijSoZvezdami()
                                dvijSUtverjdeniem()
                            }
                        }
                    })


                }
            }
        })
    }

    private fun dvijSUtverjdeniem() {
       vote.setOnClickListener {

           vote.setImageResource(R.drawable.vote_green)
           var ref = database.getReference("film_titles").child(name)

           var ref1 = database.getReference("user")
               .child(Singleton.instance.mail.toString().replace(".", "dot"))
               .child("film").child(name)

           var ref2 = database.getReference("user")
               .child(Singleton.instance.mail.toString().replace(".", "dot"))
               .child(name + "-ocenka")

           ref.addListenerForSingleValueEvent(object: ValueEventListener{
               override fun onCancelled(error: DatabaseError) {
                   TODO("Not yet implemented")
               }

               override fun onDataChange(snapshot: DataSnapshot) {
                  if (snapshot.value != null) {
                      var list = snapshot.value as MutableList<String>
                      list[5] = count.toString()
                      var i = list[6].toInt()+1
                      list[6] = i.toString()
                      var d = (list[4].toDouble()*(list[6].toDouble() - 1) + list[5].toDouble())/list[6].toDouble()
                      list[4] = d.toString()
                      ref.setValue(list)
                      ref.setValue(list)

                      var list1 = mutableListOf<String>()
                      list1.add(list[0])
                      list1.add(list[5])
                      ref1.setValue(list)
                      ref2.setValue(list1)

                      closeStar()


                  }
               }
           })




       }
    }

    private fun goOcenivat() {
        TODO("Not yet implemented")
    }

    private fun closeStar() {
        s1.isEnabled = false
        s2.isEnabled = false
        s3.isEnabled = false
        s4.isEnabled = false
        s5.isEnabled = false
        s6.isEnabled = false
        s7.isEnabled = false
        s8.isEnabled = false
        s9.isEnabled = false
        s10.isEnabled = false
    }

    private fun openStar() {
        s1.isEnabled = true
        s2.isEnabled = true
        s3.isEnabled = true
        s4.isEnabled = true
        s5.isEnabled = true
        s6.isEnabled = true
        s7.isEnabled = true
        s8.isEnabled = true
        s9.isEnabled = true
        s10.isEnabled = true
    }





    //    override fun onStart() {
//        super.onStart()
//
//        name = intent!!.extras!!["1"].toString()
//        var url = intent!!.extras!!["2"].toString()
//        var opis = intent!!.extras!!["3"].toString()
//        var comStar = intent!!.extras!!["4"].toString()
//        zvezda.text = comStar
//        titleFilm.text = name
//        imageLoader.loadImage(
//            url,
//            poster
//        )
//        opisanie.text = opis
//
//        checkOcenka()
//
//        onClickStart()
//
//
//    }
//
//    private fun checkOcenka() {
//        var ref = database.getReference("user")
//            .child(Singleton.instance.mail.toString().replace(".", "dot"))
//            .child("films").child(name)
//
//        ref.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.value != null) {
//                    var list = snapshot.value as MutableList<String>
//                    if (list[4] == "0") {} else {allEnabledStars()}
//                }
//            }
//        })
//
//
//    }
//
//    private fun onClickStart() {
//
//
//        var ref = database.getReference("film_titles")
//            .child(name)
//
//        ref.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.value != null) {
//                    var list = snapshot.value as MutableList<String>
//
//                    if (list[4] != "0") {
//
//                        Log.d("070707", "onDataChange: я тут1")
//
//                        var k = list[4].toDouble()
//
//                        if (k < 0) {
//                            starCount0()
//                        }
//                        if (k < 2) {
//                            starCount1()
//                        }
//                        if (2 <= k && k < 3) {
//                            starCount2()
//                        }
//                        if (3 <= k && k < 4) {
//                            starCount3()
//                        }
//                        if (4 <= k && k < 5) {
//                            starCount4()
//                        }
//                        if (5 <= k && k < 6) {
//                            starCount5()
//                        }
//                        if (6 <= k && k < 7) {
//                            starCount6()
//                        }
//                        if (7 <= k && k < 8) {
//                            starCount7()
//                        }
//                        if (8 <= k && k < 9) {
//                            starCount8()
//                        }
//                        if (9 <= k && k < 10) {
//                            starCount9()
//                        }
//                        if (k == 10.0) {
//                            starCount10()
//                        }
//
//                    } else {
//
//                        Log.d("070707", "onDataChange: я тут")
//                        dvijSoZvezdami()
//                        vote.setOnClickListener {
//                            bigDvijPoOcenkam()
//                        }
//
//                    }
//
//                } else {
//
//                    Log.d("070707", "onDataChange: я тут2")
//
//
//                    var ref = database.getReference("user")
//                        .child(Singleton.instance.mail.toString().replace(".", "dot"))
//                        .child(name + "-ocenki")
//
//                    allDisabledStars()
//                    dvijSoZvezdami()
//                    vote.isVisible = true
//
//                    vote.setOnClickListener {
//                        vote.setImageResource(R.drawable.vote_green)
//                        Observable.create<Int> {
//                            if (count != 0) {
//                                it.onNext(count)
//                            }
//                        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//                            .subscribe({
//                                count = it
//                                var list = mutableListOf<String>()
//                                list.add(name)
//                                list.add(count.toString())
//                                ref.setValue(list)
//                                bigDvijPoOcenkam2()
//                                allEnabledStars()
//                            }, { Log.d("060606", "onDataChange: " + it) })
//                    }
//                }
//            }
//        })
//
//
//    }
//
    private fun dvijSoZvezdami() {
        s1.setOnClickListener {
            starCount1()
            count = 1
            vote.isVisible = true
        }



        s2.setOnClickListener {
            starCount2()
            count = 2
            vote.isVisible = true
        }

        s3.setOnClickListener {
            starCount3()
            count = 3
            vote.isVisible = true
        }

        s4.setOnClickListener {
            starCount4()
            count = 4
            vote.isVisible = true
        }

        s5.setOnClickListener {
            starCount5()
            count = 5
            vote.isVisible = true
        }

        s6.setOnClickListener {
            starCount6()
            count = 6
            vote.isVisible = true
        }

        s7.setOnClickListener {
            starCount7()
            count = 7
            vote.isVisible = true
        }

        s8.setOnClickListener {
            starCount8()
            count = 8
            vote.isVisible = true
        }

        s9.setOnClickListener {
            starCount9()
            count = 9
            vote.isVisible = true
        }

        s10.setOnClickListener {
            starCount10()
            count = 10
            vote.isVisible = true
        }
    }

    private fun starCount10() {
        s1.setImageResource(R.drawable.star)
        s2.setImageResource(R.drawable.star)
        s3.setImageResource(R.drawable.star)
        s4.setImageResource(R.drawable.star)
        s5.setImageResource(R.drawable.star)
        s6.setImageResource(R.drawable.star)
        s7.setImageResource(R.drawable.star)
        s8.setImageResource(R.drawable.star)
        s9.setImageResource(R.drawable.star)
        s10.setImageResource(R.drawable.star)
    }

    private fun starCount9() {
        s1.setImageResource(R.drawable.star)
        s2.setImageResource(R.drawable.star)
        s3.setImageResource(R.drawable.star)
        s4.setImageResource(R.drawable.star)
        s5.setImageResource(R.drawable.star)
        s6.setImageResource(R.drawable.star)
        s7.setImageResource(R.drawable.star)
        s8.setImageResource(R.drawable.star)
        s9.setImageResource(R.drawable.star)
        s10.setImageResource(R.drawable.empty_star)
    }

    private fun starCount8() {
        s1.setImageResource(R.drawable.star)
        s2.setImageResource(R.drawable.star)
        s3.setImageResource(R.drawable.star)
        s4.setImageResource(R.drawable.star)
        s5.setImageResource(R.drawable.star)
        s6.setImageResource(R.drawable.star)
        s7.setImageResource(R.drawable.star)
        s8.setImageResource(R.drawable.star)
        s9.setImageResource(R.drawable.empty_star)
        s10.setImageResource(R.drawable.empty_star)
    }

    private fun starCount7() {
        s1.setImageResource(R.drawable.star)
        s2.setImageResource(R.drawable.star)
        s3.setImageResource(R.drawable.star)
        s4.setImageResource(R.drawable.star)
        s5.setImageResource(R.drawable.star)
        s6.setImageResource(R.drawable.star)
        s7.setImageResource(R.drawable.star)
        s8.setImageResource(R.drawable.empty_star)
        s9.setImageResource(R.drawable.empty_star)
        s10.setImageResource(R.drawable.empty_star)
    }

    private fun starCount6() {
        s1.setImageResource(R.drawable.star)
        s2.setImageResource(R.drawable.star)
        s3.setImageResource(R.drawable.star)
        s4.setImageResource(R.drawable.star)
        s5.setImageResource(R.drawable.star)
        s6.setImageResource(R.drawable.star)
        s7.setImageResource(R.drawable.empty_star)
        s8.setImageResource(R.drawable.empty_star)
        s9.setImageResource(R.drawable.empty_star)
        s10.setImageResource(R.drawable.empty_star)
    }

    private fun starCount5() {
        s1.setImageResource(R.drawable.star)
        s2.setImageResource(R.drawable.star)
        s3.setImageResource(R.drawable.star)
        s4.setImageResource(R.drawable.star)
        s5.setImageResource(R.drawable.star)
        s6.setImageResource(R.drawable.empty_star)
        s7.setImageResource(R.drawable.empty_star)
        s8.setImageResource(R.drawable.empty_star)
        s9.setImageResource(R.drawable.empty_star)
        s10.setImageResource(R.drawable.empty_star)
    }

    private fun starCount4() {
        s1.setImageResource(R.drawable.star)
        s2.setImageResource(R.drawable.star)
        s3.setImageResource(R.drawable.star)
        s4.setImageResource(R.drawable.star)
        s5.setImageResource(R.drawable.empty_star)
        s6.setImageResource(R.drawable.empty_star)
        s7.setImageResource(R.drawable.empty_star)
        s8.setImageResource(R.drawable.empty_star)
        s9.setImageResource(R.drawable.empty_star)
        s10.setImageResource(R.drawable.empty_star)
    }

    private fun starCount3() {
        s1.setImageResource(R.drawable.star)
        s2.setImageResource(R.drawable.star)
        s3.setImageResource(R.drawable.star)
        s4.setImageResource(R.drawable.empty_star)
        s5.setImageResource(R.drawable.empty_star)
        s6.setImageResource(R.drawable.empty_star)
        s7.setImageResource(R.drawable.empty_star)
        s8.setImageResource(R.drawable.empty_star)
        s9.setImageResource(R.drawable.empty_star)
        s10.setImageResource(R.drawable.empty_star)
    }

    private fun starCount2() {
        s1.setImageResource(R.drawable.star)
        s2.setImageResource(R.drawable.star)
        s3.setImageResource(R.drawable.empty_star)
        s4.setImageResource(R.drawable.empty_star)
        s5.setImageResource(R.drawable.empty_star)
        s6.setImageResource(R.drawable.empty_star)
        s7.setImageResource(R.drawable.empty_star)
        s8.setImageResource(R.drawable.empty_star)
        s9.setImageResource(R.drawable.empty_star)
        s10.setImageResource(R.drawable.empty_star)
    }

    private fun starCount1() {
        s1.setImageResource(R.drawable.star)
        s2.setImageResource(R.drawable.empty_star)
        s3.setImageResource(R.drawable.empty_star)
        s4.setImageResource(R.drawable.empty_star)
        s5.setImageResource(R.drawable.empty_star)
        s6.setImageResource(R.drawable.empty_star)
        s7.setImageResource(R.drawable.empty_star)
        s8.setImageResource(R.drawable.empty_star)
        s9.setImageResource(R.drawable.empty_star)
        s10.setImageResource(R.drawable.empty_star)
    }

    private fun starCount0() {
        s1.setImageResource(R.drawable.empty_star)
        s2.setImageResource(R.drawable.empty_star)
        s3.setImageResource(R.drawable.empty_star)
        s4.setImageResource(R.drawable.empty_star)
        s5.setImageResource(R.drawable.empty_star)
        s6.setImageResource(R.drawable.empty_star)
        s7.setImageResource(R.drawable.empty_star)
        s8.setImageResource(R.drawable.empty_star)
        s9.setImageResource(R.drawable.empty_star)
        s10.setImageResource(R.drawable.empty_star)
    }

    //
//    private fun bigDvijPoOcenkam() {
//        allEnabledStars()
//
//        vote.setImageResource(R.drawable.vote_green)
//
//        var ref = database.getReference("user")
//            .child(Singleton.instance.mail.toString().replace(".", "dot"))
//            .child("films").child(name)
//
//        ref.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.value != null) {
//                    var list = snapshot.value as MutableList<String>
//                    if (list[5].toInt() == 0) {
//                    list[5] = count.toString()
//                    var d = (list[4].toDouble() + list[5].toDouble()) / (list[6].toDouble() + 1)
//                    var k = list[6].toInt() + 1
//                    list[6] = k.toString()
//                    list[4] = d.toString()
//                    ref.setValue(list)
//
//                    var ref2 = database.getReference("film_titles").child(name)
//                    ref2.setValue(list)}
//                }
//
//            }
//        })
//    }
//
//
//    private fun bigDvijPoOcenkam2() {
//
//        var ref = database.getReference("film_titles").child(name)
//
//        ref.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.value != null) {
//                    var list = snapshot.value as MutableList<String>
//                    list[5] = count.toString()
//                    var d = (list[4].toDouble() + list[5].toDouble()) / (list[6].toDouble() + 1)
//                    var k = list[6].toInt() + 1
//                    list[6] = k.toString()
//                    list[4] = d.toString()
//                    ref.setValue(list)
//                }
//
//            }
//        })
//    }
//

}