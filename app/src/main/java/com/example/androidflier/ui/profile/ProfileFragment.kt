package com.example.androidflier.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Switch
import androidx.fragment.app.Fragment
import com.example.androidflier.R
import com.example.androidflier.databinding.FragmentProfileBinding

class ProfileFragment: Fragment(R.layout.fragment_profile) {

    private lateinit var tagEdit: EditText
    private lateinit var timeSpinner: Spinner
    private lateinit var radiusSpinner: Spinner
    private lateinit var switchOnOf: Switch
    private  var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        switchOnOf = binding.switchOnOff
        radiusSpinner = binding.radiusCheckSpinner
        timeSpinner = binding.timeCheckSpinner
        tagEdit = binding.tagEditText

        setSpinners()
        return binding.root
    }

    private fun setSpinners() {
        ArrayAdapter
            .createFromResource(requireContext(),R.array.timeSpinner, android.R.layout.simple_spinner_item)
            .also {
                arrayAdapter -> arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                timeSpinner.adapter = arrayAdapter
            }

        ArrayAdapter
            .createFromResource(requireContext(),R.array.radiusSpinner, android.R.layout.simple_spinner_item)
            .also {
                    arrayAdapter -> arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                radiusSpinner.adapter = arrayAdapter
            }
    }
}