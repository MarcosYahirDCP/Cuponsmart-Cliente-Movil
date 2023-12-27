package uv.tc.cuponsmart_android.archivos_dao

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import uv.tc.cuponsmart_android.modelo.ConexionWS
import uv.tc.cuponsmart_android.modelo.poko.Coordenada
import uv.tc.cuponsmart_android.modelo.poko.Mensaje
import uv.tc.cuponsmart_android.modelo.poko.Ubicacion

class UbicacionDAO {

    companion object {
        var gson = Gson()
        fun obtenerIDUbicacionCreada(context: Context, url: String, coordenada: Coordenada, callback: (Int) -> Unit) {
            var parametros : String = gson.toJson(coordenada)
            var idObtenida : Int = 0
            ConexionWS.peticionPOST(context, url, parametros) { respuesta ->

                Log.d("AAAAAAA",respuesta)
                Toast.makeText(context,"La respuesta es: $respuesta",Toast.LENGTH_SHORT).show()
                callback(respuesta.toInt())
            }
        }

        fun agregarUbicacion(context: Context, url: String, ubicacion: Ubicacion, callback: (String) -> Unit) {
            var parametros: String = gson.toJson(ubicacion)
            ConexionWS.peticionPOST(context, url, parametros) { respuesta ->

                callback(respuesta)
            }

        }

    }
}