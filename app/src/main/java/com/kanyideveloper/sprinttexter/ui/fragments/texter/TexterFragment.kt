package com.kanyideveloper.sprinttexter.ui.fragments.texter

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.kanyideveloper.sprinttexter.R
import com.kanyideveloper.sprinttexter.databinding.FragmentTexterBinding

class TexterFragment : Fragment() {

    private lateinit var binding: FragmentTexterBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTexterBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
}