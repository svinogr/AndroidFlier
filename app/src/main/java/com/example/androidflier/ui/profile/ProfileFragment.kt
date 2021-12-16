package com.example.androidflier.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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

        setSpinners()
        setSwitch()
        setEditTags()

        settingsModel = ViewModelProvider(
            requireActivity(),
            SingleEntityModelFactory(0, requireActivity().application) // 0 или любой int
        ).get(TAG, SettingsViewModel::class.java)

        setObservers()

        return binding.root
    }

    private fun setEditTags() {
        tagEdit = binding.tagEditText

   /*     tagEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.trim().isNotEmpty()) {
                    settings.listTag.clear()
                    settings.listTag = s.split(" ") as MutableList<String>
                } else {
                    settings.listTag.clear()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })*/
    }


    private fun setSwitch() {
        switchOnOf = binding.switchOnOff

        switchOnOf.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                settings.on = isChecked
            }
        })
    }


    private fun setObservers() {

        settingsObserver = Observer {
            switchOnOf.isChecked = it.on
            radiusSpinner.setSelection(it.radius)
            timeSpinner.setSelection(it.timePeriod)

            settings.on = it.on
            settings.radius = it.radius
            settings.timePeriod = it.timePeriod

            tagEdit.text.clear()
          //  settings.listTag.clear()
            Log.d(TAG, tagEdit.text.toString())
            Log.d(TAG, settings.listTag.toString())

            for (tag in it.listTag) {
                tagEdit.text.append(tag + " ")
             //   settings.listTag.add(tag)
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

        timeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.d("TIME", parent?.getItemAtPosition(position).toString())
                settings.timePeriod = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        radiusSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.d("TIME", parent?.getItemAtPosition(position).toString())
                settings.radius = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

    }

    override fun onPause() {
        super.onPause()
       // settings.listTag.clear()
        val text = tagEdit.text.trim()
        if (text.isNotEmpty()) {
            settings.listTag.clear()
            settings.listTag = text.split(" ") as MutableList<String>
        } else {
            settings.listTag.clear()
        }

        settingsModel.saveSettings(settings)
    }

    override fun onDestroy() {
        super.onDestroy()
        settingsModel.settings.removeObserver(settingsObserver)
    }
}