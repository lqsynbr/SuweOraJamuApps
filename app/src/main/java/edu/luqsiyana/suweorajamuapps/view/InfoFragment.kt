package edu.luqsiyana.suweorajamuapps.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InfoFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return TextView(requireContext()).apply {
            text = "Aplikasi Warung Jamu Suwe Ora Jamu \n" +
                    "Created By : Luqsiyana Bariq Raihan\n" +
                    "Email : lqsynbr22013@gmail.com"
            setPadding(32, 32, 32, 32)
            textSize = 18f
        }
    }
}