package uv.tc.cuponsmart_android.modelo.DAO

import android.content.Context
import uv.tc.cuponsmart_android.modelo.ConexionWS

class EmpresaDAO {
    companion object{
        fun empresaPorId(context: Context,url:String,idEmpresa: Int,callback: (String) -> Unit){
            var urlPath = "$url/$idEmpresa"
            ConexionWS.peticionGET(context, urlPath){
                respuesta ->
                callback(respuesta)
            }
        }
    }
}