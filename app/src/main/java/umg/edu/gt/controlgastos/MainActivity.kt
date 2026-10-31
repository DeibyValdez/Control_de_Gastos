package umg.edu.gt.controlgastos

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import umg.edu.gt.controlgastos.databinding.ActivityMainBinding
import umg.edu.gt.controlgastos.datos.TIPO_INGRESO

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MovimientoAdapter
    private val dao = Dependencias.dao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        respetarBarrasDelSistema(binding.root)

        // Al tocar una tarjeta se abre el formulario en modo editar (con el id del movimiento).
        adapter = MovimientoAdapter { movimiento -> abrirFormulario(movimiento.id) }
        binding.recyclerMovimientos.layoutManager = LinearLayoutManager(this)
        binding.recyclerMovimientos.adapter = adapter

        // id = 0 significa "movimiento nuevo".
        binding.btnNuevo.setOnClickListener { abrirFormulario(0) }
    }

    // onResume se ejecuta al abrir la app y cada vez que regresamos del formulario.
    override fun onResume() {
        super.onResume()
        cargarMovimientos()
    }

    private fun abrirFormulario(id: Int) {
        val intent = Intent(this, FormularioActivity::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
    }

    private fun cargarMovimientos() {
        lifecycleScope.launch {
            val lista = dao.listar()
            adapter.actualizar(lista)

            // Sumamos ingresos y gastos por separado.
            var totalIngresos = 0.0
            var totalGastos = 0.0
            for (movimiento in lista) {
                if (movimiento.tipo == TIPO_INGRESO) {
                    totalIngresos += movimiento.monto
                } else {
                    totalGastos += movimiento.monto
                }
            }
            val balance = totalIngresos - totalGastos

            binding.txtIngresos.text = formatearQuetzales(totalIngresos)
            binding.txtGastos.text = formatearQuetzales(totalGastos)
            binding.txtBalance.text = formatearQuetzales(balance)

            // Balance negativo en rojo suave; si no, en el color normal del texto.
            val colorBalance = if (balance < -0.005) R.color.gasto else R.color.texto
            binding.txtBalance.setTextColor(ContextCompat.getColor(this@MainActivity, colorBalance))

            // Mensaje cuando todavia no hay nada registrado.
            binding.txtVacio.visibility = if (lista.isEmpty()) View.VISIBLE else View.GONE
        }
    }
}
