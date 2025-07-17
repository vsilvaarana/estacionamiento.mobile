package com.example.estacionamiento

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException

class ReservaStatusActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reserva_status)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val item = intent.getStringExtra("item_key")
        ObtenerReserva(item.toString())
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


    fun ObtenerReserva(id : String){
        val url = "https://f2hq9czze2.execute-api.us-east-1.amazonaws.com/v1/api/Reserva?id=$id"

        val stringRequest = JsonObjectRequest(Request.Method.GET, url, null, {
                response ->
            try {

                Log.i("======>", response.toString())
                val placa = response.getString("placa")
                val fecha = response.getString("fecha")


                val tvCodigo = findViewById<TextView>(R.id.tvCodigo)
                tvCodigo.setText("Codigo de Reserva: $id")

                val tvPlaca = findViewById<TextView>(R.id.tvPlaca)
                tvPlaca.setText("Placa: $placa")

                val tvFecha = findViewById<TextView>(R.id.tvFecha)
                tvFecha.setText("Fecha: $fecha")

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