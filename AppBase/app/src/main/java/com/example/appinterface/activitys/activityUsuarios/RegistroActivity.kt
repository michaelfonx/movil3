package com.example.appinterface.activitys.activityUsuarios

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.R
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

        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)
        val cancelar = findViewById<TextView>(R.id.txtCancelar)


        cancelar.setOnClickListener {
            finish()
            overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        }


        btnRegistrar.setOnClickListener {

            val correoText = correo.text.toString().trim()
            val passwordText = password.text.toString().trim()


            if (
                primerNombre.text.isEmpty() ||
                primerApellido.text.isEmpty() ||
                documento.text.isEmpty() ||
                correoText.isEmpty() ||
                passwordText.isEmpty()
            ) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (!correoText.contains("@")) {
                Toast.makeText(this, "Correo inválido", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (passwordText.length < 6) {
                Toast.makeText(this, "Contraseña mínimo 6 caracteres", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            btnRegistrar.isEnabled = false

            val documentoInt = try {
                documento.text.toString().toInt()
            } catch (e: Exception) {
                Toast.makeText(this, "Documento inválido", Toast.LENGTH_LONG).show()
                btnRegistrar.isEnabled = true
                return@setOnClickListener
            }


            val usuario = Usuario(
                null,
                primerNombre.text.toString(),
                segundoNombre.text.toString(),
                primerApellido.text.toString(),
                segundoApellido.text.toString(),
                documentoInt,
                correoText,
                direccion.text.toString(),
                passwordText
            )


            RetrofitInstance.usuarioApi.registrarUsuario(usuario)
                .enqueue(object : Callback<Map<String, String>> {

                    override fun onResponse(
                        call: Call<Map<String, String>>,
                        response: Response<Map<String, String>>
                    ) {

                        btnRegistrar.isEnabled = true

                        if (response.isSuccessful) {

                            val mensaje = response.body()?.get("mensaje")

                            Log.d("API_OK", mensaje ?: "Registro exitoso")

                            Toast.makeText(
                                this@RegistroActivity,
                                mensaje ?: "Usuario creado correctamente",
                                Toast.LENGTH_LONG
                            ).show()


                            finish()
                            overridePendingTransition(
                                R.anim.slide_in_right,
                                R.anim.slide_out_left
                            )

                        } else {

                            val error = response.errorBody()?.string()

                            Log.e("API_ERROR", error ?: "Error desconocido")

                            Toast.makeText(
                                this@RegistroActivity,
                                "Error: ${error ?: "Error desconocido"}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(
                        call: Call<Map<String, String>>,
                        t: Throwable
                    ) {

                        btnRegistrar.isEnabled = true

                        Log.e("API_ERROR", t.message ?: "Error conexión")

                        Toast.makeText(
                            this@RegistroActivity,
                            "Error conexión: ${t.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
        }
    }
}