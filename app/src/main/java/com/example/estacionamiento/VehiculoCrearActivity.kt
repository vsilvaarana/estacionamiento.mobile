package com.example.estacionamiento

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONException
import org.json.JSONObject
import android.util.Log
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.android.volley.VolleyError
import android.app.Activity
import android.net.Uri
import android.widget.ImageView

class VehiculoCrearActivity : AppCompatActivity() {

    private lateinit var btnSelectImage: TextView
    private lateinit var imageView: ImageView
    private var imageUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_vehiculo_crear)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btn = findViewById<Button>(R.id.btnCrearVehiculo)

        btn.setOnClickListener {
            crear()
            var intent = Intent(this, UsuarioVehiculoActivity::class.java)
            startActivity(intent)
        }

        val textView = findViewById<TextView>(R.id.txtPlaca)
        val item = intent.getStringExtra("item_key")

        textView.text = item

        btnSelectImage = findViewById<TextView>(R.id.btnSubirImagen)
        btnSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            imagePickerLauncher.launch(intent)
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

    fun crear()
    {
        val url = "https://f2hq9czze2.execute-api.us-east-1.amazonaws.com/v1/api/Vehiculo"
        val marca = findViewById<EditText>(R.id.txtMarca)
        val placa = findViewById<EditText>(R.id.txtPlaca)

        val jsonObject = JSONObject()

        try {
            var usuarioId = 1
            jsonObject.put("vehiculoId", 0)
            jsonObject.put("usuarioId", usuarioId)
            jsonObject.put("modelo", "auto")
            jsonObject.put("marca", marca.text.toString())
            jsonObject.put("placa", placa.text.toString())
            Log.i("======>", jsonObject.toString())

        }
        catch (e: JSONException){
            Log.i("======>", e.message ?: "JSONException occurred")
        }

        val jsonObjReq = object : JsonObjectRequest(Request.Method.POST, url, jsonObject,
            Response.Listener { response ->
                Log.i("======>", "Exito")
            },
            Response.ErrorListener { error: VolleyError ->
                Log.i("======>", error.message ?: "VolleyError occurred")
            }) {}

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(jsonObjReq)
    }

    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            imageUri = result.data?.data
            imageUri?.let {
                imageView.setImageURI(it)
                // Aquí podrías enviar la imagen al servidor
                uploadImage(it)
            }
        }
    }

    private fun uploadImage(uri: Uri) {
        // Aquí podrías usar Retrofit o cualquier librería de red para enviar el archivo
        // Ejemplo: obtener el archivo y prepararlo
        try {
            val inputStream = contentResolver.openInputStream(uri)
            val bytes = inputStream?.readBytes()
            inputStream?.close()

            if (bytes != null) {
                // Envía los bytes al servidor usando Retrofit o tu método preferido
                println("Imagen lista para enviar, tamaño: ${bytes.size} bytes")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}