package com.doach.mediasearchapp.android.data.remote.retrofit

import android.content.Context
import com.doach.mediasearchapp.android.R
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber

class AuthenticationInterceptor(private val context: Context): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = getAuthorizedRequest(chain.request())
        val response = chain.proceed(request)

        Timber.d("[Request] ${request.url()}" +
                "\nheader: ${request.headers()} " +
                "\nbody: ${request.body()}")

        Timber.d("[Response] ${response.request().url()}" +
                "\nheader: ${response.headers()} " +
                "\nbody: ${response.peekBody(Long.MAX_VALUE).string()}")

        return response
    }

    private fun getAuthorizedRequest(request: Request): Request {
        return request.newBuilder()
            .addHeader("Authorization", "KakaoAK ${context.getString(R.string.kakao_rest_api_key)}")
            .build()
    }
}