package com.sportzinteractive.network.di

import com.sportzinteractive.network.helper.BaseConfigContract
import com.sportzinteractive.network.utils.CurlLoggingInterceptor
import com.sportzinteractive.network.utils.CustomRequestInterceptor
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun gson(): Gson = GsonBuilder().create()


    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BASIC
        return interceptor
    }

    @Singleton
    @Provides
    fun providesCurlInterceptor(baseInfo: BaseConfigContract): CurlLoggingInterceptor {
        return CurlLoggingInterceptor("cURL",baseInfo)
    }

    @Singleton
    @Provides
    fun providesOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        curlLoggingInterceptor: CurlLoggingInterceptor,
        customRequestInterceptor: CustomRequestInterceptor,
        baseInfo: BaseConfigContract
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .addNetworkInterceptor(customRequestInterceptor)
            .apply {
                if (baseInfo.getIsDebugMode()) {
                    addInterceptor(httpLoggingInterceptor)
                    addInterceptor(curlLoggingInterceptor)
                    addNetworkInterceptor(StethoInterceptor())
                }
            }.build()
    }






    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
        baseInfo: BaseConfigContract
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseInfo.getBaseUrl())
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build()

}