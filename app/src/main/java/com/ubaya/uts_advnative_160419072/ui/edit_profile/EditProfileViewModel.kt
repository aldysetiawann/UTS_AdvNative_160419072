package com.ubaya.uts_advnative_160419072.ui.edit_profile

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.ubaya.uts_advnative_160419072.Globals
import com.ubaya.uts_advnative_160419072.models.User
import kotlinx.coroutines.launch
import org.json.JSONObject

class EditProfileViewModel(private val app: Application) : AndroidViewModel(app) {

    val success = MutableLiveData(false)
    val loading = MutableLiveData(false)
    val error = MutableLiveData(false)
    val errorMsg = MutableLiveData("")

    fun edit(username: String, password: String) {
        loading.value = true
        viewModelScope.launch {
            val oldUser = Globals.user
            val url = "http://10.0.2.2:8080/ubayakuliner/editprofil.php"
            val queue = Volley.newRequestQueue(app.applicationContext)
            val request = object : StringRequest(
                Method.POST, url,
                {
                    loading.value = false
                    val res = JSONObject(it)

                    if (res.getString("status") == "ok") {
                        val newUser = User(oldUser.id, username)
                        Globals.user = newUser
                        val prefs =
                            app.applicationContext.getSharedPreferences("app", Context.MODE_PRIVATE)
                        prefs.edit {
                            this.clear()
                            this.putString("user", Gson().toJson(newUser))
                        }

                        success.value = true
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
                    return hashMapOf(
                        "user" to oldUser.id.toString(),
                        "username" to username,
                        "password" to password
                    )
                }
            }

            queue.add(request)
        }
    }
}