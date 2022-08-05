package com.samuel.tictac.di

import com.samuel.tictac.AppApplication
import com.samuel.tictac.di.modules.ActivitiesModule
import com.samuel.tictac.di.modules.FragmentsModule
import com.samuel.tictac.di.modules.ViewModelsModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector

@Component(
    modules = [AndroidInjectionModule::class,
        ActivitiesModule::class,
        ViewModelsModule::class, FragmentsModule::class]
)
interface AppComponent : AndroidInjector<AppApplication> {


}