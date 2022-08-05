package com.samuel.tictac.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider.getUriForFile
import androidx.core.view.drawToBitmap
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.samuel.tictac.activities.MainActivity
import com.samuel.tictac.adapters.GridAdapter
import com.samuel.tictac.adapters.PlayerListener
import com.samuel.tictac.databinding.FragmentGameBinding
import com.samuel.tictac.databinding.MessageDialogBinding
import com.samuel.tictac.utilis.Constants
import com.samuel.tictac.utilis.StorageUtils
import com.samuel.tictac.viewmodels.MainViewModel
import dagger.android.support.DaggerFragment
import java.io.File
import java.io.FileOutputStream
import java.util.*
import javax.inject.Inject


class GameFragment : DaggerFragment(), PlayerListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var adapter: GridAdapter

    private lateinit var binding: FragmentGameBinding

    private lateinit var mainActivity: MainActivity

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(mainActivity, viewModelFactory)[MainViewModel::class.java]
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        var granted = false
        for (entry in permissions.entries) {
            granted = entry.value
            if (!granted) {
                break
            }
        }
        if (granted) {
            takeScreenshot()
        } else {
            Toast.makeText(
                mainActivity,
                "Access to the device storage is required to share your result",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = activity as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.onBackPressedDispatcher.addCallback {
            viewModel.lastPlayer = ""
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rv.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                } else {
                    view.viewTreeObserver.removeGlobalOnLayoutListener(this)
                }
                val height = binding.rv.height

                adapter = GridAdapter(this@GameFragment, height / 3, viewModel.moves)
                binding.rv.layoutManager = GridLayoutManager(context, 3)
                binding.rv.adapter = adapter
                binding.textView.text = "${viewModel.nextPlayer}'s turn"
                if (viewModel.restored) {
                    adapter.updateMoves()
                }
            }
        })

    }

    override fun getCurrentPlayer(): String {
        return viewModel.nextPlayer
    }

    override fun switchPlayers() {
        val player = viewModel.nextPlayer
        if (player == Constants.PLAYER_O) {
            viewModel.nextPlayer = Constants.PLAYER_X
        } else if (player == Constants.PLAYER_X) {
            viewModel.nextPlayer = Constants.PLAYER_O
        }
        viewModel.lastPlayer = player
        binding.textView.text = "${viewModel.nextPlayer}'s turn"
    }

    override fun win(name: String) {
        val message = "$name wins"
        binding.textView.text = message
        viewModel.resetData()
        StorageUtils.clearGameData(mainActivity)
        showDialog(message)
    }

    override fun draw() {
        viewModel.resetData()
        StorageUtils.clearGameData(mainActivity)
        showDialog("A draw")
    }

    private fun showDialog(message: String) {
        val binding = MessageDialogBinding.inflate(layoutInflater, null, false)
        val builder = AlertDialog.Builder(mainActivity)
            .setView(binding.root)

        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        binding.messageTv.text = message
        binding.btn.setOnClickListener {
            dialog.dismiss()
            val action = GameFragmentDirections.actionGameFragmentToMainFragment()
            findNavController().navigate(action)
        }

        binding.share.setOnClickListener {
            if (verifyStoragePermissions()) {
                takeScreenshot()
            }
        }
        dialog.show()
    }

    private fun takeScreenshot() {
        val date = Date()
        val format: CharSequence = DateFormat.format("yyyy-MM-dd_hh:mm:ss", date)
        try {
            val imagePath = File(mainActivity.externalCacheDir, "pics")
            if (!imagePath.exists()) {
                imagePath.mkdirs()
            }
            val newFile = File(imagePath, "tictac-$format.jpeg")

            val bitmap = binding.rv.drawToBitmap()
            val outputStream = FileOutputStream(newFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()

            val contentUri: Uri =
                getUriForFile(mainActivity, "${mainActivity.packageName}.fileprovider", newFile)

            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "image/*"
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
            shareIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

            startActivity(Intent.createChooser(shareIntent, "Share your result using"))

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(
                mainActivity, "An error occurred sharing your result.", Toast.LENGTH_SHORT
            ).show()
        }
    }

    // verifying storage permission
    private fun verifyStoragePermissions(): Boolean {
        val permissions = ActivityCompat.checkSelfPermission(
            mainActivity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        // If storage permission is not given then request for External Storage Permission
        if (permissions != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Constants.STORAGE_PERMISSIONS)
            return false
        }
        return true
    }

    override fun onStop() {
        super.onStop()
        if (!adapter.gameCompleted()) {
            StorageUtils.storeData(mainActivity, viewModel.lastPlayer, viewModel.moves)
        }
    }
}