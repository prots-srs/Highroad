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
        title = "Compose theming and adaptive",
    ),
    ComposePathway(
        caseId = 5,
        title="Compose animating elements"
    ),
    ComposePathway(
        caseId = 6,
        title="Advanced State and Side Effects"
    ),
    ComposePathway(
        caseId = 7,
        title="Rally navigation"
    ),
    ComposePathway(
        caseId = 8,
        title="Accessibility case"
    )
)