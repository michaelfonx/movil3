package MainClienteActivity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.appinterface.R
import com.example.appinterface.Api.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TuPlanFragment : Fragment(R.layout.fragment_tu_plan) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cargarPlan()
    }

    override fun onResume() {
        super.onResume()
        cargarPlan()
    }

    private fun cargarPlan() {

        val currentView = view ?: return

        val prefs = requireActivity()
            .getSharedPreferences("APP_PREFS", Context.MODE_PRIVATE)

        val clienteId = prefs.getInt("ID", 0)

        if (clienteId <= 0) {
            mostrarSinPlan(currentView)
            return
        }

        RetrofitInstance.api.obtenerMiPlan(clienteId)
            .enqueue(object : Callback<Map<String, Any>> {

                override fun onResponse(
                    call: Call<Map<String, Any>>,
                    response: Response<Map<String, Any>>
                ) {

                    if (!isAdded) return

                    val v = view ?: return

                    val data = response.body()

                    if (!response.isSuccessful || data == null) {
                        mostrarSinPlan(v)
                        return
                    }

                    val nombre = data["plan_nombre"]?.toString() ?: "Sin nombre"
                    val precio = (data["plan_precio"] as? Double) ?: 0.0
                    val descripcion = data["plan_descripcion"]?.toString() ?: "Sin descripción"

                    val servicios = (data["servicios"] as? List<*>)?.map { it.toString() } ?: emptyList()
                    val productos = (data["productos"] as? List<*>)?.map { it.toString() } ?: emptyList()

                    val pagos = (data["pagos"] as? List<Map<String, Any>>)?.map {
                        val metodo = it["metodo"]?.toString() ?: ""
                        val fecha = it["fecha"]?.toString() ?: ""
                        "• $metodo - $fecha"
                    } ?: emptyList()

                    v.findViewById<TextView>(R.id.txtPlanNombre).text = nombre

                    v.findViewById<TextView>(R.id.txtPrecio).text =
                        "$$precio"

                    v.findViewById<TextView>(R.id.txtDescripcion).text = descripcion

                    v.findViewById<TextView>(R.id.txtServicios).text =
                        if (servicios.isNotEmpty())
                            servicios.joinToString("\n") { "• $it" }
                        else "Sin servicios"

                    v.findViewById<TextView>(R.id.txtProductos).text =
                        if (productos.isNotEmpty())
                            productos.joinToString("\n") { "• $it" }
                        else "Sin productos"

                    v.findViewById<TextView>(R.id.txtPagos).text =
                        if (pagos.isNotEmpty())
                            pagos.joinToString("\n")
                        else "Sin pagos"
                }

                override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                    if (!isAdded) return
                    val v = view ?: return
                    mostrarSinPlan(v)
                }
            })
    }

    private fun mostrarSinPlan(view: View) {
        view.findViewById<TextView>(R.id.txtPlanNombre).text =
            "No tienes plan activo"

        view.findViewById<TextView>(R.id.txtPrecio).text = ""

        view.findViewById<TextView>(R.id.txtDescripcion).text =
            "Adquiere un plan para ver más información"

        view.findViewById<TextView>(R.id.txtServicios).text = "Sin servicios"
        view.findViewById<TextView>(R.id.txtProductos).text = "Sin productos"
        view.findViewById<TextView>(R.id.txtPagos).text = "Sin pagos"
    }
}