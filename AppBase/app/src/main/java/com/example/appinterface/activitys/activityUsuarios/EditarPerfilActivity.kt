package com.example.appinterface.activitys.activityUsuarios

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.Api.UsuarioApiService
import com.example.appinterface.R
import com.example.appinterface.model.Usuario
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditarPerfilActivity : AppCompatActivity() {

    private lateinit var api: UsuarioApiService
    private var currentUsuario: Usuario? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_perfil)

        api = RetrofitInstance.usuarioApi

        val prefs = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
        val userId = prefs.getInt("ID", 0)

        val edtNombre = findViewById<EditText>(R.id.edtNombre)
        val edtSegundoNombre = findViewById<EditText>(R.id.edtSegundoNombre)
        val edtApellido = findViewById<EditText>(R.id.edtApellido)
        val edtSegundoApellido = findViewById<EditText>(R.id.edtSegundoApellido)
        val edtDocumento = findViewById<EditText>(R.id.edtDocumento)
        val edtCorreo = findViewById<EditText>(R.id.edtCorreo)
        val edtDireccion = findViewById<EditText>(R.id.edtDireccion)
        val edtPassword = findViewById<EditText>(R.id.edtPassword)

        val btnActualizar = findViewById<Button>(R.id.btnActualizar)
        val btnEliminar = findViewById<TextView>(R.id.btnEliminar)

        if (userId == 0) {
            Toast.makeText(this, "Error usuario", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        api.obtenerUsuario(userId).enqueue(object : Callback<Usuario> {
            override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                val user = response.body()
                if (user != null) {
                    currentUsuario = user

                    edtNombre.setText(user.usuario_primer_nombre)
                    edtSegundoNombre.setText(user.usuario_segundo_nombre ?: "")
                    edtApellido.setText(user.usuario_primer_apellido)
                    edtSegundoApellido.setText(user.usuario_segundo_apellido ?: "")
                    edtDocumento.setText(user.usuario_documento.toString())
                    edtCorreo.setText(user.usuario_correo)
                    edtDireccion.setText(user.usuario_direccion)
                }
            }

            override fun onFailure(call: Call<Usuario>, t: Throwable) {
                Toast.makeText(this@EditarPerfilActivity, "Error cargando datos", Toast.LENGTH_SHORT).show()
            }
        })

        btnActualizar.setOnClickListener {

            val old = currentUsuario ?: return@setOnClickListener

            val usuario = Usuario(
                usuario_id = userId,
                usuario_primer_nombre = if (edtNombre.text.isEmpty()) old.usuario_primer_nombre else edtNombre.text.toString(),
                usuario_segundo_nombre = if (edtSegundoNombre.text.isEmpty()) old.usuario_segundo_nombre ?: "" else edtSegundoNombre.text.toString(),
                usuario_primer_apellido = if (edtApellido.text.isEmpty()) old.usuario_primer_apellido else edtApellido.text.toString(),
                usuario_segundo_apellido = if (edtSegundoApellido.text.isEmpty()) old.usuario_segundo_apellido ?: "" else edtSegundoApellido.text.toString(),
                usuario_documento = if (edtDocumento.text.isEmpty()) old.usuario_documento else edtDocumento.text.toString().toInt(),
                usuario_correo = if (edtCorreo.text.isEmpty()) old.usuario_correo else edtCorreo.text.toString(),
                usuario_direccion = if (edtDireccion.text.isEmpty()) old.usuario_direccion else edtDireccion.text.toString(),
                usuario_credencial = old.usuario_credencial,
                fecha_nacimiento = old.fecha_nacimiento
            )

            api.actualizarUsuario(userId, usuario)
                .enqueue(object : Callback<Map<String, String>> {

                    override fun onResponse(
                        call: Call<Map<String, String>>,
                        response: Response<Map<String, String>>
                    ) {
                        if (response.isSuccessful) {

                            Toast.makeText(this@EditarPerfilActivity, "Actualizado correctamente", Toast.LENGTH_SHORT).show()

                            prefs.edit()
                                .putString("NOMBRE", usuario.usuario_primer_nombre)
                                .putString("APELLIDO", usuario.usuario_primer_apellido)
                                .putString("CORREO", usuario.usuario_correo)
                                .apply()

                            setResult(RESULT_OK)
                            finish()

                        } else {
                            Toast.makeText(this@EditarPerfilActivity, "No se actualizó", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                        Toast.makeText(this@EditarPerfilActivity, "Error conexión", Toast.LENGTH_SHORT).show()
                    }
                })
        }

        btnEliminar.setOnClickListener {

            AlertDialog.Builder(this)
                .setTitle("Eliminar cuenta")
                .setMessage("¿Seguro que deseas eliminar tu cuenta?")
                .setPositiveButton("Sí") { _, _ ->

                    api.eliminarUsuario(userId)
                        .enqueue(object : Callback<Map<String, String>> {

                            override fun onResponse(
                                call: Call<Map<String, String>>,
                                response: Response<Map<String, String>>
                            ) {

                                if (response.isSuccessful) {

                                    Toast.makeText(this@EditarPerfilActivity, "Cuenta eliminada", Toast.LENGTH_SHORT).show()

                                    prefs.edit().clear().apply()

                                    startActivity(Intent(this@EditarPerfilActivity, LoginActivity::class.java))
                                    finish()

                                } else {
                                    Toast.makeText(this@EditarPerfilActivity, "No se eliminó", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                                Toast.makeText(this@EditarPerfilActivity, "Error conexión", Toast.LENGTH_SHORT).show()
                            }
                        })
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }
}