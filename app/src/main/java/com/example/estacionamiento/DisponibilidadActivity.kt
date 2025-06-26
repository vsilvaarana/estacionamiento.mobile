package com.example.estacionamiento

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DisponibilidadActivity : AppCompatActivity() {

    private lateinit var datePicker: EditText
    private lateinit var timeFrom: EditText
    private lateinit var timeTo: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_disponibilidad)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        datePicker = findViewById(R.id.datePicker)
        timeFrom = findViewById(R.id.timeFrom)
        timeTo = findViewById(R.id.timeTo)

        val calendar = Calendar.getInstance()

        // Fecha
        datePicker.setOnClickListener {
            DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val formattedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
                    datePicker.setText(formattedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Hora desde
        timeFrom.setOnClickListener {
            TimePickerDialog(
                this,
                { _, hourOfDay, minute ->
                    timeFrom.setText(formatTime(hourOfDay, minute))
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
            ).show()
        }

        // Hora hasta
        timeTo.setOnClickListener {
            TimePickerDialog(
                this,
                { _, hourOfDay, minute ->
                    timeTo.setText(formatTime(hourOfDay, minute))
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
            ).show()
        }

        val pisos = arrayOf(
            "Piso 1",
            "Piso 2",
            "Piso 3",
            "Piso 4",
            "Piso 5"
        )
        val spinner = findViewById<Spinner>(R.id.spPisos)
        if (spinner != null){
            val  adaptador = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                pisos
            )
            spinner.adapter = adaptador

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    Toast.makeText(
                        this@DisponibilidadActivity,
                        pisos[position],
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.principal, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_inicio -> {
                var intent = Intent(this, PrincipalActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_disponibilidad -> {
                var intent = Intent(this, DisponibilidadActivity::class.java)
                startActivity(intent)
            }

            R.id.menu_cerrar_sesion -> {
                var intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_historial -> {
                var intent = Intent(this, ReservaHistoriaActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_vehiculos -> {
                var intent = Intent(this, UsuarioVehiculoActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_realizar_reserva -> {
                var intent = Intent(this, ReservaCrearActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_reserva_activa -> {
                var intent = Intent(this, ReservaStatusActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun formatTime(hourOfDay: Int, minute: Int): String {
        val amPm = if (hourOfDay >= 12) "PM" else "AM"
        val hour = if (hourOfDay % 12 == 0) 12 else hourOfDay % 12
        val min = minute.toString().padStart(2, '0')
        return "$hour:$min $amPm"
    }
}