package com.example.cinemapp.ui.main.settings.about

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.cinemapp.R
import com.example.cinemapp.databinding.FragmentSettingsAboutBinding
import com.example.cinemapp.ui.main.MainActivity
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


class SettingsAboutFragment : Fragment(){

    private var _binding: FragmentSettingsAboutBinding? = null
    private val binding get() = _binding!!
    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openUserManual(requireContext())
            } else {
                Toast.makeText(
                    activity as MainActivity,
                    getString(R.string.permission_denied_download),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsAboutBinding.inflate(inflater, container, false)
        setupMainToolbar()
        setupVersionNumber()
        setupOnClick()
        return binding.root
    }

    private fun setupMainToolbar() {
        (activity as? MainActivity)?.customizeTopNavigation(
            title = resources.getString(R.string.title_settings_about),
            navigationIconId = R.drawable.vic_arrow_back,
            isTitleCentered = false
        )
    }

    private fun setupVersionNumber() {
        activity?.let {
            binding.siVersion.setValue(it.packageManager.getPackageInfo(it.packageName, 0).versionName)
        }
    }

    private fun setupOnClick() {
        binding.btnUserManual.setOnClickListener {
            openUserManual(requireContext())
        }
    }

    private fun openUserManual(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
            ) {
                activityResultLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                return
            }
        }
        val assetManager: AssetManager = context.assets
        val files: List<String>?
        var outputDir: File? = null
        try {
            files = assetManager.list("")?.filter { path -> path.contains(USER_MANUAL, false) }

            if (files != null) for (filename in files) {
                var inStream: InputStream? = null
                var outStream: OutputStream? = null
                try {
                    inStream = assetManager.open(filename)
                    val outFile =
                        File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename)
                    outStream = FileOutputStream(outFile)
                    copyFile(inStream, outStream)
                    outputDir = outFile
                } catch (e: IOException) {
                    Log.e("tag", "Failed to copy asset file: $filename", e)
                } finally {
                    if (inStream != null) {
                        try {
                            inStream.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                    if (outStream != null) {
                        try {
                            outStream.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
            outputDir?.let {
                val uri = FileProvider.getUriForFile(
                    requireContext(),
                    context.applicationContext?.packageName + ".provider",
                    outputDir
                )
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(uri, "application/pdf")
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                startActivity(intent)
            }
        } catch (e: IOException) {
            Log.e("tag", "Failed to get asset file list.", e)
        }

    }

    @Throws(IOException::class)
    private fun copyFile(inStream: InputStream?, outStream: OutputStream) {
        outStream.use { fileOut ->
            inStream?.copyTo(fileOut)
        }
    }

    companion object {
        const val USER_MANUAL = "User-Manual.pdf"
    }
}