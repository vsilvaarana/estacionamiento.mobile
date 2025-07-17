package Util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.estacionamiento.R

class EstacionamientoAdapter(
    private val espacios: List<Estacionamiento>,
    private val onClick: (Estacionamiento) -> Unit
) : RecyclerView.Adapter<EstacionamientoAdapter.EspacioViewHolder>() {

    inner class EspacioViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageCar: ImageView = view.findViewById(R.id.image_car)
        val textSpot: TextView = view.findViewById(R.id.text_espacio)

        fun bind(espacio: Estacionamiento) {
            textSpot.text = espacio.id
            imageCar.visibility = if (espacio.isOccupied) View.VISIBLE else View.INVISIBLE
            itemView.setBackgroundResource(
                when {
                    espacio.isSelected -> R.drawable.bg_toast_warning
                    espacio.isOccupied -> R.drawable.bg_ocupado
                    else -> R.drawable.bg_disponible
                }
            )
            itemView.setOnClickListener { if (!espacio.isOccupied) onClick(espacio) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EspacioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_espacios, parent, false)
        return EspacioViewHolder(view)
    }

    override fun onBindViewHolder(holder: EspacioViewHolder, position: Int) {
        holder.bind(espacios[position])
    }

    override fun getItemCount() = espacios.size
}