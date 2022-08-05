package com.samuel.tictac.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.samuel.tictac.activities.MainActivity
import com.samuel.tictac.databinding.FragmentMainBinding
import com.samuel.tictac.utilis.Constants
import com.samuel.tictac.viewmodels.MainViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class MainFragment : DaggerFragment(), View.OnClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentMainBinding
    private lateinit var mainActivity: MainActivity

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(mainActivity, viewModelFactory)[MainViewModel::class.java]
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = activity as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.startO.setOnClickListener(this)
        binding.startX.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        viewModel.nextPlayer = if (view.id == binding.startX.id) {
            Constants.PLAYER_X
        } else {
            Constants.PLAYER_O
        }
        val action = MainFragmentDirections.actionMainFragmentToGameFragment()
        view.findNavController().navigate(action)
    }
}