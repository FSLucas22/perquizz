package com.perquizz.perquizz

import kotlin.random.Random

fun randomLong(
    start: Long = 1L,
    end: Long = Long.MAX_VALUE,
) = Random.nextLong(start, end)

fun randomString(
    length: Int = 10,
    allowed: List<Char> = ('A'..'Z') + ('a'..'z') + ('0'..'9'),
): String =
    (1..length)
        .map { allowed.random() }
        .joinToString("")

fun randomEmail(length: Int = 10) = randomString(length) + "@email.com"
