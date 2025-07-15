package com.example.estacionamiento

import Util.TipoToast
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    private lateinit var txtCorreo: EditText
    private lateinit var txtContrasena: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val util = Util.Common()

        txtCorreo = findViewById(R.id.txtCorreo)
        txtContrasena = findViewById(R.id.txtContrasena)

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        btnLogin.setOnClickListener {
            val correo = txtCorreo.text.toString().trim()
            val contrasena = txtContrasena.text.toString()

            if (correo.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                txtCorreo.error = "Correo inválido"
                return@setOnClickListener
            }

            if (contrasena.isEmpty()) {
                txtContrasena.error = "Ingrese la contraseña"
                return@setOnClickListener
            }

            val url = "https://ya8f30cdbg.execute-api.us-east-1.amazonaws.com/v1/login"

            val jsonObject = JSONObject()
            try {
                jsonObject.put("correo", correo)
                jsonObject.put("contrasena", contrasena)

                val jsonObjReq = object : JsonObjectRequest(
                    Request.Method.POST, url, jsonObject,
                    Response.Listener {
                            response ->
                        val jsonArray = response.getJSONArray("data")
                        if (jsonArray.length() > 0) {
                            val usuario = jsonArray.getJSONObject(0)

                            // Guardar datos del usuario
                            val prefs = getSharedPreferences("usuario", MODE_PRIVATE)
                            with(prefs.edit()) {
                                putInt("usuarioid", usuario.getInt("usuarioid"))
                                putString("nombre", usuario.getString("nombre"))
                                putString("apellido", usuario.getString("apellido"))
                                putString("correo", usuario.getString("correo"))
                                putString("tipodocumento", usuario.getString("tipodocumento"))
                                putString("documento", usuario.getString("documento"))
                                putBoolean("logueado", true)
                                apply()
                            }

                            var intent = Intent(this, PrincipalActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            util.mostrarToastPersonalizado(this, "Usuario no existe", TipoToast.ERROR)
                        }
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

        val tvForgotPassword = findViewById<TextView>(R.id.tvForgotPassword)
        tvForgotPassword.setOnClickListener {
            var intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        val tvSignUpText2 = findViewById<TextView>(R.id.tvSignUpText2)
        tvSignUpText2.setOnClickListener {
            var intent = Intent(this, UsuarioCrearActivity::class.java)
            startActivity(intent)
        }
    }
}