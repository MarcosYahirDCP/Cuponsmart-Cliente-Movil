package uv.tc.cuponsmart_android.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import uv.tc.cuponsmart_android.interfaces.Update
import uv.tc.cuponsmart_android.ViewPagerAdapterConfiguracion
import uv.tc.cuponsmart_android.databinding.FragmentPerfilBinding
import uv.tc.cuponsmart_android.modelo.DAO.ClienteDAO
import uv.tc.cuponsmart_android.modelo.poko.Cliente


class PerfilFragment : Fragment(), Update {
    private lateinit var binding : FragmentPerfilBinding
    var cliente = Cliente()
    var clienteConsulta = Cliente()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPerfilBinding.inflate(inflater, container, false)
        obtenerDatosActivity()
        cargarComponentes()
        cargarDatosPerfil(cliente.idCliente!!)



        return binding.root
    }

    fun cargarDatosPerfil(idCliente: Int) {
        ClienteDAO.obtenerClienteID(
            requireContext(),
            "cliente/clientePorId",
            idCliente
        ) { respuesta ->
            serializarRespuestaClieteId(respuesta)
        }

    }

    private  fun serializarRespuestaClieteId(json : String){
        val gson =Gson()
        clienteConsulta = gson.fromJson(json, Cliente ::class.java)
        var nombreCompleto = clienteConsulta.nombre + " " + clienteConsulta.apellidoPaterno + " " + clienteConsulta.apellidoMaterno
        binding.etNombreCompleto.setText(nombreCompleto)
        binding.etCorreo1.setText(clienteConsulta.correo)
    }
    private fun obtenerDatosActivity(){
        cliente = arguments?.getParcelable("cliente")!!

    }

     fun cargarComponentes(){
        binding.tabMenu.addTab(binding.tabMenu.newTab().setText("Perfil"))
        binding.tabMenu.addTab(binding.tabMenu.newTab().setText("Ubicación"))

        val viewPagerAdapter = ViewPagerAdapterConfiguracion(childFragmentManager, lifecycle)
        viewPagerAdapter.setCliente(cliente)
        binding.vpConfiguraciones.adapter = viewPagerAdapter

        binding.tabMenu.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null){
                    binding.vpConfiguraciones.currentItem= tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
        binding.vpConfiguraciones.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tabMenu.selectTab(binding.tabMenu.getTabAt(position))
            }
        })
    }

    override fun actualizarNombreCompleto(nombreCompleto: String) {
        binding.etNombreCompleto.setText(nombreCompleto)
    }

}