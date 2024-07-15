package com.sportzinteractive.network.helper



interface BaseConfigContract {
    fun getBaseUrl(): String
    fun getApiAuthKey(): String
    fun getIsDebugMode(): Boolean
    fun getUserToken():String?
}