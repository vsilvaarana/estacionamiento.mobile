package com.example.estacionamiento

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import java.text.SimpleDateFormat
import java.util.*

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

        val fechaActual = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val util = Util.Common()
        val desde = findViewById<EditText>(R.id.txtFechaDesde)
        val hasta = findViewById<EditText>(R.id.txtFechaHasta)
        val calendar = util.SoloFecha(this, desde)
        val calendar2 = util.SoloFecha(this, hasta)

        desde.setText(fechaActual.format(calendar.time))
        hasta.setText(fechaActual.format(calendar.time))

        val btn = findViewById<Button>(R.id.btnBuscarReservas)

        btn.setOnClickListener {
            buscar()
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

    fun buscar(){

        val usuarioId = 1
        val desde = findViewById<EditText>(R.id.txtFechaDesde)
        val hasta = findViewById<EditText>(R.id.txtFechaHasta)
        val fechaInicio = reformatearFecha(desde.text.toString())
        val fechaFin = reformatearFecha(hasta.text.toString())

        val url = "https://f2hq9czze2.execute-api.us-east-1.amazonaws.com/v1/api/Reserva/ListarPorUsuario?usuarioId=$usuarioId&fechaInicio=$fechaInicio&fechaFin=$fechaFin&tipo=0"

        val stringRequest = JsonArrayRequest(Request.Method.GET, url, null, {
                response ->
            try {
                //val jsonArray = response.getJSONArray("data")

                val jsonArray = response // JSONArray(response.toString())
                Log.i("======>", jsonArray.toString())
                val items = mutableListOf<String>()
                for (i in 0 until jsonArray.length()) {
                    val reservas = jsonArray.getJSONObject(i)
                    items.add("${reservas.getString("placa")} - ${reservas.getString("espacio")} - ${reservas.getString("fecha")}")
                }

                val lstReservas = findViewById<ListView>(R.id.lstReservas)
                val adaptador = ArrayAdapter(
                    this,
                    android.R.layout.simple_list_item_1,
                    items
                )
                lstReservas.adapter = adaptador

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

    fun reformatearFecha(fechaOriginal: String): String {
        return try {
            val formatoEntrada = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formatoSalida = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
            val fecha = formatoEntrada.parse(fechaOriginal) ?: return ""
            formatoSalida.format(fecha)
        } catch (e: Exception) {
            "" // Error de formato
        }
    }
}