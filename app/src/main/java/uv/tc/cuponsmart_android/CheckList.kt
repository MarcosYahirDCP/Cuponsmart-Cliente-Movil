import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AlertDialog

class Checklist(
    context: Context,
    private val items: List<String>,
    private val onItemsSelected: (List<String>) -> Unit,
    private val onItemClicked: (String) -> Unit
) {

    private lateinit var listView: ListView
    private val alertDialog: AlertDialog

    init {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Select Items")

        listView = ListView(context)
        listView.adapter = ArrayAdapter(context, android.R.layout.simple_list_item_multiple_choice, items)
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        builder.setView(listView)

        builder.setPositiveButton("OK") { _, _ ->
            val selectedItems = mutableListOf<String>()
            val checkedItemPositions = listView.checkedItemPositions

            for (i in 0 until checkedItemPositions.size()) {
                val position = checkedItemPositions.keyAt(i)
                if (checkedItemPositions.valueAt(i)) {
                    selectedItems.add(items[position])
                }
            }

            onItemsSelected(selectedItems)
        }

        builder.setNegativeButton("Cancel") { _, _ ->
            // Dismiss handled externally
        }

        alertDialog = builder.create()

        listView.setOnItemClickListener { _, _, position, _ ->
            val clickedItem = items[position]
            onItemClicked(clickedItem)
        }
    }

    fun show() {
        alertDialog.show()
    }

    fun dismiss() {
        alertDialog.dismiss()
    }
}
