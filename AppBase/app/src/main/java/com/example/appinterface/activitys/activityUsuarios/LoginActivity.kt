package com.example.appinterface.activitys.activityUsuarios

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.R
import com.example.appinterface.activitys.rolesActivity.AsesorActivity
import com.example.appinterface.MainClienteActivity.MainClienteActivity
import com.example.appinterface.MainAdminActivity.MainAdminActivity
import com.example.appinterface.model.LoginRequest
import com.example.appinterface.model.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val txtCorreo = findViewById<EditText>(R.id.txtCorreo)
        val txtPassword = findViewById<EditText>(R.id.txtPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val progress = findViewById<ProgressBar>(R.id.progressLogin)
        val btnRegistro = findViewById<TextView>(R.id.btnIrRegistro)
        val card = findViewById<View>(R.id.cardLogin)

        card.alpha = 0f
        card.translationY = 100f
        card.animate().alpha(1f).translationY(0f).setDuration(800).start()

        var isPasswordVisible = false

        txtPassword.setOnTouchListener { _, event ->
            val DRAWABLE_END = 2

            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (txtPassword.right - txtPassword.compoundDrawables[DRAWABLE_END].bounds.width())) {

                    isPasswordVisible = !isPasswordVisible

                    if (isPasswordVisible) {
                        txtPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    } else {
                        txtPassword.inputType =
                            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    }

                    txtPassword.setSelection(txtPassword.text.length)
                    return@setOnTouchListener true
                }
            }
            false
        }

        btnLogin.setOnClickListener {

            val correo = txtCorreo.text.toString().trim()
            val password = txtPassword.text.toString().trim()

            if (correo.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            progress.visibility = View.VISIBLE
            btnLogin.isEnabled = false

            val login = LoginRequest(correo, password)

            RetrofitInstance.usuarioApi.login(login)
                .enqueue(object : Callback<LoginResponse> {

                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {

                        progress.visibility = View.GONE
                        btnLogin.isEnabled = true

                        if (response.isSuccessful && response.body() != null) {

                            val data = response.body()!!

                            val sharedPref = getSharedPreferences("APP_PREFS", MODE_PRIVATE)

                            val clienteId = data.cliente_id ?: 0
                            val usuarioId = data.usuario_id ?: 0
                            val rol = data.rol?.trim()?.uppercase()
                            val principalId = if (rol == "CLIENTE") {
                                if (clienteId > 0) clienteId else usuarioId
                            } else {
                                usuarioId
                            }

                            println(" ID GUARDADO: $principalId")

                            if (principalId <= 0) {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Error: usuario sin identificación válida",
                                    Toast.LENGTH_LONG
                                ).show()
                                return
                            }

                            sharedPref.edit()
                                .putString("TOKEN", data.token)
                                .putString("ROL", rol)
                                .putInt("ID", principalId)
                                .putInt("USUARIO_ID", usuarioId)
                                .putString("NOMBRE", data.usuario_primer_nombre)
                                .putString("APELLIDO", data.usuario_primer_apellido)
                                .putString("CORREO", data.usuario_correo)
                                .apply()
                            println(" USUARIO_ID GUARDADO: $usuarioId")


                            when (rol) {

                                "CLIENTE" -> {
                                    startActivity(
                                        Intent(
                                            this@LoginActivity,
                                            MainClienteActivity::class.java
                                        )
                                    )
                                }

                                "ASESOR" -> {
                                    startActivity(
                                        Intent(
                                            this@LoginActivity,
                                            AsesorActivity::class.java
                                        )
                                    )
                                }

                                "ADMINISTRADOR" -> {
                                    startActivity(
                                        Intent(
                                            this@LoginActivity,
                                            MainAdminActivity::class.java
                                        )
                                    )
                                }

                                else -> {
                                    Toast.makeText(
                                        this@LoginActivity,
                                        "Rol no reconocido: ${data.rol}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }

                            finish()

                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                "Credenciales incorrectas",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {

                        progress.visibility = View.GONE
                        btnLogin.isEnabled = true

                        Toast.makeText(
                            this@LoginActivity,
                            "Error conexión",
                            Toast.LENGTH_LONG
                        ).show()

                        println(" ERROR LOGIN: ${t.message}")
                    }
                })
        }

        btnRegistro.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }
    }
}