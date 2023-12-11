package uv.tc.cuponsmart_android.archivos_dao

import android.content.Context
import uv.tc.cuponsmart_android.modelo.ConexionWS

class ClienteDAO {
    companion object{

        fun iniciarSesion (context :Context, url : String, json:String, callback : (String) ->Unit){
            ConexionWS.peticionPOST(context, url, json) { respuesta ->
                callback(respuesta)
            }
        }

    }
}