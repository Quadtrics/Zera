package com.example.zeraapp.models.AuthUsersListRoot

import com.example.zeraapp.models.AuthUsers.AuthUsersDatum

class AuthUserRoot {
    var success = false
    var code = 0
    var message: String? = null
    var data: AuthUsersDatum? = null
}
