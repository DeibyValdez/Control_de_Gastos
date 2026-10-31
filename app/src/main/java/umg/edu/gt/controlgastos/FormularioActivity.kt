package umg.edu.gt.controlgastos

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import umg.edu.gt.controlgastos.databinding.ActivityFormularioBinding
import umg.edu.gt.controlgastos.datos.Movimiento
import umg.edu.gt.controlgastos.datos.TIPO_GASTO
import umg.edu.gt.controlgastos.datos.TIPO_INGRESO
import java.util.Locale

/**
 * Una sola pantalla para CREAR y para EDITAR un movimiento.
 * Si llega id = 0 es nuevo; si llega otro id, cargamos ese movimiento para editarlo.
 */
class FormularioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormularioBinding
    private val dao = Dependencias.dao

    private var idEditando = 0
    private var fechaElegida = fechaDeHoy()

    private val categoriasGasto = listOf(
        "Alimentación", "Transporte", "Servicios", "Salud",
        "Educación", "Entretenimiento", "Otros"
    )
    private val categoriasIngreso = listOf("Salario", "Ventas", "Regalo", "Otros")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormularioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        respetarBarrasDelSistema(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }

        llenarCategorias(categoriasGasto)
        binding.btnFecha.text = fechaBonita(fechaElegida)

        // Si cambia entre Gasto e Ingreso, cambia la lista de categorias.
        binding.rgTipo.setOnCheckedChangeListener { _, _ ->
            if (binding.rbIngreso.isChecked) {
                llenarCategorias(categoriasIngreso)
            } else {
                llenarCategorias(categoriasGasto)
            }
        }

        binding.btnFecha.setOnClickListener { elegirFecha() }
        binding.btnGuardar.setOnClickListener { guardar() }
        binding.btnEliminar.setOnClickListener { confirmarEliminacion() }

        // Modo editar: cambiamos textos, mostramos Eliminar y cargamos los datos.
        idEditando = intent.getIntExtra("id", 0)
        if (idEditando != 0) {
            binding.toolbar.title = "Editar movimiento"
            binding.btnGuardar.text = "Guardar cambios"
            binding.btnEliminar.visibility = View.VISIBLE
            cargarMovimiento(idEditando)
        }
    }

    private fun llenarCategorias(categorias: List<String>) {
        val adaptador = ArrayAdapter(this, R.layout.item_spinner, categorias)
        adaptador.setDropDownViewResource(R.layout.item_spinner)
        binding.spCategoria.adapter = adaptador
    }

    private fun cargarMovimiento(id: Int) {
        lifecycleScope.launch {
            val movimiento = dao.buscarPorId(id)
            if (movimiento == null) {
                Toast.makeText(this@FormularioActivity, "No existe el movimiento", Toast.LENGTH_SHORT).show()
                finish()
                return@launch
            }

            // Marcar el tipo cambia la lista de categorias (por el listener de arriba).
            if (movimiento.tipo == TIPO_INGRESO) {
                binding.rbIngreso.isChecked = true
            } else {
                binding.rbGasto.isChecked = true
            }

            binding.etDescripcion.setText(movimiento.descripcion)
            binding.etMonto.setText(String.format(Locale.US, "%.2f", movimiento.monto))

            val categorias = if (movimiento.tipo == TIPO_INGRESO) categoriasIngreso else categoriasGasto
            val posicion = categorias.indexOf(movimiento.categoria)
            if (posicion >= 0) {
                binding.spCategoria.setSelection(posicion)
            }

            fechaElegida = movimiento.fecha
            binding.btnFecha.text = fechaBonita(fechaElegida)
        }
    }

    private fun elegirFecha() {
        // fechaElegida es "yyyy-MM-dd". En DatePickerDialog los meses empiezan en 0 (enero = 0).
        val partes = fechaElegida.split("-")
        val anio = partes[0].toInt()
        val mes = partes[1].toInt() - 1
        val dia = partes[2].toInt()

        DatePickerDialog(this, { _, a, m, d ->
            fechaElegida = String.format(Locale.US, "%04d-%02d-%02d", a, m + 1, d)
            binding.btnFecha.text = fechaBonita(fechaElegida)
        }, anio, mes, dia).show()
    }

    private fun guardar() {
        val descripcion = binding.etDescripcion.text.toString().trim()
        // Por si el teclado escribe coma en vez de punto decimal.
        val monto = binding.etMonto.text.toString().replace(",", ".").toDoubleOrNull()

        if (descripcion.isEmpty()) {
            Toast.makeText(this, "Escribe una descripción", Toast.LENGTH_SHORT).show()
            return
        }
        if (monto == null || monto <= 0) {
            Toast.makeText(this, "El monto debe ser mayor que 0", Toast.LENGTH_SHORT).show()
            return
        }

        val tipo = if (binding.rbIngreso.isChecked) TIPO_INGRESO else TIPO_GASTO
        val categoria = binding.spCategoria.selectedItem.toString()

        // Con id = 0 Room crea uno nuevo; con un id existente lo actualiza.
        val movimiento = Movimiento(
            id = idEditando,
            tipo = tipo,
            descripcion = descripcion,
            monto = monto,
            categoria = categoria,
            fecha = fechaElegida
        )

        lifecycleScope.launch {
            if (idEditando == 0) {
                dao.insertar(movimiento)
            } else {
                dao.actualizar(movimiento)
            }
            Toast.makeText(this@FormularioActivity, "Guardado", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    /** Pregunta antes de borrar: eliminar no se puede deshacer. */
    private fun confirmarEliminacion() {
        val nombre = binding.etDescripcion.text.toString().trim()
        MaterialAlertDialogBuilder(this)
            .setTitle("Eliminar movimiento")
            .setMessage("¿Eliminar \"$nombre\"?\nEsta acción no se puede deshacer.")
            // Este bloque se ejecuta SOLO si el usuario toca "Eliminar".
            .setPositiveButton("Eliminar") { _, _ -> eliminar() }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun eliminar() {
        lifecycleScope.launch {
            dao.eliminar(idEditando)
            Toast.makeText(this@FormularioActivity, "Movimiento eliminado", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
