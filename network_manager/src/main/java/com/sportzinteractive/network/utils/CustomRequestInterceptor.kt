package com.sportzinteractive.network.utils

import com.sportzinteractive.network.helper.BaseConfigContract
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class CustomRequestInterceptor @Inject constructor(
    private val baseConfigContract: BaseConfigContract
) : Interceptor {

    @Throws(IllegalArgumentException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        builder.addHeader("auth", baseConfigContract.getApiAuthKey())
        val token = baseConfigContract.getUserToken()
        token?.let {
            builder.addHeader("usertoken", it)
        }
        return chain.proceed(builder.build())
    }
}
