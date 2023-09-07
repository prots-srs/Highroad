package com.protsprog.highroad.data

import com.protsprog.highroad.data.network.SallyResponseResource
import com.protsprog.highroad.data.network.asSallyResponseResourceSuspend
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/*
suspend fun getDetailAgentVisitFullResponse(
    token: String,
    id: Int
): SallyResponseResource<BaseResponse<GetAgentDetailResponse>> {
    return asSallyResponseResourceSuspend {
        agentVisitService.getDetailAgentVisitWithFullResponse(
            token = token,
            id = id
        )
    }
}

fun getDetailAgentVisitFullResponseFlow(
    token: String,
    id: Int
): Flow<BaseResponse<GetAgentDetailResponse>> {
    return flow {
        while (true) {
            val getDetailAgent = agentVisitService.getDetailAgentVisitWithFullResponse(
                token = token,
                id = id
            )
            emit(getDetailAgent)
            delay(5000L)
        }
    }
}
*/