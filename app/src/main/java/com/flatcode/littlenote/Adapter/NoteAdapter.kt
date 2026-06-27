package com.flatcode.littlenote.Adapter

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.flatcode.littlenote.Model.Note
import com.flatcode.littlenote.Unit.CLASS
import com.flatcode.littlenote.Unit.DATA
import com.flatcode.littlenote.Unit.VOID
import com.flatcode.littlenote.databinding.ItemNoteBinding

class NoteAdapter(
    private val context: Context,
    options: FirestoreRecyclerOptions<Note>,
    private val onNotesCountChanged: (Int) -> Unit
) : FirestoreRecyclerAdapter<Note, NoteAdapter.NoteViewHolder>(options) {

    override fun onBindViewHolder(noteViewHolder: NoteViewHolder, i: Int, note: Note) {
        val binding = noteViewHolder.binding

        binding.title.text = note.title
        binding.description.text = note.content

        val code = DATA.randomColor
        binding.card.setCardBackgroundColor(ContextCompat.getColor(context, code))

        val docId = snapshots.getSnapshot(i).id
        binding.root.setOnClickListener {
            VOID.IntentExtraDetails(
                context, CLASS.DETAILS, DATA.TITLE, note, DATA.CONTENT, note,
                DATA.COLOR, code, DATA.ID_PATH, docId
            )
        }

        binding.menuIcon.setOnClickListener { v ->
            val docId1 = snapshots.getSnapshot(i).id
            val menu = PopupMenu(v.context, v).apply {
                gravity = Gravity.END
            }

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
                    onNotesCountChanged(itemCount)
                }
                false
            }
            menu.show()
        }
        onNotesCountChanged(itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    class NoteViewHolder(val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root)
}