package com.jspersonal.projectcha
import androidx.room.*

@Dao
interface QueryDao {
    @Query("SELECT c.id as cid, c.name, c.tel, c.addr, a.lastacpt, c.etc FROM Clients c, (SELECT id, max(acptdate) as lastacpt FROM AcptInfos GROUP BY id) a WHERE c.id = a.id")
    fun getClients():List<Client>

    @Query("SELECT c.id as cid, c.name, c.tel, c.addr, a.acptdate as lastacpt, c.etc FROM Clients c, (SELECT id, GROUP_CONCAT(acptdate, '/') acptdate FROM ACPTINFOS GROUP BY id) a WHERE c.id = a.id")
    fun getAllData():List<Client>

    @Query("SELECT c.id as cid, c.name, c.tel, c.addr, a.lastacpt, c.etc FROM Clients c, (SELECT id, max(acptdate) as lastacpt FROM AcptInfos GROUP BY id) a WHERE c.id = a.id AND :opt LIKE :q")
    fun getClient(opt: String, q: String):List<Client>

    @Insert
    fun insClients(vararg clients: Clients)

    @Insert
    fun insAcptInfos(vararg acptinfos: AcptInfos)

    @Delete
    fun delClient(clients: Clients)

    @Query("DELETE FROM CLIENTS")
    fun delClients()

    @Delete
    fun delAcptInfo(acptInfos: AcptInfos)

    @Query("DELETE FROM ACPTINFOS")
    fun delAcptInfos()
}