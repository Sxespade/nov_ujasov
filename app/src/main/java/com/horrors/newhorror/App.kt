package com.horrors.newhorror

import android.app.Application
import com.horrors.myapplication.di.module.AppComponent
import com.horrors.myapplication.di.module.DaggerAppComponent
import com.horrors.newhorror.di.module.AppModule
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class App : Application() {
    companion object {
        lateinit var instance: App
    }

    private var retrofit: Retrofit? = null

    lateinit var appComponent: AppComponent
        private set

//    var userSubcomponent: RepositorySubcomponent? = null
//        private set

    override fun onCreate() {
        super.onCreate()
        instance = this

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()

        initRetrofit()
    }


    private fun initRetrofit() {
        retrofit = Retrofit.Builder()
            .baseUrl("http://api.themoviedb.org/")
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getRetrofit(): Retrofit? {
        return retrofit
    }


//    fun initUserSubcomponent() = appComponent.userSubcomponent().also {
//        userSubcomponent = it
//    }


//    fun initRetrofit() {
//        val gson =
//            GsonBuilder().setFieldNamingStrategy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
//                .excludeFieldsWithoutExposeAnnotation().create()
//        retrofit = Retrofit.Builder()
//            .baseUrl("https://api.github.com/")
//            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
//            .addConverterFactory(GsonConverterFactory.create(gson))
//            .build()
////        dataSource = retrofit.create(IDataSource::class.java)
//    }
//
//    fun getRetrofit(): Retrofit? {
//        return retrofit
//    }



    //
//    fun releaseUserSubcomponent() {
//        userSubcomponent = null
//    }
//
//    fun initRepositorySubcomponent() = userSubcomponent?.repositorySubcomponent().also {
//        repositorySubcomponent = it
//    }
//
//    fun releaseRepositorySubcomponent() {
//        repositorySubcomponent = null
//    }
}