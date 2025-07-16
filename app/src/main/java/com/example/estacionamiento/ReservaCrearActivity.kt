package com.example.estacionamiento

import Util.Common
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class ReservaCrearActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reserva_crear)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val util = Util.Common()
        val fecha = findViewById<EditText>(R.id.etFecha)
        val hora = findViewById<EditText>(R.id.etHora)
        val horaFin = findViewById<EditText>(R.id.etHoraFin)
        val calendar = util.SoloFecha(this, fecha)

        util.SoloHora(this, hora, calendar)
        util.SoloHora(this, horaFin, calendar)
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

    fun crear(view: View){

        /*
        {
  "reservaId": 0,
  "usuarioId": 1,
  "estacionamientoId": 1,
  "placa": "AAA-123",
  "fecha": "2025-07-14T03:34:32.912Z",
  "tipo": 0,
  "estado": 1
}
         */

        val util = Util.Common()
        val etFecha = util.reformatearFecha(findViewById<EditText>(R.id.etFecha).text.toString(), "yyyy-MM-dd")
        val etHora = findViewById<EditText>(R.id.etHora).text.toString()

        val url = "https://f2hq9czze2.execute-api.us-east-1.amazonaws.com/v1/api/Reserva"
        val espacio = findViewById<EditText>(R.id.etEspacio)
        val placa = findViewById<EditText>(R.id.etPlaca).text.toString()
        val estacionamientoId = 1
        val tipo = 0
        val estado = 1
        val fecha = "$etFecha $etHora"
        val jsonObject = JSONObject()

        val formatter = SimpleDateFormat("yyyy-MM-dd h:mm a", Locale.ENGLISH)
        val fechaHora = formatter.parse(fecha)

        val calendar = Calendar.getInstance()
        calendar.time = fechaHora

        val formatoSalida = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:00'Z'", Locale.ENGLISH)
        val fechaFormateada = formatoSalida.format(calendar.time)


        try {
            var usuarioId = 1
            jsonObject.put("reservaId", 0)
            jsonObject.put("usuarioId", usuarioId)
            jsonObject.put("estacionamientoId", estacionamientoId)
            jsonObject.put("placa", placa)
            jsonObject.put("fecha", fechaFormateada)
            jsonObject.put("tipo", tipo)
            jsonObject.put("estado", estado)
            Log.i("======>", jsonObject.toString())

        }
        catch (e: JSONException){
            Log.i("======>", e.message ?: "JSONException occurred")
        }
        //JsonObjectRequest
        val jsonObjReq = object : JsonObjectRequest(Request.Method.POST, url, jsonObject,
            Response.Listener { response ->
                Log.i("======>", "Exito ${response}")
            },
            Response.ErrorListener { error: VolleyError ->
                Log.i("======>", error.message ?: "VolleyError occurred")
            }) {}

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(jsonObjReq)

    }
}