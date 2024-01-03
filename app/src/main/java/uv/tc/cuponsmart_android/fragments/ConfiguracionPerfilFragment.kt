package uv.tc.cuponsmart_android.fragments

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import uv.tc.cuponsmart_android.R
import uv.tc.cuponsmart_android.Update
import uv.tc.cuponsmart_android.databinding.FragmentConfiguracionPerfilBinding
import uv.tc.cuponsmart_android.modelo.DAO.ClienteDAO
import uv.tc.cuponsmart_android.modelo.poko.Cliente
import uv.tc.cuponsmart_android.modelo.poko.Mensaje
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ofPattern

class ConfiguracionPerfilFragment : Fragment() {
    private var update: Update? = null

    private lateinit var binding: FragmentConfiguracionPerfilBinding
    private lateinit var cliente: Cliente
    private var clienteConsulta = Cliente()
    var bandera:Boolean=false
    var fechaSQL = ""
    val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConfiguracionPerfilBinding.inflate(inflater, container, false)
        obtenerDatosActivity()
        cargarDatosPerfil (cliente.idCliente!!)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        personalizarComponentes()
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment is Update) {
            update = parentFragment as Update
        } else {
            throw ClassCastException("El padre debe implementar OnNombreCompletoUpdateListener")
        }
    }
    private fun mostrarDatePicker() {
        val datePicker = DatePickerFragment { dia, mes, anio -> onDateSelected(dia, mes, anio) }
        datePicker.show(childFragmentManager, "datepicker")

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun cargarDatosPerfil(idCliente: Int) {
        ClienteDAO.obtenerClienteID(
            requireContext(),
            "cliente/clientePorId",
            idCliente
        ) { respuesta ->
            serializarRespuestaClieteId(respuesta)
        }

    }

    private fun obtenerDatosActivity() {
        cliente = arguments?.getParcelable("cliente")!!
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun serializarRespuestaClieteId(json: String) {

        clienteConsulta = gson.fromJson(json, Cliente::class.java)
        binding.etNombrePerfil.setText(clienteConsulta.nombre)
        binding.etApellidoPaternoPerfil.setText(clienteConsulta.apellidoPaterno)
        binding.etApellidoMaternoPerfil.setText(clienteConsulta.apellidoMaterno)
        binding.etNumeroTelefonoPerfil.setText(clienteConsulta.telefono)
        binding.etFechaNacimientoPerfil.setText(convertirFormatoFecha(clienteConsulta.fechaNacimiento!!))
        fechaSQL = cliente.fechaNacimiento!!
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun convertirFormatoFecha(fechaOriginal: String): String {
        try {
            val formatoOriginal = ofPattern("yyyy-MM-dd")
            val fechaLocalDate: LocalDate = LocalDate.parse(fechaOriginal, formatoOriginal)

            val formatoNuevo = ofPattern("dd-MM-yyyy")
            return fechaLocalDate.format(formatoNuevo)
        } catch (e: Exception) {
            e.printStackTrace()
            return fechaOriginal
        }
    }
    fun onDateSelected(dia: Int, mes: Int, anio: Int) {
        val fechaFormateadaSQL = String.format("%04d-%02d-%02d", anio, mes + 1, dia)
        val fechaFormateadaView = String.format("%02d-%02d-%04d", dia, mes + 1, anio)
        binding.etFechaNacimientoPerfil.setText(fechaFormateadaView)
        fechaSQL = fechaFormateadaSQL
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun personalizarComponentes() {
        binding.etFechaNacimientoPerfil.setOnClickListener {
            binding.etFechaNacimientoPerfil.getBackground()
                .setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP)
            Handler(Looper.getMainLooper()).postDelayed({
                binding.etFechaNacimientoPerfil.getBackground().setColorFilter(
                    getResources().getColor(R.color.black),
                    PorterDuff.Mode.SRC_ATOP
                )
            }, 100)
            mostrarDatePicker()
        }
        binding.etNombrePerfil.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                // El editText tiene el foco
                binding.etNombrePerfil.getBackground().setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP);
            } else {
                // El editText no tiene el foco
                binding.etNombrePerfil.getBackground().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
            }
        }
        binding.etApellidoPaternoPerfil.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                // El editText tiene el foco
                binding.etApellidoPaternoPerfil.getBackground().setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP);
            } else {
                // El editText no tiene el foco
                binding.etApellidoPaternoPerfil.getBackground().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
            }
        }
        binding.etApellidoMaternoPerfil.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                // El editText tiene el foco
                binding.etApellidoMaternoPerfil.getBackground().setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP);
            } else {
                // El editText no tiene el foco
                binding.etApellidoMaternoPerfil.getBackground().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
            }
        }
        binding.etNumeroTelefonoPerfil.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                // El editText tiene el foco
                binding.etNumeroTelefonoPerfil.getBackground().setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP);
            } else {
                // El editText no tiene el foco
                binding.etNumeroTelefonoPerfil.getBackground().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
            }
        }
        binding.etPasswordPerfil1.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                // El editText tiene el foco
                binding.etPasswordPerfil1.getBackground().setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP);
            } else {
                // El editText no tiene el foco
                binding.etPasswordPerfil1.getBackground().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
            }
        }
        binding.etPasswordPerfil2.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                // El editText tiene el foco
                binding.etPasswordPerfil2.getBackground().setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP);
            } else {
                // El editText no tiene el foco
                binding.etPasswordPerfil2.getBackground().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
            }
        }
        binding.btnEditar.setOnClickListener(){
            animacionBotones(binding.btnEditar)
            espera(true,bandera)
        }
        binding.btnCancelar.setOnClickListener(){
            animacionBotones(binding.btnCancelar)
            bandera =false
            espera(false,bandera)
            binding.etNombrePerfil.setText(clienteConsulta.nombre)
            binding.etApellidoPaternoPerfil.setText(clienteConsulta.apellidoPaterno)
            binding.etApellidoMaternoPerfil.setText(clienteConsulta.apellidoMaterno)
            binding.etNumeroTelefonoPerfil.setText(clienteConsulta.telefono)
            binding.etFechaNacimientoPerfil.setText(convertirFormatoFecha(clienteConsulta.fechaNacimiento!!))
//            binding.etPasswordPerfil.setText(clienteConsulta.contraseña)
        }
        binding.btnPassword.setOnClickListener(){
            animacionBotones(binding.btnPassword)
            bandera=true
            espera(true,bandera)



        }
        binding.btnGuardar.setOnClickListener(){
            animacionBotones(binding.btnGuardar)
            guardarCambios()

        }
    }
    private fun guardarCambios(){
        var clienteActualizado = Cliente()
        clienteActualizado.nombre = binding.etNombrePerfil.text.toString()
        clienteActualizado.apellidoPaterno= binding.etApellidoPaternoPerfil.text.toString()
        clienteActualizado.apellidoMaterno = binding.etApellidoMaternoPerfil.text.toString()
        clienteActualizado.telefono = binding.etNumeroTelefonoPerfil.text.toString()
        clienteActualizado.fechaNacimiento=fechaSQL
        clienteActualizado.idCliente = cliente.idCliente
        var respuesta = validarPassword()
        if (!respuesta){
            return
        }else{
            clienteActualizado.contraseña = binding.etPasswordPerfil1.text.toString()
        }
//        Toast.makeText(context,"contraseña: ${clienteActualizado.contraseña}",Toast.LENGTH_LONG).show()
        ClienteDAO.editarCliente(requireContext(),"cliente/editarCliente", clienteActualizado){
            respuesta->

            serializarRespuestaCrearCliente(respuesta)
        }
        var nombreCompleto = clienteActualizado.nombre!! + " " + clienteActualizado.apellidoPaterno + " " + clienteActualizado.apellidoMaterno
        update?.actualizarNombreCompleto(nombreCompleto)

    }

    private fun serializarRespuestaCrearCliente(json : String){
        var respuesta = gson.fromJson(json, Mensaje::class.java)
        Toast.makeText(context,respuesta.mensaje,Toast.LENGTH_SHORT).show()
        if (!respuesta.error){
            espera(false,false)

        }
    }
    private fun validarPassword(): Boolean {
        val password1 = binding.etPasswordPerfil1.text.toString()
        val password2 = binding.etPasswordPerfil2.text.toString()

        if (password1 == password2) {
            return true
        } else {
            Toast.makeText(requireContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            binding.etPasswordPerfil1.error ="Las contraseñas no coinciden"
            binding.etPasswordPerfil2.error ="Las contraseñas no coinciden"
            return false
        }
    }
    private fun espera(estado : Boolean,bandera: Boolean){
        val handler = Handler()
        handler.postDelayed({
            actualizarVisibilidadEdicion(estado,bandera)
        }, 200)
    }
    private fun actualizarVisibilidadEdicion(habilitarEdicion: Boolean, bandera : Boolean) {
        binding.etNombrePerfil.isEnabled = habilitarEdicion
        binding.etApellidoPaternoPerfil.isEnabled = habilitarEdicion
        binding.etApellidoMaternoPerfil.isEnabled = habilitarEdicion
        binding.etNumeroTelefonoPerfil.isEnabled = habilitarEdicion
        binding.etFechaNacimientoPerfil.isEnabled = habilitarEdicion
        binding.lnEdicion.visibility = if (habilitarEdicion) View.VISIBLE else View.GONE
        binding.btnPassword.visibility = if (habilitarEdicion) View.VISIBLE else View.GONE
        binding.btnEditar.visibility = if (habilitarEdicion) View.GONE else View.VISIBLE
        if (bandera){
            binding.lnPassword.visibility=View.VISIBLE
            binding.etPasswordPerfil1.isEnabled = true
            binding.etPasswordPerfil2.isEnabled = true
            binding.etPasswordPerfil1.setText("")
            binding.etPasswordPerfil2.setText("")
        }else{
            binding.lnPassword.visibility=View.GONE
            binding.etPasswordPerfil1.isEnabled = false
            binding.etPasswordPerfil2.isEnabled = true
            binding.etPasswordPerfil1.setText(clienteConsulta.contraseña)
            binding.etPasswordPerfil2.setText(clienteConsulta.contraseña)
        }
    }
    private fun animacionBotones(boton : Button){
        val colorAnimation = ObjectAnimator.ofArgb(boton, "backgroundColor",
            Color.parseColor("#03e9f4"),
            Color.parseColor("#000000"))
        colorAnimation.duration = 1000
        colorAnimation.start()
    }

    companion object {
        fun newInstance(cliente: Cliente): ConfiguracionPerfilFragment {
            val fragment = ConfiguracionPerfilFragment()
            val args = Bundle()
            args.putParcelable("cliente", cliente)
            fragment.arguments = args
            return fragment
        }
    }
}