package com.jimmywu.snapshotsforreddit.network.responses

//json objects that are returned after making the exchange request

data class TokenResponse(
    val access_token: String?,
    val expires_in: String?,
    val id_token: String?,
    val refresh_token: String?,
    val scope: String?,
    val token_type: String?
)
