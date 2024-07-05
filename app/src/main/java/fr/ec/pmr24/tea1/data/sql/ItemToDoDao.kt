package fr.ec.pmr24.tea1.data.sql

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import fr.ec.pmr24.tea1.data.classes.ItemToDo
import fr.ec.pmr24.tea1.data.classes.ItemsSync
import fr.ec.pmr24.tea1.data.classes.ListeToDo

@Dao
interface ItemToDoDao {

    @Query("SELECT * FROM lists WHERE id = :listId ")
    suspend fun getList(listId: String): ListeToDo?

    @Query("SELECT * FROM lists")
    suspend fun getLists(): List<ListeToDo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: ListeToDo)

    @Update
    suspend fun updateList(list: ListeToDo)

    @Query("SELECT items.* FROM items JOIN items_sync ON items.id = items_sync.itemId  WHERE items_sync.listId = :listId")
    suspend fun getListItems(listId: String): List<ItemToDo>

    @Query("SELECT * FROM items WHERE id = :itemId ")
    suspend fun getItem(itemId: String): ItemToDo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ItemToDo)

    @Update
    suspend fun updateItem(item: ItemToDo)

    @Update
    suspend fun updateSync(sync: ItemsSync)

    @Query("SELECT * FROM items_sync WHERE unsynced = 1")
    suspend fun getUnsynced(): List<ItemsSync>

    @Query("SELECT * FROM items_sync WHERE itemId = :itemId ")
    suspend fun getSync(itemId: String): ItemsSync?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSync(sync: ItemsSync)

}