package es.cachau;

import es.cachau.managers.entities.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Logger;

public class Client {

  public static final Scanner scanner = new Scanner(System.in);
  public static Logger logger = Logger.getLogger("[CHAT]");

  public static Socket socket;
  public static BufferedReader in;

  public static final User currentUser = null;

  public static void main(String[] args) throws IOException, SQLException {
    logger.info("Iniciant client");

    socket = new Socket("127.0.0.1", 8100);
    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

    // Iniciar un nuevo hilo para leer la entrada del usuario
    new Thread(() -> {
      BufferedReader reader = new BufferedReader(
        new InputStreamReader(System.in)
      );
      String lineaConsola;
      try {
        while ((lineaConsola = reader.readLine()) != null) {
          out.println(lineaConsola);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    })
      .start();
    // Leer los mensajes del servidor en el hilo principal
    String mensajeServidor;
    while ((mensajeServidor = in.readLine()) != null) {
      System.out.println(mensajeServidor);
    }

    String solicitudServidor;
    while ((solicitudServidor = in.readLine()) != null) {
      System.out.println(solicitudServidor);
      String respuestaUsuario = scanner.nextLine();
      out.println(respuestaUsuario);
    }
  }

  public static void setCurrentUser(User user) {
    logger.info("Usuari actual: " + user.getName());
  }

  public static Scanner getScanner() {
    return scanner;
  }

  public static Logger getLogger() {
    return logger;
  }

  public static Socket getSocket() {
    return socket;
  }

  public static BufferedReader getIn() {
    return in;
  }
}
