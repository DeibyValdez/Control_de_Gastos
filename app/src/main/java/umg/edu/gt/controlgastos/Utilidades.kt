package umg.edu.gt.controlgastos

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** 1234.5 -> "Q 1,234.50"   y   -50.0 -> "-Q 50.00" */
fun formatearQuetzales(monto: Double): String {
    val centavos = Math.round(monto * 100)
    val texto = String.format(Locale.US, "%,.2f", Math.abs(centavos) / 100.0)
    return if (centavos < 0) "-Q $texto" else "Q $texto"
}

/** La fecha de hoy como "yyyy-MM-dd" (el formato en que se guarda). */
fun fechaDeHoy(): String {
    return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
}

/** "2026-10-30" -> "30/10/2026" (como se muestra en pantalla). */
fun fechaBonita(fecha: String): String {
    val partes = fecha.split("-")
    return "${partes[2]}/${partes[1]}/${partes[0]}"
}

/**
 * En Android moderno la app se dibuja debajo de la barra de estado y de navegacion.
 * Esto deja un margen para que la barra superior y los botones no queden tapados.
 */
fun respetarBarrasDelSistema(vista: View) {
    ViewCompat.setOnApplyWindowInsetsListener(vista) { v, insets ->
        val barras = insets.getInsets(
            WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime()
        )
        v.setPadding(barras.left, barras.top, barras.right, barras.bottom)
        insets
    }
}
