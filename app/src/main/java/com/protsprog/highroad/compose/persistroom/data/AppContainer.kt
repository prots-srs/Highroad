package com.protsprog.highroad.compose.persistroom.data

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppInventoryContainer {
    val itemsRepository: ItemsRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineItemsRepository]
 */
class AppDataInventoryContainer(private val context: Context) : AppInventoryContainer {
    /**
     * Implementation for [ItemsRepository]
     */
    override val itemsRepository: ItemsRepository by lazy {
        OfflineItemsRepository(InventoryDatabase.getDatabase(context).itemDao())
    }
}