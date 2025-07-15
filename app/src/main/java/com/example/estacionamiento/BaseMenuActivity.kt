package com.example.estacionamiento

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

open class BaseMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.principal, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_inicio -> {
                startActivity(Intent(this, PrincipalActivity::class.java))
                return true
            }
            R.id.menu_disponibilidad -> {
                startActivity(Intent(this, DisponibilidadActivity::class.java))
                return true
            }
            R.id.menu_cerrar_sesion -> {
                startActivity(Intent(this, LoginActivity::class.java))
                return true
            }
            R.id.menu_historial -> {
                startActivity(Intent(this, ReservaHistoriaActivity::class.java))
                return true
            }
            R.id.menu_vehiculos -> {
                startActivity(Intent(this, UsuarioVehiculoActivity::class.java))
                return true
            }
            R.id.menu_realizar_reserva -> {
                startActivity(Intent(this, ReservaCrearActivity::class.java))
                return true
            }
            R.id.menu_reserva_activa -> {
                startActivity(Intent(this, ReservaStatusActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}