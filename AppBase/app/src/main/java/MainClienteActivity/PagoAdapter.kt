package MainClienteActivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.R

class PagoAdapter(private val lista: List<Map<String, Any>>) :
    RecyclerView.Adapter<PagoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val metodo: TextView = view.findViewById(R.id.txtMetodo)
        val fecha: TextView = view.findViewById(R.id.txtFecha)
        val total: TextView = view.findViewById(R.id.txtTotal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pago, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = lista[position]

        holder.metodo.text = "💳 ${item["pago_metodo"]}"
        holder.fecha.text = "📅 ${item["pago_fecha"]}"
        holder.total.text = "💰 $${item["contrato_valor"]}"
    }
}