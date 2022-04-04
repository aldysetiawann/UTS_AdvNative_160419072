package com.ubaya.uts_advnative_160419072.models

data class Review(
    val id: Int,
    val user: User,
    val rating: Int,
    val komentar: String
)
