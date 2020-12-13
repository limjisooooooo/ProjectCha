package com.jspersonal.projectcha

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class ListAdapter(_cData: ArrayList<Client>): BaseAdapter() {
    private var inflater: LayoutInflater? = null
    private val m_Cdata = _cData

    override fun getCount(): Int{
        return m_Cdata.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        var convertview :View? = view
        if (view == null){
            val context = parent?.getContext()
            if (this.inflater == null){
                this.inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            }
            convertview = this.inflater?.inflate(R.layout.list_item, parent, false)!!
        }
        val cName = convertview!!.findViewById(R.id.txtName) as TextView
        val cTel = convertview!!.findViewById(R.id.txtTel) as TextView
        val cAddr = convertview!!.findViewById(R.id.txtAddr) as TextView
        val cLacpt = convertview!!.findViewById(R.id.txtLastAcpt) as TextView
        val cEtc = convertview!!.findViewById(R.id.txtEtc) as TextView

        cName.setText(m_Cdata.get(position).name)
        cTel.setText(m_Cdata.get(position).tel)
        cAddr.setText(m_Cdata.get(position).addr)
        cLacpt.setText(m_Cdata.get(position).lastacpt)
        cEtc.setText(m_Cdata.get(position).etc)
        return convertview
    }

}