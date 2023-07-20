package com.protsprog.highroad

import com.protsprog.highroad.Cookie.Companion.toStiker
import org.junit.Test

class CollectionsTest {
    @Test
    fun TestForEach() {
        cookies.forEach {
            println("Menu item: ${it.toStiker()}")
        }
    }

    @Test
    fun TestMap() {
        val fullMenu = cookies.map {
            "${it.name} - $${it.price}"
        }
        println("Full menu:")
        fullMenu.forEach {
            println(it)
        }
    }

    @Test
    fun TestFilter() {
        println("")
        val expensiveMenu = cookies.filter { it.price > 1.5 }
        println("Expensive menu:")
        expensiveMenu.forEach {
            println(it.toStiker())
        }

        val softBakedMenu = cookies.filter {
            it.softBaked
        }
        println("Soft cookies:")
        softBakedMenu.forEach {
            println(it.toStiker())
        }
        println("")
    }

    @Test
    fun TestGroupBy() {
        //return Map, here can use partition() https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/partition.html
        val groupedMenu = cookies.groupBy {
            it.softBaked
        }
        val softbakedMenu = groupedMenu[true] ?: listOf()
        val crunchyMeny = groupedMenu[false] ?: listOf()
        println("Soft cookies:")
        softbakedMenu.forEach {
            println(it.toStiker())
        }
        println("Crunchy cookies:")
        crunchyMeny.forEach {
            println(it.toStiker())
        }
    }

    @Test
    fun TestFold() {
        val totalPrice = cookies.fold(0.0) { total, cookie ->
            total + cookie.price
        }
        println("Total price: $${totalPrice}")
    }

    @Test
    fun TestSortedBy() {
        println("")
        val sortedByPrice = cookies.sortedBy { it.price }
        val sortedByName = cookies.sortedBy { it.name }
        println(" Sorted by price:")
        sortedByPrice.forEach {
            println(it.toStiker())
        }
        println(" Alphabetical menu:")
        sortedByName.forEach {
            println(it.toStiker())
        }
        println("")
    }
}

data class Cookie(
    val name: String,
    val softBaked: Boolean,
    val hasFilling: Boolean,
    val price: Double
) {
    companion object {
        fun Cookie.toStiker(): String = "$name - $$price"
    }
}

val cookies = listOf(
    Cookie(
        name = "Chocolate Chip",
        softBaked = false,
        hasFilling = false,
        price = 1.69
    ),
    Cookie(
        name = "Banana Walnut",
        softBaked = true,
        hasFilling = false,
        price = 1.49
    ),
    Cookie(
        name = "Vanilla Creme",
        softBaked = false,
        hasFilling = true,
        price = 1.59
    ),
    Cookie(
        name = "Chocolate Peanut Butter",
        softBaked = false,
        hasFilling = true,
        price = 1.49
    ),
    Cookie(
        name = "Snickerdoodle",
        softBaked = true,
        hasFilling = false,
        price = 1.39
    ),
    Cookie(
        name = "Blueberry Tart",
        softBaked = true,
        hasFilling = true,
        price = 1.79
    ),
    Cookie(
        name = "Sugar and Sprinkles",
        softBaked = false,
        hasFilling = false,
        price = 1.39
    )
)