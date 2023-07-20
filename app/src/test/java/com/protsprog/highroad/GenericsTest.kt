package com.protsprog.highroad

import org.junit.Assert
import org.junit.Test

class GenericsTest {
    @Test
    fun `InInteger should read an Int and return this Int in String`() {
        /** Arrange **/
        val sut = InInteger()
        val readItem = 42
        val expectedResult = readItem.toString()

        /** Act **/
        val result = sut.read(readItem)

        /** Assert **/
        Assert.assertEquals(expectedResult, result)
    }

    @Test
    fun `OutInteger should write 34`() {
        /** Arrange **/
        val sut = OutInteger()
        val expectedResult = 34

        /** Act **/
        val result = sut.write()

        /** Assert **/
        Assert.assertEquals(expectedResult, result)
    }

    @Test
    fun `WhereNumberIsDouble should get the length of the String version of a Double`() {
        /** Arrange **/
        val sut = WhereNumberIsDouble()
        val item = 1234.5678
        val expectedResult = 9

        /** Act **/
        val result = sut.length(item)

        /** Assert **/
        Assert.assertEquals(expectedResult, result)
    }
}

/**
 * Interface to declare the type [T] read-only.
 */
interface In<in T> {
    fun read(item: T): String
}

/**
 * [Int] is read-only.
 */
class InInteger : In<Int> {
    override fun read(item: Int) = item.toString()
}

/**
 * Interface to declare the type [T] in write-only.
 */
interface Out<out T> {
    fun write(): T
}

/**
 * [Int] is write-only.
 */
class OutInteger : Out<Int> {
    override fun write(): Int = 34
}

/**
 * Interface for [Number].
 */
interface WhereNumber<in T> where T : Number {
    fun length(item: T): Int
}

/**
 * Implementation of [WhereNumber] with [Double].
 */
class WhereNumberIsDouble : WhereNumber<Double> {
    override fun length(item: Double): Int = item.toString().length
}