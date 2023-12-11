package uv.tc.cuponsmart_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import uv.tc.cuponsmart_android.databinding.ActivityRecuperarPasswordBinding

class RecuperarPasswordActivity : AppCompatActivity() {
    lateinit var binding : ActivityRecuperarPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecuperarPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

    }
}