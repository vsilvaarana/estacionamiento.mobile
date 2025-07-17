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

class ReservaStatusActivity : BaseMenuActivity() {
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