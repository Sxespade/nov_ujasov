package com.horrors.newhorror

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.get
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.horrors.newhorror.navigation.Screens
import kotlinx.android.synthetic.main.activity_main.*
import moxy.MvpAppCompatActivity
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import javax.inject.Inject


class MainActivity : MvpAppCompatActivity(), MainView {

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    lateinit var router: Router

    private var googleSignInClient: GoogleSignInClient? = null

    private val navigator: Navigator =
        SupportAppNavigator(this, supportFragmentManager, R.id.container)


    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    router.replaceScreen(Screens.ProfileScreen())
                    return@OnNavigationItemSelectedListener true;
                }
                R.id.navigation_notifications -> {
                    router.replaceScreen(Screens.LoadFriends())
                    return@OnNavigationItemSelectedListener true;
                }
                R.id.navigation_notificatio -> {
                    router.replaceScreen(Screens.TopFirstQuartalFragment())
                    return@OnNavigationItemSelectedListener true;
                }

                R.id.navigation_notificatio1 -> {
                    router.replaceScreen(Screens.LoadAdminka())
                    return@OnNavigationItemSelectedListener true;
                }

                R.id.navigation_notificatio2 -> {
                    router.replaceScreen(Screens.ProfileFragment())
                    return@OnNavigationItemSelectedListener true;
                }


            }
            false
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        App.instance.appComponent.inject(this)
        bottom_nav_view.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)


        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()


        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    override fun onStart() {
        super.onStart()
        if (intent.extras != null && intent.extras!!["1"] == "1") {
            Thread.sleep(600)
            bottom_nav_view.menu[4].isChecked = true
            signOut()
            router.replaceScreen(Screens.ProfileFragment())
        } else if (intent.extras != null && intent.extras!!["2"] == "2") {router.replaceScreen(Screens.LoadAdminka())} else {router.replaceScreen(Screens.ProfileScreen())}

        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            // Пользователь уже входил, сделаем кнопку недоступной
            // Обновим почтовый адрес этого пользователя и выведем его на экран
            Singleton.instance.mail = account.email
        }
    }



    private fun signOut() {
        googleSignInClient!!.signOut()
            .addOnCompleteListener(this) {
            Singleton.instance.mail = null
            }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.removeNavigator()
    }

    override fun onBackPressed() {
//        super.onBackPressed()
//        for (fragment in supportFragmentManager.fragments) {
//            if (fragment is BackButtonListener && (fragment as BackButtonListener).backPressed()) {
//                return
//            }
//        }
//        presenter.backClicked()
    }


    fun toast() {
        runOnUiThread { Toast.makeText(this, "DDDDD", Toast.LENGTH_SHORT) }
    }





}