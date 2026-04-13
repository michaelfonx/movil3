package com.example.appinterface.activitys.rolesActivity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.appinterface.R
import com.example.appinterface.activitys.ServiciosActivity
import com.example.appinterface.activitys.activityContrato.BuscarContratoActivity
import com.google.android.material.tabs.TabLayout

class AdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_tabs)

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)

        tabLayout.addTab(tabLayout.newTab().setText("Usuarios"))
        tabLayout.addTab(tabLayout.newTab().setText("Roles"))
        tabLayout.addTab(tabLayout.newTab().setText("Servicios"))
        tabLayout.addTab(tabLayout.newTab().setText("Contratos"))

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, AdminUsuariosFragment())
            .commit()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> supportFragmentManager.beginTransaction()
                        .replace(R.id.container, AdminUsuariosFragment())
                        .commit()
                    1 -> supportFragmentManager.beginTransaction()
                        .replace(R.id.container, AdminRolesFragment())
                        .commit()
                    2 -> startActivity(Intent(this@AdminActivity, ServiciosActivity::class.java))
                    3 -> startActivity(Intent(this@AdminActivity, BuscarContratoActivity::class.java))
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
}