package com.samuel.tictac.di.modules

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.samuel.tictac.di.DaggerFragmentFactory
import com.samuel.tictac.di.FragmentKey
import com.samuel.tictac.fragments.GameFragment
import com.samuel.tictac.fragments.MainFragment
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class FragmentsModule {

    @Binds
    abstract fun bindFragmentFactory(
        factory: DaggerFragmentFactory
    ): FragmentFactory

    @ContributesAndroidInjector
    abstract fun contributeMainFragment(): MainFragment

    @ContributesAndroidInjector
    abstract fun contributeGameFragment(): GameFragment

    @Binds
    @IntoMap
    @FragmentKey(MainFragment::class)
    abstract fun bindMainFragment(fragment: MainFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(GameFragment::class)
    abstract fun bindGameFragment(fragment: GameFragment): GameFragment
}