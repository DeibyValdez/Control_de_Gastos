package umg.edu.gt.controlgastos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import umg.edu.gt.controlgastos.databinding.ItemMovimientoBinding
import umg.edu.gt.controlgastos.datos.Movimiento
import umg.edu.gt.controlgastos.datos.TIPO_INGRESO

/**
 * El Adapter conecta la lista de movimientos con las tarjetas que se ven en pantalla.
 * alTocar: funcion que MainActivity nos da para saber que hacer cuando tocan una tarjeta.
 */
class MovimientoAdapter(
    private val alTocar: (Movimiento) -> Unit
) : RecyclerView.Adapter<MovimientoAdapter.MovimientoViewHolder>() {

    private var lista: List<Movimiento> = emptyList()

    class MovimientoViewHolder(val binding: ItemMovimientoBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun actualizar(nuevaLista: List<Movimiento>) {
        lista = nuevaLista
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovimientoViewHolder {
        val binding = ItemMovimientoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovimientoViewHolder(binding)
    }

    override fun getItemCount(): Int = lista.size

    override fun onBindViewHolder(holder: MovimientoViewHolder, position: Int) {
        val movimiento = lista[position]
        val contexto = holder.itemView.context

        holder.binding.txtDescripcion.text = movimiento.descripcion
        holder.binding.txtDetalle.text = "${movimiento.categoria} · ${fechaBonita(movimiento.fecha)}"

        // Ingreso: verde con "+". Gasto: rojo suave con "-".
        if (movimiento.tipo == TIPO_INGRESO) {
            holder.binding.txtMonto.text = "+ " + formatearQuetzales(movimiento.monto)
            holder.binding.txtMonto.setTextColor(ContextCompat.getColor(contexto, R.color.ingreso))
        } else {
            holder.binding.txtMonto.text = "- " + formatearQuetzales(movimiento.monto)
            holder.binding.txtMonto.setTextColor(ContextCompat.getColor(contexto, R.color.gasto))
        }

        holder.binding.root.setOnClickListener { alTocar(movimiento) }
    }
}
