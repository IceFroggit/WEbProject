package com.example.webproject

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.webproject.databinding.FragmentSecondBinding

private const val NAME_INDEX = "name"
private const val ADDRESS_INDEX = "address"
private const val AGE_INDEX = "age"
private const val PHONE_INDEX = "phone"
private const val CONDITIONS_INDEX = "address"
private const val GENDER_INDEX = "gender"


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {



    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    private var genderBool = false
    private var cookie: CookieCache? = null
    private var name =""

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

        val sharedPref = this.getActivity()?.getSharedPreferences("myPref", MODE_PRIVATE)
        val editor = sharedPref?.edit()

        genderBool = conditionsCheckBox.isChecked
        val maleRadioButton = binding.male
        val femaleRadioButton = binding.female
        if(savedInstanceState != null){
            nameEditText.text = Editable.Factory.getInstance().newEditable(sharedPref?.getString("name","DEFAULT_NAME"))
            ageEditText.text = Editable.Factory.getInstance().newEditable("20")
            phoneEditText.text = Editable.Factory.getInstance().newEditable("89992431186")
        }



        //binding.names.text = Editable.Factory.getInstance().newEditable(savedInstanceState?.getString(
           // NAME_INDEX,"").toString())





        maleRadioButton.setOnClickListener(radioButtonClickListener)
        femaleRadioButton.setOnClickListener(radioButtonClickListener)

        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
        binding.button.setOnClickListener {
            if(null == null){
                if (conditionsCheckBox.isChecked) {

                    editor?.putString("username",nameEditText.text.toString())
                    editor!!.commit()
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
            else{

            }
        }
    }

    override fun onPause() {
        super.onPause()
        onSaveInstanceState(Bundle())
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(AGE_INDEX, binding.age.text.toString().toInt())
        outState.putString(NAME_INDEX, "sdfsdffdfdsfsd")
        outState.putString(PHONE_INDEX, binding.phone.text.toString())
        outState.putString(ADDRESS_INDEX, binding.address.text.toString())
        outState.putBoolean(GENDER_INDEX, genderBool)
        outState.putBoolean(CONDITIONS_INDEX, binding.conditions.isChecked)
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