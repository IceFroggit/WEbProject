package com.example.webproject

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.webproject.api.ApiRequest
import com.example.webproject.databinding.FragmentFirstBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        getCurrentData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun getCurrentData(){
        binding.textviewFirst.visibility = View.INVISIBLE
        binding.progressBar.visibility = View.VISIBLE
        val api:ApiRequest = Retrofit.Builder()
            .baseUrl("https://catfact.ninja")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiRequest::class.java)

        GlobalScope.launch(Dispatchers.IO){
            val response: Response<Catfact> = api.getQuestListItem().awaitResponse()
            if (response.isSuccessful){
                val data: Catfact = response.body()!!
                Log.d("TAG",data.message)

                withContext(Dispatchers.Main){
                    binding.textviewFirst.text = data.message
                    binding.textviewFirst.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE

                }
            }
        }
    }
}