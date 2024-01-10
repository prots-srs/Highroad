package com.protsprog.highroad.datetime


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import javax.inject.Inject
import kotlin.time.DurationUnit

data class DatetimeUiState(
    val localDateTime: LocalDateTime,
    val datetimeFormatted: String = "",
    val localDate: LocalDate,
    val localTime: LocalTime,
//    val atStartOfDay: String = "",
//    val atEndOfDay: String = ""
    val dateTimeAfterOneHour: LocalDateTime,
    val dateTimeAfterOneDay: LocalDateTime,
    val dateTime30MinEarlier: LocalDateTime,
    val dayOfYear: LocalDate,
    val daysInMonth: LocalDate,
    val nextMonth: YearMonth,
    val prevMonth: YearMonth,
    val range: ClosedRange<LocalDate>
)

@HiltViewModel
class DatetimeViewModel @Inject constructor(

) : ViewModel() {
    private val datetime: LocalDateTime = LocalDateTime.now()
    private val localDate = LocalDate.now()
    private val localTime = LocalTime.now()

    private val yearMonth = YearMonth(2023, Month.APRIL)
    private val twoMonthsEarlier: YearMonth = yearMonth.plusMonths(2)
    private val sevenMonthsForward: YearMonth = yearMonth.minusMonths(7)

    var uIState by mutableStateOf(
        DatetimeUiState(
            localDateTime = datetime,
            localDate = localDate,
            localTime = localTime,
            datetimeFormatted = "%s.%s.%s %s:%s".format(
                insertPrefZero(datetime.date.dayOfMonth),
                insertPrefZero(datetime.date.monthNumber),
                datetime.date.year.toString(),
                insertPrefZero(localTime.hour),
                insertPrefZero(localTime.minute)
            ),
//            atStartOfDay = localDate.atStartOfDay().toString(),
//            atEndOfDay = localDate.atEndOfDay().toString()
            dateTimeAfterOneHour = datetime.plus(1, DateTimeUnit.HOUR),
            dateTimeAfterOneDay = datetime.plus(24, DateTimeUnit.HOUR),
            dateTime30MinEarlier = datetime.minus(30, DateTimeUnit.MINUTE),
            dayOfYear = yearMonth.atDay(13),
            daysInMonth = yearMonth.atEndOfMonth(),
            nextMonth = twoMonthsEarlier,
            prevMonth = sevenMonthsForward,
            range = twoMonthsEarlier.atDay(1)..sevenMonthsForward.atEndOfMonth(),
        )
    )
}