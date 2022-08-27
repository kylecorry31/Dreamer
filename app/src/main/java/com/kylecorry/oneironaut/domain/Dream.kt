package com.kylecorry.oneironaut.domain

import java.time.Instant

data class Dream(
    val id: Long = 0,
    val time: Instant = Instant.now(),
    val title: String = "",
    val description: String = "",
    val isLucid: Boolean = false,
    val isRecurring: Boolean = false,
    val isNightmare: Boolean = false,
    val elements: List<DreamElement> = emptyList()
)
