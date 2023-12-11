package uv.tc.cuponsmart_android

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.gson.Gson
import uv.tc.cuponsmart_android.databinding.ActivityCrearCuentaBinding
import uv.tc.cuponsmart_android.fragments.DatosCuentaFragment
import uv.tc.cuponsmart_android.modelo.poko.Cliente
import java.util.Objects


class CrearCuentaActivity : AppCompatActivity(), OnFragmentInteractionListener {
        lateinit var binding: ActivityCrearCuentaBinding
        var cliente = Cliente()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityCrearCuentaBinding.inflate(layoutInflater)
            setContentView(binding.root)

            inicializarStepView()
            inicializarViewPager()
            personalizarComponentes()

        }

        private fun inicializarStepView() {
            binding.stepView.state
                .steps(object : ArrayList<String?>() {
                    init {
                        add("Info. Personal")
                        add("Cuenta")
                        add("Dirección")
                    }
                })
                .stepsNumber(4)
                .animationDuration(resources.getInteger(android.R.integer.config_shortAnimTime))
                .commit()
            binding.vp2FrameContainer.isUserInputEnabled = false
        }

        private fun inicializarViewPager() {
            binding.vp2FrameContainer.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle, this)
            binding.vp2FrameContainer.registerOnPageChangeCallback(
                object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        binding.stepView.go(position, true)
                        botones(position)
                    }
                }
            )
        }

        private fun personalizarComponentes() {

            binding.btnRegresar.setOnClickListener {

                animacionBotones(binding.btnRegresar)

                // Obtener datos del fragmento actual si es necesario
                binding.vp2FrameContainer.setCurrentItem(
                    binding.vp2FrameContainer.currentItem - 1,
                    false
                )
            }

            binding.btnIniciarSesion.setOnClickListener {
                animacionBotones(binding.btnIniciarSesion)
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }

            binding.btnSiguiente.setOnClickListener {

                animacionBotones(binding.btnSiguiente)

                val currentItem = binding.vp2FrameContainer.currentItem
                val fragment = supportFragmentManager.fragments[currentItem] as? OnFragmentInteractionListener

                if (fragment != null && fragment.validarCamposLlenos()) {
                    if (fragment is DatosCuentaFragment && !fragment.validarPassword()) {
                        // Si el fragmento actual es DatosCuentaFragment y las contraseñas no coinciden, no avanzar
                        return@setOnClickListener
                    }
                    // Obtener y procesar los datos del fragmento actual
                    val datos = fragment.obtenerDatos()
//                    Toast.makeText(this, "El nombre es ${datos.get("nombre")}", Toast.LENGTH_SHORT).show()
                    // Obtener el identificador del fragmento actual
                    val fragmentId = fragment.obtenerFragmentId()

                    // Puedes usar fragmentId para diferenciar entre los fragmentos
                    when (fragmentId) {
                        0 -> {
                            // Fragmento de DatosPersonales
                            cliente.nombre = datos.get("nombre")
                            cliente.apellidoPaterno = datos.get("apellidoP")
                            cliente.apellidoMaterno = datos.get("apellidoM")
                            cliente.fechaNacimiento = datos.get("fechaNacimiento")
                            Toast.makeText(this, "El nombre es ${cliente.nombre}", Toast.LENGTH_SHORT).show()
                        }
                        1 -> {

                            // Fragmento de DatosCuenta
                            // Puedes acceder a los datos de cuenta si es necesario
                            cliente.correo = datos.get("correo")
                            cliente.contraseña = datos.get("password")
                            cliente.telefono = datos.get("numeroTelefono")
                            Toast.makeText(this, "El correo es ${cliente.correo}", Toast.LENGTH_SHORT).show()

                        }

                    }
                    if (currentItem == 2) {

                        // Último fragmento, realizar acciones finales con los datos
                        val gson = Gson()
                        val jsonParam = gson.toJson(
                            mapOf(
                                "calle" to datos.get("calle"),
                                "municipio" to datos.get("municipio"),
                                "estado" to datos.get("estado"),
                                "codigoPostal" to datos.get("codig0oPostal"),
                                "colonia" to datos.get("colonia"),
                                "numero" to datos.get("numero"),
                                "logintud" to datos.get("longitud"),
                                "latitud" to datos.get("latitud")
                            )
                        )
                    } else {
                        // Pasar a la siguiente página
                        binding.vp2FrameContainer.setCurrentItem(currentItem + 1, false)
                    }

                } else {
                    // Mostrar un mensaje de error o realizar otra acción si los campos no están llenos
                    Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
                }
            }
        }
    private fun animacionBotones(boton : Button){
        val colorAnimation = ObjectAnimator.ofArgb(boton, "backgroundColor", Color.parseColor("#03e9f4"), R.color.black)
        colorAnimation.duration = 1000
        colorAnimation.start()
    }
        private fun botones(position: Int) {
            when (position) {
                0 -> {
                    binding.btnRegresar.visibility = View.INVISIBLE
                    binding.btnSiguiente.visibility = View.VISIBLE
                    binding.btnSiguiente.text = "Siguiente"
                }
                1 -> {
                    binding.btnRegresar.visibility = View.VISIBLE
                    binding.btnSiguiente.visibility = View.VISIBLE
                    binding.btnSiguiente.text = "Siguiente"
                }
                2 -> {
                    binding.scv.isNestedScrollingEnabled=false
                    binding.btnRegresar.visibility = View.VISIBLE
                    binding.btnSiguiente.visibility = View.VISIBLE
                    binding.btnSiguiente.text = "Finalizar"
                }
            }
        }

    override fun obtenerDatos(): Map<String, String> {
        TODO("Not yet implemented")
    }

    override fun validarCamposLlenos(): Boolean {
        TODO("Not yet implemented")
    }

    override fun validarPassword(): Boolean {
        TODO("Not yet implemented")
    }

    override fun obtenerFragmentId(): Int {
        TODO("Not yet implemented")
    }

}