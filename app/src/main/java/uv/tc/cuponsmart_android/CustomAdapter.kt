package uv.tc.cuponsmart_android

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import uv.tc.cuponsmart_android.interfaces.NotificarClick
import uv.tc.cuponsmart_android.modelo.DAO.EmpresaDAO
import uv.tc.cuponsmart_android.modelo.poko.Empresa
import uv.tc.cuponsmart_android.modelo.poko.Promocion


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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val promocion = promociones[position]
        holder.etNombrePromocion.text="${promocion.nombre}"
        EmpresaDAO.empresaPorId(context,"empresa/empresaPorId", promocion.idEmpresa!!){
            respuesta->
            serializarRespuesta(holder,respuesta)
        }
        holder.etTipo.text = "${promocion.tipo}"
        holder.etFecha.text = "${promocion.inicioPromocion} - ${promocion.finPromocion}"
        holder.button.setOnClickListener{
           observador.seleccionarItem(position,promocion)
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
        val button : Button = itemView.findViewById(R.id.btn_ver_detalles)
    }
}