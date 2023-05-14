package com.protsprog.highroad.compose

data class ComposePathway(
    val caseId: Int,
    val title: String,
)

val composePathways = listOf<ComposePathway>(
    ComposePathway(
        caseId = 1,
        title = "Compose introduce",
    ),
    ComposePathway(
        caseId = 2,
        title = "Compose basic layout",
    ),
    ComposePathway(
        caseId = 3,
        title = "Compose states",
    ),
    ComposePathway(
        caseId = 4,
        title = "Compose theming",
    )
)