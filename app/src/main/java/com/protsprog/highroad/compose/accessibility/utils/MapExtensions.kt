package com.protsprog.highroad.compose.accessibility.utils

internal fun <E> MutableSet<E>.addOrRemove(element: E) {
    if (!add(element)) {
        remove(element)
    }
}
