package uv.tc.cuponsmart_android.modelo.DAO

import android.content.Context
import com.google.gson.Gson
import uv.tc.cuponsmart_android.modelo.ConexionWS
import uv.tc.cuponsmart_android.modelo.poko.Cliente

class ClienteDAO {

    companion object{
        val gson = Gson()
        var parametros :String = ""
        fun iniciarSesion (context :Context, url : String, json:String, callback : (String) ->Unit){
            ConexionWS.peticionPOST(context, url, json) { respuesta ->
                callback(respuesta)
            }
        }
        fun crearClienteNuevo(context: Context, url: String, cliente: Cliente, callback: (String) -> Unit){
            parametros = gson.toJson(cliente)
            ConexionWS.peticionPOST(context,url,parametros){respuesta ->
                callback(respuesta)
            }
        }

        fun obtenerClienteID(context: Context,url :String, idCliente : Int, callback: (String) -> Unit){
            var urlPath = "$url/$idCliente"
            ConexionWS.peticionGET(context,urlPath){
                respuesta->
                callback(respuesta)
            }
        }
        fun editarCliente(context: Context, url: String, cliente: Cliente,callback: (String) -> Unit){
            parametros = gson.toJson(cliente)
            ConexionWS.peticionPUT(context,url, parametros){
                respuesta->
                callback(respuesta)
            }
        }

    }
}