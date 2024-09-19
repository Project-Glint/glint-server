package com.hola.glint.security.oauth2.user

import com.hola.glint.security.oauth2.AuthProvider

data class OAuth2UserInfo(
    var id: String = "",
    var name: String = "",
    var email: String = "",
    var imageUrl: String = "",
    var provider: AuthProvider = AuthProvider.LOCAL
)
