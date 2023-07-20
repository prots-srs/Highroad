package com.protsprog.highroad.compose.persistroom.ui.item

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.protsprog.highroad.compose.persistroom.data.ItemsRepository
import com.protsprog.highroad.nav.InventoryItemEditDestination
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel to retrieve and update an item from the [ItemsRepository]'s data source.
 */
class ItemEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val itemRepository: ItemsRepository
) : ViewModel() {

    /**
     * Holds current item ui state
     */
    var itemUiState by mutableStateOf(ItemUiState())
        private set

    private val itemId: Int = checkNotNull(savedStateHandle[InventoryItemEditDestination.itemIdArg])

    private fun validateInput(uiState: ItemDetails = itemUiState.itemDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && price.isNotBlank() && quantity.isNotBlank()
        }
    }

    init {
        viewModelScope.launch {
            itemUiState = itemRepository.getItemStream(itemId)
                .filterNotNull()
                .first()
                .toItemUiState()
        }
    }

    fun updateUiState(itemDetail: ItemDetails) {
        itemUiState = ItemUiState(itemDetails = itemDetail, isEntryValid = validateInput(itemDetail))
    }

    suspend fun updateItem() {
        if(validateInput(itemUiState.itemDetails)) {
            itemRepository.updateItem(itemUiState.itemDetails.toItem())
        }
    }
}
