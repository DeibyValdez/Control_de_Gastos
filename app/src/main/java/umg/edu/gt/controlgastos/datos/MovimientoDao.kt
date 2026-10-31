package umg.edu.gt.controlgastos.datos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 * DAO = las operaciones sobre la tabla. Solo escribimos la interface;
 * Room genera el codigo al compilar.
 * Todas son suspend porque Room las ejecuta fuera del hilo principal.
 */
@Dao
interface MovimientoDao {

    @Insert
    suspend fun insertar(movimiento: Movimiento): Long

    // Primero los mas recientes. Si hay misma fecha, el ultimo registrado va arriba.
    @Query("SELECT * FROM movimientos ORDER BY fecha DESC, id DESC")
    suspend fun listar(): List<Movimiento>

    @Query("SELECT * FROM movimientos WHERE id = :id")
    suspend fun buscarPorId(id: Int): Movimiento?

    @Update
    suspend fun actualizar(movimiento: Movimiento): Int

    @Query("DELETE FROM movimientos WHERE id = :id")
    suspend fun eliminar(id: Int): Int
}
