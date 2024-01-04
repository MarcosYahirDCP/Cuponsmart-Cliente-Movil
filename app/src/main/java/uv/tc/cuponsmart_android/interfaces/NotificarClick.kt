package uv.tc.cuponsmart_android.interfaces

import uv.tc.cuponsmart_android.modelo.poko.Promocion


interface NotificarClick {
    fun seleccionarItem(posicion : Int, promocion: Promocion)

}