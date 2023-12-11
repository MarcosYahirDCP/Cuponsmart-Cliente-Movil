package uv.tc.cuponsmart_android

import android.animation.ObjectAnimator
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.UnderlineSpan
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.koushikdutta.ion.Ion
import uv.tc.cuponsmart_android.archivos_dao.ClienteDAO
import uv.tc.cuponsmart_android.databinding.ActivityLoginBinding
import uv.tc.cuponsmart_android.modelo.poko.Cliente
import uv.tc.cuponsmart_android.modelo.poko.RespuestaLogin


class LoginActivity : AppCompatActivity() {
    lateinit var binding : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        personalizacionComponentes()


    }
    private fun personalizacionComponentes(){

        val mitextoU = SpannableString("¿Olvidaste tu contraseña? Clic aquí")
        mitextoU.setSpan(UnderlineSpan(), 0, mitextoU.length, 0)
        binding.tvRecuperarPassword.setText(mitextoU)


        //---------------> Poner color al estar presionado un edit text y cuando no cambiarlo a negro por defecto
        binding.etCorreoUsuario.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                // El editText tiene el foco
                binding.etCorreoUsuario.getBackground().setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP);
            } else {
                // El editText no tiene el foco
                binding.etCorreoUsuario.getBackground().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
            }
        }
        binding.etPassword.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                // El editText tiene el foco
                binding.etPassword.getBackground().setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP);
            } else {
                // El editText no tiene el foco
                binding.etPassword.getBackground().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
            }
        }

        binding.tvRecuperarPassword.setMovementMethod(LinkMovementMethod.getInstance())
        binding.tvRecuperarPassword.setOnClickListener {
            binding.tvRecuperarPassword.setTextColor(resources.getColor(R.color.blue))
            startActivity(Intent(this, RecuperarPasswordActivity::class.java))
            Handler(Looper.getMainLooper()).postDelayed({
                binding.tvRecuperarPassword.setTextColor(resources.getColor(R.color.black))
            }, 500)
        }

        binding.btnInicioSesion.setOnClickListener {
            // Cambia el color del botón a un tono azul
            animacionBotones(binding.btnInicioSesion)
            if (validarCamposLogin()){
                verificarCredencialesVacias(binding.etCorreoUsuario.text.toString(), binding.etPassword.text.toString())
            }

        }

        binding.btnCrearCuenta.setOnClickListener{
           animacionBotones(binding.btnCrearCuenta)

            startActivity(Intent(this, CrearCuentaActivity::class.java))
            this.finish()
        }

    }

    private fun animacionBotones(boton : Button){
        val colorAnimation = ObjectAnimator.ofArgb(boton, "backgroundColor", Color.parseColor("#03e9f4"), R.color.black)
        colorAnimation.duration = 1000
        colorAnimation.start()
    }

    fun validarCamposLogin():Boolean{
        var esCorrecto = true
        if (binding.etCorreoUsuario.text.isNullOrBlank()){
            esCorrecto=false
            binding.etCorreoUsuario.error = "Correo Electronico obligatorio"
        }
        if (binding.etPassword.text.isNullOrBlank()){
            esCorrecto = false
            binding.etPassword.error="Contraseña obligatoria"
        }
        return esCorrecto
    }


    fun verificarCredencialesVacias(email:String, password : String) {
        Ion.getDefault(this@LoginActivity).conscryptMiddleware.enable(false)
        val gson = Gson()
        val jsonParam = gson.toJson(mapOf("correo" to email, "contraseña" to password))
        ClienteDAO.iniciarSesion(this@LoginActivity,"autenticacion/iniciarSesionMobil/", jsonParam){
            respuesta ->
            serializarRespuestaLogin(respuesta)
        }
    }

    fun serializarRespuestaLogin (json : String){
        val gson = Gson()
        Log.d("Respuesta JSON", json)
        val respuestaLogin = gson.fromJson(json, RespuestaLogin::class.java)
        Toast.makeText(this@LoginActivity,respuestaLogin.mensaje, Toast.LENGTH_LONG).show()
        if (!respuestaLogin.error){
            irPantallaPrincipal(respuestaLogin.clienteSesion) //a como debe ser en el postman
        }else{

        }
    }
    fun irPantallaPrincipal( cliente : Cliente){
        /* val gson = Gson()
         val stringPaciente = gson.toJson(paciente)*/

        val intent = Intent(this@LoginActivity,HomeActivity::class.java)
       // intent.extras!!.putString("cliente",string)
        // intent.extras!!.putString("paciente", stringPaciente)
        startActivity(intent)
        this.finish()
    }
}