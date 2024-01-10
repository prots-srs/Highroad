package com.protsprog.highroad.datetime

/*
TO READ

https://github.com/Kotlin/kotlinx-datetime

https://raed-o-ghazal.medium.com/kotlinx-localdatetime-manipulation-for-kmm-eacfede93aba

 */

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.minus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.DurationUnit
import kotlin.time.toDuration

fun LocalDateTime.Companion.now(): LocalDateTime {
    return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
}

fun LocalDate.Companion.now(): LocalDate {
    return LocalDateTime.now().date
}

fun LocalTime.Companion.now(): LocalTime {
    return LocalDateTime.now().time
}

/*fun LocalTime.Companion.min(): LocalTime {
    return LocalTime(0, 0)
}

fun LocalTime.Companion.max(): LocalTime {
    return LocalTime(23, 59, 59, 999999999)
}

fun LocalDate.atStartOfDay(): LocalDateTime {
    return LocalDateTime(this, LocalTime.min())
}

fun LocalDate.atEndOfDay(): LocalDateTime {
    return LocalDateTime(this, LocalTime.max())
}*/

fun LocalDateTime.plus(value: Long, unit: DateTimeUnit): LocalDateTime {
    val timeZone = TimeZone.currentSystemDefault()
    return this.toInstant(timeZone)
//        .plus(value.toDuration(unit))
        .plus(value, unit, timeZone)
        .toLocalDateTime(timeZone)
}

fun LocalDateTime.minus(value: Long, unit: DateTimeUnit): LocalDateTime {
    val timeZone = TimeZone.currentSystemDefault()
    return this.toInstant(timeZone)
//        .minus(value.toDuration(unit))
        .minus(value, unit, timeZone)
        .toLocalDateTime(timeZone)
}

//fun LocalDateTime.sprintf(format: String, args: Array<String>):String {
//    return format.rep
//}

fun insertPrefZero(value: Int) :String  = if (value < 10) "0${value}" else "$value"