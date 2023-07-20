package com.protsprog.highroad.compose

data class ComposePathway(
    val caseId: Int,
    val title: String,
)

val composePathways = listOf<ComposePathway>(
    ComposePathway(
        caseId = 1,
        title = "1. Compose introduce",
    ),
    ComposePathway(
        caseId = 2,
        title = "2. Compose basic layout",
    ),
    ComposePathway(
        caseId = 3,
        title = "3. Compose states",
    ),
    ComposePathway(
        caseId = 4,
        title = "4. Compose theming and adaptive",
    ),
    ComposePathway(
        caseId = 5,
        title="5. Compose animating elements"
    ),
    ComposePathway(
        caseId = 6,
        title="6. Advanced State and Side Effects"
    ),
    ComposePathway(
        caseId = 7,
        title="7. Rally navigation"
    ),
    ComposePathway(
        caseId = 8,
        title="8. Accessibility case"
    ),
    ComposePathway(
        caseId = 9,
        title="9. Persist data with Room"
    ),
    ComposePathway(
        caseId = 10,
        title="10. Bus Schedule app"
    )
)