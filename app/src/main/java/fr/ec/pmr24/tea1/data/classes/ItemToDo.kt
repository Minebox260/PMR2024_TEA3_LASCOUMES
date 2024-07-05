package fr.ec.pmr24.tea1.data.classes

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class ItemToDo(
    @PrimaryKey val id: String,
    val label: String = "",
    var checked: Int = 0,
    val url: String? = null,
)

@Entity(
    tableName = "items_sync",
    foreignKeys =  [ForeignKey(
        entity = ItemToDo::class,
        parentColumns = ["id"],
        childColumns = ["itemId"],
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    ),
        ForeignKey(
            entity = ListeToDo::class,
            parentColumns = ["id"],
            childColumns = ["listId"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )]
)
data class ItemsSync(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val itemId: String,
    val listId: String,
    var unsynced: Boolean = false
)