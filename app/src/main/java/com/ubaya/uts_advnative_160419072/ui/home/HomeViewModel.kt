package com.ubaya.uts_advnative_160419072.ui.home

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
import com.ubaya.uts_advnative_160419072.models.Resto
import kotlinx.coroutines.launch
import org.json.JSONObject

class HomeViewModel(val app: Application) : AndroidViewModel(app) {

    private val _listResto = MutableLiveData<List<Resto>>(listOf())
    val listResto: LiveData<List<Resto>> = _listResto

    val loading = MutableLiveData(true)
    val error = MutableLiveData(false)
    val errorMsg = MutableLiveData("")

    fun loadListResto() {
        viewModelScope.launch {
            val url = "http://10.0.2.2/ubayakuliner/restoran.php"
            val queue = Volley.newRequestQueue(app.applicationContext)
            val request = StringRequest(url,
                {
                    Log.d("RES_HOME", it)
                    loading.value = false
                    val res = JSONObject(it)

                    if (res.getString("status") == "ok") {
                        val typeToken = object : TypeToken<ArrayList<Resto>>() {}.type
                        _listResto.value = Gson().fromJson(res.getString("data"), typeToken)
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