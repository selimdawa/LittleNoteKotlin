package com.flatcode.littlenote.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.flatcode.littlenote.data.model.Note
import com.flatcode.littlenote.databinding.ItemNoteBinding
import com.flatcode.littlenote.utils.DATA

class NoteAdapter(
    private val context: Context,
    private val onDeleteClicked: (Note) -> Unit,
    private val onItemClicked: (Note, Int) -> Unit,
    private val onEditClicked: (Note) -> Unit
) : ListAdapter<Note, NoteAdapter.NoteViewHolder>(NoteDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = getItem(position)
        val binding = holder.binding

        binding.title.text = note.title
        binding.description.text = note.content

        val code = DATA.randomColor
        binding.card.setCardBackgroundColor(ContextCompat.getColor(context, code))

        binding.root.setOnClickListener {
            onItemClicked(note, code)
        }

        var isIconClicked = false
        @SuppressLint("ClickableViewAccessibility")
        binding.description.setOnTouchListener { v, event ->
            val textView = v as TextView
            val drawableEnd = textView.compoundDrawablesRelative[2]
            if (drawableEnd != null) {
                if (event.action == MotionEvent.ACTION_DOWN) {
                    val iconStartX = textView.width - textView.totalPaddingEnd - 30
                    isIconClicked = event.x >= iconStartX
                }
                if (isIconClicked) {
                    if (event.action == MotionEvent.ACTION_UP) {
                        val iconStartX = textView.width - textView.totalPaddingEnd - 30
                        if (event.x >= iconStartX) {
                            val menu = PopupMenu(v.context, v).apply {
                                gravity = Gravity.END
                            }

                            menu.menu.add(DATA.EDIT).setOnMenuItemClickListener {
                                onEditClicked(note)
                                false
                            }

                            menu.menu.add(DATA.DELETE).setOnMenuItemClickListener {
                                onDeleteClicked(note)
                                false
                            }
                            menu.show()
                        }
                        isIconClicked = false
                    }
                    return@setOnTouchListener true
                }
            }
            false
        }
    }

    class NoteViewHolder(val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root)

    class NoteDiffCallback : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean = oldItem == newItem
    }
}