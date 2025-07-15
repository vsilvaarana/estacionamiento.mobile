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

class DisponibilidadActivity : BaseMenuActivity() {

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

        val util = Util.Common()
        val fecha = findViewById<EditText>(R.id.datePicker)
        val hora = findViewById<EditText>(R.id.timeFrom)
        val horaFin = findViewById<EditText>(R.id.timeTo)
        val calendar = util.SoloFecha(this, fecha)

        util.SoloHora(this, hora, calendar)
        util.SoloHora(this, horaFin, calendar)

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
}