package com.example.androidflier.ui.profile

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.androidflier.R
import com.example.androidflier.databinding.FragmentProfileBinding
import com.example.androidflier.model.SettingsSearch
import com.example.androidflier.ui.viewmodels.SingleEntityModelFactory

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var settingsModel: SettingsViewModel
    private lateinit var tagEdit: EditText
    private lateinit var timeSpinner: Spinner
    private lateinit var radiusSpinner: Spinner
    private lateinit var switchOnOf: Switch
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var settings: SettingsSearch
    private lateinit var settingsObserver: Observer<SettingsSearch>

    private val requestFeatureLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
        ::onGotPermissionsResultForFeature
    )

    companion object {
        const val TAG = "profile fragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        settings = SettingsSearch()

        settingsModel = ViewModelProvider(
            requireActivity(),
            SingleEntityModelFactory(0, requireActivity().application) // 0 или любой int
        ).get(TAG, SettingsViewModel::class.java)



        setSpinners()
        setEditTags()
        setObservers()

        settingsModel.getSettings()

        setSwitch()

        return binding.root
    }

    private fun setEditTags() {
        tagEdit = binding.tagEditText
    }

    private fun setSwitch() {
        switchOnOf = binding.switchOnOff

        switchOnOf.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                Log.d(TAG, "${switchOnOf.isChecked} switch")
                if (isChecked) requestFeatureLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)

                  hideOrShowSettings()
                stopOrStartShopWorker(isChecked)
            }
        })
    }

    private fun stopOrStartShopWorker(toOn: Boolean) {
        settingsModel.stopOrStartShopWorker(toOn)
    }

    private fun hideOrShowSettings() {
        Log.d(TAG, "${switchOnOf.isChecked} hide settings")
        timeSpinner.isEnabled = switchOnOf.isChecked
        radiusSpinner.isEnabled = switchOnOf.isChecked
        tagEdit.isEnabled = switchOnOf.isChecked
    }

    private fun setObservers() {
        settingsObserver = Observer {
            Log.d(TAG, "${switchOnOf.isChecked} observ")
            switchOnOf.isChecked = it.on
            radiusSpinner.setSelection(it.radius)
            timeSpinner.setSelection(it.timePeriod)
            hideOrShowSettings()
            settings.on = it.on
            settings.radius = it.radius
            settings.timePeriod = it.timePeriod

            tagEdit.text.clear()

            for (tag in it.listTag) {
                tagEdit.text.append(tag + " ")
            }
        }

        settingsModel.settings.observe(viewLifecycleOwner, settingsObserver)
    }

    private fun setSpinners() {
        radiusSpinner = binding.radiusCheckSpinner
        timeSpinner = binding.timeCheckSpinner

        ArrayAdapter
            .createFromResource(
                requireContext(),
                R.array.timeSpinner,
                android.R.layout.simple_spinner_item
            )
            .also { arrayAdapter ->
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                timeSpinner.adapter = arrayAdapter
            }

        ArrayAdapter
            .createFromResource(
                requireContext(),
                R.array.radiusSpinner,
                android.R.layout.simple_spinner_item
            )
            .also { arrayAdapter ->
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                radiusSpinner.adapter = arrayAdapter
            }
    }

    override fun onPause() {
        super.onPause()
        Log.d("onPause", switchOnOf.isChecked.toString())

        settings.radius = radiusSpinner.selectedItemPosition
        settings.timePeriod = timeSpinner.selectedItemPosition
        settings.on = switchOnOf.isChecked

        val text = tagEdit.text.trim().split(" ")
        settings.listTag = text

        settingsModel.saveSettings(settings)
    }

    override fun onDestroy() {
        super.onDestroy()
        settingsModel.settings.removeObserver(settingsObserver)
    }

    private fun onGotPermissionsResultForFeature(grantedResult: Boolean) {
        if (grantedResult) {
            stopOrStartShopWorker(switchOnOf.isChecked)
        } else {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.d("perm", "not gra forever")
                //если запретили навсегда
                switchOnOf.isChecked = false
                askUserForOpeningSettings()
            } else {
                Log.d("perm", "not gra NOT forever")
                switchOnOf.isChecked = false
                showDialogWithExplanationAndRequest()
            }
        }

        hideOrShowSettings()
    }

    private fun askUserForOpeningSettings() {
        val appsettingIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", requireActivity().packageName, null)
        )

        if (requireActivity().packageManager.resolveActivity(
                appsettingIntent,
                PackageManager.MATCH_DEFAULT_ONLY
            ) == null
        ) {
            Toast.makeText(requireContext(), "permissions are denied forever :(", Toast.LENGTH_LONG)
                .show()
        } else {
            AlertDialog.Builder(requireContext()).setTitle("Permissions denied")
                .setMessage("Would you like to open settings?")
                .setPositiveButton("open") { _, _ ->
                    startActivity(appsettingIntent)
                }
                .create()
                .show()
        }
    }

    private fun showDialogWithExplanationAndRequest() {
        Toast.makeText(requireContext(), "permissions are need for works", Toast.LENGTH_LONG)
            .show()
    }
}