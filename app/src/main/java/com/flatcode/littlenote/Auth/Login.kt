package com.flatcode.littlenote.Auth

import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.flatcode.littlenote.R
import com.flatcode.littlenote.Unit.CLASS
import com.flatcode.littlenote.Unit.DATA
import com.flatcode.littlenote.Unit.THEME
import com.flatcode.littlenote.Unit.VOID
import com.flatcode.littlenote.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Objects

class Login : AppCompatActivity() {

    private var binding: ActivityLoginBinding? = null
    var auth: FirebaseAuth? = null
    var store: FirebaseFirestore? = null
    var user: FirebaseUser? = null
    private var dialog: ProgressDialog? = null
    private val context: Context = this@Login

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        VOID.Logo(baseContext, binding!!.logo)
        VOID.Intro(baseContext, binding!!.background, binding!!.backWhite, binding!!.backBlack)

        dialog = ProgressDialog(this)
        dialog!!.setTitle("Please wait...")
        dialog!!.setCanceledOnTouchOutside(false)

        user = FirebaseAuth.getInstance().currentUser
        auth = FirebaseAuth.getInstance()
        store = FirebaseFirestore.getInstance()
        showWarning()

        binding!!.loginBtn.setOnClickListener {
            dialog!!.setMessage("Logging In...")
            val Email = binding!!.emailEt.text.toString()
            val Password = binding!!.passwordEt.text.toString()
            if (Email.isEmpty() || Password.isEmpty()) {
                Toast.makeText(context, R.string.empty_required, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                // delete notes first
                dialog!!.show()
                auth!!.signInWithEmailAndPassword(Email, Password).addOnSuccessListener {
                    Toast.makeText(context, R.string.login_success, Toast.LENGTH_SHORT).show()
                    if (Objects.requireNonNull(auth!!.currentUser)!!.isAnonymous) {
                        val user = auth!!.currentUser
                        store!!.collection(DATA.PARENT_PATH).document(user!!.uid).delete()
                            .addOnSuccessListener {
                                Toast.makeText(
                                    context, R.string.delete_message_notes, Toast.LENGTH_SHORT
                                ).show()
                            }

                        // delete Temp user
                        user.delete().addOnSuccessListener {
                            Toast.makeText(
                                context, R.string.delete_message_user, Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    VOID.IntentClear(context, CLASS.HOME)
                    finish()
                }.addOnFailureListener { e: Exception ->
                    Toast.makeText(
                        context, R.string.login_failure.toString() + e.message, Toast.LENGTH_SHORT
                    ).show()
                    dialog!!.dismiss()
                }
            }
        }
        binding!!.forget.setOnClickListener { VOID.Intent1(context, CLASS.FORGET_PASSWORD) }
        binding!!.noAccount.setOnClickListener { VOID.Intent1(context, CLASS.REGISTER) }
    }

    private fun showWarning() {
        val warning = AlertDialog.Builder(context)
            .setTitle(R.string.alert_delete_title)
            .setMessage(R.string.alert_login_message)
            .setPositiveButton(R.string.alert_login_positive) { dialog: DialogInterface?, which: Int ->
                VOID.Intent1(context, CLASS.REGISTER)
                finish()
            }
            .setNegativeButton(R.string.alert_login_negative) { dialog: DialogInterface?, which: Int -> }
        warning.show()
    }
}