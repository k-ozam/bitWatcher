package jp.maskedronin.bitwatcher.data.extension

import okhttp3.Request
import retrofit2.Invocation
import kotlin.reflect.KClass

fun <T : Annotation> Request.getAnnotation(annotationClass: KClass<T>): T? =
    tag(Invocation::class.java)
        ?.method()
        ?.getAnnotation(annotationClass.java)