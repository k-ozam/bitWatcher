package jp.maskedronin.bitwatcher

import jp.maskedronin.bitwatcher.domain.valueobject.Exchange

interface ApiAuthInfoProvider {
    fun getInfo(exchange: Exchange): ApiAuthInfo
}

data class ApiAuthInfo(val apiKey: String, val apiSecret: String)