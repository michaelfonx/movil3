package com.example.appinterface.MainClienteActivity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.R
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.Adapter.PlanAdapter
import com.example.appinterface.Adapter.UsuarioResumenAdapter
import com.example.appinterface.MainClienteActivity.MainClienteActivity
import com.example.appinterface.activitys.tiendaActivity.CarritoActivity
import com.example.appinterface.model.Plan
import com.example.appinterface.model.Usuario
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InicioFragment : Fragment(R.layout.fragment_inicio) {

    private lateinit var txtBienvenido: TextView
    private lateinit var txtNombre: TextView
    private lateinit var txtServiciosAdicionales: TextView
    private lateinit var txtUsuariosActuales: TextView
    private lateinit var recyclerUsuarios: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txtBienvenido = view.findViewById(R.id.txtBienvenido)
        txtNombre = view.findViewById(R.id.txtNombre)

        val btnMenu = view.findViewById<ImageView>(R.id.btnMenu)
        val btnCarrito = view.findViewById<ImageView>(R.id.btnCarrito)

        btnMenu.setOnClickListener {
            if (activity is MainClienteActivity) {
                (activity as MainClienteActivity).abrirMenu()
            } else if (activity is com.example.appinterface.activitys.rolesActivity.AsesorActivity) {
                (activity as com.example.appinterface.activitys.rolesActivity.AsesorActivity).abrirMenu()
            }
        }

        btnCarrito.setOnClickListener {
            startActivity(Intent(requireContext(), CarritoActivity::class.java))
        }

        actualizarBienvenido()

        val recyclerPlanes = view.findViewById<RecyclerView>(R.id.recyclerPlanes)
        recyclerUsuarios = view.findViewById(R.id.recyclerUsuarios)
        txtServiciosAdicionales = view.findViewById(R.id.txtServiciosAdicionales)
        txtUsuariosActuales = view.findViewById(R.id.txtUsuariosActuales)

        recyclerPlanes.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerUsuarios.layoutManager = LinearLayoutManager(requireContext())

        if (activity is com.example.appinterface.activitys.rolesActivity.AsesorActivity) {
            txtServiciosAdicionales.visibility = View.GONE
            recyclerPlanes.visibility = View.GONE
            cargarUsuarios()
        } else {
            txtUsuariosActuales.visibility = View.GONE
            recyclerUsuarios.visibility = View.GONE
        }

        RetrofitInstance.planApi.obtenerPlanes().enqueue(object : Callback<List<Plan>> {

            override fun onResponse(call: Call<List<Plan>>, response: Response<List<Plan>>) {
                if (response.isSuccessful) {
                    recyclerPlanes.adapter = PlanAdapter(response.body() ?: emptyList())
                } else {
                    Toast.makeText(context, "Error en respuesta", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Plan>>, t: Throwable) {
                Toast.makeText(context, "Error conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun cargarUsuarios() {
        RetrofitInstance.usuarioApi.obtenerUsuarios().enqueue(object : Callback<List<Usuario>> {
            override fun onResponse(call: Call<List<Usuario>>, response: Response<List<Usuario>>) {
                if (response.isSuccessful) {
                    val usuarios = response.body() ?: emptyList()
                    recyclerUsuarios.adapter = UsuarioResumenAdapter(usuarios) { usuario ->
                        mostrarDialogUsuario(usuario)
                    }
                } else {
                    Toast.makeText(context, "Error al cargar usuarios", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Usuario>>, t: Throwable) {
                Toast.makeText(context, "Error conexión usuarios", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun mostrarDialogUsuario(usuario: Usuario) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_usuario_info, null)
        dialogView.findViewById<TextView>(R.id.txtInfoNombre).text = "${usuario.usuario_primer_nombre} ${usuario.usuario_primer_apellido}"
        dialogView.findViewById<TextView>(R.id.txtInfoDocumento).text = "Documento: ${usuario.usuario_documento}"
        dialogView.findViewById<TextView>(R.id.txtInfoCorreo).text = "Correo: ${usuario.usuario_correo}"
        dialogView.findViewById<TextView>(R.id.txtInfoDireccion).text = "Dirección: ${usuario.usuario_direccion}"
        dialogView.findViewById<TextView>(R.id.txtInfoNacimiento).text = "Fecha de nacimiento: ${usuario.fecha_nacimiento.takeIf { it.isNotBlank() } ?: "No registrada"}"
        dialogView.findViewById<TextView>(R.id.txtInfoRol).text = "Rol: ${usuario.rol_id ?: "Desconocido"}"

        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setPositiveButton("Cerrar", null)
            .show()
    }

    fun actualizarBienvenido() {

        val prefs = requireActivity()
            .getSharedPreferences("APP_PREFS", AppCompatActivity.MODE_PRIVATE)

        val nombre = prefs.getString("NOMBRE", "") ?: ""
        val apellido = prefs.getString("APELLIDO", "") ?: ""

        txtBienvenido.text = "Bienvenido"
        txtNombre.text = "${nombre} ${apellido}"
    }
}