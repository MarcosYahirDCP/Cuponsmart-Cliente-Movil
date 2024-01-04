package uv.tc.cuponsmart_android

import android.app.AlertDialog
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import uv.tc.cuponsmart_android.interfaces.NotificarClick
import uv.tc.cuponsmart_android.modelo.DAO.EmpresaDAO
import uv.tc.cuponsmart_android.modelo.DAO.PromocionDAO
import uv.tc.cuponsmart_android.modelo.poko.Empresa
import uv.tc.cuponsmart_android.modelo.poko.Promocion
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class CustomAdapter(val promociones : ArrayList<Promocion>, val observador: NotificarClick, val context: Context) :RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    var empresa= Empresa()
    var gson = Gson()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder
    {
        val itemView = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_layout,viewGroup, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return promociones.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val promocion = promociones[position]
        holder.etNombrePromocion.text="${promocion.nombre}"
        EmpresaDAO.empresaPorId(context,"empresa/empresaPorId", promocion.idEmpresa!!){
            respuesta->
            serializarRespuesta(holder,respuesta)
        }
        descargarImagenPerfil(holder, promocion.idPromocion!!)
        holder.etTipo.text = "${promocion.tipo}"
        holder.etCupones.text = "${promocion.numeroCupones}"
        holder.etFecha.text = convertirFormatoFecha(promocion.inicioPromocion!!) + " / "+convertirFormatoFecha(promocion.finPromocion!!)
        holder.button.setOnClickListener{
           observador.seleccionarItem(position,promocion)
            // Crear un AlertDialog personalizado
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_layout, null)
            val builder = AlertDialog.Builder(context)
                .setView(dialogView)

            // Configurar elementos del diálogo
            val imageView = dialogView.findViewById<ImageView>(R.id.dialogImageView)
            val nameTextView = dialogView.findViewById<TextView>(R.id.tv_nombre_promocion)
            val numeroCupones = dialogView.findViewById<TextView>(R.id.tv_numero_cupones_dialog)
            val descripcion = dialogView.findViewById<TextView>(R.id.tv_descripcion)
            val fecha = dialogView.findViewById<TextView>(R.id.tv_fecha)
            val tipo = dialogView.findViewById<TextView>(R.id.tv_tipo)
            val codigo = dialogView.findViewById<TextView>(R.id.tv_codigo)
            val restricciones = dialogView.findViewById<TextView>(R.id.tv_restriccion)
            val closeButton = dialogView.findViewById<Button>(R.id.dialogCloseButton)

            // Configurar la información del elemento actual
            val currentPromocion = promociones[position]
            nameTextView.text = "Nombre: "+currentPromocion.nombre
            numeroCupones.text = "Numero Cupones: "+promocion.numeroCupones
            descripcion.text = "Descripcion: "+promocion.descripcion
            fecha.text = "Vigencia: "+convertirFormatoFecha(promocion.inicioPromocion!!) + " / "+convertirFormatoFecha(promocion.finPromocion!!)
            tipo.text = "Tipo: "+promocion.tipo
            codigo.text = "Código: "+promocion.codigoPromocion
            restricciones.text = "Restricciones: "+promocion.restriccion



            // Descargar e insertar la imagen en el ImageView (puedes usar la lógica existente)
            descargarImagenParaDialogo(imageView, currentPromocion.idPromocion!!)

            // Crear y mostrar el diálogo
            val dialog = builder.create()
            dialog.show()

            // Configurar el OnClickListener del botón de cierre
            closeButton.setOnClickListener {
                dialog.dismiss()
            }
        }
    }
    private fun descargarImagenParaDialogo(imageView: ImageView, idPromocion: Int) {
        PromocionDAO.descargarFotoPromocion(context, "promocion/obtenerImg", idPromocion) { respuesta ->
            // Procesar la respuesta y obtener la imagen en formato Base64
            val gson = Gson()
            val promocionFoto = gson.fromJson(respuesta, Promocion::class.java)

            // Verificar si la imagenBase64 no está vacía
            if (promocionFoto.imagenBase64!!.isNotEmpty()) {
                // Decodificar Base64 y mostrar la imagen en el ImageView
                val byteIMG = Base64.decode(promocionFoto.imagenBase64, Base64.DEFAULT)
                val bitMapImg = BitmapFactory.decodeByteArray(byteIMG, 0, byteIMG.size)
                imageView.setImageBitmap(bitMapImg)
            } else {
                // Si la imagenBase64 está vacía, puedes mostrar una imagen de marcador de posición o manejarlo según tus necesidades.
                //imageView.setImageResource(R.drawable.placeholder_image)
            }
        }
    }
    private fun descargarImagenPerfil(holder:ViewHolder,idPromocion : Int){
        PromocionDAO.descargarFotoPromocion(context,"promocion/obtenerImg", idPromocion){
            respuesta->
            serializarRespuestaFoto(holder,respuesta)
        }
    }
    private fun serializarRespuestaFoto(holder: ViewHolder,json : String){
        var promocionFoto =gson.fromJson(json, Promocion::class.java)
        if(promocionFoto.imagenBase64!!.isNotEmpty()){
            cargarFoto(holder,promocionFoto.imagenBase64!!)
        }
    }
    private  fun cargarFoto(holder:ViewHolder,imagenBase64 : String){
        val byteIMG = Base64.decode(imagenBase64, Base64.DEFAULT)
        val bitMapImg = BitmapFactory.decodeByteArray(byteIMG, 0,byteIMG.size)
        holder.imagenView.setImageBitmap(bitMapImg)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun convertirFormatoFecha(fechaOriginal: String): String {
        try {
            val formatoOriginal = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val fechaLocalDate: LocalDate = LocalDate.parse(fechaOriginal, formatoOriginal)

            val formatoNuevo = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            return fechaLocalDate.format(formatoNuevo)
        } catch (e: Exception) {
            e.printStackTrace()
            return fechaOriginal
        }
    }
    private fun serializarRespuesta(holder:ViewHolder,json: String){
        empresa = gson.fromJson(json,Empresa::class.java)
        holder.etNombreEmpresa.text = empresa.nombreComercial
    }
    class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val etNombrePromocion: TextView = itemView.findViewById(R.id.et_nombre_promocion)
        val etNombreEmpresa : TextView = itemView.findViewById(R.id.et_empresa)
        val etTipo:TextView = itemView.findViewById(R.id.et_tipo_promocion)
        val etFecha : TextView = itemView.findViewById(R.id.et_fecha_promocion)
        val etCupones : TextView = itemView.findViewById(R.id.et_numero_cupones)
        val button : Button = itemView.findViewById(R.id.btn_ver_detalles)
        val imagenView : ImageView = itemView.findViewById(R.id.iv_promocion)
    }
}