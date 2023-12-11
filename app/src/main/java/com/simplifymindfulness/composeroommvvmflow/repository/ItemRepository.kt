package com.simplifymindfulness.composeroommvvmflow.repository

import com.simplifymindfulness.composeroommvvmflow.room.Item
import com.simplifymindfulness.composeroommvvmflow.room.ItemDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ItemRepository @Inject constructor(private val itemDao: ItemDao) {
    val allItems: Flow<List<Item>> = itemDao.getAllItems()

    suspend fun insert(item: Item) {
        itemDao.insertItem(item)
    }

    suspend fun update(item: Item) {
        itemDao.updateItem(item)
    }

    suspend fun delete(item: Item) {
        itemDao.deleteItem(item)
    }
}
