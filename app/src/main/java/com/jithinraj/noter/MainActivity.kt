package com.jithinraj.noter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.android.synthetic.main.activity_main.*
class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    private var mAdapter: NoteListAdapter? = null

    private var firestoreDB: FirebaseFirestore? = null
    private var firestoreListener: ListenerRegistration? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val list=listOf<String>("First Note","Second Note","Third Note")
        //val recyclerView=findViewById<RecyclerView>(R.id.recyclerview)
       // recyclerview.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
       // recyclerview.adapter=NoteListAdapter(list)

        firestoreDB = FirebaseFirestore.getInstance()

        loadNotesList()

        firestoreListener = firestoreDB!!.collection("notes")
            .addSnapshotListener(EventListener { documentSnapshots, e ->
                if (e != null) {
                    Log.e(TAG, "Listen failed!", e)
                    return@EventListener
                }

                val notesList = mutableListOf<NoteModel>()

                if (documentSnapshots != null) {
                    for (doc in documentSnapshots) {
                        val note = doc.toObject(NoteModel::class.java)
                        note.id = doc.id
                        notesList.add(note)
                    }
                }

                mAdapter = NoteListAdapter(notesList, applicationContext, firestoreDB!!)
                rvNoteList.adapter = mAdapter
            })

    }

    override fun onDestroy() {
        super.onDestroy()

        firestoreListener!!.remove()
    }

    private fun loadNotesList() {
        firestoreDB!!.collection("notes")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val notesList = mutableListOf<NoteModel>()

                    for (doc in task.result!!) {
                        val note = doc.toObject<NoteModel>(NoteModel::class.java)
                        note.id = doc.id
                        notesList.add(note)
                    }

                    mAdapter = NoteListAdapter(notesList, applicationContext, firestoreDB!!)
                    val mLayoutManager = LinearLayoutManager(applicationContext)
                    rvNoteList.layoutManager = mLayoutManager
                    rvNoteList.itemAnimator = DefaultItemAnimator()
                    rvNoteList.adapter = mAdapter
                } else {
                    Log.d(TAG, "Error getting documents: ", task.exception)
                }
            }
    }


    fun addClicked(view: android.view.View) {
        val intent = Intent(this, AddNoteActivity::class.java)
        startActivity(intent)
    }
}