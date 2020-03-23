package jp.maskedronin.bitwatcher.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ClientBuiltin

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Encrypt