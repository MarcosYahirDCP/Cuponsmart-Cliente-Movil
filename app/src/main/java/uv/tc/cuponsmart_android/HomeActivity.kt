package uv.tc.cuponsmart_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import uv.tc.cuponsmart_android.databinding.ActivityHomeBinding
import uv.tc.cuponsmart_android.fragments.ListaCatalogoFragment
import uv.tc.cuponsmart_android.fragments.PerfilFragment
import uv.tc.cuponsmart_android.fragments.PromocionesFragment
import uv.tc.cuponsmart_android.modelo.poko.Cliente

class HomeActivity : AppCompatActivity() {
    //------------- DECALRACION DE VARIABLES --------------//
    lateinit var binding : ActivityHomeBinding
    lateinit var cliente : Cliente
    val perfilFragment = PerfilFragment()
    val categoriasFragment = ListaCatalogoFragment()
    val promocionesFragment = PromocionesFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        obtenerDatosCliente()
        if (cliente!= null){
            cargarInformacionCliente(perfilFragment)
            cargarInformacionCliente(categoriasFragment)
            cargarInformacionCliente(promocionesFragment)
        }
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        accionBottomBar()
    }
//-------------FUNCION PARA ASIGNAR LOS FRAGMENTOS ACORDE AL BUTTOM MENU ----------------//
    private fun accionBottomBar(){
        binding.bottomBar.onTabSelected={
                it->
            when(it.title){
                "Perfil"->{

                    replaceFragment(perfilFragment)
                    true
                }
                "Promociones" ->{
                    replaceFragment(promocionesFragment)
                    true
                }
                "Categorias" ->{
                    replaceFragment(categoriasFragment)
                    true
                }
                else->false
            }
        }
        replaceFragment(promocionesFragment)
    }
    //-------------- FUNCION AUXILIAR PARA REALIZAR EL CAMBIO DE FRAGMENTOS ----------------//
    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.fragment_contenedor,fragment).commit()
    }
    //-------- OBTENER LOS DATOS DEL CLIENTE QUE SE PASAN DESDE EL LOGIN -------------//
    private fun obtenerDatosCliente(){
        val jsonCliente = intent.getStringExtra("cliente")
        val gson = Gson()
        cliente = gson.fromJson(jsonCliente, Cliente::class.java)
    }
    //------------- FUNCION PARA CARGAR LOS DATOS DEL CLIENTE A LOS FRAGMENTS ----------------------//
    private  fun cargarInformacionCliente(fragment : Fragment){
        val args = Bundle()
        args.putParcelable("cliente",cliente)
        fragment.arguments = args
    }
}