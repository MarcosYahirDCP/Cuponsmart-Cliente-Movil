package uv.tc.cuponsmart_android.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import uv.tc.cuponsmart_android.databinding.FragmentPromocionesBinding

class PromocionesFragment : Fragment() {

    private lateinit var binding: FragmentPromocionesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPromocionesBinding.inflate(inflater, container, false)


        return binding.root
    }

}