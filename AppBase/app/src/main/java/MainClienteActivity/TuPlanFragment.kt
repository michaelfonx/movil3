package MainClienteActivity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.appinterface.R
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.model.MiPlanDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TuPlanFragment : Fragment(R.layout.fragment_tu_plan) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = requireActivity()
            .getSharedPreferences("APP_PREFS", Context.MODE_PRIVATE)

        val clienteId = prefs.getInt("ID", 0)

        println(" ID REAL DESDE ANDROID: $clienteId")

        if (clienteId <= 0) {
            println("ID inválido, no se consulta API")
            mostrarSinPlan(view)
            return
        }

        RetrofitInstance.api.obtenerMiPlan(clienteId)
            .enqueue(object : Callback<MiPlanDTO> {

                override fun onResponse(
                    call: Call<MiPlanDTO>,
                    response: Response<MiPlanDTO>
                ) {
                    println("RESPONSE CODE: ${response.code()}")

                    if (!response.isSuccessful) {
                        println("ERROR BACKEND: ${response.code()}")
                        mostrarSinPlan(view)
                        return
                    }


                    val data = response.body()

                    if (data == null) {
                        println("DATA NULL")
                        mostrarSinPlan(view)
                        return
                    }

                    view.findViewById<TextView>(R.id.txtPlanNombre).text =
                        data.plan_nombre ?: "Sin nombre"

                    view.findViewById<TextView>(R.id.txtPrecio).text =
                        data.plan_precio?.let { "$$it" } ?: ""

                    view.findViewById<TextView>(R.id.txtDescripcion).text =
                        data.plan_descripcion ?: "Sin descripción"

                    view.findViewById<TextView>(R.id.txtServicios).text =
                        if (data.servicios.isNullOrEmpty()) "Sin servicios"
                        else data.servicios.joinToString("\n") { "• $it" }

                    view.findViewById<TextView>(R.id.txtProductos).text =
                        if (data.productos.isNullOrEmpty()) "Sin productos"
                        else data.productos.joinToString("\n") { "• $it" }

                    view.findViewById<TextView>(R.id.txtPagos).text =
                        if (data.pagos.isNullOrEmpty()) "Sin pagos"
                        else data.pagos.joinToString("\n") {
                            "• ${it.metodo} - ${it.fecha}"
                        }
                }

                override fun onFailure(call: Call<MiPlanDTO>, t: Throwable) {
                    t.printStackTrace()
                    println("ERROR REAL: ${t.message}")
                    mostrarSinPlan(view)
                }




            })


    }

    private fun mostrarSinPlan(view: View) {
        view.findViewById<TextView>(R.id.txtPlanNombre).text =
            "No tienes plan activo"

        view.findViewById<TextView>(R.id.txtPrecio).text = ""

        view.findViewById<TextView>(R.id.txtDescripcion).text =
            "Adquiere un plan para ver más información"

        view.findViewById<TextView>(R.id.txtServicios).text = ""
        view.findViewById<TextView>(R.id.txtProductos).text = ""
        view.findViewById<TextView>(R.id.txtPagos).text = ""
    }
    override fun onResume() {
        super.onResume()
        view?.let { onViewCreated(it, null) }
    }
}