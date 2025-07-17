package com.example.estacionamiento

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
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

        ObtenerEstacionamientoLibre()
        ObtenerVehiculo()
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

        val prefer = getSharedPreferences("usuario", MODE_PRIVATE)

        val util = Util.Common()
        val etFecha = util.reformatearFecha(findViewById<EditText>(R.id.etFecha).text.toString(), "yyyy-MM-dd")
        val etHora = findViewById<EditText>(R.id.etHora).text.toString()
        val spPlaca = findViewById<Spinner>(R.id.spPlaca)

        val url = "https://f2hq9czze2.execute-api.us-east-1.amazonaws.com/v1/api/Reserva"
        val espacio = "" // findViewById<EditText>(R.id.etEspacio)

        val placa = spPlaca.selectedItem.toString() // findViewById<EditText>(R.id.etPlaca).text.toString()
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
            var usuarioId = prefer.getInt("usuarioid", 1)
            jsonObject.put("reservaId", 0)
            jsonObject.put("usuarioId", usuarioId)
            jsonObject.put("estacionamientoId", estacionamientoId)
            jsonObject.put("placa", placa)
            jsonObject.put("fecha", fechaFormateada)
            jsonObject.put("fechafin", fechaFormateada)
            jsonObject.put("tipo", tipo)
            jsonObject.put("estado", estado)
            Log.i("======>", jsonObject.toString())

        }
        catch (e: JSONException){
            Log.i("======>", e.message ?: "JSONException occurred")
        }


        var reservaId = 0
        val request = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                val numero = response.toIntOrNull()
                if (numero != null) {
                    Log.d("Respuesta", "Valor recibido: $numero")

                    val intent = Intent(this, ReservaStatusActivity::class.java)
                    intent.putExtra("item_key", numero.toString())
                    startActivity(intent)

                } else {
                    Log.e("ParseError", "No se pudo convertir: $response")
                }
            },
            Response.ErrorListener { error ->
                Log.e("VolleyError", error.toString())
            }
        ) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return jsonObject.toString().toByteArray(Charsets.UTF_8)
            }
        }

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(request)


    }

    fun ObtenerEstacionamientoLibre(){
        val url = "https://f2hq9czze2.execute-api.us-east-1.amazonaws.com/v1/api/Estacionamiento/BuscarEstacionamientoLibre"

        val stringRequest = JsonObjectRequest(Request.Method.GET, url, null, {
                response ->
            try {

                Log.i("======>", response.toString())
                val espacio = response.getString("espacio")
                val estacionamientoId = response.getString("estacionamientoId")
                val txt = findViewById<EditText>(R.id.etEspacio)
                txt.setText(espacio)
                txt.tag = estacionamientoId
                txt.isEnabled = false

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

    fun ObtenerVehiculo(){

        val prefer = getSharedPreferences("usuario", MODE_PRIVATE)
        val usuarioId = prefer.getInt("usuarioid", 1)

        Log.i("======>", prefer.toString())
        val url = "https://f2hq9czze2.execute-api.us-east-1.amazonaws.com/v1/api/Vehiculo/ListarPorNombre?placa=0&usuarioId=$usuarioId"
        val stringRequest = JsonArrayRequest(Request.Method.GET, url, null, {
                response ->
            try {

                val jsonArray = response
                Log.i("======>", jsonArray.toString())
                val items = mutableListOf<String>()
                for (i in 0 until jsonArray.length()) {
                    val vehiculo = jsonArray.getJSONObject(i)
                    items.add("${vehiculo.getString("placa")}")
                }

                val spinner = findViewById<Spinner>(R.id.spPlaca)
                val adaptador = ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_spinner_item,
                    items
                )
                spinner.adapter = adaptador

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