package uv.tc.cuponsmart_android.modelo

import android.content.Context
import android.widget.Toast
import com.koushikdutta.ion.Ion
import uv.tc.cuponsmart_android.modelo.poko.RespuestaHTTP
import uv.tc.cuponsmart_android.util.Constantes
import java.net.MalformedURLException
import java.util.Objects

class ConexionWS {
    companion object {
        fun peticionGET(context: Context, url: String, callback: (String) -> Unit) {
            var respuestaPeticion = ""
            Ion.with(context).load("GET", Constantes.URL_WS + url).asString(Charsets.UTF_8)
                .setCallback { e, response ->
                    if (e == null && response != null) {
                        respuestaPeticion = response
                        callback(respuestaPeticion)
                    } else {
                        Toast.makeText(
                            context, "Error de conexión, Inténtalo más tarde", Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }

        fun peticionPOST(context: Context, url: String, json: String, callback: (String) -> Unit) {
            var respuestaPeticion = ""
            Ion.with(context).load("POST", Constantes.URL_WS + url)
                .setHeader("Content-Type", "application/json").setStringBody(json).asString(Charsets.UTF_8)
                .setCallback { e, response ->
                    if (e == null && response != null) {
                        respuestaPeticion = response
                        callback(respuestaPeticion)
                    } else {
                        Toast.makeText(
                            context, "Error de conexión, Intentalo más tarde", Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }


        fun peticionPUT(context: Context, url: String, json: String, callback: (String) -> Unit) {
            var respuestaPeticion = ""
            Ion.with(context).load("PUT", Constantes.URL_WS + url)
                .setHeader("Content-Type", "application/json").setStringBody(json).asString(Charsets.UTF_8)
                .setCallback { e, response ->
                    if (e == null && response != null) {
                        respuestaPeticion = response
                        callback(respuestaPeticion)
                    } else {
                        Toast.makeText(
                            context, "Error de conexión, Inténtalo más tarde", Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }

        fun peticionDELETE(context: Context, url: String, callback: (String) -> Unit) {
            var respuestaPeticion = ""
            Ion.with(context).load("DELETE", Constantes.URL_WS + url).asString(Charsets.UTF_8)
                .setCallback { e, response ->
                    if (e == null && response != null) {
                        respuestaPeticion = response
                        callback(respuestaPeticion)
                    } else {
                        Toast.makeText(
                            context, "Error de conexión, Inténtalo más tarde", Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }

    }
}