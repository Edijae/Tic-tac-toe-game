package com.samuel.tictac

import com.bumptech.glide.Glide
import com.samuel.tictac.di.DaggerAppComponent
import com.samuel.tictac.utilis.Constants
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication


class AppApplication : DaggerApplication() {


    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().build()
    }

    override fun onCreate() {
        super.onCreate()
        downloadImagesIntoDiskCache(Constants.O_MARK)
        downloadImagesIntoDiskCache(Constants.X_MARK)
    }

    private fun downloadImagesIntoDiskCache(url: String) {
        Glide.with(applicationContext)
            .downloadOnly().load(url).submit()

    }
}