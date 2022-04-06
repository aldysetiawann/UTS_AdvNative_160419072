package com.ubaya.uts_advnative_160419072.ui.register

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.ubaya.uts_advnative_160419072.models.User
import kotlinx.coroutines.launch
import org.json.JSONObject


class RegisterViewModel(private val app: Application) : AndroidViewModel(app) {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    val loading = MutableLiveData(false)
    val error = MutableLiveData(false)
    val errorMsg = MutableLiveData("")

    fun register(username: String, password: String) {
        loading.value = true
        viewModelScope.launch {
            val url = "http://10.0.2.2:8080/ubayakuliner/register.php"
            val queue = Volley.newRequestQueue(app.applicationContext)
            val request = object : StringRequest(
                Method.POST, url,
                {
                    Log.d("REGISTER", it)
                    loading.value = false
                    val res = JSONObject(it)

                    if (res.getString("status") == "ok") {
                        _user.value = User(res.getInt("data"), username)
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
                    return hashMapOf("username" to username, "password" to password)
                }
            }

            queue.add(request)
        }
    }
}