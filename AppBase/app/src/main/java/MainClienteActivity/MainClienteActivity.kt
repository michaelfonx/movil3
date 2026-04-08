package com.example.appinterface.MainClienteActivity

import MainClienteActivity.InicioFragment
import MainClienteActivity.TuPlanFragment
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.appinterface.R
import com.example.appinterface.activities.ContratoActivity
import com.example.appinterface.activitys.activityUsuarios.EditarPerfilActivity
import com.example.appinterface.activitys.activityUsuarios.LoginActivity
import com.example.appinterface.fragments.AfiliadosFragment
import com.google.android.material.navigation.NavigationView

class MainClienteActivity : AppCompatActivity() {

    private lateinit var drawer: DrawerLayout
    private lateinit var imgPerfil: ImageView

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                imgPerfil.setImageURI(it)
                val prefs = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
                prefs.edit().putString("IMG_PERFIL", it.toString()).apply()
            }
        }

    private val editarPerfilLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                recargarPerfil()
                actualizarFragmentInicio()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_cliente)

        drawer = findViewById(R.id.drawer_layout)
        drawer.setScrimColor(Color.TRANSPARENT)

        val navView = findViewById<NavigationView>(R.id.nav_view)

        // Fragment inicial (SIN backstack)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, InicioFragment())
                .commit()
        }

        findViewById<FrameLayout>(R.id.nav_inicio).setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, InicioFragment())
                .addToBackStack(null)
                .commit()
        }

        findViewById<LinearLayout>(R.id.nav_servicios).setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, ServiciosFragment())
                .addToBackStack(null)
                .commit()
        }

        findViewById<LinearLayout>(R.id.nav_tienda).setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, TiendaFragment())
                .addToBackStack(null)
                .commit()
        }

        findViewById<LinearLayout>(R.id.nav_pagos).setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, PagosFragment())
                .addToBackStack(null)
                .commit()
        }

        findViewById<LinearLayout>(R.id.nav_perfil).setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, PerfilFragment())
                .addToBackStack(null)
                .commit()
        }

        val header = navView.getHeaderView(0)

        val txtNombre = header.findViewById<TextView>(R.id.txtNombreHeader)
        val txtCorreo = header.findViewById<TextView>(R.id.txtCorreoHeader)
        val btnEditar = header.findViewById<Button>(R.id.btnEditarPerfil)
        imgPerfil = header.findViewById(R.id.imgPerfil)

        val prefs = getSharedPreferences("APP_PREFS", MODE_PRIVATE)

        val nombre = prefs.getString("NOMBRE", "") ?: ""
        val apellido = prefs.getString("APELLIDO", "") ?: ""
        val correo = prefs.getString("CORREO", "") ?: ""

        txtNombre.text = "${nombre.lowercase()}\n${apellido.lowercase()}"
        txtCorreo.text = correo

        val imgGuardada = prefs.getString("IMG_PERFIL", null)
        if (imgGuardada != null) {
            imgPerfil.setImageURI(Uri.parse(imgGuardada))
        }

        imgPerfil.setOnClickListener {
            pickImage.launch("image/*")
        }

        btnEditar.setOnClickListener {
            val intent = Intent(this, EditarPerfilActivity::class.java)
            editarPerfilLauncher.launch(intent)
        }

        val btnPlan = header.findViewById<TextView>(R.id.nav_plan)
        val btnAfiliados = header.findViewById<TextView>(R.id.nav_afiliados)
        val btnCartera = header.findViewById<TextView>(R.id.nav_cartera)
        val btnPagos = header.findViewById<TextView>(R.id.nav_pagos)
        val btnSedes = header.findViewById<TextView>(R.id.nav_sedes)
        val btnAsesor = header.findViewById<TextView>(R.id.nav_asesor)
        val btnLogout = header.findViewById<TextView>(R.id.nav_logout)

        btnPlan.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, TuPlanFragment())
                .addToBackStack(null)
                .commit()
            cerrarDrawer()
        }

        btnAfiliados.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, AfiliadosFragment())
                .addToBackStack(null)
                .commit()
            cerrarDrawer()
        }

        btnCartera.setOnClickListener {
            val intent = Intent(this, ContratoActivity::class.java)
            startActivity(intent)
            cerrarDrawer()
        }

        btnPagos.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, PagosFragment())
                .addToBackStack(null)
                .commit()
            cerrarDrawer()
        }

        btnSedes.setOnClickListener {
            toast("Sedes próximamente")
            cerrarDrawer()
        }

        btnAsesor.setOnClickListener {
            toast("Asesor próximamente")
            cerrarDrawer()
        }

        btnLogout.setOnClickListener {
            cerrarSesion()
        }
    }

    private fun recargarPerfil() {
        val navView = findViewById<NavigationView>(R.id.nav_view)
        val header = navView.getHeaderView(0)

        val txtNombre = header.findViewById<TextView>(R.id.txtNombreHeader)
        val txtCorreo = header.findViewById<TextView>(R.id.txtCorreoHeader)

        val prefs = getSharedPreferences("APP_PREFS", MODE_PRIVATE)

        val nombre = prefs.getString("NOMBRE", "") ?: ""
        val apellido = prefs.getString("APELLIDO", "") ?: ""
        val correo = prefs.getString("CORREO", "") ?: ""

        txtNombre.text = "${nombre.lowercase()}\n${apellido.lowercase()}"
        txtCorreo.text = correo
    }

    private fun actualizarFragmentInicio() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        if (fragment is InicioFragment) {
            fragment.actualizarBienvenido()
        }
    }

    override fun onResume() {
        super.onResume()
        actualizarFragmentInicio()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    fun abrirMenu() {
        drawer.openDrawer(GravityCompat.START)
    }

    private fun cerrarDrawer() {
        drawer.closeDrawer(GravityCompat.START)
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun cerrarSesion() {
        AlertDialog.Builder(this)
            .setTitle("Cerrar sesión")
            .setMessage("¿Estás seguro que deseas salir?")
            .setPositiveButton("Sí") { _, _ ->
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}