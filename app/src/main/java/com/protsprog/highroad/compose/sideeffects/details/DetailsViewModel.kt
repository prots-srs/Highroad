package com.protsprog.highroad.compose.sideeffects.details

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.protsprog.highroad.compose.sideeffects.base.Result
import com.protsprog.highroad.compose.sideeffects.data.DestinationsRepository
import com.protsprog.highroad.compose.sideeffects.data.ExploreModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val destinationsRepository: DestinationsRepository,
//    savedStateHandle: SavedStateHandle
) : ViewModel() {

    //    private val cityName = savedStateHandle.get<String>(KEY_ARG_DETAILS_CITY_NAME)!!
    private var cityName:String = ""// = mutableStateOf("")

    fun setCity(city:String) {
        cityName = city
    }

    val cityDetails: Result<ExploreModel>
        get() {
            val destination = destinationsRepository.getDestination(cityName)
            return if (destination != null) {
                Result.Success(destination)
            } else {
                Result.Error(IllegalArgumentException("City doesn't exist"))
            }
        }
}
