package jp.maskedronin.bitwatcher.data.extension

import android.content.SharedPreferences
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.map

inline fun <reified T : Enum<T>> SharedPreferences.Editor.putEnum(
    key: String,
    value: T
): SharedPreferences.Editor = putString(key, value.name)

inline fun <reified T : Enum<T>> SharedPreferences.getNullableEnumAsFlow(targetKey: String): Flow<T?> =
    containsAsFlow(targetKey)
        .map { contains ->
            if (contains) {
                getString(targetKey, null)?.let { enumValueOf<T>(it) }
            } else null
        }

fun SharedPreferences.containsAsFlow(targetKey: String): Flow<Boolean> = channelFlow {
    channel.offer(contains(targetKey))

    val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key == targetKey) {
            channel.offer(contains(targetKey))
        }
    }
    registerOnSharedPreferenceChangeListener(listener)

    awaitClose {
        unregisterOnSharedPreferenceChangeListener(listener)
    }
}