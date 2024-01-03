package uv.tc.cuponsmart_android.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uv.tc.cuponsmart_android.R
import uv.tc.cuponsmart_android.modelo.poko.Cliente


class ConfiguracionUbicacionFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_configuracion_ubicacion, container, false)
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
}