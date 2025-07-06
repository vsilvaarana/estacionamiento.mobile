package com.example.estacionamiento

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray
import org.json.JSONException

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

        val btnAgregar = findViewById<FloatingActionButton>(R.id.fabAgregar)

        btnAgregar.setOnClickListener {
            var intent = Intent(this, VehiculoCrearActivity::class.java)
            startActivity(intent)
        }

//        val vehiculos: ArrayList<String> = ArrayList()
//        vehiculos.add("Honda Civic - ABC123")
//        vehiculos.add("Toyota Camry - DEF456")
//        vehiculos.add("Honda Civic - GHI321")
//        vehiculos.add("Ford Focus - XYZ-987")
//
//        val listview = findViewById<ListView>(R.id.lstUsuarioVehiculo)
//
//        if (listview != null) {
//            val adaptador = ArrayAdapter (
//                this,
//                android.R.layout.simple_list_item_1,
//                vehiculos
//            )
//
//            listview.adapter = adaptador
//
//            listview.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
//                val value = parent.getItemAtPosition(position) as String
//
//                Toast.makeText(
//                    this@UsuarioVehiculoActivity,
//                    value,
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }

        buscar()
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

    fun buscar(){

        val usuarioId = 1

        val url = "https://f2hq9czze2.execute-api.us-east-1.amazonaws.com/v1/api/Vehiculo/ListarPorNombre?placa=0&usuarioId=$usuarioId"

//val stringRequest = JsonObjectRequest(Request.Method.GET, url, null, {

        val stringRequest = JsonArrayRequest(Request.Method.GET, url, null, {
                response ->
                try {
                    //val jsonArray = response.getJSONArray("data")

                    val jsonArray = response // JSONArray(response.toString())
                    Log.i("======>", jsonArray.toString())
                    val items = mutableListOf<String>()
                    for (i in 0 until jsonArray.length()) {
                        val estacionamiento = jsonArray.getJSONObject(i)
                        items.add("${estacionamiento.getString("marca")} - ${estacionamiento.getString("placa")}")
                    }

                    val lstProductos = findViewById<ListView>(R.id.lstUsuarioVehiculo)
                    val adaptador = ArrayAdapter(
                        this,
                        android.R.layout.simple_list_item_1,
                        items
                    )
                    lstProductos.adapter = adaptador

                } catch (e: JSONException) {
                    Log.i("======>", "Error:")
                    Log.i("======>", e.message.toString())
                }

        }, {
            error ->
            Log.i("=======", error.toString())
        } )

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }


}