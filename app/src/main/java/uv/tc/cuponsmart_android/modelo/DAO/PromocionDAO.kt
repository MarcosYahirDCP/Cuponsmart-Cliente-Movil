package uv.tc.cuponsmart_android.modelo.DAO

import android.content.Context
import uv.tc.cuponsmart_android.modelo.ConexionWS

class PromocionDAO {
    companion object{
        fun obtenerPromocionesCategoria(context: Context,url: String, idCategoria : Int, callback: (String) -> Unit){
            var urlPath = "$url/$idCategoria"
            ConexionWS.peticionGET(context,urlPath){
                respuesta ->
                callback(respuesta)
            }
        }
        fun obtenerPromocionesPorEmpresa(context: Context, url: String,idEmpresa : Int, callback: (String) -> Unit){
            var urlPath = "$url/$idEmpresa"
            ConexionWS.peticionGET(context,urlPath){
                respuesta->
                callback(respuesta)
            }
        }

        fun descargarFotoPromocion(context: Context,url: String,idPromocion: Int, callback: (String) -> Unit){
            var urlPath = "$url/$idPromocion"
            ConexionWS.peticionGET(context, urlPath){
                respuesta->
                callback(respuesta)
            }
        }
        fun obtenerPromociones(context: Context,url: String,callback: (String) -> Unit){
            ConexionWS.peticionGET(context,url){
                respuesta ->
                callback(respuesta)
            }
        }
        fun obtenerPromocionesFechaInicio(context: Context, url: String, fechaInicio :String, callback: (String) -> Unit){
            var urlPath = "$url/$fechaInicio"
            ConexionWS.peticionGET(context,urlPath){
                respuesta ->
                callback(respuesta)
            }
        }

        fun obtenerEmpresas(context: Context, url: String, callback: (String) -> Unit){
            ConexionWS.peticionGET(context,url){
                respuesta->
                callback(respuesta)
            }
        }
    }
}