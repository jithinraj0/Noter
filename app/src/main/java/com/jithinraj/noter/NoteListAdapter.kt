package com.jithinraj.noter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.jithinraj.noter.NoteListAdapter.*

class NoteListAdapter(
    private val notesList: MutableList<NoteModel>,
    private val context: Context,
    private val firestoreDB: FirebaseFirestore
) : RecyclerView.Adapter<NoteListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notelist, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val note = notesList[position]
        if (note.title != null) {
            holder.title.text = note.title
            holder.description.text = note.description

            holder.edit.setOnClickListener { updateNote(note) }
            holder.delete.setOnClickListener { deleteNote(note.id!!, position) }
        }
        else{
            holder.title.text = "note.title"
            holder.description.text = "note.description"
        }
        holder.itemView.setOnClickListener{
            viewNote(note)
        }
    }

    override fun getItemCount() = notesList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var title: TextView
        internal var description: TextView
         internal var edit:ImageView
         internal var delete: ImageView

        init {
            title = itemView.findViewById(R.id.lbltitle)
            description = itemView.findViewById(R.id.lbldesc)

             edit = itemView.findViewById(R.id.btnedit)
             delete = itemView.findViewById(R.id.btndelete)
        }
    }


    private fun updateNote(note: NoteModel) {
        val intent = Intent(context, AddNoteActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("UpdateNoteId", note.id)
        intent.putExtra("UpdateNoteTitle", note.title)
        intent.putExtra("UpdateNoteDescription", note.description)
        context.startActivity(intent)
    }

    private fun viewNote(note: NoteModel) {
        val intent = Intent(context, AddNoteActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("UpdateNoteId", note.id)
        intent.putExtra("UpdateNoteTitle", note.title)
        intent.putExtra("UpdateNoteDescription", note.description)
        intent.putExtra("isFromView", "true")
        context.startActivity(intent)
    }

    private fun deleteNote(id: String, position: Int) {
        firestoreDB.collection("notes")
            .document(id)
            .delete()
            .addOnCompleteListener {
                notesList.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, notesList.size)
                Toast.makeText(context, "Note has been deleted!", Toast.LENGTH_SHORT).show()
            }
    }
}