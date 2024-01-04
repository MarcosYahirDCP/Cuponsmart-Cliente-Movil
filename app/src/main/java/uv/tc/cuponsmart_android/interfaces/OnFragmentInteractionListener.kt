package uv.tc.cuponsmart_android.interfaces


interface OnFragmentInteractionListener {
    fun obtenerDatos(): Map<String,String>
    fun validarCamposLlenos(): Boolean
    fun validarPassword(): Boolean
    fun obtenerFragmentId(): Int
}