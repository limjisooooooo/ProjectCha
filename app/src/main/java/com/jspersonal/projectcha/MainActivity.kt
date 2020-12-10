package com.jspersonal.projectcha

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.util.TypedValue
import android.view.Menu
import android.widget.ArrayAdapter
import android.widget.Spinner
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.spinner_item.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tlbMain = findViewById<Toolbar>(R.id.tlbMain)
        setSupportActionBar(tlbMain)
        val actionBar = supportActionBar!!
        actionBar.setDisplayShowCustomEnabled(true)
        actionBar.setDisplayShowTitleEnabled(false)
        val spinner = findViewById<Spinner>(R.id.spinner)
        val items = resources.getStringArray(R.array.spinnerItem)
        val spinerAdapter = ArrayAdapter(this, R.layout.spinner_item, items)
        spinner.adapter = spinerAdapter
        spinner.dropDownVerticalOffset =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, resources.displayMetrics).toInt()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchView:SearchView = menu?.findItem(R.id.action_search)?.actionView as SearchView
        searchView.maxWidth = tlbMain.width - spinner.right
        return super.onCreateOptionsMenu(menu)
    }
}
