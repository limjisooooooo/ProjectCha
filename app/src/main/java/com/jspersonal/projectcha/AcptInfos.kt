package com.jspersonal.projectcha
import androidx.room.*

@Entity(primaryKeys = arrayOf("id", "acptdate"))
data class AcptInfos(
    val id: Long,
    val acptdate: String
)
