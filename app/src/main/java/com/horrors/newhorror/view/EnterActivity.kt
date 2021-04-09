package com.horrors.newhorror.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.horrors.newhorror.MainActivity
import com.horrors.newhorror.Singleton
import com.horrors.newhorror.databinding.ActivityEnterBinding


class EnterActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 40404
    private val TAG = "GoogleAuth"
    private lateinit var mAuth: FirebaseAuth

    var binding: ActivityEnterBinding? = null

    // Клиент для регистрации пользователя через Google
    private var googleSignInClient: GoogleSignInClient? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.horrors.newhorror.R.layout.activity_enter)


        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser

        Handler().postDelayed({
            if(user != null) {
                Singleton.instance.mail = mAuth.currentUser!!.email
                Singleton.instance.displayName = mAuth.currentUser!!.displayName
                Singleton.instance.givenName = mAuth.currentUser!!.displayName
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this,SignInActivity::class.java)
                startActivity(intent)
                finish()
            }
        },1100)

    }
}
