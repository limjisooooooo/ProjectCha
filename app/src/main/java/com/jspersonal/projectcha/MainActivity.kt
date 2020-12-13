package com.jspersonal.projectcha

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.spinner_item.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.*

class MainActivity : AppCompatActivity() {
    val cData: ArrayList<Client> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl")
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl")
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl")

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
        CoroutineScope(IO).launch {
            var clients = getClients()
            withContext(Main) {
                cData.clear()
                for(client in clients) {
                    cData.add(client)
                }
                val listView = findViewById<ListView>(R.id.listView)
                val listAdapter = ListAdapter(cData)
                listView.adapter = listAdapter
            }

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchView:SearchView = menu?.findItem(R.id.action_search)?.actionView as SearchView
        searchView.maxWidth = tlbMain.width - spinner.right
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                CoroutineScope(IO).launch {
                    val spinner = findViewById<Spinner>(R.id.spinner)
                    val clients = getClients(spinner.selectedItem.toString(), query)
                    withContext(Main) {
                        cData.clear()
                        for(client in clients) {
                            cData.add(client)
                        }
                        val listView = findViewById<ListView>(R.id.listView)
                        val listAdapter = ListAdapter(cData)
                        listView.adapter = listAdapter

                        Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()
                    }

                }
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
                            when(item.itemId){
                                R.id.menu0 ->{
                                    DBSync()
                                }
                                R.id.menu2 -> {
                                    //Excel Export
                                    if (!cData.isEmpty()) exportExcel()
                                }
                            }
                        }
                        return false
                    }

                })
                p.show()
            }

        }
        return super.onOptionsItemSelected(item)
    }
    fun exportExcel(){
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet()

        var row = sheet.createRow(0)
        var cell = row.createCell(0)
        cell.setCellValue("성명")
        cell = row.createCell(1)
        cell.setCellValue("연락처")
        cell = row.createCell(2)
        cell.setCellValue("마지막접수일")
        cell = row.createCell(3)
        cell.setCellValue("주소")
        cell = row.createCell(4)
        cell.setCellValue("비고")

        val job = CoroutineScope(IO).launch {
            val clients = getAll()
            for (item in clients) {
                row = sheet.createRow(item.cid.toInt())
                cell = row.createCell(0)
                cell.setCellValue(item.name)
                cell = row.createCell(1)
                cell.setCellValue(item.tel)
                cell = row.createCell(2)
                cell.setCellValue(item.lastacpt)
                cell = row.createCell(3)
                cell.setCellValue(item.addr)
                cell = row.createCell(4)
                cell.setCellValue(item.etc)
            }
            try {
                val xlsFile = File(getExternalFilesDir(null), "Clients.xlsx")
                val os = FileOutputStream(xlsFile)
                workbook.write(os)
                workbook.close()
                os.close()
                withContext(Main){
                    Toast.makeText(
                        this@MainActivity,
                        "${xlsFile.absolutePath} Export Success!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } catch (e: IOException) {
                withContext(Main){
                    Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    fun DBSync(){
        try{
            val inputstream: InputStream? = FileInputStream("${getExternalFilesDir(null)}/Clients.xlsx")
            CoroutineScope(IO).launch{
                delAll()
                if (inputstream != null) {
                    cData.clear()
                    val workbook = XSSFWorkbook(inputstream)
                    val sheet = workbook.getSheetAt(0)
                    val rows = sheet.physicalNumberOfRows
                    for (i in 0 until rows) {
                        var row = sheet.getRow(i)
                        var cid = i.toLong()

                        var cell: Cell? = row.getCell(0)
                        var name = ""
                        if (cell != null) {
                            name = cell.stringCellValue
                        }
                        cell = row.getCell(1)
                        var tel = ""
                        if (cell != null) {
                            tel = cell.stringCellValue
                        }
                        cell = row.getCell(2)
                        var lastacpt = ""
                        if (cell != null) {
                            lastacpt = cell.stringCellValue
                        }
                        cell = row.getCell(3)
                        var addr = ""
                        if (cell != null) {
                            addr = cell.stringCellValue
                        }
                        cell = row.getCell(4)
                        var etc = ""
                        if (cell != null) {
                            etc = cell.stringCellValue
                        }
                        var client = Client(cid, name, addr, tel, lastacpt, etc)
                        insAll(client)
                        //cData.add(client)
                    }

                    val clients = getClients()
                    withContext(Main) {
                        cData.clear()
                        for(client in clients) {
                            cData.add(client)
                        }
                        val listView = findViewById<ListView>(R.id.listView)
                        val listAdapter = ListAdapter(cData)
                        listView.adapter = listAdapter
                    }
                }
            }



        }catch (e: IOException){
            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
        }

    }
    suspend fun delAll(){
        AppDataBase.getInstance(applicationContext)!!.queryDao().delClients()
        AppDataBase.getInstance(applicationContext)!!.queryDao().delAcptInfos()
    }
    suspend fun insAll(client: Client){
        val id = client.cid
        val name = client.name
        val tel = client.tel
        val addr = client.addr
        val etc = client.etc
        val acptinfo = client.lastacpt.split('/')

        AppDataBase.getInstance(applicationContext)!!.queryDao().insClients(Clients(id,name,tel,addr,etc))
        for(item in acptinfo) {
            AppDataBase.getInstance(applicationContext)!!.queryDao()
                .insAcptInfos(AcptInfos(id, item))
        }
    }
    suspend fun getClients(): List<Client>{
        return AppDataBase.getInstance(applicationContext)!!.queryDao().getClients()
    }
    suspend fun getClients(option: String, query: String?): List<Client>{
        if (query != null){
            return AppDataBase.getInstance(applicationContext)!!.queryDao().getClient(option, query)
        }else {
            return AppDataBase.getInstance(applicationContext)!!.queryDao().getClients()
        }
    }
    suspend fun getAll(): List<Client>{
        return AppDataBase.getInstance(applicationContext)!!.queryDao().getAllData()
    }
}