package uv.tc.cuponsmart_android.modelo.DAO

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import uv.tc.cuponsmart_android.modelo.ConexionWS
import uv.tc.cuponsmart_android.modelo.poko.Coordenada
import uv.tc.cuponsmart_android.modelo.poko.Municipio
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

        fun obtenerUbicacionPorId(context: Context, url: String, idUbicacion : Int, callback: (String) -> Unit){
            var urlPath = "$url/$idUbicacion"
            ConexionWS.peticionGET(context,urlPath){
                respuesta ->
                callback(respuesta)
            }
        }
        fun obtenerMunicipioPorId(context: Context,url: String,idMunicipio: Int,callback: (String) -> Unit){
            var urlPath= "$url/$idMunicipio"
            ConexionWS.peticionGET(context,urlPath){
                respuesta ->
                callback(respuesta)
            }
        }
        fun obtenerEstadoPorId(context: Context,url: String, idEstado : Int, callback: (String) -> Unit){
            var urlPath = "$url/$idEstado"
            ConexionWS.peticionGET(context,urlPath){
                respuesta ->
                callback(respuesta)
            }

        }
    }
}