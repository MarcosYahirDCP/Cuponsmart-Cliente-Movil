package uv.tc.cuponsmart_android.modelo.poko

class Estado {
    var idEstado: Int = 0
    var nombre: String? = ""

    override fun toString(): String {
        return nombre ?: nombre!!
    }
}