package com.diplomproject.utils.ui

import android.app.AlertDialog
import android.content.Context
import com.diplomproject.R
import com.diplomproject.navigation.IScreens
import com.github.terrakok.cicerone.Router
import com.google.firebase.auth.FirebaseAuth

object ExitDialog {

    fun showExitDialog(context: Context, auth: FirebaseAuth?, router: Router, screen: IScreens){
        val builder = AlertDialog.Builder(context)
        val dialog = builder.create()
        dialog.setTitle("Выход")
        dialog.setMessage(context.getString(R.string.exit_message))
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Выйти"){
                _,_ ->
            auth?.signOut()
            router.navigateTo(screen.startSettingsFragment())

        }

        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Отмена"){
                _,_ ->
        }
        dialog.show()

    }
}