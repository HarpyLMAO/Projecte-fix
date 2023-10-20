package es.cachau;

import static es.cachau.Client.scanner;

import es.cachau.managers.ChatManager;
import es.cachau.managers.database.Database;
import es.cachau.managers.entities.User;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Servidor {

  public static Database database;
  private static final int PUERTO = 8100;
  private static final List<Server> clientes = Collections.synchronizedList(
          new ArrayList<>()
  );

  public Servidor() throws ClassNotFoundException {
    database = new Database();
  }

  public static void main(String[] args) throws ClassNotFoundException {
    new Servidor();
    User.loadUsers();

    try (ServerSocket servidor = new ServerSocket(PUERTO)) {
      System.out.println("Servidor listo para aceptar conexiones...");

      while (true) {
        Socket socket = servidor.accept();
        System.out.println("Nuevo cliente conectado desde " + InetAddress.getLocalHost());

        Server nuevoCliente = new Server(socket, clientes);
        clientes.add(nuevoCliente);

        new Thread(nuevoCliente).start();
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      // Cierre la conexión aquí después de que todos los datos hayan sido enviados
      for (Server cliente : clientes) {
        cliente.cerrarConexion();
      }
    }
  }

  public static class Server implements Runnable {

    private final Socket sk;
    private final DataOutputStream out;
    private final DataInputStream in;
    private final List<Server> clientes;

    private static ChatManager chatManager;

    public Server(Socket socket, List<Server> clientes) throws IOException {
      this.sk = socket;
      this.clientes = clientes;
      this.out = new DataOutputStream(socket.getOutputStream());
      this.in = new DataInputStream(socket.getInputStream());

      chatManager = new ChatManager(socket, out);
    }

    public void menu() throws IOException, SQLException {
      boolean sortir1 = false;
      out.writeUTF("\nBenvingut al Xat " + User.alias + "\nQue vols fer?");
      out.writeUTF("1 - Llistar usuaris");
      out.writeUTF("2 - Crear grup");
      out.writeUTF("3 - Eliminar grup");
      out.writeUTF("4 - Administrar grup");
      out.writeUTF("5 - Transmissió d'un fitxer");
      out.writeUTF("6 - Enviar missatge al servidor");
      out.writeUTF("7 - Llegir missatges");
      out.writeUTF("8 - Enviar fitxers al servidor");
      out.writeUTF("9 - Llistar fitxers");
      out.writeUTF("10 - Descarregar fitxers");
      out.writeUTF("11 - Sortir");

      do {
        int menu = scanner.nextInt();
        scanner.nextLine();
        switch (menu) {
          case 1:
            chatManager.llistaUsuaris();
            break;
          case 2:
            chatManager.createGroup();
            break;
          case 3:
            chatManager.deleteGroup();
            break;
          case 4:

            break;
          case 5:

            break;
          case 6:

            break;
          case 7:

            break;
          case 8:

            break;
          case 9:

            break;
          case 10:

            break;
          case 11:
            System.out.println("Nos vemos pronto!");
            sortir1 = true;
            enviarMenuInicial();
            break;
          default:
            System.out.println("Escoge un numero entre los valores.");
            break;
        }
      } while (!sortir1);
    }

    @Override
    public void run() {
      try {
        enviarMenuInicial();
        String mensaje;
        while ((mensaje = in.readUTF()) != null) {
          System.out.println("Mensaje recibido: " + mensaje);
        }
      } catch (IOException | SQLException e) {
        System.out.println(
                "Error en la conexión con el cliente: " + e.getMessage()
        );
      } finally {
      }
    }

    public void enviarMenuInicial() throws IOException, SQLException {
      out.writeUTF("1 - Crear usuario");
      out.writeUTF("2 - Acceder usuario");
      out.writeUTF("3 - Salir");
      out.writeUTF("Respuesta");

      String respuesta = in.readUTF();

      switch (respuesta) {
        case "1":
          try {
            chatManager.createUser();
          } catch (Exception exception) {
            exception.printStackTrace();
          }
          this.menu();
          break;
        case "2":
          try {
            chatManager.iniciaSessio();
          } catch (Exception exception) {
            exception.printStackTrace();
          }
          this.menu();
          break;
        case "3":
          // Opción para salir del cliente y cerrar el socket
          break;
        default:
          System.out.println(
                  "Error, no se ha seleccionado ninguna opcion, se cierra la sesion"
          );
          cerrarConexion();
          break;
      }
    }

    public void cerrarConexion() {
      try {
        if (out != null) out.close();
        if (in != null) in.close();
        if (sk != null) sk.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public Database getDatabase() {
    return database;
  }
}
