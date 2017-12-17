package client.view

import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import client.R
import client.controller.Controller

class Connecting : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_connecting)

    connect()
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    // Maybe not the best way to reconnect...
    connect()
  }

  private fun connect() {
    Controller.connect(error = { msg ->
      runOnUiThread({
        AlertDialog.Builder(this)
          .setTitle("Failed to connect!")
          .setMessage(msg)
          .setPositiveButton("Retry", { _: DialogInterface, _: Int ->
            connect()
          })
          .create()
          .show()
      })
    }, success = {
      startActivity(Intent(this, Game::class.java))
    })
  }
}
