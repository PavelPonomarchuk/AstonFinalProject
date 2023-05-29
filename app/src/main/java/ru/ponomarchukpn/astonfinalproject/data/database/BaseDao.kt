package ru.ponomarchukpn.astonfinalproject.data.database

import androidx.room.Insert
import androidx.room.OnConflictStrategy

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: List<T>)
}
