package fr.ec.pmr24.tea1.data.api

import fr.ec.pmr24.tea1.data.api.responses.AuthenticateResponse
import fr.ec.pmr24.tea1.data.api.responses.GetItemsResponse
import fr.ec.pmr24.tea1.data.api.responses.GetListsResponse
import fr.ec.pmr24.tea1.data.api.responses.PostItemResponse
import fr.ec.pmr24.tea1.data.api.responses.PostListResponse
import fr.ec.pmr24.tea1.data.api.responses.PutItemResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ToDoAPIService {

    @POST("authenticate")
    suspend fun authenticate(
        @Query("user") user: String,
        @Query("password") password: String
    ): AuthenticateResponse

    @GET("lists")
    suspend fun getLists(
        @Header("hash") hash: String
    ): GetListsResponse

    @POST("lists")
    suspend fun postList(
        @Header("hash") hash: String,
        @Query("label") label: String
    ): PostListResponse

    @GET("lists/{list_id}/items")
    suspend fun getItems(
        @Header("hash") hash: String,
        @Path("list_id") listId: String
    ): GetItemsResponse

    @POST("lists/{list_id}/items")
    suspend fun postItem(
        @Header("hash") hash: String,
        @Path("list_id") listId: String,
        @Query("label") label: String,
        @Query("url") url: String?
    ): PostItemResponse

    @PUT("lists/{list_id}/items/{item_id}")
    suspend fun putItem(
        @Header("hash") hash: String,
        @Path("list_id") listId: String,
        @Path("item_id") itemId: String,
        @Query("check") checked: Number
    ): PutItemResponse

}
