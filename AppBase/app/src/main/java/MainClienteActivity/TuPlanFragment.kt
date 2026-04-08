package MainClienteActivity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.appinterface.R
import com.example.appinterface.Api.RetrofitInstance
import model.DTO.MiPlanDTO
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
            .enqueue(object : Callback<MiPlanDTO> {

                override fun onResponse(
                    call: Call<MiPlanDTO>,
                    response: Response<MiPlanDTO>
                ) {

                    if (!isAdded) return

                    val v = view ?: return

                    val data = response.body()

                    if (!response.isSuccessful || data == null) {
                        mostrarSinPlan(v)
                        return
                    }

                    v.findViewById<TextView>(R.id.txtPlanNombre).text =
                        data.plan_nombre ?: "Sin nombre"

                    v.findViewById<TextView>(R.id.txtPrecio).text =
                        "$${data.plan_precio ?: 0}"

                    v.findViewById<TextView>(R.id.txtDescripcion).text =
                        data.plan_descripcion ?: "Sin descripción"

                    v.findViewById<TextView>(R.id.txtServicios).text =
                        data.servicios?.joinToString("\n") { "• $it" }
                            ?: "Sin servicios"

                    v.findViewById<TextView>(R.id.txtProductos).text =
                        data.productos?.joinToString("\n") { "• $it" }
                            ?: "Sin productos"

                    v.findViewById<TextView>(R.id.txtPagos).text =
                        data.pagos?.joinToString("\n") {
                            "• ${it.metodo} - ${it.fecha}"
                        } ?: "Sin pagos"
                }

                override fun onFailure(call: Call<MiPlanDTO>, t: Throwable) {
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