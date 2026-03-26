package com.example.appinterface.activitys.activityUsuarios

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.MainClienteActivity.MainClienteActivity
import com.example.appinterface.R
import com.example.appinterface.model.LoginRequest
import com.example.appinterface.model.LoginResponse
import com.example.appinterface.model.Usuario
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        val primerNombre = findViewById<EditText>(R.id.txtPrimerNombre)
        val segundoNombre = findViewById<EditText>(R.id.txtSegundoNombre)
        val primerApellido = findViewById<EditText>(R.id.txtPrimerApellido)
        val segundoApellido = findViewById<EditText>(R.id.txtSegundoApellido)
        val documento = findViewById<EditText>(R.id.txtDocumento)
        val correo = findViewById<EditText>(R.id.txtCorreo)
        val direccion = findViewById<EditText>(R.id.txtDireccion)
        val password = findViewById<EditText>(R.id.txtPassword)
        val fechaNacimiento = findViewById<EditText>(R.id.txtFechaNacimiento)

        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)
        val cancelar = findViewById<TextView>(R.id.txtCancelar)

        cancelar.setOnClickListener {
            finish()
        }

        btnRegistrar.setOnClickListener {

            val correoText = correo.text.toString().trim()
            val passwordText = password.text.toString().trim()

            if (
                primerNombre.text.isEmpty() ||
                primerApellido.text.isEmpty() ||
                documento.text.isEmpty() ||
                correoText.isEmpty() ||
                passwordText.isEmpty() ||
                fechaNacimiento.text.isEmpty()
            ) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val documentoInt = documento.text.toString().toInt()
            val fechaFormateada = fechaNacimiento.text.toString().replace("/", "-")

            val usuario = Usuario(
                null,
                primerNombre.text.toString(),
                segundoNombre.text.toString(),
                primerApellido.text.toString(),
                segundoApellido.text.toString(),
                documentoInt,
                correoText,
                direccion.text.toString(),
                passwordText,
                fechaFormateada,
                1
            )

            btnRegistrar.isEnabled = false

            RetrofitInstance.usuarioApi.registrarUsuario(usuario)
                .enqueue(object : Callback<Map<String, String>> {

                    override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {

                        if (response.isSuccessful) {

                            Toast.makeText(
                                this@RegistroActivity,
                                "Usuario creado correctamente",
                                Toast.LENGTH_SHORT
                            ).show()

                            loginAutomatico(correoText, passwordText)

                        } else {
                            btnRegistrar.isEnabled = true
                            Toast.makeText(this@RegistroActivity, "Error al registrar", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                        btnRegistrar.isEnabled = true
                        Toast.makeText(this@RegistroActivity, "Error conexión", Toast.LENGTH_LONG).show()
                    }
                })
        }
    }

    private fun loginAutomatico(correo: String, password: String) {

        val login = LoginRequest(correo, password)

        RetrofitInstance.usuarioApi.login(login)
            .enqueue(object : Callback<LoginResponse> {

                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                    if (response.isSuccessful && response.body() != null) {

                        val data = response.body()!!

                        val prefs = getSharedPreferences("APP_PREFS", MODE_PRIVATE)

                        prefs.edit()
                            .putString("TOKEN", data.token)
                            .putString("ROL", data.rol)
                            .putInt("ID", data.usuario_id ?: 0)
                            .putString("NOMBRE", data.usuario_primer_nombre)
                            .putString("APELLIDO", data.usuario_primer_apellido)
                            .putString("CORREO", data.usuario_correo)
                            .apply()

                        startActivity(Intent(this@RegistroActivity, MainClienteActivity::class.java))
                        finish()

                    } else {
                        Toast.makeText(this@RegistroActivity, "Error login automático", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(this@RegistroActivity, "Error conexión", Toast.LENGTH_LONG).show()
                }
            })
    }
}