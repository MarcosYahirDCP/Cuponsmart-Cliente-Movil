package uv.tc.cuponsmart_android.fragments

import android.graphics.PorterDuff
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import uv.tc.cuponsmart_android.interfaces.OnFragmentInteractionListener
import uv.tc.cuponsmart_android.R
import uv.tc.cuponsmart_android.databinding.FragmentDatosCuentaBinding


class DatosCuentaFragment (private val listener: OnFragmentInteractionListener): Fragment(),
    OnFragmentInteractionListener {
    //------------ DECLARACION DE VARIABLES ---------//
    private lateinit var binding: FragmentDatosCuentaBinding

    //--------- METODOS DEL FRAGMENT ---------//
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDatosCuentaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        personalizarComponentes()
    }

    //------- METODO PARA PERSONALIZAR COMPONENTES Y EVENTOS ----------//
        private  fun personalizarComponentes(){
            binding.etCorreo.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus){
                    binding.etCorreo.getBackground().setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP);
                }else{
                    binding.etCorreo.getBackground().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
                }
            }
            binding.etPassword1.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus){
                    binding.etPassword1.getBackground().setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP);
                }else{
                    binding.etPassword1.getBackground().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
                }
            }
            binding.etPassword2.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus){
                    binding.etPassword2.getBackground().setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP);
                }else{
                    binding.etPassword2.getBackground().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
                }
            }
            binding.etNumeroTelefono.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus){
                    binding.etNumeroTelefono.getBackground().setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP);
                }else{
                    binding.etNumeroTelefono.getBackground().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
                }
            }
        }

    //------ METODO PARA OBTENER LOS DATOS DE LOS COMPONENTES --------//
    override fun obtenerDatos(): Map<String, String> {
        val datos = mapOf(
            "correo" to binding.etCorreo.text.toString(),
            "password" to binding.etPassword1.text.toString(),
            "numeroTelefono" to binding.etNumeroTelefono.text.toString()
        )
        return datos
    }

    //---------- METODO PARA VALIDAR QUE LOS CAMPOS ESTEN LLENOS ---------//
    override fun validarCamposLlenos(): Boolean {
        var esCorrecto = true
        if (binding.etCorreo.text.isNullOrBlank()){
            esCorrecto = false
            binding.etCorreo.error = "Ingresa tu correo electrónico"
        }
        if (binding.etPassword1.text.isNullOrBlank()){
            esCorrecto = false
            binding.etPassword1.error = "Ingresa una contraseña"
        }
        if (binding.etPassword2.text.isNullOrBlank()){
            esCorrecto = false
            binding.etPassword2.error = "Ingresa nuevamente tu contraseña"
        }
        if (binding.etNumeroTelefono.text.isNullOrBlank()){
            esCorrecto = false
            binding.etNumeroTelefono.error = "Ingresa tu número de teléfono"
        }
        return esCorrecto
    }

    //------- METODO PARA VALIDAR QUE LAS CONTRASEÑAS COINCIDAN ---------//
    override fun validarPassword(): Boolean {
        val password1 = binding.etPassword1.text.toString()
        val password2 = binding.etPassword2.text.toString()

        if (password1 == password2) {
            return true
        } else {
            Toast.makeText(requireContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            binding.etPassword1.error ="Las contraseñas no coinciden"
            binding.etPassword2.error ="Las contraseñas no coinciden"
            return false
        }
    }

    //------------ METODO PARA OBTENER EL ID DEL FRAGMENTO DESDE 0 A 2 ---------//
    override fun obtenerFragmentId(): Int {
        return 1
    }

}
