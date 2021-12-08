package com.jithinraj.noter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_note.*

class AddNoteActivity : AppCompatActivity() {

    private val TAG = "AddNoteActivity"

    private var firestoreDB: FirebaseFirestore? = null

     var id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        firestoreDB = FirebaseFirestore.getInstance()

        val bundle = intent.extras
        if (bundle != null) {
            id = bundle.getString("UpdateNoteId").toString()

            txttitle.setText(bundle.getString("UpdateNoteTitle"))
            txtdesc.setText(bundle.getString("UpdateNoteDescription"))

            if(bundle.getString("isFromView")!=null){
                btnAdd.isInvisible=true
            }
        }

        btnAdd.setOnClickListener(){
            val title=txttitle.text.toString()
            val description=txtdesc.text.toString()

            if (title.isNotEmpty()) {
                if (id.isNotEmpty()) {
                    updateNote(id, title, description)
                } else {
                    addNote(title, description)
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }


        }

    }

    private fun updateNote(id: String, title: String, content: String) {
        val note = NoteModel(id, title, content).toMap()

        firestoreDB!!.collection("notes")
            .document(id)
            .set(note)
            .addOnSuccessListener {
                Log.e(TAG, "Note document update successful!")
                Toast.makeText(applicationContext, "Note has been updated!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error adding Note document", e)
                Toast.makeText(applicationContext, "Note could not be updated!", Toast.LENGTH_SHORT).show()
            }
    }



    private fun addNote(title: String, description: String) {
        val note = NoteModel(title, description).toMap()

        firestoreDB!!.collection("notes")
            .add(note)
            .addOnSuccessListener { documentReference ->
                Log.e(TAG, "DocumentSnapshot written with ID: " + documentReference.id)
                Toast.makeText(applicationContext, "Note has been added!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error adding Note document", e)
                Toast.makeText(applicationContext, "Note could not be added!", Toast.LENGTH_SHORT).show()
            }
    }
}