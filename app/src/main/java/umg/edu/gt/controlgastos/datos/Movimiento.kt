package umg.edu.gt.controlgastos.datos

import androidx.room.Entity
import androidx.room.PrimaryKey

// Los dos tipos de movimiento que guardamos como texto en la base.
const val TIPO_GASTO = "GASTO"
const val TIPO_INGRESO = "INGRESO"

/**
 * Una FILA de la tabla "movimientos" en SQLite.
 * Un movimiento puede ser un GASTO (sale dinero) o un INGRESO (entra dinero).
 *
 * autoGenerate = true: si llega id = 0, SQLite le asigna el siguiente numero.
 * La fecha se guarda como texto "yyyy-MM-dd" (asi se ordena bien).
 */
@Entity(tableName = "movimientos")
data class Movimiento(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val tipo: String,
    val descripcion: String,
    val monto: Double,
    val categoria: String,
    val fecha: String
)
