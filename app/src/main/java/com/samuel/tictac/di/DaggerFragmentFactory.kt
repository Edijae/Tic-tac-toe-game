package com.samuel.tictac.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import dagger.MapKey
import javax.inject.Inject
import javax.inject.Provider
import kotlin.reflect.KClass

class DaggerFragmentFactory @Inject constructor(
    private val providers: @JvmSuppressWildcards Map<Class<out Fragment>, Provider<Fragment>>
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        val fragmentClass = loadFragmentClass(classLoader, className)
        val provider = providers[fragmentClass]
        return provider?.get() ?: super.instantiate(classLoader, className)
    }
}

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@MapKey
annotation class FragmentKey(val value: KClass<out Fragment>)