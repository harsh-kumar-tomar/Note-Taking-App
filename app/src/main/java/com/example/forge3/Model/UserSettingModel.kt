package com.example.forge3.Model

data class UserSettingModel(
    val userID: String = "",
    val userName: String = "",
    val userEmail: String = "",
    val userPhoto: String = ""
) {
    constructor() : this("", "", "", "")
}