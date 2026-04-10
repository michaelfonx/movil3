package com.example.appinterface.MainClienteActivity

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.R
import com.example.appinterface.activitys.activityUsuarios.LoginActivity
import com.example.appinterface.fragments.AfiliadosFragment
import com.example.appinterface.model.Usuario
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.jvm.java

class PerfilFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val txtNombre = view.findViewById<TextView>(R.id.txtNombre)
        val txtCorreo = view.findViewById<TextView>(R.id.txtCorreo)
        val txtDocumento = view.findViewById<TextView>(R.id.txtDocumento)
        val txtDireccion = view.findViewById<TextView>(R.id.txtDireccion)

        val btnEditar = view.findViewById<Button>(R.id.btnEditar)
        val btnPassword = view.findViewById<Button>(R.id.btnPassword)
        val btnAfiliados = view.findViewById<Button>(R.id.btnAfiliados)
        val btnCompras = view.findViewById<Button>(R.id.btnCompras)
        val btnCerrar = view.findViewById<Button>(R.id.btnCerrarSesion)

        val shared = requireContext()
            .getSharedPreferences("APP_PREFS", AppCompatActivity.MODE_PRIVATE)

        val usuarioId = shared.getInt("USUARIO_ID", 0)

        txtNombre.text = shared.getString("NOMBRE", "") + " " +
                shared.getString("APELLIDO", "")

        txtCorreo.text = shared.getString("CORREO", "")

        if (usuarioId != 0) {

            RetrofitInstance.usuarioApi.obtenerUsuarioPorId(usuarioId)
                .enqueue(object : Callback<Usuario> {

                    override fun onResponse(
                        call: Call<Usuario>,
                        response: Response<Usuario>
                    ) {

                        if (response.isSuccessful && response.body() != null) {

                            val user = response.body()!!

                            txtDocumento.text = "Documento: ${user.usuario_documento}"
                            txtDireccion.text = "Dirección: ${user.usuario_direccion}"
                        }
                    }

                    override fun onFailure(call: Call<Usuario>, t: Throwable) {}
                })
        }

        btnCompras.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, PagosFragment())
                .addToBackStack(null)
                .commit()
        }

        btnEditar.setOnClickListener {
            startActivity(
                Intent(requireContext(), com.example.appinterface.activitys.activityUsuarios.EditarPerfilActivity::class.java)
            )
        }

        btnPassword.setOnClickListener {
            Toast.makeText(requireContext(), "Cambio de contraseña (luego lo conectamos)", Toast.LENGTH_SHORT).show()
        }

        btnAfiliados.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, AfiliadosFragment())
                .addToBackStack(null)
                .commit()
        }

        btnCerrar.setOnClickListener {

            shared.edit().clear().apply()

            startActivity(
                Intent(requireContext(), LoginActivity::class.java)
            )

            requireActivity().finish()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_perfil, container, false)
    }
}