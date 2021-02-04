package com.example.savemoney

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListenerb
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class reccomend_fragment : AppCompatActivity() {
    private var labelView: TextView? = null
    private var emailText: EditText? = null
    private var passText: EditText? = null
    private val mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        labelView = findViewById(R.id.textView)
        emailText = findViewById(R.id.emailView)
        passText = findViewById(R.id.passView)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            // not used.
        }

        // FirebaseApp.initializeApp(this); // ●can't initialize?

        /* ●re-initialize manually.
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApplicationId(" アプリID ")
                .setApiKey(" APIキー ").build();
        FirebaseApp fapp = FirebaseApp.initializeApp(this, options);

        mAuth = FirebaseAuth.getInstance();
        */
    }

    public override fun onStart() {
        super.onStart()
        val currentUser: FirebaseUser = mAuth.getCurrentUser()
        updateUI(currentUser)
    }

    fun doLogin(view: View?) {
        val email = emailText!!.text.toString() + ""
        val password = passText!!.text.toString() + ""
        Toast.makeText(this@recommend_fragment, "Login start.",
                Toast.LENGTH_SHORT).show()
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, OnCompleteListener<Any?> { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this@recommend_fragment, "Logined!!",
                                Toast.LENGTH_SHORT).show()
                        val user: FirebaseUser = mAuth.getCurrentUser()
                        updateUI(user)
                    } else {
                        Toast.makeText(this@recommend_fragment, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }
                })
    }

    fun doLogout(view: View?) {
        mAuth.signOut()
        Toast.makeText(this@recommend_fragment, "logouted...",
                Toast.LENGTH_SHORT).show()
        updateUI(null)
    }

    fun updateUI(user: FirebaseUser?) {
        if (user == null) {
            labelView!!.text = "no login..."
        } else {
            labelView!!.text = "login: " + user.getEmail()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }
}