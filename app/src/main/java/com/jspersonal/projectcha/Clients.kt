package com.jspersonal.projectcha
import androidx.room.*

@Entity(indices = arrayOf(Index(value = ["name","tel","addr"])))
data class Clients(
    @PrimaryKey val id:Long,
    val name: String,
    val tel: String,
    val addr: String,
    val etc: String?
)