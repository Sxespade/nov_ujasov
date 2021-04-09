package com.horrors.newhorror.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.horrors.newhorror.App
import com.horrors.newhorror.IUserListPresenter
import com.horrors.newhorror.R
import com.horrors.newhorror.SerialItemView
import com.horrors.newhorror.di.module.Serial
import com.horrors.newhorror.navigation.Screens
import kotlinx.android.synthetic.main.fragment_top.*
import moxy.MvpAppCompatFragment
import ru.terrakok.cicerone.Router
import javax.inject.Inject


class TopsFragment : MvpAppCompatFragment() {

    @Inject
    lateinit var router: Router


    fun getInstance(): TopsFragment? {
        return TopsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onStart() {
        super.onStart()
        App.instance.appComponent.inject(this)
        cccc.setOnClickListener { drawerLayout.openDrawer(GravityCompat.START) }

        nav_draw.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.item1 -> {
                    router.replaceScreen(Screens.TopFirstQuartalFragment())
                    true
                }
                else -> false
            }
        }
    }


    inner class UsersListPresenter :
        IUserListPresenter {
        var users: List<Serial> = mutableListOf()

        override fun onItemClick(view: SerialItemView?) {
            val result = users[view!!.getPos()]
//            Singleton.instance.serial = result
//            router.replaceScreen(Screens.SerialScreen())
//            Singleton.instance.serial = result
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
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        val lp: ViewGroup.LayoutParams = recycleView.layoutParams
        lp.width = ViewGroup.LayoutParams.WRAP_CONTENT
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT
        recycleView.requestLayout()
        recycleView.layoutManager = layoutManager
    }


}
