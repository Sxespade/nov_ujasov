package com.horrors.myapplication.di.module

import com.horrors.newhorror.MainActivity
import com.horrors.newhorror.SerialsAdapter3
import com.horrors.newhorror.di.module.AppModule
import com.horrors.newhorror.di.module.DatabaseModule
import com.horrors.newhorror.view.LoadFilmInsert
import com.horrors.newhorror.view.ProfileFragment
import com.horrors.newhorror.view.TopsFragment
import com.horrors.serialgraphicinteres.di.module.CiceroneModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class,
        CiceroneModule::class,
        DatabaseModule::class]
)
interface AppComponent {
//    fun repositorySubcomponent() : RepositorySubcomponent

    fun inject(mainActivity: MainActivity)
    fun inject(topsFragment: TopsFragment)
    fun inject(profileFragment: ProfileFragment)
    fun inject(loadFilmInsert: LoadFilmInsert)
    fun inject(serialsAdapter3: SerialsAdapter3)
}