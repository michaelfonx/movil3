package com.example.appinterface.activitys.rolesActivity

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.appinterface.R
import com.example.appinterface.activitys.ServiciosActivity
import com.example.appinterface.activitys.activityContrato.BuscarContratoActivity
import com.example.appinterface.activitys.activityUsuarios.EditarPerfilActivity
import com.example.appinterface.activitys.activityUsuarios.LoginActivity
import com.example.appinterface.MainClienteActivity.*
import com.google.android.material.navigation.NavigationView

class AsesorActivity : AppCompatActivity() {

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
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asesor)

        drawer = findViewById(R.id.drawer_layout)
        drawer.setScrimColor(Color.parseColor("#55000000"))
        drawer.setDrawerElevation(18f)

        val navView = findViewById<NavigationView>(R.id.nav_view)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, InicioFragment())
                .commit()
        }

        findViewById<LinearLayout>(R.id.nav_inicio).setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, InicioFragment())
                .addToBackStack(null)
                .commit()
        }

        findViewById<LinearLayout>(R.id.nav_servicios).setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, SedesFragment())
                .addToBackStack(null)
                .commit()
        }

        findViewById<LinearLayout>(R.id.nav_tienda).setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, EditarTiendaFragment())
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

        val btnProductos = header.findViewById<TextView>(R.id.nav_productos)
        val btnEditarTienda = header.findViewById<TextView>(R.id.nav_editar_tienda)
        val btnServicios = header.findViewById<TextView>(R.id.nav_servicios_asesor)
        val btnContratos = header.findViewById<TextView>(R.id.nav_contratos_asesor)
        val btnLogout = header.findViewById<TextView>(R.id.nav_logout)

        btnProductos.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, AsesorProductosFragment())
                .addToBackStack(null)
                .commit()
            cerrarDrawer()
        }

        btnEditarTienda.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, EditarTiendaFragment())
                .addToBackStack(null)
                .commit()
            cerrarDrawer()
        }

        btnServicios.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, EditarServiciosFragment())
                .addToBackStack(null)
                .commit()
            cerrarDrawer()
        }

        btnContratos.setOnClickListener {
            startActivity(Intent(this, BuscarContratoActivity::class.java))
            cerrarDrawer()
        }

        btnLogout.setOnClickListener {
            val prefsLogout = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
            prefsLogout.edit().clear().apply()
            startActivity(Intent(this@AsesorActivity, LoginActivity::class.java))
            finish()
        }
    }

    private fun recargarPerfil() {
        val prefs = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
        val nombre = prefs.getString("NOMBRE", "") ?: ""
        val apellido = prefs.getString("APELLIDO", "") ?: ""
        val correo = prefs.getString("CORREO", "") ?: ""

        val navView = findViewById<NavigationView>(R.id.nav_view)
        val header = navView.getHeaderView(0)
        val txtNombre = header.findViewById<TextView>(R.id.txtNombreHeader)
        val txtCorreo = header.findViewById<TextView>(R.id.txtCorreoHeader)

        txtNombre.text = "${nombre.lowercase()}\n${apellido.lowercase()}"
        txtCorreo.text = correo
    }

    private fun cerrarDrawer() {
        drawer.closeDrawer(GravityCompat.START)
    }

    fun abrirMenu() {
        drawer.openDrawer(GravityCompat.START)
    }
}
