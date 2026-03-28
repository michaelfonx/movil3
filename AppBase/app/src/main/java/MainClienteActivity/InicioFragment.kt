package MainClienteActivity

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.R
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.MainClienteActivity.MainClienteActivity
import com.example.appinterface.adapter.PlanAdapter
import com.example.appinterface.model.Plan
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InicioFragment : Fragment(R.layout.activity_cliente) {

    private lateinit var txtBienvenido: TextView
    private lateinit var txtNombre: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txtBienvenido = view.findViewById(R.id.txtBienvenido)
        txtNombre = view.findViewById(R.id.txtNombre)
        val btnMenu = view.findViewById<ImageView>(R.id.btnMenu)

        btnMenu.setOnClickListener {
            (activity as MainClienteActivity).abrirMenu()
        }


        actualizarBienvenido()

        val recycler = view.findViewById<RecyclerView>(R.id.recyclerPlanes)
        recycler.layoutManager = GridLayoutManager(requireContext(), 2)

        RetrofitInstance.planApi.obtenerPlanes().enqueue(object : Callback<List<Plan>> {

            override fun onResponse(call: Call<List<Plan>>, response: Response<List<Plan>>) {
                if (response.isSuccessful) {
                    recycler.adapter = PlanAdapter(response.body() ?: emptyList())
                } else {
                    Toast.makeText(context, "Error en respuesta", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Plan>>, t: Throwable) {
                Toast.makeText(context, "Error conexión", Toast.LENGTH_SHORT).show()
            }
        })
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