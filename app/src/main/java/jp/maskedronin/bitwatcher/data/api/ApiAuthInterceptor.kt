package jp.maskedronin.bitwatcher.data.api

import jp.maskedronin.bitwatcher.data.extension.getAnnotation
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

abstract class ApiAuthInterceptor : Interceptor {
    final override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()

        val isAuthRequired: Boolean = originalRequest.getAnnotation(RequireAuth::class) != null
        val request: Request = if (isAuthRequired) {
            getAuthenticatedRequest(originalRequest)
        } else {
            originalRequest
        }
        return chain.proceed(request)
    }

    abstract fun getAuthenticatedRequest(request: Request): Request
}

