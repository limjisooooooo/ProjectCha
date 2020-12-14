package com.jspersonal.projectcha


import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_client.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ClientActivity: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_client)
        var id: Long? = null
        val txtName = findViewById<EditText>(R.id.txtNameC)
        val txtTel = findViewById<EditText>(R.id.txtTelC)
        val txtAddr = findViewById<EditText>(R.id.txtAddrC)
        val txtAcpt = findViewById<EditText>(R.id.txtAcptC)
        val txtEtc = findViewById<EditText>(R.id.txtEtcC)
        var addFlag = false
        val btnInsert = findViewById<Button>(R.id.btnInsert)

        CoroutineScope(IO).launch {
            if (intent.hasExtra("cid")) {
                addFlag = false
                id = intent.getLongExtra("cid", 0)
                val client = getClient(id!!)
                withContext(Main){
                    txtName.setText(client.name)
                    txtTel.setText(client.tel)
                    txtAddr.setText(client.addr)
                    txtAcpt.setText(client.lastacpt)
                    txtEtc.setText(client.etc)
                }
            }   else{
                addFlag = true
                id = getNextId()
                txtName.setText("")
                txtTel.setText("")
                txtAddr.setText("")
                txtAcpt.setText("")
                txtEtc.setText("")
            }
        }

        btnInsert.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (id == null) return

                val name = txtName.text.toString()
                val tel = txtTel.text.toString()
                val addr = txtAddr.text.toString()
                val etc = txtEtc.text.toString()
                val acptinfos = txtAcpt.text.toString().split('/')

                CoroutineScope(IO).launch {
                    if (!addFlag) {
                        delClient(id!!)
                    }
                    insClient(id!!, name, tel, addr, acptinfos, etc)
                    withContext(Main) {
                        Toast.makeText(this@ClientActivity, "save success!", Toast.LENGTH_SHORT)
                            .show()
                    }
                    finish()
                }
            }

        })

    }

    suspend fun getNextId(): Long{
        return AppDataBase.getInstance(this)!!.queryDao().getNextId()
    }
    suspend fun getClient(cid: Long): Client{
        return AppDataBase.getInstance(this)!!.queryDao().getClientDetail(cid)
    }

    suspend fun delClient(id: Long){
        AppDataBase.getInstance(this)!!.queryDao().delClient(id)
        AppDataBase.getInstance(this)!!.queryDao().delAcptInfo(id)
    }

    suspend fun insClient(id: Long, name: String, tel: String, addr: String, acptinfos: List<String>, etc: String){
        AppDataBase.getInstance(this)!!.queryDao().insClients(Clients(id, name, tel, addr, etc))
        for(acptinfo in acptinfos){
            AppDataBase.getInstance(this)!!.queryDao().insAcptInfos(AcptInfos(id, acptinfo))
        }
    }

}