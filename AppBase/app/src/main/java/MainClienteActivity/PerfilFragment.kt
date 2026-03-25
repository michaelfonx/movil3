package com.example.appinterface.MainClienteActivity

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.appinterface.R

class PerfilFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_perfil, container, false)
    }
}