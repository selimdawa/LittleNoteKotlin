package com.flatcode.littlenote.Auth

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.flatcode.littlenote.R
import com.flatcode.littlenote.Unit.CLASS
import com.flatcode.littlenote.Unit.DATA
import com.flatcode.littlenote.Unit.THEME
import com.flatcode.littlenote.Unit.VOID
import com.flatcode.littlenote.databinding.ActivityRegisterBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import java.util.Objects

class Register : AppCompatActivity() {

    private var binding: ActivityRegisterBinding? = null
    var auth: FirebaseAuth? = null
    private var dialog: ProgressDialog? = null
    private val context: Context = this@Register

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        VOID.Logo(baseContext, binding!!.logo)
        VOID.Intro(baseContext, binding!!.background, binding!!.backWhite, binding!!.backBlack)

        dialog = ProgressDialog(this)
        dialog!!.setTitle("Please wait...")
        dialog!!.setCanceledOnTouchOutside(false)
        auth = FirebaseAuth.getInstance()

        binding!!.login.setOnClickListener { VOID.Intent1(context, CLASS.LOGIN) }
        binding!!.go.setOnClickListener {
            dialog!!.setMessage("A new account is created...")

            val Username = binding!!.nameEt.text.toString()
            val UserEmail = binding!!.emailEt.text.toString()
            val UserPass = binding!!.cPasswordEt.text.toString()
            val ConfirmPass = binding!!.passwordEt.text.toString()

            if (UserEmail.isEmpty() || Username.isEmpty() || UserPass.isEmpty() || ConfirmPass.isEmpty()) {
                Toast.makeText(context, R.string.empty_required_all, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (UserPass != ConfirmPass) {
                binding!!.passwordEt.error = DATA.ERR_PASS
            } else {
                dialog!!.show()
                val credential = EmailAuthProvider.getCredential(UserEmail, UserPass)
                Objects.requireNonNull(auth!!.currentUser)!!.linkWithCredential(credential)
                    .addOnSuccessListener {
                        Toast.makeText(context, R.string.notes_synced, Toast.LENGTH_SHORT).show()
                        VOID.Intent1(context, CLASS.HOME)
                        val usr = auth!!.currentUser
                        val request =
                            UserProfileChangeRequest.Builder().setDisplayName(Username).build()
                        usr!!.updateProfile(request)
                        VOID.Intent1(context, CLASS.HOME)
                        overridePendingTransition(R.anim.slide_up, R.anim.slide_down)
                        finish()
                    }.addOnFailureListener {
                        Toast.makeText(context, R.string.failed_connect, Toast.LENGTH_SHORT).show()
                        dialog!!.dismiss()
                    }
            }
        }
    }
}