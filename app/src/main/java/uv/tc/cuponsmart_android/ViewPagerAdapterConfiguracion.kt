package uv.tc.cuponsmart_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import uv.tc.cuponsmart_android.fragments.ConfiguracionPerfilFragment
import uv.tc.cuponsmart_android.fragments.ConfiguracionUbicacionFragment
import uv.tc.cuponsmart_android.modelo.poko.Cliente

class ViewPagerAdapterConfiguracion(
    fragmentManager: FragmentManager,
    lifecycle:Lifecycle,
) : FragmentStateAdapter(fragmentManager, lifecycle){
    private var cliente = Cliente()

    fun setCliente(cliente: Cliente) {
        this.cliente = cliente
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->ConfiguracionPerfilFragment.newInstance(cliente)
            1->ConfiguracionUbicacionFragment.newInstance(cliente)
            else->throw IllegalArgumentException("Posición de fragmento no válida: $position")
        }

    }

}