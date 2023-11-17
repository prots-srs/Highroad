package com.protsprog.highroad.data

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