package com.mrh.storyapp.auth

import okhttp3.Interceptor
import okhttp3.Response

class OAuthInterceptor constructor(
    private val accessToken: String
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder().header("Authorization", "$accessToken")
            .build()
        return chain.proceed(request)
    }
}