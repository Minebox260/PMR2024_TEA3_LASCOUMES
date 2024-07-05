package fr.ec.pmr24.tea1.data.api.responses

data class AuthenticateResponse(
    val version: Number,
    val success: Boolean,
    val status: Number,
    val apiname: String,
    val hash: String?
)
