package com.ubaya.uts_advnative_160419072.ui.resto

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
import com.ubaya.uts_advnative_160419072.models.Menu
import kotlinx.coroutines.launch
import org.json.JSONObject

class RestoViewModel(private val app: Application) : AndroidViewModel(app) {

    private val _listMenu = MutableLiveData<List<Menu>>(listOf())
    val listMenu: LiveData<List<Menu>> = _listMenu

    val loading = MutableLiveData(true)
    val error = MutableLiveData(false)
    val errorMsg = MutableLiveData("")

    fun loadResto(resto: Int) {
        viewModelScope.launch {
            val url = "http://10.0.2.2/ubayakuliner/detailrestoran.php?restoran=$resto"
            val queue = Volley.newRequestQueue(app.applicationContext)
            val request = StringRequest(url,
                {
                    Log.d("RES_RESTO", it)
                    loading.value = false
                    val res = JSONObject(it)

                    if (res.getString("status") == "ok") {
                        val type = object : TypeToken<ArrayList<Menu>>() {}.type
                        _listMenu.value = Gson().fromJson(res.getString("data"), type)
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
}