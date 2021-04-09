package com.horrors.newhorror.navigation

import com.horrors.newhorror.view.AdminFragment
import com.horrors.newhorror.view.FriendsFragment
import com.horrors.newhorror.view.MainFragment
import com.horrors.newhorror.view.TopsFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class Screens {
    class ProfileScreen : SupportAppScreen() {
        override fun getFragment() = MainFragment().getInstance()
    }

    class LoadFriends : SupportAppScreen() {
        override fun getFragment() = FriendsFragment().getInstance()
    }

    class LoadTops : SupportAppScreen() {
        override fun getFragment() = TopsFragment().getInstance()
    }

    class LoadAdminka : SupportAppScreen() {
        override fun getFragment() = AdminFragment().getInstance()
    }

    class TopFirstQuartalFragment : SupportAppScreen() {
        override fun getFragment() = com.horrors.newhorror.view.TopFirstQuartalFragment()
    }

    class ProfileFragment : SupportAppScreen() {
        override fun getFragment() = com.horrors.newhorror.view.ProfileFragment()
    }

}
