package uv.tc.cuponsmart_android.modelo.poko

class Empresa {
    val idEmpresa: Int? = null
    val nombre: String? = null
    val nombreComercial: String? = null
    val representante: String? = null
    val correo: String? = null
    val telefono: String? = null
    val paginaWeb: String? = null
    val RFC: String? = null
    val estatus: String? = null
    val idUbicacion: Int? = null
    val fotoBase64: String? = null

    override fun toString(): String {
        return nombreComercial ?: nombre ?: "Nombre Desconocido"
    }
}