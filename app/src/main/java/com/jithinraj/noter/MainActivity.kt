package com.jithinraj.noter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.android.synthetic.main.activity_main.*
class MainActivity : AppCompatActivity() {
   // private val TAG = "MainActivity"
    private val TAG = "zz"

    private var mAdapter: NoteListAdapter? = null

    private var firestoreDB: FirebaseFirestore? = null
    private var firestoreListener: ListenerRegistration? = null
    lateinit var sharedPref: SharedPreferences
    lateinit var userName:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPref = this.getPreferences(Context.MODE_PRIVATE)

        if(intent.getStringExtra("Username")!=null){
             userName= intent.getStringExtra("Username")!!
             val editor: SharedPreferences.Editor =  sharedPref.edit()
            editor.putString("storename",userName)
            editor.putInt("storenum",10)
            editor.commit()
        }

        val displayName = sharedPref.getString("storename","0")
        //Toast.makeText(this, displayName, Toast.LENGTH_SHORT).show()
        supportActionBar!!.setTitle("Hi "+displayName)
        firestoreDB = FirebaseFirestore.getInstance()

        loadNotesList()

        firestoreListener = firestoreDB!!.collection("notes")
            .addSnapshotListener(EventListener { documentSnapshots, e ->
                if (e != null) {
                    Log.e(TAG, "Listen failed!", e)
                    return@EventListener
                }

                val notesList = mutableListOf<NoteModel>()
                notesList.clear()
                if (documentSnapshots != null) {
                    print(documentSnapshots.documents)
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
                    rvNoteList.addItemDecoration( DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL));
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