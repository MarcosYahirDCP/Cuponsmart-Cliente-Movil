package uv.tc.cuponsmart_android

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import uv.tc.cuponsmart_android.fragments.DatosCuentaFragment
import uv.tc.cuponsmart_android.fragments.DatosPersonalesFragment
import uv.tc.cuponsmart_android.fragments.DatosUbicacionFragment

class ViewPagerAdapterCrearCuenta(
    fragmentManager: FragmentManager,
    lifecycle:Lifecycle,
    private val listener: OnFragmentInteractionListener
)  : FragmentStateAdapter(fragmentManager, lifecycle){
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->DatosPersonalesFragment(listener)
            1 -> DatosCuentaFragment(listener)
            2 -> DatosUbicacionFragment(listener)
            else -> throw IllegalArgumentException("Posición de fragmento no válida: $position")
        }
    }
}