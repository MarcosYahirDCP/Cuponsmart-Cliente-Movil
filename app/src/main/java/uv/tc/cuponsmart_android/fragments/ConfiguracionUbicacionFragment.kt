package uv.tc.cuponsmart_android.fragments

import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.os.Handler
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import com.google.gson.Gson
import uv.tc.cuponsmart_android.R
import uv.tc.cuponsmart_android.databinding.FragmentConfiguracionUbicacionBinding
import uv.tc.cuponsmart_android.databinding.FragmentDatosUbicacionBinding
import uv.tc.cuponsmart_android.modelo.ConexionWS
import uv.tc.cuponsmart_android.modelo.DAO.CatalogoDAO
import uv.tc.cuponsmart_android.modelo.DAO.ClienteDAO
import uv.tc.cuponsmart_android.modelo.DAO.UbicacionDAO
import uv.tc.cuponsmart_android.modelo.poko.Cliente
import uv.tc.cuponsmart_android.modelo.poko.Estado
import uv.tc.cuponsmart_android.modelo.poko.Municipio
import uv.tc.cuponsmart_android.modelo.poko.Ubicacion


class ConfiguracionUbicacionFragment : Fragment(), AdapterView.OnItemSelectedListener {
    //------------------ DECLARACION DE VARIABLES ---------------//
    private lateinit var binding: FragmentConfiguracionUbicacionBinding
    private lateinit var cliente: Cliente
    private lateinit var estados: List<Estado>
    private lateinit var municipios: List<Municipio>
    private var ubicacionConsulta = Ubicacion()
    var gson = Gson()
    private var municipio = Municipio()
    private var estado = Estado()
    private var idMunicipioSeleccionado: Int =0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConfiguracionUbicacionBinding.inflate(inflater,container,false)
        obtenerDatosActivity()
        cargarDatosUbicacion(cliente.idUbicacion!!)
        cargarSpiners()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        personalizarComponentes()

    }
  
    //-------------- METODO PARA OBTENER LOS DATOS DE LA UBICACION ---------------//
    private  fun cargarDatosUbicacion(idUbicacion:Int){
        UbicacionDAO.obtenerUbicacionPorId(requireContext(),"ubicacion/obtenerUbicacion",idUbicacion){
            respuesta ->
            serializarRespuestaUbicacion(respuesta)
        }
    }
    //-------------- INTERPRETAR RESPUESTA DE CONSULTA UBICACION POR ID ------------//
    private fun serializarRespuestaUbicacion(json:String){
        ubicacionConsulta = gson.fromJson(json, Ubicacion::class.java)
        binding.etCalle.setText(ubicacionConsulta.calle)
        binding.etCodigoPostal.setText(ubicacionConsulta.codigoPostal)
        binding.etColonia.setText(ubicacionConsulta.colonia)
        binding.etNumeroCasa.setText(ubicacionConsulta.numero)
        UbicacionDAO.obtenerMunicipioPorId(requireContext(),"catalogo/obtenerMunicipioPorId", ubicacionConsulta.idMunicipio!!){
            respuesta ->
            Log.d("Respuesta",respuesta)
            serializarRespuestaMunicipio(respuesta)
        }
    }
    //---------- INTERPRETAR RESPUESTA DE LA BUSQUEDA DE MUNICIPIO -------------//
    private fun serializarRespuestaMunicipio(json:String){
        municipio = gson.fromJson(json,Municipio::class.java)
        cargarSpinnersPerfil(municipio)

    }




    //----------- METODO PARA ESTABLECER EN EL SPINNER LOS DATOS POR DEFECTO ----------------//
    private fun cargarSpinnersPerfil(municipio: Municipio){
        Toast.makeText(requireContext(), "El municipio pasado es ${municipio.nombre} con id ${municipio.idMunicipio}",Toast.LENGTH_SHORT).show()
        val indiceEstado = estados.indexOfFirst { it.idEstado == municipio.idEstado }
        binding.spEstado.setSelection(indiceEstado)
       espera(municipio.idMunicipio)
    }

    //----------- METODO PARA PAUSAR EL SISTEMA 200 MILISEGUNDOS PARA QUE SE INICIE LA LISTA DE MUNICIPIOS ------//
    private fun espera(idMunicipio: Int){
        val handler = Handler()
        handler.postDelayed({
            val indiceMunicipio = municipios.indexOfFirst { it.idMunicipio == idMunicipio }
            binding.spMunicipio.setSelection(indiceMunicipio)
        }, 200)

    }

    //-------------- OBTENER LOS DATOS DEL CLIENTE DE LA ACTIVITY DESDE EL FRAGMENT -------//
    private fun obtenerDatosActivity() {
        cliente = arguments?.getParcelable("cliente")!!
    }

    //--------- METODO PARA LLENAR EL SPINER ESTADO -------------//
    private fun cargarSpiners(){
        CatalogoDAO.obtenerEstados(requireContext(),"catalogo/obtenerEstados"){ estados ->
            if (estados != null) {
                this.estados = estados
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, estados)
                adapter.setDropDownViewResource(com.google.android.material.R.layout.support_simple_spinner_dropdown_item)
                binding.spEstado.adapter = adapter
                binding.spEstado.onItemSelectedListener=this@ConfiguracionUbicacionFragment
            } else {
                Toast.makeText(context, "Error al obtener estados", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //-------- METODO PARA LLENAR EL SPINNER DE LOS MUNICIPIOS ----------//
    private fun llenarMunicipios(idEstado : Int){
        CatalogoDAO.obtenerMunicipiosEstado(requireContext(), "catalogo/obtenerMunicipiosEstados/$idEstado"){
                municipios ->
            if (municipios !=null){
                this.municipios = municipios
                val adaptadorMunicipios = ArrayAdapter(requireActivity(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item, municipios)
                binding.spMunicipio.adapter = adaptadorMunicipios
                binding.spMunicipio.onItemSelectedListener=this@ConfiguracionUbicacionFragment
            }
        }
    }

    //-------- METODO PARA PERSONALIZAR COMPONENTES Y EVENTOS ---------//
    private fun personalizarComponentes(){
        binding.spMunicipio.isEnabled=false
        binding.spMunicipio.isEnabled=false
        binding.etCalle.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus){
                binding.etCalle.getBackground().setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP);
            }else{
                binding.etCalle.getBackground().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
            }
        }

        binding.etColonia.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus){
                binding.etColonia.getBackground().setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP);
            }else{
                binding.etColonia.getBackground().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
            }
        }

        binding.etCodigoPostal.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus){
                binding.etCodigoPostal.getBackground().setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP);
            }else{
                binding.etCodigoPostal.getBackground().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
            }
        }

        binding.etNumeroCasa.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus){
                binding.etNumeroCasa.getBackground().setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP);
            }else{
                binding.etNumeroCasa.getBackground().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
            }
        }


    }

    //------ METODO PARA GUARDAR LOS CAMBIOS DEL PERFIL --------//

    //-------- METODO PARA LAS ANIMACIONES DE LOS BOTONES ----------//
    private fun animacionBotones(boton : Button){
        val colorAnimation = ObjectAnimator.ofArgb(boton, "backgroundColor",
            Color.parseColor("#03e9f4"),
            Color.parseColor("#000000"))
        colorAnimation.duration = 1000
        colorAnimation.start()
    }

    //------------ METODO ESTATICO PARA EL ENVIO/RECEPCION DE DATOS EN EL FRAGMENTE ------------//
    companion object {
        fun newInstance(cliente: Cliente): ConfiguracionUbicacionFragment {
            val fragment = ConfiguracionUbicacionFragment()
            val args = Bundle()
            args.putParcelable("cliente", cliente)
            fragment.arguments = args
            return fragment
        }
    }
    //-------------- METODOS DE LA INTERFACE -------------//
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when(parent?.id){
            R.id.sp_estado->{
                llenarMunicipios(estados[position].idEstado)
            }
            R.id.sp_municipio ->{
                idMunicipioSeleccionado =municipios[position].idMunicipio
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}