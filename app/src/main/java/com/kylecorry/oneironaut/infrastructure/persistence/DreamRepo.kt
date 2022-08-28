package com.kylecorry.oneironaut.infrastructure.persistence

import com.kylecorry.oneironaut.domain.Dream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DreamRepo @Inject constructor(private val dao: DreamDao) {

    suspend fun get(date: LocalDate): Dream? = withContext(Dispatchers.IO) {
        dao.get(date.format(DateTimeFormatter.ISO_LOCAL_DATE))?.toDream()
    }

    suspend fun get(id: Long): Dream? = withContext(Dispatchers.IO) {
        dao.get(id)?.toDream()
    }

    suspend fun add(dream: Dream): Long = withContext(Dispatchers.IO) {
        if (dream.id == 0L) {
            dao.insert(DreamEntity.fromDream(dream))
        } else {
            dao.update(DreamEntity.fromDream(dream))
            dream.id
        }
    }

    suspend fun delete(dream: Dream) = withContext(Dispatchers.IO) {
        dao.delete(DreamEntity.fromDream(dream))
    }
}