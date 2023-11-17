package com.protsprog.highroad

/*
TO READ

https://www.ictdemy.com/kotlin/oop/date-and-time-in-kotlin-creating-and-formatting

https://www.oracle.com/technical-resources/articles/java/jf14-date-time.html
 */
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class DateTimeTest {
    @Test
    fun check_Date() {
        println("-")

        // Current date and time
        val dateTime = LocalDateTime.now()
        println(dateTime)

// Curent date
        val date = LocalDate.now()
        println(date)

// Current time
        val time = LocalTime.now()
        println(time)

        println(dateTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)))
        println(dateTime.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)))
        println(dateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT)))
        println(dateTime.format(DateTimeFormatter.ofPattern("M/d/y H:m:ss")))
        println("${dateTime.toEpochSecond(ZoneOffset.UTC).toInt()}")

        println("-")
    }
}