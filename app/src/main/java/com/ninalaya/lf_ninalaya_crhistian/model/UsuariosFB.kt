package com.ninalaya.lf_ninalaya_crhistian.model

import android.provider.ContactsContract.CommonDataKinds.Email

data class UsuariosFB(
    val id: Int,
    var name:String,
    var email: String
){
    constructor() : this(0, "", "")
}
