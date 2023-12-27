package uv.tc.cuponsmart_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.google.android.material.R
import uv.tc.cuponsmart_android.archivos_dao.CatalogoDAO
import uv.tc.cuponsmart_android.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    lateinit var binding : ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

    }
}