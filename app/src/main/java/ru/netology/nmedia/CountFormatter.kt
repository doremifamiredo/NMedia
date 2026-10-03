package ru.netology.nmedia

fun formatCount(count: Int): String {
    val number = count.toString()
    return when (count) {
        in 0..999 -> number
        in 1_000..9_999 -> when (number.substring(1, 2)) {
            "0" -> number.substring(0, 1) + "K"
            else -> number.substring(0, 1) + "." + number.substring(1, 2) + "K"
        }

        in 10_000..999_999 -> number.dropLast(3) + "K"
        else -> when (number.substring(number.length - 6, number.length - 5)) {
            "0" -> number.substring(0, number.length - 6) + "M"
            else -> number.substring(0, number.length - 6) +
                    "." + number.substring(number.length - 6, number.length - 5) + "M"
        }
    }
}