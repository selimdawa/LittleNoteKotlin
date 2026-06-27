package com.flatcode.littlenote.Activity

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.flatcode.littlenote.Adapter.NoteAdapter
import com.flatcode.littlenote.Model.Note
import com.flatcode.littlenote.R
import com.flatcode.littlenote.Unit.CLASS
import com.flatcode.littlenote.Unit.DATA
import com.flatcode.littlenote.Unit.THEME
import com.flatcode.littlenote.Unit.VOID
import com.flatcode.littlenote.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import java.text.MessageFormat

class Home : AppCompatActivity(), OnSharedPreferenceChangeListener {

    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding!!

    var noteAdapter: NoteAdapter? = null
    private val context: Context get() = this

    override fun onCreate(savedInstanceState: Bundle?) {
        PreferenceManager.getDefaultSharedPreferences(baseContext)
            .registerOnSharedPreferenceChangeListener(this)
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                VOID.closeApp(context, this@Home)
            }
        })

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingFragment())
            .commit()

        val firebaseUser = DATA.FIREBASE_USER
        if (firebaseUser != null && firebaseUser.isAnonymous) {
            binding.toolbar.info.visibility = View.GONE
            binding.toolbar.sync.visibility = View.VISIBLE
        } else {
            binding.toolbar.info.visibility = View.VISIBLE
            binding.toolbar.sync.visibility = View.GONE
        }

        val query = DATA.FIREBASE_STORE.collection(DATA.PARENT_PATH).document(DATA.FirebaseUserUid)
            .collection(DATA.CHILD_PATH).orderBy(DATA.TITLE, Query.Direction.DESCENDING)

        val allNotes = FirestoreRecyclerOptions.Builder<Note>()
            .setQuery(query, Note::class.java)
            .build()

        noteAdapter = NoteAdapter(context, allNotes) { count ->
            binding.toolbar.number.text = MessageFormat.format(" ({0})", count)
        }

        binding.recyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.adapter = noteAdapter

        binding.toolbar.sync.setOnClickListener {
            val userSync = DATA.FIREBASE_USER
            if (userSync != null && userSync.isAnonymous) {
                VOID.Intent1(context, CLASS.LOGIN)
                applyTransition()
            } else {
                showToast(getString(R.string.temporary_connect))
            }
        }

        binding.toolbar.add.setOnClickListener {
            VOID.Intent1(context, CLASS.ADD)
            applyTransition()
        }

        binding.toolbar.logout.setOnClickListener { checkUser() }
        binding.toolbar.info.setOnClickListener {
            val userClick = DATA.FIREBASE_USER
            if (userClick != null) {
                VOID.aboutAccount(context, userClick.displayName, userClick.email)
            }
        }
    }

    private fun checkUser() {
        val firebaseUser = DATA.FIREBASE_USER
        if (firebaseUser != null && firebaseUser.isAnonymous) {
            displayAlert()
        } else {
            FirebaseAuth.getInstance().signOut()
            VOID.Intent1(context, CLASS.SPLASH)
            applyTransition()
            finish()
        }
    }

    private fun displayAlert() {
        AlertDialog.Builder(context)
            .setTitle(R.string.alert_delete_title)
            .setMessage(R.string.alert_delete_message)
            .setPositiveButton(R.string.alert_delete_positive) { _, _ ->
                VOID.Intent1(context, CLASS.REGISTER)
                finish()
            }
            .setNegativeButton(R.string.alert_delete_negative) { _, _ ->
                DATA.FIREBASE_USER?.delete()
                    ?.addOnSuccessListener {
                        VOID.Intent1(context, CLASS.SPLASH)
                        applyTransition()
                        finish()
                    }
            }.show()
    }

    override fun onStart() {
        super.onStart()
        noteAdapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        noteAdapter?.stopListening()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == DATA.COLOR_OPTION) recreate()
    }

    private fun applyTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(
                OVERRIDE_TRANSITION_OPEN,
                R.anim.slide_up,
                R.anim.slide_down
            )
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        PreferenceManager.getDefaultSharedPreferences(baseContext)
            .unregisterOnSharedPreferenceChangeListener(this)
        _binding = null
    }

    class SettingFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }
    }
}