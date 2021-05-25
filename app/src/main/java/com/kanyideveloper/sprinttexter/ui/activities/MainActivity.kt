package com.kanyideveloper.sprinttexter.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.kanyideveloper.sprinttexter.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.about -> {
                Toast.makeText(applicationContext, "About", Toast.LENGTH_SHORT).show()
            }
            R.id.history -> {
                Toast.makeText(applicationContext, "History", Toast.LENGTH_SHORT).show()
            }
            R.id.feedback -> {
                Toast.makeText(applicationContext, "Feedback", Toast.LENGTH_SHORT).show()
            }
            R.id.help -> {
                Toast.makeText(applicationContext, "Help", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}