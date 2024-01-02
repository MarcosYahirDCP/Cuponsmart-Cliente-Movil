package uv.tc.cuponsmart_android.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import uv.tc.cuponsmart_android.R
import uv.tc.cuponsmart_android.databinding.FragmentListaCatalogoBinding


class ListaCatalogoFragment : Fragment() {
    private lateinit var binding:FragmentListaCatalogoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListaCatalogoBinding.inflate(inflater, container, false)
        return binding.root
    }

}