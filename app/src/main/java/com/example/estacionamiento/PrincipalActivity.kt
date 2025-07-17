package com.example.estacionamiento

import Util.TipoToast
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PrincipalActivity : BaseMenuActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_principal)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val util = Util.Common()

        //se obtiene el usuario logueado
        val prefs = getSharedPreferences("usuario", MODE_PRIVATE)
        val nombre = prefs.getString("nombre", "")
        val apellido = prefs.getString("apellido", "")

        //se valida si esta logueado
        if (prefs.getBoolean("logueado", false)) {
            //Toast.makeText(this, "Hola $nombre $apellido", Toast.LENGTH_SHORT).show()
            util.mostrarToastPersonalizado(this, "Hola $nombre $apellido", TipoToast.EXITO)
        } else {
            with(prefs.edit()) {
                clear()
                apply()
            }

            // Ir al login
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        val btnIr = findViewById<Button>(R.id.btnIr)
        btnIr.setOnClickListener {
            var intent = Intent(this, DisponibilidadActivity::class.java)
            startActivity(intent)
        }
    }
}