package com.protsprog.highroad.compose.persistroom.ui.item

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.protsprog.highroad.compose.persistroom.data.Item
import com.protsprog.highroad.compose.persistroom.data.ItemsRepository
import com.protsprog.highroad.compose.persistroom.ui.home.HomeUiState
import com.protsprog.highroad.compose.persistroom.ui.home.HomeViewModel
import com.protsprog.highroad.nav.InventoryItemDetailsDestination
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel to retrieve, update and delete an item from the [ItemsRepository]'s data source.
 */
class ItemDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val itemRepository: ItemsRepository
) : ViewModel() {

    private val itemId: Int = checkNotNull(savedStateHandle[InventoryItemDetailsDestination.itemIdArg])
    val stateUi: StateFlow<ItemDetailsUiState> = itemRepository.getItemStream(itemId)
        .filterNotNull()
        .map {
            ItemDetailsUiState(outOfStock = it.quantity <= 0, itemDetails = it.toItemDetails())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = ItemDetailsUiState()
        )

    fun reduceQuantityByOne() {
        viewModelScope.launch {
            val currentItem = stateUi.value.itemDetails.toItem()
            if(currentItem.quantity > 0) {
                itemRepository.updateItem(currentItem.copy(quantity = currentItem.quantity - 1))
            }
        }
    }

    suspend fun deleteItem() {
        itemRepository.deleteItem(stateUi.value.itemDetails.toItem())
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * UI state for ItemDetailsScreen
 */
data class ItemDetailsUiState(
    val outOfStock: Boolean = true,
    val itemDetails: ItemDetails = ItemDetails()
)
