package com.ubaya.uts_advnative_160419072.ui.reviews

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ubaya.uts_advnative_160419072.Globals
import com.ubaya.uts_advnative_160419072.models.Review
import kotlinx.coroutines.launch
import org.json.JSONObject

class ReviewsViewModel(private val app: Application) : AndroidViewModel(app) {

    private val _listReview = MutableLiveData<List<Review>>(listOf())
    val listReview: LiveData<List<Review>> = _listReview // list[review(asd, 4, "tes"), ...]

    private val _review = MutableLiveData<Review>()
    val review: LiveData<Review> = _review

    val isReviewed = MutableLiveData(false)
    val loading = MutableLiveData(true)
    val error = MutableLiveData(false)
    val errorMsg = MutableLiveData("")

    fun loadReview(resto: Int) {
        viewModelScope.launch {
            val user = Globals.user
            val url = "http://10.0.2.2:8080/ubayakuliner/reviews.php?restoran=$resto&user=${user.username}"
            val queue = Volley.newRequestQueue(app.applicationContext)
            val request = StringRequest(url,
                {
                    Log.d("RES_REVIEW_LOAD", it)
                    loading.value = false
                    val res = JSONObject(it)
                    val data = res.getJSONObject("data")

                    if (res.getString("status") == "ok") {
                        val type = object : TypeToken<ArrayList<Review>>() {}.type

                        _listReview.value = Gson().fromJson(data.getString("reviews"), type)
                        isReviewed.value = data.getBoolean("reviewed")
                    } else {
                        error.value = true
                        errorMsg.value = res.getString("msg")
                    }
                },
                {
                    loading.value = false
                    error.value = true
                    errorMsg.value = it.message
                }
            )

            queue.add(request)
        }
    }

    fun addReview(resto: Int, rating: Int, komentar: String? = null) {
        viewModelScope.launch {
            val user = Globals.user
            val url = "http://10.0.2.2:8080/ubayakuliner/addreview.php"
            val queue = Volley.newRequestQueue(app.applicationContext)
            val request = object : StringRequest(Method.POST, url,
                {
                    Log.d("RES_REVIEW_ADD", it)
                    loading.value = false
                    val res = JSONObject(it)

                    if (res.getString("status") == "ok") {
                        _listReview.value
                        _review.value = Review(user.username, rating, komentar)
                    } else {
                        error.value = true
                        errorMsg.value = res.getString("msg")
                    }
                },
                {
                    loading.value = false
                    error.value = true
                    errorMsg.value = it.message
                }
            ) {
                override fun getParams(): MutableMap<String, String> {
                    return if (!komentar.isNullOrEmpty()) {
                        hashMapOf(
                            "restoran" to resto.toString(),
                            "user" to user.id.toString(),
                            "rating" to rating.toString(),
                            "komentar" to komentar
                        )
                    } else {
                        hashMapOf(
                            "restoran" to resto.toString(),
                            "user" to user.id.toString(),
                            "rating" to rating.toString()
                        )
                    }
                }
            }

            queue.add(request)
        }
    }
}