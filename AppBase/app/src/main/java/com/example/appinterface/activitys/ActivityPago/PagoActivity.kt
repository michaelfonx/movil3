package com.example.appinterface.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.appinterface.R
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.model.Pago
import example.appinterface.model.AdquirirPlanRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import com.example.appinterface.MainClienteActivity.MainClienteActivity

class PagoActivity : AppCompatActivity() {

    private var clienteId: Int = 0
    private var planId: Int = 0
    private var valor: Double = 0.0

    private lateinit var spinnerMetodo: Spinner
    private lateinit var spinnerYpse: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pago)

        val btnAtras = findViewById<Button>(R.id.btnAtras)
        btnAtras.setOnClickListener {
            finish()
        }

        clienteId = intent.getIntExtra("CLIENTE_ID", 0)
        planId = intent.getIntExtra("PLAN_ID", 0)
        valor = intent.getDoubleExtra("VALOR", 0.0)

        if (clienteId == 0 || planId == 0) {
            Toast.makeText(this, "Error datos", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        val btnPagar = findViewById<Button>(R.id.btnPagar)

        val txtValor = findViewById<TextView>(R.id.txtValor)
        spinnerMetodo = findViewById(R.id.spinnerMetodo)
        spinnerYpse = findViewById(R.id.spinnerYpse)

        val layoutEfectivo = findViewById<LinearLayout>(R.id.layoutEfectivo)
        val layoutTarjeta = findViewById<LinearLayout>(R.id.layoutTarjeta)
        val layoutYpse = findViewById<LinearLayout>(R.id.layoutYpse)

        txtValor.text = "Total: $${valor.toInt()}"

        val metodos = listOf("EFECTIVO", "CREDITO", "DEBITO", "YPSE")
        spinnerMetodo.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, metodos)

        val ypseOpciones = listOf("NEQUI", "DAVIPLATA")
        spinnerYpse.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, ypseOpciones)

        spinnerMetodo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {

                layoutEfectivo.visibility = View.GONE
                layoutTarjeta.visibility = View.GONE
                layoutYpse.visibility = View.GONE

                when (metodos[position]) {
                    "EFECTIVO" -> layoutEfectivo.visibility = View.VISIBLE
                    "CREDITO", "DEBITO" -> layoutTarjeta.visibility = View.VISIBLE
                    "YPSE" -> layoutYpse.visibility = View.VISIBLE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        btnPagar.setOnClickListener {
            btnPagar.isEnabled = false
            realizarPago(btnPagar)
        }
    }

    private fun realizarPago(btn: Button) {

        val metodoSeleccionado = spinnerMetodo.selectedItem.toString()


        if (metodoSeleccionado == "CREDITO" || metodoSeleccionado == "DEBITO") {

            val numero = findViewById<EditText>(R.id.etNumero).text.toString()
            val nombre = findViewById<EditText>(R.id.etNombre).text.toString()
            val cvv = findViewById<EditText>(R.id.etCVV).text.toString()

            if (numero.isEmpty() || nombre.isEmpty() || cvv.isEmpty()) {
                Toast.makeText(this, "Completa los datos de la tarjeta", Toast.LENGTH_SHORT).show()
                btn.isEnabled = true
                return
            }
        }

        if (metodoSeleccionado == "YPSE") {
            val celular = findViewById<EditText>(R.id.etCelular).text.toString()

            if (celular.isEmpty()) {
                Toast.makeText(this, "Ingresa el número celular", Toast.LENGTH_SHORT).show()
                btn.isEnabled = true
                return
            }
        }

        val request = AdquirirPlanRequest(clienteId, planId, valor)

        RetrofitInstance.api.adquirirPlan(request)
            .enqueue(object : Callback<Map<String, Int>> {

                override fun onResponse(call: Call<Map<String, Int>>, response: Response<Map<String, Int>>) {

                    if (!response.isSuccessful || response.body() == null) {
                        btn.isEnabled = true
                        Toast.makeText(this@PagoActivity, "Error al crear contrato", Toast.LENGTH_SHORT).show()
                        return
                    }

                    val contratoId = response.body()!!["contrato_id"] ?: 0

                    if (contratoId == 0) {
                        btn.isEnabled = true
                        Toast.makeText(this@PagoActivity, "Contrato inválido", Toast.LENGTH_SHORT).show()
                        return
                    }

                    crearPago(contratoId, btn)
                }

                override fun onFailure(call: Call<Map<String, Int>>, t: Throwable) {
                    btn.isEnabled = true
                    Toast.makeText(this@PagoActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun crearPago(contratoId: Int, btn: Button) {

        Toast.makeText(this, "Procesando pago...", Toast.LENGTH_SHORT).show()

        val aprobado = (0..100).random() > 20

        if (!aprobado) {
            btn.isEnabled = true
            Toast.makeText(this, "Pago rechazado ", Toast.LENGTH_LONG).show()
            return
        }

        val fecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val metodoSeleccionado = spinnerMetodo.selectedItem.toString()

        val metodoFinal = if (metodoSeleccionado == "YPSE") {
            spinnerYpse.selectedItem.toString()
        } else {
            metodoSeleccionado
        }

        when (metodoFinal) {
            "EFECTIVO" -> Toast.makeText(this, "Acércate a un punto de pago", Toast.LENGTH_LONG).show()
            "CREDITO" -> Toast.makeText(this, "Procesando tarjeta crédito...", Toast.LENGTH_SHORT).show()
            "DEBITO" -> Toast.makeText(this, "Procesando tarjeta débito...", Toast.LENGTH_SHORT).show()
            "NEQUI", "DAVIPLATA" -> Toast.makeText(this, "Confirma el pago en tu app", Toast.LENGTH_SHORT).show()
        }

        val pago = Pago(null, metodoFinal, fecha, contratoId)

        RetrofitInstance.pagoApi.crearPago(pago)
            .enqueue(object : Callback<Void> {

                override fun onResponse(call: Call<Void>, response: Response<Void>) {

                    btn.isEnabled = true

                    if (response.isSuccessful) {

                        if (metodoFinal == "EFECTIVO") {
                            Toast.makeText(
                                this@PagoActivity,
                                "Pago registrado. Acércate a pagar en efectivo",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                this@PagoActivity,
                                "Pago realizado correctamente ✅",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        val intent = Intent(this@PagoActivity, MainClienteActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                        finish()

                    } else {
                        Toast.makeText(this@PagoActivity, "Error registrando pago", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {

                    btn.isEnabled = true
                    Toast.makeText(this@PagoActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
    }
}