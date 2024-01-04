package uv.tc.cuponsmart_android.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import uv.tc.cuponsmart_android.R
import java.util.Calendar

class DatePickerFragment2(val listener: (dia:Int, mes:Int, anio:Int) -> Unit): DialogFragment(),
    DatePickerDialog.OnDateSetListener {

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        listener(dayOfMonth,month,year)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val dia = c.get(Calendar.DAY_OF_MONTH)
        val mes = c.get(Calendar.MONTH)
        val anio = c.get(Calendar.YEAR)

    //------- APLICAR TEMA AL DATEPICKER -------//
        val picker = DatePickerDialog(requireContext(), R.style.DatePickerTheme, this, anio, mes, dia)

    //------- PARCHANDO EL COLOR DE LOS BOTONES DE TEXTO ----------//
        picker.setOnShowListener { dialog ->
            val positiveButton = (dialog as DatePickerDialog).getButton(DialogInterface.BUTTON_POSITIVE)
            val negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE)

    //------- CAMBIAR EL COLOR DE LOS BOTONES --------//
            positiveButton.setTextColor(Color.BLACK)
            negativeButton.setTextColor(Color.BLACK)
        }


        return picker
    }
}
