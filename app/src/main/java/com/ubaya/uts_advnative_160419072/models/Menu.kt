package com.ubaya.uts_advnative_160419072.models

import java.text.NumberFormat
import java.util.*

data class Menu(
    val id: Int,
    val nama: String,
    val deskripsi: String,
    val harga: Int
) {
    fun hargaText() : String {
        val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        val rupiah = formatter.format(harga).replace("Rp", "")

        return "Rp " + rupiah.substring(0, rupiah.length - 3)
    }
}
