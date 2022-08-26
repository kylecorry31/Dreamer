package com.kylecorry.oneironaut.infrastructure.persistence

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.kylecorry.oneironaut.app.AppDatabase
import com.kylecorry.oneironaut.domain.Dream

class DreamRepo(private val dao: DreamDao) {

    fun getAllLive(): LiveData<List<Dream>> {
        return Transformations.map(dao.getAll()) {
            it.map { it.toDream() }
        }
    }

    suspend fun get(id: Long): Dream? {
        return dao.get(id)?.toDream()
    }

    suspend fun add(dream: Dream): Long {
        return if (dream.id == 0L) {
            dao.insert(DreamEntity.fromDream(dream))
        } else {
            dao.update(DreamEntity.fromDream(dream))
            dream.id
        }
    }

    suspend fun delete(dream: Dream) {
        dao.delete(DreamEntity.fromDream(dream))
    }

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: DreamRepo? = null

        fun getInstance(context: Context): DreamRepo {
            return instance ?: synchronized(this) {
                instance ?: DreamRepo(AppDatabase.getInstance(context).dreamDao()).also {
                    instance = it
                }
            }
        }
    }
}