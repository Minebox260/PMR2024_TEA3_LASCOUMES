package fr.ec.pmr24.tea1.data.api.responses

import fr.ec.pmr24.tea1.data.classes.ListeToDo

data class PostListResponse(
    val version: Number,
    val success: Boolean,
    val status: Number,
    val apiname: String,
    val list: ListeToDo?
)
