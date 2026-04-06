package com.example.appinterface.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.appinterface.R
import java.io.File
import java.io.FileOutputStream

class ContratoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contrato)

        val btn = findViewById<Button>(R.id.btnDescargar)

        val file = File(getExternalFilesDir(null), "contrato.pdf")

        val input = assets.open("contrato.pdf")
        val output = FileOutputStream(file)
        input.copyTo(output)
        input.close()
        output.close()

        val uri = FileProvider.getUriForFile(
            this,
            "${packageName}.provider",
            file
        )

        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "application/pdf")
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

        startActivity(intent)

        btn.setOnClickListener {
            val intentDescarga = Intent(Intent.ACTION_VIEW)
            intentDescarga.setDataAndType(uri, "application/pdf")
            intentDescarga.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            startActivity(intentDescarga)
        }
    }
}