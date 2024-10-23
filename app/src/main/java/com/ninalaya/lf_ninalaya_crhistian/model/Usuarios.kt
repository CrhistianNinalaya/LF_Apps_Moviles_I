package com.ninalaya.lf_ninalaya_crhistian.model

data class Usuarios(
    var id: Int,
    var email: String,
    var password: String,
    var name: String,
    var role: String,
    var avatar: String,
    var creationAt: String,
    var updatedAt: String
) {
    constructor() : this(
        id = 0,
        email = "",
        password = "",
        name = "",
        role = "",
        avatar = "",
        creationAt = "",
        updatedAt = ""
    )
}