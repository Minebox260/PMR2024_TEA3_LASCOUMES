package fr.ec.pmr24.tea1.data.api.responses

import fr.ec.pmr24.tea1.data.classes.ListeToDo

data class GetListsResponse(
    val version: Number,
    val success: Boolean,
    val status: Number,
    val apiname: String,
    val lists: List<ListeToDo>?
)
