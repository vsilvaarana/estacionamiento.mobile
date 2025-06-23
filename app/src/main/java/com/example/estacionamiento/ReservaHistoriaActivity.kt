package com.example.estacionamiento

import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ReservaHistoriaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reserva_historia)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val reservas: ArrayList<String> = ArrayList()
        reservas.add("Space 1 - 01/06/2025")
        reservas.add("Space 2 - 02/06/2025")
        reservas.add("Space 1 - 04/06/2025")
        reservas.add("Space 1 - 05/06/2025")

        val listview = findViewById<ListView>(R.id.lstReservas)

        if (listview != null) {
            val adaptador = ArrayAdapter (
                this,
                android.R.layout.simple_list_item_1,
                reservas
            )

            listview.adapter = adaptador

            listview.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                val value = parent.getItemAtPosition(position) as String

                Toast.makeText(
                    this@ReservaHistoriaActivity,
                    value,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }
}