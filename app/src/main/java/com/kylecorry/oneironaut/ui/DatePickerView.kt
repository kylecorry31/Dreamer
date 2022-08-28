package com.kylecorry.oneironaut.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import com.kylecorry.andromeda.pickers.Pickers
import com.kylecorry.oneironaut.R
import java.time.LocalDate
import java.time.ZoneId

class DatePickerView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    var date: LocalDate = LocalDate.now()
        set(value) {
            val changed = field != value
            field = value
            if (changed) {
                dateText.text = formatter.formatDate(value.atStartOfDay(ZoneId.systemDefault()), includeWeekDay = false)
                onDateChange(value)
            }
        }

    private val formatter = FormatService(context)
    private val calendar: ImageButton
    private val dateText: TextView
    private val next: ImageButton
    private val previous: ImageButton
    private var onDateChange: (LocalDate) -> Unit = {}
    private var onCalendarLongPress: () -> Unit = {
        date = LocalDate.now()
    }

    init {
        inflate(context, R.layout.view_date_picker, this)
        calendar = findViewById(R.id.date_btn)
        dateText = findViewById(R.id.date)
        next = findViewById(R.id.next_date)
        previous = findViewById(R.id.prev_date)

        dateText.text = formatter.formatDate(date.atStartOfDay(ZoneId.systemDefault()), includeWeekDay = false)

        calendar.setOnClickListener {
            Pickers.date(context, date) {
                if (it != null) {
                    date = it
                }
            }
        }

        calendar.setOnLongClickListener {
            onCalendarLongPress()
            true
        }

        next.setOnClickListener {
            date = date.plusDays(1)
        }

        previous.setOnClickListener {
            date = date.minusDays(1)
        }

    }

    fun setOnDateChangeListener(listener: (LocalDate) -> Unit) {
        onDateChange = listener
    }

    fun setOnCalendarLongPressListener(listener: () -> Unit) {
        onCalendarLongPress = listener
    }

}