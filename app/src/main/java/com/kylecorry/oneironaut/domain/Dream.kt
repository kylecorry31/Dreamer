package com.kylecorry.oneironaut.domain

import java.time.LocalDate

data class Dream(
    val id: Long = 0,
    val date: LocalDate,
    val description: String = "",
    val isLucid: Boolean = false,
    val isRecurring: Boolean = false,
    val isNightmare: Boolean = false,
    val elements: List<DreamElement> = emptyList()
)
