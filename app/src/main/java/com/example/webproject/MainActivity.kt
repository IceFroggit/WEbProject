package com.example.webproject

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import com.example.webproject.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

@Suppress("DEPRECATION")
class  MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var mAuth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser
        Handler().postDelayed({
            if (user != null){
                val dashboardIntent = Intent(this,DashBoardActivity::class.java)
                startActivity(dashboardIntent)
            }
            else{
                val signInIntent = Intent(this,SignInActivity::class.java)
                startActivity(signInIntent)
                finish()
            }

        },2000)
    }



}