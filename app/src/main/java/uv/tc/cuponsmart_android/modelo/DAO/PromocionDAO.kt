package uv.tc.cuponsmart_android.modelo.DAO

import android.content.Context
import uv.tc.cuponsmart_android.modelo.ConexionWS

class PromocionDAO {
    companion object{
        fun obtenerPromociones(context: Context,url: String,callback: (String) -> Unit){
            ConexionWS.peticionGET(context,url){
                respuesta ->
                callback(respuesta)
            }
        }
    }
}