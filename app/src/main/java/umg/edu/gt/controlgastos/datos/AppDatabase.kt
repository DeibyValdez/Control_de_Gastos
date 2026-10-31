package umg.edu.gt.controlgastos.datos

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * La base de datos completa: que tablas tiene y que DAOs ofrece.
 * Si cambias la clase Movimiento (agregas un campo), hay que subir la version.
 */
@Database(entities = [Movimiento::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movimientoDao(): MovimientoDao
}
