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
import uv.tc.cuponsmart_android.modelo.DAO.ClienteDAO
import uv.tc.cuponsmart_android.modelo.DAO.UbicacionDAO
import uv.tc.cuponsmart_android.databinding.ActivityCrearCuentaBinding
import uv.tc.cuponsmart_android.fragments.DatosCuentaFragment
import uv.tc.cuponsmart_android.modelo.poko.Cliente
import uv.tc.cuponsmart_android.modelo.poko.Coordenada
import uv.tc.cuponsmart_android.modelo.poko.Mensaje
import uv.tc.cuponsmart_android.modelo.poko.Ubicacion


class CrearCuentaActivity : AppCompatActivity(), OnFragmentInteractionListener {
    //--------- DECLARACION DE VARAIBLES ---------//
    lateinit var binding: ActivityCrearCuentaBinding
    var cliente = Cliente()
    var idUbicacion: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrearCuentaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inicializarStepView()
        inicializarViewPager()
        personalizarComponentes()

    }

    //--------- METODO PARA INICIALIZAR EL CONTADOR DE PASOS DE CREAR CUENTA ----------//
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

    //--------- CONFIGURAR VIEWPAGER2 DE LOS FORMULARIOS DE LA CUENTA ----------//
    private fun inicializarViewPager() {
        binding.vp2FrameContainer.adapter =
            ViewPagerAdapterCrearCuenta(supportFragmentManager, lifecycle, this)
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

    //----------- PERSONALZIACION DE LOS COMPONENTES ------------//
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
            val fragment =
                supportFragmentManager.fragments[currentItem] as? OnFragmentInteractionListener

            if (fragment != null && fragment.validarCamposLlenos()) {
                if (fragment is DatosCuentaFragment && !fragment.validarPassword()) {
                    // Si el fragmento actual es DatosCuentaFragment y las contraseñas no coinciden, no avanzar
                    return@setOnClickListener
                }
                // Obtener y procesar los datos del fragmento actual
                val datos = fragment.obtenerDatos()
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
                        Toast.makeText(this, "El nombre es ${cliente.nombre}", Toast.LENGTH_SHORT)
                            .show()
                    }

                    1 -> {

                        // Fragmento de DatosCuenta

                        cliente.correo = datos.get("correo")
                        cliente.contraseña = datos.get("password")
                        cliente.telefono = datos.get("numeroTelefono")
                        Toast.makeText(this, "El correo es ${cliente.correo}", Toast.LENGTH_SHORT)
                            .show()

                    }

                }
                if (currentItem == 2) {
                    crearUbicacion(datos)
                } else {
                    // Pasar a la siguiente página
                    binding.vp2FrameContainer.setCurrentItem(currentItem + 1, false)
                }
            } else {
                // Mostrar un mensaje de error o realizar otra acción si los campos no están llenos
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun animacionBotones(boton: Button) {
        val colorAnimation = ObjectAnimator.ofArgb(
            boton,
            "backgroundColor",
            Color.parseColor("#03e9f4"),
            R.color.black
        )
        colorAnimation.duration = 1000
        colorAnimation.start()
    }

    //-------- METODO PARA CREAR LA UBICACION CON LOS DATOS INGRESADOS -----------//
    fun crearUbicacion(datos: Map<String, String>) {
        var ubicacionObtenida: Ubicacion = Ubicacion()
        ubicacionObtenida.calle = datos.get("calle")
        ubicacionObtenida.idMunicipio = Integer.parseInt(datos.get("idMunicipio"))
        ubicacionObtenida.codigoPostal = datos.get("codigoPostal")
        ubicacionObtenida.colonia = datos.get("colonia")
        ubicacionObtenida.numero = datos.get("numero")
        ubicacionObtenida.longitud = datos.get("longitud")
        ubicacionObtenida.latitud = datos.get("latitud")
        UbicacionDAO.agregarUbicacion(
            this@CrearCuentaActivity,
            "ubicacion/agregarUbicacion/",
            ubicacionObtenida
        ) { respuesta ->
            serializarRespuestaAgregarUbicacion(
                respuesta,
                ubicacionObtenida.latitud,
                ubicacionObtenida.longitud
            )

        }
    }

    fun serializarRespuestaAgregarUbicacion(json: String, latitud: String?, longitud: String?) {
        val gson = Gson()
        val respuestaUbicacion = gson.fromJson(json, Mensaje::class.java)
        Toast.makeText(this@CrearCuentaActivity, respuestaUbicacion.mensaje, Toast.LENGTH_LONG)
            .show()
        if (!respuestaUbicacion.error) {
            obtenerIdUbicacion(latitud, longitud)
        }
    }

    //--------- METODO PARA OBTENER EL ID DE LA UBICACION QUE SE CREA A PARTIR DE LAS COORDENADAS ----------//
    fun obtenerIdUbicacion(latitud: String?, longitud: String?) {
        var coordenada = Coordenada()
        coordenada.latitud = latitud.toString()
        coordenada.longitud = longitud.toString()

        UbicacionDAO.obtenerIDUbicacionCreada(
            this@CrearCuentaActivity,
            "ubicacion/obtenerUbicacionRegistro/",
            coordenada
        ) { respuesta ->
            var idUbicacion = respuesta
            cliente.idUbicacion = idUbicacion
            crearClienteNuevo(cliente)
        }
    }

    //---------- CREACION DEL NUEVO CLIETNE -------------//
    fun crearClienteNuevo(cliente: Cliente) {
        ClienteDAO.crearClienteNuevo(
            this@CrearCuentaActivity,
            "cliente/agregarCliente/",
            cliente
        ) { respuesta ->
            serializarRespuestaAgregarCliente(respuesta)
        }
    }

    //---------- INTERPRETAR RESPUESTA DE LA CONSULTA AGREGAR CLIENTE ----------//
    fun serializarRespuestaAgregarCliente(json: String) {
        val gson = Gson()
        val respuestaCliente = gson.fromJson(json, Mensaje::class.java)
        Toast.makeText(this@CrearCuentaActivity, respuestaCliente.mensaje, Toast.LENGTH_LONG).show()
        if (!respuestaCliente.error) {
            irPantallaLogin()
        }
    }

    //--------- METODO PARA IR A LA PANTALLA DE LOGIN TRAS CREAR CUENTA -----------//
    fun irPantallaLogin() {
        val intent = Intent(this@CrearCuentaActivity, LoginActivity::class.java)
        startActivity(intent)
        this.finish()
    }
    //--------- FUNCIONES DE BOTONES ----------//
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
                binding.scv.isNestedScrollingEnabled = false
                binding.btnRegresar.visibility = View.VISIBLE
                binding.btnSiguiente.visibility = View.VISIBLE
                binding.btnSiguiente.text = "Finalizar"
            }
        }
    }
    //---------- METODOS DE LA INTERFACE -----------//
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