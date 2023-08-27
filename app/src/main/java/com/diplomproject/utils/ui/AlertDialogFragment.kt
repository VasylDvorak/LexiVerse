package com.diplomproject.utils.ui

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.diplomproject.R

const val TITLE_EXTRA = "89cbce59-e28f-418f-b470-ff67125c2e2f"
const val MESSAGE_EXTRA = "0dd00b66-91c2-447d-b627-530065040905"
class AlertDialogFragment : AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var alertDialog = getStubAlertDialog()
        val args = arguments
        if (args != null) {
            val title = args.getString(TITLE_EXTRA)
            val message = args.getString(MESSAGE_EXTRA)
            alertDialog = getAlertDialog(title, message)
        }
        return alertDialog
    }

    fun getStubAlertDialog(): AlertDialog {
        return getAlertDialog(null, null)
    }

    fun getAlertDialog(title: String?, message: String?): AlertDialog {
        val builder = AlertDialog.Builder(requireContext())
        var finalTitle: String? = requireContext().getString(R.string.dialog_title_stub)
        if (!title.isNullOrBlank()) {
            finalTitle = title
        }
        builder.setTitle(finalTitle)
        if (!message.isNullOrBlank()) {
            builder.setMessage(message)
        }
        builder.setCancelable(true)
        builder.setPositiveButton(R.string.dialog_button_cancel) { dialog, _ -> dialog.dismiss() }
        return builder.create()
    }

    companion object {
        fun newInstance(title: String?, message: String?): AlertDialogFragment {
            val dialogFragment = AlertDialogFragment()
            val args = Bundle()
            args.putString(TITLE_EXTRA, title)
            args.putString(MESSAGE_EXTRA, message)
            dialogFragment.arguments = args
            return dialogFragment
        }
    }
}
