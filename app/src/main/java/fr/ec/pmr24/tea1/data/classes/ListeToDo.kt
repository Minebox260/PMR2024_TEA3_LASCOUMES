package fr.ec.pmr24.tea1.data.classes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lists")
data class ListeToDo(
    @PrimaryKey val id: String = "",
    val label: String = ""
)