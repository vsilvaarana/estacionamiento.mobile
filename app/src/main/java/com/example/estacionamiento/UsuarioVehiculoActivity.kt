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

class UsuarioVehiculoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_usuario_vehiculo)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val vehiculos: ArrayList<String> = ArrayList()
        vehiculos.add("Honda Civic - ABC123")
        vehiculos.add("Toyota Camry - DEF456")
        vehiculos.add("Honda Civic - GHI321")
        vehiculos.add("Ford Focus - XYZ-987")

        val listview = findViewById<ListView>(R.id.lstUsuarioVehiculo)

        if (listview != null) {
            val adaptador = ArrayAdapter (
                this,
                android.R.layout.simple_list_item_1,
                vehiculos
            )

            listview.adapter = adaptador

            listview.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                val value = parent.getItemAtPosition(position) as String

                Toast.makeText(
                    this@UsuarioVehiculoActivity,
                    value,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}