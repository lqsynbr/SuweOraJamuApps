package edu.luqsiyana.suweorajamuapps.util

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateTimePickerUtil {

    /**
     * Menampilkan DatePicker dan TimePicker berurutan,
     * lalu mengembalikan hasilnya dalam format "yyy-MM-dd HH:mm"
     */

    fun showDateTimePicker(context: Context, onDateTimeSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()

        val datePicker = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)

                // setelah pilih tanggal, lanjut ke time picker
                TimePickerDialog(
                    context,
                    {_, hour, minute ->
                        selectedDate.set(Calendar.HOUR_OF_DAY, hour)
                        selectedDate.set(Calendar.MINUTE, minute)

                        val format = SimpleDateFormat("yyy-MM-dd HH:mm", Locale.getDefault())
                        val formatted = format.format(selectedDate.time)
                        onDateTimeSelected(formatted)
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                ).show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePicker.show()
    }
}