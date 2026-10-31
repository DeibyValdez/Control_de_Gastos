package umg.edu.gt.controlgastos

import android.content.Context
import androidx.room.Room
import umg.edu.gt.controlgastos.datos.AppDatabase
import umg.edu.gt.controlgastos.datos.MovimientoDao

/**
 * Lo que TODA la app comparte: el acceso a la base de datos.
 * object = existe un solo objeto. Las dos pantallas usan Dependencias.dao.
 */
object Dependencias {

    // lateinit: se le da valor despues, en iniciar(), antes de que alguien la use.
    lateinit var dao: MovimientoDao
        private set

    fun iniciar(context: Context) {
        // Abre (o crea la primera vez) el archivo gastos.db en la carpeta privada de la app.
        val baseDeDatos = Room.databaseBuilder(context, AppDatabase::class.java, "gastos.db")
            .build()
        dao = baseDeDatos.movimientoDao()
    }
}
