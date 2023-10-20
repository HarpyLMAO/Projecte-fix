package es.cachau;

import es.cachau.managers.entities.User;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Logger;

public class Client {

  public static final Scanner scanner = new Scanner(System.in);
  public static Logger logger = Logger.getLogger("[CHAT]");

  public static Socket socket;
  public static DataInputStream in;
  public static DataOutputStream out;

  public static final User currentUser = null;

  public static void main(String[] args) throws IOException, SQLException {
    logger.info("Iniciant client");

    socket = new Socket("localhost", 8100);
    in = new DataInputStream(socket.getInputStream());
    out = new DataOutputStream(socket.getOutputStream());

    // Iniciar un nuevo hilo para leer la entrada del usuario
    new Thread(() -> {
      try {
        while (true) {
          String lineaConsola = scanner.nextLine();
          out.writeUTF(lineaConsola);
          out.flush();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }).start();

    new Thread(() -> {
      try {
        while (true) {
          String mensajeServidor = in.readUTF();
          System.out.println(mensajeServidor);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }).start();

    new Thread(() -> {
      try {
        while (true) {
          String solicitudServidor = in.readUTF();
          System.out.println(solicitudServidor);
          String respuestaUsuario = scanner.nextLine();
          out.writeUTF(respuestaUsuario);
          out.flush();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }).start();
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

  public static DataInputStream getIn() {
    return in;
  }
}
