package com.horrors.newhorror.view

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.drawToBitmap
import androidx.core.view.isVisible
import com.horrors.newhorror.GlideImageLoader.GlideImageLoader
import com.horrors.newhorror.MainActivity
import com.horrors.newhorror.R
import com.horrors.newhorror.Singleton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import moxy.MvpAppCompatFragment
import ru.terrakok.cicerone.Router
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.net.URLEncoder
import javax.inject.Inject


class ProfileFragment : MvpAppCompatFragment() {

    @Inject
    lateinit var router: Router

    private val PICK_IMAGE = 0
    private val RC_SIGN_IN = 40404
    private val TAG = "GoogleAuth"
    val GSTORAGE = "gs://goodhorror-11922.appspot.com/"
    private val imageLoader = GlideImageLoader()
    lateinit var avat: ImageView
    var s :String? = null
    // Клиент для регистрации пользователя через Google
    private var googleSignInClient: GoogleSignInClient? = null

    fun getInstance(): ProfileFragment? {
        return ProfileFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }


    override fun onStart() {
        super.onStart()
        textNotLogIn.isVisible = false
        if (Singleton.instance.mail == null) {
            textNotLogIn.isVisible = true
            load.isEnabled = false
        }

        Log.d("dddddddddddddddddd", "onStart: " + Singleton.instance.mail)
        s = URLEncoder.encode(Singleton.instance.mail.toString(), "utf-8") as String
        avat = view!!.findViewById<ImageView>(R.id.avat)
        nick.text = Singleton.instance.givenName.toString()
        mail.text = Singleton.instance.mail.toString()
        fullName.text = Singleton.instance.displayName.toString()
        val email: String = Singleton.instance.mail.toString()
        val storageRef = FirebaseStorage.getInstance().reference
        var riversRef = storageRef.child("avatars").child(email + "/avatar")
        Picasso.with(context)
            .invalidate("https://firebasestorage.googleapis.com/v0/b/goodhorror-11922.appspot.com/o/avatars%2F$s%2Favatar?alt=media&token=040f8ca5-eafa-44aa-80bd-5079b6bc7aa0")

        riversRef.downloadUrl.addOnSuccessListener {
            Picasso.with(context)
                .load("https://firebasestorage.googleapis.com/v0/b/goodhorror-11922.appspot.com/o/avatars%2F$s%2Favatar?alt=media&token=040f8ca5-eafa-44aa-80bd-5079b6bc7aa0")
                .into(avat)
        }.addOnFailureListener { avat.setImageResource(R.drawable.avat) }


        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        extended_fab.setOnClickListener {
            googleSignInClient!!.signOut()
                .addOnCompleteListener(requireActivity()) {
                }

            var intent = Intent(requireContext(), SignInActivity::class.java)
            startActivity(intent)
        }

        mail.text = Singleton.instance.mail.toString()

        load.setOnClickListener {
            val getIntent = Intent(Intent.ACTION_GET_CONTENT)
            getIntent.type = "image/*"

            val pickIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            pickIntent.type = "image/*"

            val chooserIntent = Intent.createChooser(getIntent, "Select Image")

            startActivityForResult(chooserIntent, PICK_IMAGE)
        }

    }


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if (data != null) {
            avat.setImageURI(data.data)
            var bitmap = avat.drawToBitmap()
            val stream = ByteArrayOutputStream()
            bitmap = Bitmap.createScaledBitmap(bitmap, 132, 176, false)
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
            val byteArray: ByteArray = stream.toByteArray()
            bitmap.recycle()
            initPutFileOnStorage(byteArray)
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.putExtra("1", "1")
            startActivity(intent)
        }


    }

    private fun initPutFileOnStorage(byteArray: ByteArray) {
        val email: String = Singleton.instance.mail.toString()
        val mStorageRef = FirebaseStorage.getInstance().reference
        var riversRef = mStorageRef.child("avatars").child(email + "/avatar")
        val bis = ByteArrayInputStream(byteArray)
        val uploadTask: UploadTask = riversRef.putStream(bis)
        uploadTask.addOnFailureListener {}
            .addOnSuccessListener {
            }
    }
}




