package com.example.estacionamiento

import Util.Estacionamiento
import Util.EstacionamientoAdapter
import Util.TipoToast
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class DisponibilidadActivity : BaseMenuActivity() {

    private lateinit var spinner: Spinner
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: EstacionamientoAdapter

    private val allSpots = mutableListOf<Estacionamiento>()
    private val displayedSpots = mutableListOf<Estacionamiento>()
    private var selectedSpotId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_disponibilidad)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        spinner = findViewById(R.id.spinner_pisos)
        recycler = findViewById(R.id.recycler_espacios)
        recycler.layoutManager = GridLayoutManager(this, 2)

        /*
        // Cargar espacios simulados
        allSpots.addAll(listOf(
            Estacionamiento("E1", 1, true),
            Estacionamiento("E2", 1, false),
            Estacionamiento("E3", 1, false),
            Estacionamiento("E4", 1, true),
            Estacionamiento("E5", 1, false),
            Estacionamiento("E6", 1, true),
            Estacionamiento("E7", 1, false),
            Estacionamiento("E8", 1, false)
        ))
        */

        adapter = EstacionamientoAdapter(displayedSpots) { spot ->
            selectedSpotId = spot.id
            displayedSpots.forEachIndexed { i, s ->
                displayedSpots[i] = s.copy(isSelected = s.id == spot.id)
            }
            adapter.notifyDataSetChanged()
        }
        recycler.adapter = adapter

        loadPisos()

        findViewById<Button>(R.id.BtnConfirmar).setOnClickListener {
            selectedSpotId?.let { sendSpotSelection(it) }
        }
    }

    private fun loadPisos() {
        val pisos = listOf(1, 2, 3)

        if(spinner != null) {
            val adaptador = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                pisos
            )
            spinner.adapter = adaptador

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedFloor = pisos[position]
                    loadEspacios(selectedFloor)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
        }
    }

    private fun loadEspacios(floor: Int) {
        /*
        displayedSpots.clear()
        displayedSpots.addAll(allSpots.filter { it.floor == floor })
        adapter.notifyDataSetChanged()*/

        val url = "https://ya8f30cdbg.execute-api.us-east-1.amazonaws.com/v1/estacionamiento?piso=$floor"

        val stringRequest = JsonObjectRequest(
            Request.Method.GET, url, null, {
                response ->
                displayedSpots.clear()
                try {
                    val jsonArray = response.getJSONArray("data")
                    Log.i("=====>", jsonArray.toString())

                    for (i in 0 until jsonArray.length()){
                        val obj = jsonArray.getJSONObject(i)
                        val spot = Estacionamiento(
                            id = obj.getString("espacio"),
                            floor = floor,
                            isOccupied = obj.getBoolean("estado")
                        )
                        displayedSpots.add(spot)
                    }
                    adapter.notifyDataSetChanged()
                } catch (e: JSONException){
                    Log.i("=====>", e.message.toString())
                }
            },
            {
                error ->
                Log.i("=====>", error.toString())
            }
        )

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }

    private fun sendSpotSelection(spotId: String) {
        val util = Util.Common()
        util.mostrarToastPersonalizado(this, spotId, TipoToast.EXITO)

        /*val intent = Intent(this, ReservaCrearActivity::class.java)
        intent.putExtra("spotId", spotId)  // enviar el ID
        startActivity(intent)
        //Cortar la linea de abajo para recuperar el id en el otro activity
        val id = intent.getStringExtra("id")
        */
    }
}