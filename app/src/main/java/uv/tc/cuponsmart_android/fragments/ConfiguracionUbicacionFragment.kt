package uv.tc.cuponsmart_android.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.gson.Gson
import uv.tc.cuponsmart_android.R
import uv.tc.cuponsmart_android.databinding.FragmentConfiguracionUbicacionBinding
import uv.tc.cuponsmart_android.databinding.FragmentDatosUbicacionBinding
import uv.tc.cuponsmart_android.modelo.ConexionWS
import uv.tc.cuponsmart_android.modelo.DAO.CatalogoDAO
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
    private fun serializarRespuestaMunicipio(json:String){
        municipio = gson.fromJson(json,Municipio::class.java)
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

    companion object {
        fun newInstance(cliente: Cliente): ConfiguracionUbicacionFragment {
            val fragment = ConfiguracionUbicacionFragment()
            val args = Bundle()
            args.putParcelable("cliente", cliente)
            fragment.arguments = args
            return fragment
        }
    }

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
        TODO("Not yet implemented")
    }
}