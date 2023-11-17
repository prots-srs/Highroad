package com.protsprog.highroad.authentication.data

/*
TO READ
https://betterprogramming.pub/android-access-token-logic-with-retrofit-okhttp-interceptors-740ea48547a0

https://square.github.io/okhttp/
https://square.github.io/retrofit/

https://developer.android.com/quality/privacy-and-security

https://stackoverflow.com/questions/74670362/how-to-make-a-correct-post-request-with-retrofit-in-kotlin

APP tests:
https://developer.android.com/docs/quality-guidelines/core-app-quality#sc
 */
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

private val API_SERVER = "https://protsprog.com/"

@JsonClass(generateAdapter = true)
data class AuthTokenResponse(
    val token: String = "",
    val error: String = ""
)

@JsonClass(generateAdapter = true)
data class AuthUserResponse(
    val name: String = "",
    val email: String = ""
)

fun AuthUserResponse.asModel() = UserModel(
    name = name,
    email = email
)

data class AuthUpdateAnswer(
    val result: String = ""
)

interface AuthApiService {
    @FormUrlEncoded
    @POST("/api/sanctum/token")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("device_name") deviceName: String
    ): Response<AuthTokenResponse>

    @GET("/api/user")
    suspend fun getUser(@Header("Authorization") token: String): Response<AuthUserResponse>

    @FormUrlEncoded
    @POST("/api/user")
    suspend fun updateUser(
        @Header("Authorization") token: String,
        @Field("_method") _method:String = "patch",
        @Field("name") name: String
    ): Response<AuthUpdateAnswer>
}

object AuthService {
    val authService: AuthApiService by lazy { retrofit.create(AuthApiService::class.java) }
//    val userService: AuthApiService by lazy { retrofitJson.create(AuthApiService::class.java) }
}

private val moshi = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(API_SERVER)
    .build()

//private val retrofitJson = Retrofit.Builder()
//    .addConverterFactory(MoshiConverterFactory.create(moshi))
//    .addConverterFactory(GsonConverterFactory.create())
//    .baseUrl(API_SERVER)
//  .build()
