package com.example.estacionamiento

import Util.TipoToast
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
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

class UsuarioCrearActivity : AppCompatActivity() {


    private lateinit var txtNombresC: EditText
    private lateinit var txtApellidosC: EditText
    private lateinit var txtTipoDocC: EditText
    private lateinit var txtNroDocC: EditText
    private lateinit var txtCorreoC: EditText
    private lateinit var txtContrasenaC: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_usuario_crear)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val util = Util.Common()
        txtNombresC = findViewById(R.id.txtNombresC)
        txtApellidosC = findViewById(R.id.txtApellidosC)
        txtTipoDocC = findViewById(R.id.txtTipoDocC)
        txtNroDocC = findViewById(R.id.txtNroDocC)
        txtCorreoC = findViewById(R.id.txtCorreoC)
        txtContrasenaC = findViewById(R.id.txtContrasenaC)

        val tvInicieSesion = findViewById<TextView>(R.id.tvInicieSesion)
        tvInicieSesion.setOnClickListener {
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val btnRegistrarUsuario = findViewById<Button>(R.id.btnRegistrarUsuario)
        btnRegistrarUsuario.setOnClickListener {
            val nombre = txtNombresC.text.toString()
            val apellido = txtApellidosC.text.toString()
            val tipodocumento = txtTipoDocC.text.toString()
            val documento = txtNroDocC.text.toString()
            val correo = txtCorreoC.text.toString()
            val contrasena = txtContrasenaC.text.toString()

            if (nombre.isEmpty()) {
                txtNombresC.error = "Ingrese Nombres"
                return@setOnClickListener
            }

            if (apellido.isEmpty()) {
                txtApellidosC.error = "Ingrese Apellidos"
                return@setOnClickListener
            }

            if (tipodocumento.isEmpty()) {
                txtTipoDocC.error = "Ingrese Tipo de documento"
                return@setOnClickListener
            }

            if (documento.isEmpty()) {
                txtNroDocC.error = "Ingrese Nro de documento"
                return@setOnClickListener
            }

            if (correo.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                txtCorreoC.error = "Correo inválido"
                return@setOnClickListener
            }

            if (contrasena.isEmpty()) {
                txtContrasenaC.error = "Ingrese la contraseña"
                return@setOnClickListener
            }

            val url = "https://ya8f30cdbg.execute-api.us-east-1.amazonaws.com/v1/usuario?correo=$correo"

            val stringRequest = JsonObjectRequest(
                Request.Method.GET, url, null, {
                        response ->
                    try {
                        val jsonArray = response.getJSONArray("data")

                        if (jsonArray.length() > 0) {
                            util.mostrarToastPersonalizado(this, "El Correo ya fue registrado anteriormente", TipoToast.ERROR)
                        }
                        else {
                            val url2 = "https://ya8f30cdbg.execute-api.us-east-1.amazonaws.com/v1/usuario"

                            val jsonObject = JSONObject()
                            try {
                                jsonObject.put("nombre", nombre)
                                jsonObject.put("apellido", apellido)
                                jsonObject.put("tipodocumento", tipodocumento)
                                jsonObject.put("documento", documento)
                                jsonObject.put("correo", correo)
                                jsonObject.put("contrasena", contrasena)

                                val jsonObjReq = object : JsonObjectRequest(
                                    Request.Method.POST, url2, jsonObject,
                                    Response.Listener {
                                            response ->
                                        //Log.i("=====>", response.toString())
                                        val mensaje = response.getString("msg")
                                        util.mostrarToastPersonalizado(this, mensaje, TipoToast.EXITO)
                                        util.limpiarCampos(txtNombresC, txtApellidosC, txtTipoDocC, txtNroDocC, txtCorreoC, txtContrasenaC)
                                    },
                                    Response.ErrorListener {
                                            error: VolleyError ->
                                        //Log.i("=====>", error.message.toString())
                                        util.mostrarToastPersonalizado(this, error.message.toString(), TipoToast.ERROR)
                                    }
                                ) {}

                                val requestQueue = Volley.newRequestQueue(this)
                                requestQueue.add(jsonObjReq)
                            } catch (e: JSONException){
                                //Log.i("=====>", e.message.toString())
                                util.mostrarToastPersonalizado(this, e.message.toString(), TipoToast.ERROR)
                            }
                        }
                    } catch (e: JSONException){
                        //Log.i("=====>", e.message.toString())
                        util.mostrarToastPersonalizado(this, e.message.toString(), TipoToast.ERROR)
                    }
                },
                {
                        error ->
                    //Log.i("=====>", error.toString())
                    util.mostrarToastPersonalizado(this, error.toString(), TipoToast.ERROR)
                }
            )

            val requestQueue = Volley.newRequestQueue(this)
            requestQueue.add(stringRequest)
        }
    }
}