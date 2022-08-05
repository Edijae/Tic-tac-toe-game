package com.samuel.tictac.activities

import android.os.Bundle
import android.provider.Settings
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.samuel.tictac.R
import com.samuel.tictac.databinding.ActivityMainBinding
import com.samuel.tictac.utilis.Constants
import com.samuel.tictac.utilis.StorageUtils
import com.samuel.tictac.viewmodels.MainViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MobileAds.initialize(this) {}
        val mId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        val configuration =
            RequestConfiguration.Builder().setTestDeviceIds(listOf(mId)).build()
        MobileAds.setRequestConfiguration(configuration)

        val adReq = AdRequest.Builder().build()
        binding.adView.loadAd(adReq)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
                as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)

        checkLastGameState()
    }

    private fun checkLastGameState() {
        val player = StorageUtils.getLastPlayer(this)
        if (player.isNotEmpty()) {
            mainViewModel.moves = StorageUtils.getMoves(this)
            mainViewModel.restored = true
            mainViewModel.lastPlayer = player
            mainViewModel.nextPlayer = if (mainViewModel.lastPlayer == Constants.PLAYER_O) {
                Constants.PLAYER_X
            } else {
                Constants.PLAYER_O
            }
            navController.navigate(R.id.GameFragment)
        }
    }

    override fun onBackPressed() {
        if (!navController.popBackStack()) {
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}