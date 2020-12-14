package com.jspersonal.projectcha
import androidx.room.*

@Dao
interface QueryDao {
    @Query("SELECT c.id as cid, c.name, c.tel, c.addr, a.lastacpt, c.etc FROM Clients c, (SELECT id, max(acptdate) as lastacpt FROM AcptInfos GROUP BY id) a WHERE c.id = a.id ORDER BY c.id")
    fun getClients():List<Client>

    @Query("SELECT c.id as cid, c.name, c.tel, c.addr, a.acptdate as lastacpt, c.etc FROM Clients c, (SELECT id, GROUP_CONCAT(acptdate, '/') acptdate FROM ACPTINFOS GROUP BY id) a WHERE c.id = a.id ORDER BY c.id")
    fun getAllData():List<Client>

    @Query("SELECT c.id as cid, c.name, c.tel, c.addr, a.lastacpt, c.etc FROM Clients c, (SELECT id, max(acptdate) as lastacpt FROM AcptInfos GROUP BY id) a WHERE c.id = a.id AND name LIKE :q ORDER BY c.id")
    fun getClientforName(q: String):List<Client>

    @Query("SELECT c.id as cid, c.name, c.tel, c.addr, a.lastacpt, c.etc FROM Clients c, (SELECT id, max(acptdate) as lastacpt FROM AcptInfos GROUP BY id) a WHERE c.id = a.id AND addr LIKE :q ORDER BY c.id")
    fun getClientforAddr(q: String):List<Client>

    @Query("SELECT c.id as cid, c.name, c.tel, c.addr, a.lastacpt, c.etc FROM Clients c, (SELECT id, max(acptdate) as lastacpt FROM AcptInfos GROUP BY id) a WHERE c.id = a.id AND tel LIKE :q ORDER BY c.id")
    fun getClientforTel(q: String):List<Client>

    @Query("SELECT c.id as cid, c.name, c.tel, c.addr, a.acptdate as lastacpt, c.etc FROM Clients c, (SELECT id, GROUP_CONCAT(acptdate, '/') acptdate FROM ACPTINFOS GROUP BY id) a WHERE c.id = a.id AND c.id = :id")
    fun getClientDetail(id: Long): Client

    @Query("SELECT max(id) + 1 FROM Clients")
    fun getNextId(): Long

    @Insert
    fun insClients(vararg clients: Clients)

    @Insert
    fun insAcptInfos(vararg acptinfos: AcptInfos)

    @Query("DELETE FROM CLIENTS WHERE id = :id")
    fun delClient(id: Long)

    @Query("DELETE FROM CLIENTS")
    fun delClients()

    @Query("DELETE FROM ACPTINFOS WHERE id = :id")
    fun delAcptInfo(id: Long)

    @Query("DELETE FROM ACPTINFOS")
    fun delAcptInfos()

}