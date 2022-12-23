package com.example.webproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.webproject.databinding.ActivityDashBoardBinding
import com.example.webproject.databinding.ActivitySignInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class DashBoardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashBoardBinding
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        binding.idTxt.text = currentUser?.uid
        binding.nameTxt.text = currentUser?.displayName
        binding.emailTxt.text = currentUser?.email
        Glide.with(this).load(currentUser?.photoUrl).into(binding.profilePic)

        binding.signout.setOnClickListener { signOut() }
        binding.continueBtn.setOnClickListener{
            val intent = Intent(this,DataActivity::class.java)
            startActivity(intent)
            finish()

        }
    }
    private fun signOut(){
        mAuth.signOut()
            val intent= Intent(this, SignInActivity::class.java)
            intent.putExtra("logOut",true);
            startActivity(intent)
            finish()
    }
}