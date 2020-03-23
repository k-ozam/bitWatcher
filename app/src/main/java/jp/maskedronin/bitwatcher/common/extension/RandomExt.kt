package jp.maskedronin.bitwatcher.common.extension

import java.security.SecureRandom

private const val ALPHANUMERIC_AND_SYMBOL =
    "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!#$%&'()-^@[;:],./=~|`{+*}<>?_"

fun SecureRandom.nextCharArray(charArray: CharArray) {
    repeat(charArray.size) { index ->
        charArray[index] = nextChar()
    }
}

fun SecureRandom.nextChar(): Char = ALPHANUMERIC_AND_SYMBOL[nextInt(
    ALPHANUMERIC_AND_SYMBOL.length)]