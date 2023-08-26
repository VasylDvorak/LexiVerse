package com.diplomproject.learningtogether.ui

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import com.diplomproject.learningtogether.Key
import com.diplomproject.learningtogether.R
import com.squareup.picasso.Picasso

class SuccessFragment : Fragment() {

    private lateinit var victoryImageView: AppCompatImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_success, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(view)

        view.findViewById<View>(R.id.repeat_button).setOnClickListener {
            getController().finishSuccessFragment()
        }

        victoryImageView.setOnClickListener {
            getController().finishSuccessFragment()
        }

        pressingBackStackButton(view)

    }

    //это перехват нажатия на BackStack
    private fun pressingBackStackButton(view: View) {
        view.isFocusableInTouchMode = true
        view.requestFocus()
        var clickedBack = true
        view.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK && clickedBack) {
                clickedBack = false
                Toast.makeText(
                    requireContext(),
                    "Нажмите еще раз, чтобы выйти из приложения",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnKeyListener true
            } else {
                return@setOnKeyListener false
            }
        }
    }

    private fun getController(): Controller = activity as? Controller
        ?: throw IllegalStateException("Активити должна наследовать контроллер!!!")//проверки, наследует или нет активити контроллер

    interface Controller {
        fun finishSuccessFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getController()  // агресивный способ проверки наличия контроллера. Если нет контроллера, приложение свалтится на присоединение к фрагмента к активити
    }

    private fun initView(view: View) {
        victoryImageView = view.findViewById(R.id.victory_image_view)
    }
}
