package com.ekochkov.burnthiscalories.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import com.ekochkov.burnthiscalories.data.entity.Profile
import com.ekochkov.burnthiscalories.databinding.FragmentProfileBinding
import com.ekochkov.burnthiscalories.util.Constants
import com.ekochkov.burnthiscalories.viewModel.ProfileFragmentViewModel
import kotlinx.coroutines.launch

class ProfileFragment: Fragment() {

    lateinit var binding: FragmentProfileBinding

    private val viewModel: ProfileFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycle.coroutineScope.launch{
            viewModel.prodileLiveData.observe(viewLifecycleOwner) { profile ->
                profile?.apply {
                    binding.heightEditText.setText(height)
                    binding.weightEditText.setText(weight)
                    binding.ageEditText.setText(age.toString())
                    binding.nameEditText.setText(name)
                    binding.sexTypeRadioGroup.check(
                        when(sex) {
                            Profile.MALE -> {
                                binding.maleRbtn.id}
                            else -> {
                                binding.femaleRbtn.id}
                        }
                    )
                }
            }
        }

        binding.acceptBtn.setOnClickListener {
            if (isProfileFilled()) {
                val sex = when (binding.sexTypeRadioGroup.checkedRadioButtonId) {
                    binding.maleRbtn.id -> Profile.MALE
                    else -> Profile.FEMALE
                }
                val profile = Profile(
                    name = binding.nameEditText.text.toString(),
                    height = binding.heightEditText.text.toString(),
                    weight = binding.weightEditText.text.toString(),
                    age = binding.ageEditText.text.toString().toInt(),
                    sex = sex)
                viewModel.saveProfile(profile)
            } else {
                showToast(Constants.FIELDS_NOT_FILLED)
            }
        }
    }

    private fun isProfileFilled(): Boolean {
        return !(binding.heightEditText.text.isNullOrEmpty() &&
                binding.weightEditText.text.isNullOrEmpty() &&
                binding.ageEditText.text.isNullOrEmpty() &&
                binding.nameEditText.text.isNullOrEmpty())
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }
}