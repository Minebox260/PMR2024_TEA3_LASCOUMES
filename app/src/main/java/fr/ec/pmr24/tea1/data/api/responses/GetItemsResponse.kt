package fr.ec.pmr24.tea1.data.api.responses

import fr.ec.pmr24.tea1.data.classes.ItemToDo

data class GetItemsResponse(
    val version: Number,
    val success: Boolean,
    val status: Number,
    val apiname: String,
    val items: List<ItemToDo>?
)
