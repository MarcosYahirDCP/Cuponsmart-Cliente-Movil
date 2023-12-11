package uv.tc.cuponsmart_android.modelo.poko

class Municipio {
    val idMunicipio : Int = 0
    val nombre : String? =""
    val idEstado : Int = 0
    override fun toString(): String {
        return nombre ?: nombre!!
    }
}