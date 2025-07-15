package Util

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.estacionamiento.R

class Common {

    public fun SoloFecha(context: Context, datePicker: EditText): Calendar {

        val calendar = Calendar.getInstance()

        // Fecha
        datePicker.setOnClickListener {
            DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    val formattedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
                    datePicker.setText(formattedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        return  calendar
    }

    public fun SoloHora(context: Context, hora: EditText, calendar: Calendar) {
        // Hora
        hora.setOnClickListener {
            TimePickerDialog(
                context,
                { _, hourOfDay, minute ->
                    hora.setText(formatTime(hourOfDay, minute))
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
            ).show()
        }

    }

    private fun formatTime(hourOfDay: Int, minute: Int): String {
        val amPm = if (hourOfDay >= 12) "PM" else "AM"
        val hour = if (hourOfDay % 12 == 0) 12 else hourOfDay % 12
        val min = minute.toString().padStart(2, '0')
        return "$hour:$min $amPm"
    }

    public fun mostrarToastPersonalizado(context: Context, mensaje: String, tipo: TipoToast) {
        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(R.layout.toast_personalizado, null)

        val txtMensaje = layout.findViewById<TextView>(R.id.mensaje)
        val icono = layout.findViewById<ImageView>(R.id.icono)
        txtMensaje.text = mensaje

        when (tipo) {
            TipoToast.EXITO -> {
                layout.setBackgroundResource(R.drawable.bg_toast_success)
                icono.setImageResource(R.drawable.ic_success)
            }
            TipoToast.ADVERTENCIA -> {
                layout.setBackgroundResource(R.drawable.bg_toast_warning)
                icono.setImageResource(R.drawable.ic_warning)
            }
            TipoToast.ERROR -> {
                layout.setBackgroundResource(R.drawable.bg_toast_error)
                icono.setImageResource(R.drawable.ic_error)
            }
            TipoToast.NORMAL -> {
                layout.setBackgroundResource(R.drawable.bg_toast_default)
                icono.setImageResource(R.drawable.ic_info)
            }
        }

        Toast(context).apply {
            duration = Toast.LENGTH_LONG
            view = layout
            show()
        }
    }

    fun limpiarCampos(vararg campos: EditText) {
        campos.forEach { it.text.clear() }
    }
}