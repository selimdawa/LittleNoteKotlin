package com.flatcode.littlenote.ui.adapter

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.flatcode.littlenote.data.model.Note
import com.flatcode.littlenote.databinding.ItemNoteBinding
import com.flatcode.littlenote.utils.DATA

class NoteAdapter(
    private val context: Context,
    options: FirestoreRecyclerOptions<Note>,
    private val onNotesCountChanged: (Int) -> Unit,
    private val onDeleteClicked: (String) -> Unit,
    private val onItemClicked: (Note, String, Int) -> Unit,
    private val onEditClicked: (Note, String) -> Unit
) : FirestoreRecyclerAdapter<Note, NoteAdapter.NoteViewHolder>(options) {

    override fun onBindViewHolder(noteViewHolder: NoteViewHolder, i: Int, note: Note) {
        val docId = snapshots.getSnapshot(i).id
        val code = DATA.randomColor

        noteViewHolder.binding.run {
            title.text = note.title
            description.text = note.content
            card.setCardBackgroundColor(ContextCompat.getColor(context, code))
            root.setOnClickListener { onItemClicked(note, docId, code) }
            menuIcon.setOnClickListener { v ->
                PopupMenu(v.context, v).apply {
                    gravity = Gravity.END
                    menu.add(DATA.EDIT).setOnMenuItemClickListener {
                        onEditClicked(note, docId)
                        false
                    }
                    menu.add(DATA.DELETE).setOnMenuItemClickListener {
                        onDeleteClicked(docId)
                        false
                    }
                    show()
                }
            }
        }
        onNotesCountChanged(itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder =
        NoteViewHolder(ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    class NoteViewHolder(val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root)
}