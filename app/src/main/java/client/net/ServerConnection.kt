package client.net

import common.GameGuess
import common.GameState
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.InetSocketAddress
import java.net.Socket

/**
 * Singleton which handles the connection to the server.
 */
object ServerConnection {
  private val host = "192.168.1.246"
  private val port = 8080
  private val socketTimeout = 1800000 // Set socket timeout to half a minute
  private var socket: Socket? = null
  private var output: ObjectOutputStream? = null
  private var input: ObjectInputStream? = null

  /**
   * Creates a socket connection to the server.
   */
  fun connect() {
    socket = Socket()

    socket?.connect(InetSocketAddress(host, port), socketTimeout)
    socket?.soTimeout = socketTimeout

    output = ObjectOutputStream(socket?.getOutputStream())
    input = ObjectInputStream(socket?.getInputStream())
  }

  /**
   * Listens for new messages on the socket.
   */
  fun listen(gameStateUpdate: (GameState) -> Unit) {
    while (true) {
      val gameState = input?.readObject() as GameState
      gameStateUpdate(gameState)
    }
  }

  /**
   * Sends a guess message to the server.
   */
  fun guess(guess: String) {
    output?.writeObject(GameGuess(guess))
    output?.flush()
    output?.reset()
  }

  /**
   * Closes the socket connection to the server.
   */
  fun disconnect() {
    socket?.close()
    socket = null
    output = null
    input = null
  }

  /**
   * Checks if the client is connected to the server.
   */
  fun isConnected(): Boolean {
    return socket != null
  }
}
