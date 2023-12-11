package uv.tc.cuponsmart_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.google.android.material.R
import uv.tc.cuponsmart_android.archivos_dao.CatalogoDAO
import uv.tc.cuponsmart_android.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    lateinit var binding : ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        cargarTexto()

    }
    fun cargarTexto() {
        CatalogoDAO.obtenerEstados(this@HomeActivity, "catalogo/obtenerEstados") { estados ->
            if (estados != null) {
                val estadosText = estados.joinToString("\n") { it.nombre ?: "" }
                binding.hola.text = estadosText
            } else {
                // Manejar el caso en que no se pudieron obtener los estados
                binding.hola.text = "Error al obtener los estados"
            }
        }
    }
}