package com.kylecorry.oneironaut.ui.lists

interface ListItemMapper<T> {
    fun map(value: T): ListItem
}