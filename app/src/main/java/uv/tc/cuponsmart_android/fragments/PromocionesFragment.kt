package uv.tc.cuponsmart_android.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import uv.tc.cuponsmart_android.CustomAdapter
import uv.tc.cuponsmart_android.databinding.FragmentPromocionesBinding
import uv.tc.cuponsmart_android.interfaces.NotificarClick
import uv.tc.cuponsmart_android.modelo.DAO.PromocionDAO
import uv.tc.cuponsmart_android.modelo.poko.Promocion
import java.util.ArrayList

class PromocionesFragment : Fragment(), NotificarClick{
    private  var listaPromociones: ArrayList<Promocion> = ArrayList()
    val gson = Gson()

    private lateinit var binding: FragmentPromocionesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPromocionesBinding.inflate(inflater, container, false)
        obtenerPromociones()

        return binding.root
    }
    private fun obtenerPromociones(){
        PromocionDAO.obtenerPromociones(requireContext(), "promocion/promociones"){
            respuesta->
            serializarRespuestaPromociones(respuesta)
        }
    }
    private fun serializarRespuestaPromociones(json : String){
        val typeLista= object : TypeToken<ArrayList<Promocion>>() {}.type
        listaPromociones = gson.fromJson(json,typeLista)
        cargarInformacionRecycler()
    }
    private  fun cargarInformacionRecycler(){
        binding.recyclerPromocion.layoutManager = LinearLayoutManager(context)

        if (listaPromociones.size > 0){
            binding.recyclerPromocion.adapter=CustomAdapter(listaPromociones,this,requireContext())
        }
    }

    override fun seleccionarItem(posicion: Int, promocion: Promocion) {
        
    }

}