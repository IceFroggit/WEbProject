package com.example.webproject

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.webproject.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.example.webproject.databinding.ActivitySignInBinding
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser

@Suppress("DEPRECATION")
class SignInActivity : AppCompatActivity() {
    private lateinit var  mAuth:FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var task:Task<GoogleSignInAccount>

    private lateinit var binding: ActivitySignInBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var extras: Bundle? = getIntent().getExtras()

        //конфигурация входа в Google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("168741599364-ududdmtgjdvdvmtir42vi10rvosp7dp3.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this,gso)

        if(extras != null){
            googleSignInClient.signOut().addOnCompleteListener {}
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("168741599364-ududdmtgjdvdvmtir42vi10rvosp7dp3.apps.googleusercontent.com")
                .requestEmail()
                .build()
            googleSignInClient = GoogleSignIn.getClient(this,gso)
        }

        //FireBase Auth instance
        mAuth = FirebaseAuth.getInstance()
        binding.twitterBtn.setOnClickListener{
            var email = binding.username.text.toString()
            var password = binding.password.text.toString()
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = mAuth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }
                }
        }
        binding.googleBtn.setOnClickListener{
            signIn()
        }
        binding.loginbtn.setOnClickListener{
            var email = binding.username.text.toString()
            var password = binding.password.text.toString()
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = mAuth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }
                }



        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //Result returned from launching the Intent From GoogleSignInApi.getSignIntent()
        if(requestCode == RC_SIGN_IN){
            task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if(task.isSuccessful){
                try {
                    //Google Sign In was succesful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)
                    Log.d(TAG,"firebaseAuthWithGoogle: "+ account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                }catch (e:ApiException){
                    Log.w(TAG,"Google sign in failed")
                }
            }else
                Log.w(TAG,exception.toString())

        }
    }
    private fun firebaseAuthWithGoogle(idToken:String){
        val credential = GoogleAuthProvider.getCredential(idToken,null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this){task->
                if(task.isSuccessful){
                    //Sign in success, update UI with the signed-in user's information
                    Log.d(TAG,"signInwithCredential:success")
                    val intent  = Intent(this,DashBoardActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else{
                    //if Sign in fails,display exception message
                    Log.w(TAG,"signInwithCredential:failure",task.exception)
                }
            }

    }
    private fun signIn(){
        val signIntent = googleSignInClient.signInIntent
        startActivityForResult(signIntent,RC_SIGN_IN)
    }
    private fun createUser(){
        var email = binding.username.text.toString()
        var password = binding.password.text.toString()
        if(TextUtils.isEmpty(email)){
            binding.username.setError("Email cannot be empty")
            binding.username.requestFocus()
        }else if(password.isEmpty()){
            binding.password.setError("Password cannot be empty")
            binding.password.requestFocus()
        }else{
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (task.isSuccessful){
                    val intent  = Intent(this,DashBoardActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
    private fun updateUI(user:FirebaseUser?){
        if(user != null){
        val intent = Intent(this,DashBoardActivity::class.java)
        startActivity(intent)
        }else
            Toast.makeText(baseContext, "Authentication failed.",
                Toast.LENGTH_SHORT).show()

    }

    companion object{
        const val RC_SIGN_IN = 120
        const val EXTRA_NAME = "EXTRA NAME"
    }
}