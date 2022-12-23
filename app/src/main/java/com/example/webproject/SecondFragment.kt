package com.example.webproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.webproject.databinding.FragmentSecondBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    private var genderBool = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val genderCheckBox = binding.radioGroup
        val resultMsg = binding.msg
        val nameEditText = binding.names
        val ageEditText = binding.age
        val phoneEditText = binding.phone
        val addressEditText = binding.address
        val conditionsCheckBox = binding.conditions
        genderBool = conditionsCheckBox.isChecked
        val maleRadioButton = binding.male
        val femaleRadioButton = binding.female

        maleRadioButton.setOnClickListener(radioButtonClickListener)
        femaleRadioButton.setOnClickListener(radioButtonClickListener)

        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
        binding.button.setOnClickListener {
            if (conditionsCheckBox.isChecked) {
                nameEditText.visibility = View.GONE
                ageEditText.visibility = View.GONE
                phoneEditText.visibility = View.GONE
                addressEditText.visibility = View.GONE
                conditionsCheckBox.visibility = View.GONE
                genderCheckBox.visibility = View.GONE
                val gender = if (genderBool) "Мужской"  else "Женcкий"
                val title = binding.title
                title.text = "Регистрация завершена"
                resultMsg.text =
                    nameEditText.text.toString() + " Возраст: " + ageEditText.text.toString() + " Телефон: " + phoneEditText.text.toString() + " Адресс: " + addressEditText.text.toString() + " Пол: " + gender
                resultMsg.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private var radioButtonClickListener = View.OnClickListener { v ->
        when (v.id) {
            R.id.male -> genderBool = true
            R.id.female -> genderBool = false
            else -> {}
        }
    }
}