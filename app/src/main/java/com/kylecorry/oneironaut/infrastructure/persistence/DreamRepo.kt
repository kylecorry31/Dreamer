package com.kylecorry.oneironaut.infrastructure.persistence

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.kylecorry.oneironaut.domain.Dream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DreamRepo @Inject constructor(private val dao: DreamDao) {

    fun getLive(start: Instant, end: Instant): LiveData<List<Dream>> {
        return Transformations.map(dao.getAll(start.toEpochMilli(), end.toEpochMilli())) {
            it.map { it.toDream() }
        }
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