package com.jspersonal.projectcha

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
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
        val spinneritems = resources.getStringArray(R.array.spinnerItem)
        val spinnerAdapter = ArrayAdapter(this, R.layout.spinner_item, spinneritems)
        spinner.adapter = spinnerAdapter
        spinner.dropDownVerticalOffset =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, resources.displayMetrics).toInt()

        val listitems = arrayOf("List1", "List2", "List3")
        val listView = findViewById<ListView>(R.id.listView)
        //Adapter Connect에서 부터 다시 작업해야지!
        val listAdapter = ArrayAdapter(this, R.layout.list_item, listitems)
        listView.adapter = listAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchView:SearchView = menu?.findItem(R.id.action_search)?.actionView as SearchView
        searchView.maxWidth = tlbMain.width - spinner.right
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                return false
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home -> {
                val p :PopupMenu = PopupMenu(this, tlbMain)
                menuInflater.inflate(R.menu.menu_home, p.menu)
                p.setOnMenuItemClickListener(object :PopupMenu.OnMenuItemClickListener{
                    override fun onMenuItemClick(item: MenuItem?): Boolean {
                        if (item != null) {
                            Toast.makeText(this@MainActivity,item.title,Toast.LENGTH_SHORT).show()
                        }
                        return false
                    }

                })
                p.show()
            }

        }
        return super.onOptionsItemSelected(item)
    }
}
