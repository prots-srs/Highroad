package com.protsprog.highroad.compose.sideeffects

/*
READ
https://developer.android.com/topic/architecture/ui-layer/state-production
https://developer.android.com/kotlin/flow/stateflow-and-sharedflow
https://developer.android.com/jetpack/androidx/releases/lifecycle
https://medium.com/androiddevelopers/consuming-flows-safely-in-jetpack-compose-cde014d0d5a3
 */

import com.protsprog.highroad.compose.sideeffects.data.DestinationsRepository
import com.protsprog.highroad.compose.sideeffects.data.ExploreModel
import com.protsprog.highroad.compose.sideeffects.di.DefaultDispatcher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.random.Random

const val MAX_PEOPLE = 4

@HiltViewModel
class MainViewModel @Inject constructor(
    private val destinationsRepository: DestinationsRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    val hotels: List<ExploreModel> = destinationsRepository.hotels
    val restaurants: List<ExploreModel> = destinationsRepository.restaurants

    private val _suggestedDestinations = MutableStateFlow<List<ExploreModel>>(emptyList())
    val suggestedDestinations: StateFlow<List<ExploreModel>> = _suggestedDestinations.asStateFlow()

    init {
        _suggestedDestinations.value = destinationsRepository.destinations
    }

    fun updatePeople(people: Int) {
        viewModelScope.launch {
            if (people > MAX_PEOPLE) {
                _suggestedDestinations.value = emptyList()
            } else {
                val newDestinations = withContext(defaultDispatcher) {
                    destinationsRepository.destinations
                        .shuffled(Random(people * (1..100).shuffled().first()))
                }
                _suggestedDestinations.value = newDestinations
            }
        }
    }

    fun toDestinationChanged(newDestination: String) {
        viewModelScope.launch {
            val newDestinations = withContext(defaultDispatcher) {
                destinationsRepository.destinations
                    .filter { it.city.nameToDisplay.contains(newDestination) }
            }
            _suggestedDestinations.value = newDestinations
        }
    }
}