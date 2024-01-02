package uv.tc.cuponsmart_android.modelo.poko

import android.os.Parcel
import android.os.Parcelable

class Cliente() : Parcelable {
    var idCliente: Int? = null
    var nombre: String? = null
    var apellidoPaterno: String? = null
    var apellidoMaterno: String? = null
    var telefono: String? = null
    var correo: String? = null
    var contraseña: String? = null
    var fechaNacimiento: String? = null
    var idUbicacion: Int? = null

    //------------ CONSTRUCTOR SECUNDARIO PARA EL PARCEL AL MOMENO DE MANDAR DATOS EN LOS FFRAGMENTS ------------//
    constructor(parcel: Parcel) :this(){
        idCliente = parcel.readInt()
        nombre = parcel.readString() ?: ""
        apellidoPaterno = parcel.readString() ?: ""
        apellidoMaterno = parcel.readString() ?: ""
        telefono = parcel.readString() ?: ""
        correo = parcel.readString() ?: ""
        contraseña = parcel.readString() ?: ""
        fechaNacimiento = parcel.readString() ?: ""
        idUbicacion = parcel.readInt()
    }
    //-------------- METODOS NECESARIOS PARA EL PARCEL --------------//
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(idCliente!!)
        parcel.writeString(nombre)
        parcel.writeString(apellidoPaterno)
        parcel.writeString(apellidoMaterno)
        parcel.writeString(telefono)
        parcel.writeString(correo)
        parcel.writeString(contraseña)
        parcel.writeString(fechaNacimiento)
        parcel.writeInt(idUbicacion!!)

    }
    companion object CREATOR : Parcelable.Creator<Cliente> {
        override fun createFromParcel(parcel: Parcel): Cliente {
            return Cliente(parcel)
        }

        override fun newArray(size: Int): Array<Cliente?> {
            return arrayOfNulls(size)
        }
    }
}