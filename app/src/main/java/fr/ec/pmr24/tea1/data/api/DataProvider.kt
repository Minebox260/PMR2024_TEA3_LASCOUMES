package fr.ec.pmr24.tea1.data.api

import android.content.Context
import fr.ec.pmr24.tea1.data.api.responses.AuthenticateResponse
import fr.ec.pmr24.tea1.data.api.responses.GetItemsResponse
import fr.ec.pmr24.tea1.data.api.responses.GetListsResponse
import fr.ec.pmr24.tea1.data.api.responses.PostItemResponse
import fr.ec.pmr24.tea1.data.api.responses.PostListResponse
import fr.ec.pmr24.tea1.data.api.responses.PutItemResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DataProvider {

    private lateinit var retrofit: Retrofit
    private lateinit var service: ToDoAPIService

    fun init(context: Context) {
        val baseUrl = context.getSharedPreferences("user", Context.MODE_PRIVATE)
            .getString("base_url", "http://tomnab.fr/todo-api/") ?: "http://tomnab.fr/todo-api/"

        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        service = retrofit.create(ToDoAPIService::class.java)
    }

    fun isInitialized(): Boolean {
        return ::service.isInitialized
    }
    suspend fun authenticate(username: String, password: String) : AuthenticateResponse = service.authenticate(username, password)
    suspend fun getLists(hash: String) : GetListsResponse = service.getLists(hash)
    suspend fun getItems(hash: String, listId: String) : GetItemsResponse = service.getItems(hash, listId)
    suspend fun postList(hash: String, label: String) : PostListResponse = service.postList(hash, label)
    suspend fun postItem(hash: String, listId: String, label: String, url: String?) : PostItemResponse = service.postItem(hash, listId, label, url)
    suspend fun putItem(hash: String, listId: String, itemId: String, checked: Number) : PutItemResponse = service.putItem(hash, listId, itemId, checked)

}
