package uv.tc.cuponsmart_android.modelo.DAO

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import uv.tc.cuponsmart_android.modelo.ConexionWS
import uv.tc.cuponsmart_android.modelo.poko.Estado
import uv.tc.cuponsmart_android.modelo.poko.Municipio

class CatalogoDAO {
    companion object{
        fun obtenerEstados(context : Context, url : String, callback: (List <Estado>?) -> Unit ){
            ConexionWS.peticionGET(context,url){
                respuesta ->
                Log.i("Estados:", ""+respuesta)
                    val gson = Gson()
                    val estados = gson.fromJson(respuesta, Array<Estado> ::class.java).toList()
                    callback(estados)
            }
        }

        fun obtenerMunicipiosEstado(context : Context, url : String, callback: (List <Municipio>?) -> Unit ){
            ConexionWS.peticionGET(context,url){
                    respuesta ->
                val gson = Gson()
                val municipios = gson.fromJson(respuesta, Array<Municipio> ::class.java).toList()
                callback(municipios)
            }
        }
    }
}