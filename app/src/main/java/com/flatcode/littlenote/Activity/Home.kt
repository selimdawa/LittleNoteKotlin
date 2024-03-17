package com.flatcode.littlenote.Activity

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
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

    private var binding: ActivityHomeBinding? = null
    var noteAdapter: FirestoreRecyclerAdapter<Note, NoteViewHolder>? = null
    private var activity: Activity? = null
    private val context: Context = also { activity = it }

    override fun onCreate(savedInstanceState: Bundle?) {
        PreferenceManager.getDefaultSharedPreferences(baseContext)
            .registerOnSharedPreferenceChangeListener(this)
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        // Color Mode ----------------------------- Start
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingFragment())
            .commit()
        // Color Mode -------------------------------- End
        val sharedPreferences = PreferenceManager
            .getDefaultSharedPreferences(baseContext)
        if (sharedPreferences.getString(DATA.COLOR_OPTION, "ONE") == "ONE") {
            binding!!.toolbar.mode.setBackgroundResource(R.drawable.sun)
        } else if (sharedPreferences.getString(DATA.COLOR_OPTION, "NIGHT_ONE") == "NIGHT_ONE") {
            binding!!.toolbar.mode.setBackgroundResource(R.drawable.moon)
        }

        if (DATA.FIREBASE_USER!!.isAnonymous) {
            binding!!.toolbar.info.visibility = View.GONE
            binding!!.toolbar.sync.visibility = View.VISIBLE
        } else {
            binding!!.toolbar.info.visibility = View.VISIBLE
            binding!!.toolbar.sync.visibility = View.GONE
        }

        val query = DATA.FIREBASE_STORE.collection(DATA.PARENT_PATH).document(DATA.FirebaseUserUid)
            .collection(DATA.CHILD_PATH).orderBy(DATA.TITLE, Query.Direction.DESCENDING)
        // query notes > uid > myNotes
        val allNotes = FirestoreRecyclerOptions.Builder<Note>()
            .setQuery(query, Note::class.java)
            .build()
        noteAdapter = object : FirestoreRecyclerAdapter<Note, NoteViewHolder>(allNotes) {
            override fun onBindViewHolder(noteViewHolder: NoteViewHolder, i: Int, note: Note) {
                noteViewHolder.title.text = note.title
                noteViewHolder.description.text = note.content
                val code = DATA.randomColor
                noteViewHolder.card.setCardBackgroundColor(
                    noteViewHolder.view.resources.getColor(code, null)
                )
                val docId = noteAdapter!!.snapshots.getSnapshot(i).id
                noteViewHolder.view.setOnClickListener {
                    VOID.IntentExtraDetails(
                        context, CLASS.DETAILS, DATA.TITLE, note, DATA.CONTENT, note,
                        DATA.COLOR, code, DATA.ID_PATH, docId
                    )
                }
                val menuIcon = noteViewHolder.view.findViewById<ImageView>(R.id.menuIcon)
                menuIcon.setOnClickListener { v: View ->
                    val docId1 = noteAdapter!!.snapshots.getSnapshot(i).id
                    val menu = PopupMenu(v.context, v)
                    menu.gravity = Gravity.END
                    menu.menu.add(DATA.EDIT).setOnMenuItemClickListener {
                        VOID.IntentExtraDetails(
                            context, CLASS.EDIT,
                            DATA.TITLE, note, DATA.CONTENT, note, null, 0, DATA.ID_PATH, docId
                        )
                        false
                    }
                    menu.menu.add(DATA.DELETE).setOnMenuItemClickListener {
                        val docRef = DATA.FIREBASE_STORE.collection(DATA.PARENT_PATH)
                            .document(DATA.FirebaseUserUid).collection(DATA.CHILD_PATH)
                            .document(docId1)
                        docRef.delete().addOnSuccessListener {
                            // note deleted
                            val n = noteAdapter!!.itemCount
                            binding!!.toolbar.number.text = MessageFormat.format(" ({0})", n)
                        }.addOnFailureListener {
                            Toast.makeText(
                                context, R.string.error_delete, Toast.LENGTH_SHORT
                            ).show()
                        }
                        false
                    }
                    menu.show()
                }
                val n = noteAdapter!!.itemCount
                binding!!.toolbar.number.text = MessageFormat.format(" ({0})", n)
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
                return NoteViewHolder(view)
            }
        }
        binding!!.recyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding!!.recyclerView.adapter = noteAdapter
        binding!!.toolbar.sync.setOnClickListener {
            if (DATA.FIREBASE_USER.isAnonymous) {
                VOID.Intent1(context, CLASS.LOGIN)
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down)
            } else {
                Toast.makeText(context, R.string.temporary_connect, Toast.LENGTH_SHORT).show()
            }
        }
        binding!!.toolbar.add.setOnClickListener {
            VOID.Intent1(context, CLASS.ADD)
            overridePendingTransition(R.anim.slide_up, R.anim.slide_down)
        }
        binding!!.toolbar.logout.setOnClickListener { checkUser() }
        binding!!.toolbar.info.setOnClickListener {
            VOID.aboutAccount(context, DATA.FIREBASE_USER.displayName, DATA.FIREBASE_USER.email)
        }
    }

    private fun checkUser() {
        // if user is real or not
        if (DATA.FIREBASE_USER!!.isAnonymous) {
            displayAlert()
        } else {
            FirebaseAuth.getInstance().signOut()
            VOID.Intent1(context, CLASS.SPLASH)
            overridePendingTransition(R.anim.slide_up, R.anim.slide_down)
            finish()
        }
    }

    private fun displayAlert() {
        val warning = AlertDialog.Builder(context)
            .setTitle(R.string.alert_delete_title)
            .setMessage(R.string.alert_delete_message)
            .setPositiveButton(R.string.alert_delete_positive) { dialog: DialogInterface?, which: Int ->
                VOID.Intent1(context, CLASS.REGISTER)
                finish()
            }
            .setNegativeButton(R.string.alert_delete_negative) { dialog: DialogInterface?, which: Int ->
                // ToDO: delete all the notes created by the Anon user
                // TODO: delete the anon user
                DATA.FIREBASE_USER!!.delete()
                    .addOnSuccessListener { aVoid: Void? ->
                        VOID.Intent1(context, CLASS.SPLASH)
                        overridePendingTransition(R.anim.slide_up, R.anim.slide_down)
                        finish()
                    }
            }
        warning.show()
    }

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView
        var description: TextView
        var view: View
        var card: CardView

        init {
            title = itemView.findViewById(R.id.title)
            description = itemView.findViewById(R.id.description)
            card = itemView.findViewById(R.id.card)
            view = itemView
        }
    }

    override fun onStart() {
        super.onStart()
        noteAdapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        if (noteAdapter != null) {
            noteAdapter!!.stopListening()
        }
    }

    override fun onBackPressed() {
        VOID.closeApp(context, activity)
    }

    // Color Mode ----------------------------- Start
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == DATA.COLOR_OPTION) recreate()
    }

    class SettingFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SETTINGS_CODE) recreate()
    }
    // Color Mode -------------------------------- End

    companion object {
        private const val SETTINGS_CODE = 234
    }
}