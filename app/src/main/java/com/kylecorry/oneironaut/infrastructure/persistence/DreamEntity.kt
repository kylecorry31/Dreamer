package com.kylecorry.oneironaut.infrastructure.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kylecorry.oneironaut.domain.Dream
import java.time.Instant

@Entity(
    tableName = "dreams"
)
data class DreamEntity(
    @ColumnInfo(name = "time") val time: Instant,
    @ColumnInfo(name = "title") val title: String = "",
    @ColumnInfo(name = "description") val description: String = "",
    @ColumnInfo(name = "is_lucid") val isLucid: Boolean = false,
    @ColumnInfo(name = "is_recurring") val isRecurring: Boolean = false,
    @ColumnInfo(name = "is_nightmare") val isNightmare: Boolean = false,
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var id: Long = 0

    fun toDream(): Dream {
        return Dream(
            id,
            time,
            title,
            description,
            isLucid,
            isRecurring,
            isNightmare
        )
    }

    companion object {
        fun fromDream(dream: Dream): DreamEntity {
            return DreamEntity(
                dream.time,
                dream.title,
                dream.description,
                dream.isLucid,
                dream.isRecurring,
                dream.isNightmare
            ).also {
                it.id = dream.id
            }
        }
    }

}