package com.protsprog.highroad

import android.net.Uri
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.protsprog.highroad.articles.ArticlePutModel
import com.protsprog.highroad.articles.ServiceApi
import com.protsprog.highroad.authentication.domen.AuthAppLogin
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class JsonAnswerTest {

    private val itemPut = ArticlePutModel(
        id = 132, publish = false, sort = "a 0", title = "", description = "", picture = Uri.EMPTY
    )

    @Test
    fun test_parse_net_url_to_uri() {
        val url =
            "http://192.168.50.107/storage/articles/K0SJXyncOp4LRZyWi9l8GQPeHuHausBtQAQ7MyKg.jpg"

        Log.d("TEST_JSON", "test url: ${url}")

        val uri = Uri.parse(url)

        Log.d("TEST_JSON", "test uri: ${uri}")
        Log.d("TEST_JSON", "test uri: ${uri.path}")
        Log.d("TEST_JSON", "test uri: ${uri.host}")
        Log.d("TEST_JSON", "test uri: ${uri.scheme}")
    }

    @Test
    fun test_json_decode_response() = runTest {
        try {
            val response = ServiceApi.articleService.testPutItem(
                id = itemPut.id.toString()
                    .toRequestBody("text/plain".toMediaTypeOrNull()),
//                publish = itemPut.publish.toString()
//                    .toRequestBody("text/plain".toMediaTypeOrNull()),
//                sort = itemPut.sort
//                    .toRequestBody("text/plain".toMediaTypeOrNull()),
//                title = itemPut.title
//                    .toRequestBody("text/plain".toMediaTypeOrNull()),
//                description = itemPut.description
//                    .toRequestBody("text/plain".toMediaTypeOrNull()),
            )

            println("-->>")
            println("${response.body()}")
            println("<<--")
//            Log.d("TEST_JSON", "response code: ${response.code()}")
//            Log.d("TEST_JSON", "response body: ${response.body()}")

        } catch (e: IOException) {
            Log.d("TEST_JSON", "network error: ${e.message}")
        }
    }
}