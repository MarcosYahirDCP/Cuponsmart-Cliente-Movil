package uv.tc.cuponsmart_android.fragments

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import uv.tc.cuponsmart_android.OnFragmentInteractionListener
import uv.tc.cuponsmart_android.R
import uv.tc.cuponsmart_android.databinding.FragmentDatosPersonalesBinding
import uv.tc.cuponsmart_android.modelo.poko.Cliente

class DatosPersonalesFragment(private val listener: OnFragmentInteractionListener) : Fragment(),OnFragmentInteractionListener {
    private lateinit var binding: FragmentDatosPersonalesBinding
    var fechaSQL =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDatosPersonalesBinding.inflate(inflater, container, false)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        personalizarComponentes()
    }

    private fun mostrarDatePicker() {
        val datePicker = DatePickerFragment { dia, mes, anio -> onDateSelected(dia, mes, anio) }
        datePicker.show(childFragmentManager,"datepicker")

    }

    fun onDateSelected (dia:Int, mes: Int, anio:Int){
        val fechaFormateadaSQL = String.format("%04d-%02d-%02d", anio, mes + 1, dia)
        val fechaFormateadaView = String.format("%02d-%02d-%04d", dia,mes+1,anio)
        binding.etFechaNacimiento.setText(fechaFormateadaView)
        fechaSQL = fechaFormateadaSQL
    }

    override fun obtenerDatos(): Map<String, String> {
        val datos = mapOf(
            "nombre" to binding.etNombre.text.toString(),
            "apellidoP" to binding.etApellidoP.text.toString(),
            "apellidoM" to binding.etApellidoM.text.toString(),
            "fechaNacimiento" to fechaSQL
        )
        return datos
    }

    fun personalizarComponentes(){
        binding.etNombre.setOnFocusChangeListener {v, hasFocus ->
            if (hasFocus) {
                // El editText tiene el foco
                binding.etNombre.getBackground().setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP);
            } else {
                // El editText no tiene el foco
                binding.etNombre.getBackground().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
            }
        }

        binding.etApellidoP.setOnFocusChangeListener {v, hasFocus ->
            if (hasFocus) {
                // El editText tiene el foco
                binding.etApellidoP.getBackground().setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP);
            } else {
                // El editText no tiene el foco
                binding.etApellidoP.getBackground().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
            }
        }
        binding.etApellidoM.setOnFocusChangeListener {v, hasFocus ->
            if (hasFocus) {
                // El editText tiene el foco
                binding.etApellidoM.getBackground().setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP);
            } else {
                // El editText no tiene el foco
                binding.etApellidoM.getBackground().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
            }
        }
        binding.etFechaNacimiento.setOnClickListener {
            binding.etFechaNacimiento.getBackground().setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP)
            Handler(Looper.getMainLooper()).postDelayed({
                binding.etFechaNacimiento.getBackground().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP)
            }, 100)
            mostrarDatePicker()
        }

    }

    override fun validarCamposLlenos(): Boolean {
        var esCorrecto = true
        if (binding.etNombre.text.isNullOrBlank()){
            esCorrecto = false
            binding.etNombre.error = "Ingresa tu(s) nombre(s)"
        }
        if (binding.etApellidoP.text.isNullOrBlank()){
            esCorrecto = false
            binding.etApellidoP.error = "Ingresa tu apellido paterno"
        }
        if (binding.etApellidoM.text.isNullOrBlank()){
            esCorrecto = false
            binding.etApellidoM.error = "Ingresa tu apellido materno"
        }
        if (binding.etFechaNacimiento.text.isNullOrBlank()){
            esCorrecto = false
            binding.etFechaNacimiento.error = "Ingresa tu fecha de nacimiento"
        }
        return esCorrecto
    }

    override fun validarPassword(): Boolean {
        TODO("Not yet implemented")
    }

    override fun obtenerFragmentId(): Int {
        return 0
    }

}