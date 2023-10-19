package es.cachau;

import static es.cachau.Client.scanner;

import es.cachau.managers.ChatManager;
import es.cachau.managers.database.Database;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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

    try (ServerSocket servidor = new ServerSocket(PUERTO)) {
      System.out.println("Servidor listo para aceptar conexiones...");

      while (true) {
        Socket socket = servidor.accept();
        System.out.println("Nuevo cliente conectado");

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
    private final PrintWriter out;
    private final BufferedReader in;
    private final List<Server> clientes;

    private static ChatManager chatManager;

    public Server(Socket socket, List<Server> clientes) throws IOException {
      this.sk = socket;
      this.clientes = clientes;
      this.out = new PrintWriter(socket.getOutputStream(), true);
      this.in =
        new BufferedReader(new InputStreamReader(socket.getInputStream()));

      chatManager = new ChatManager(socket, out);
    }

    public void menu() throws IOException, SQLException {
      boolean sortir1 = false;
      out.println("\nBenvingut al Xat.\nQue vols fer?");
      out.println("1 - Connectar al servidor");
      out.println("2 - Llistar usuaris");
      out.println("3 - Crear grup");
      out.println("4 - Eliminar grup");
      out.println("5 - Administrar grup");
      out.println("6 - Transmissió d'un fitxer");
      out.println("7 - Enviar missatge al servidor");
      out.println("8 - Llegir missatges");
      out.println("9 - Enviar fitxers al servidor");
      out.println("10 - Llistar fitxers");
      out.println("11 - Descarregar fitxers");
      out.println("12 - Sortir");

      do {
        int menu = scanner.nextInt();
        scanner.nextLine();
        switch (menu) {
          case 1:
            //sk = new Socket("localhost", 8100); //acepta una conexión
            break;
          case 2:
            chatManager.llistaUsuaris();
            break;
          case 3:
            chatManager.createGroup();
            break;
          case 4:
            chatManager.deleteGroup();
            break;
          case 5:
            // Código para administrar grupo
            break;
          case 6:
            // Código para transmisión de un archivo
            break;
          case 7:
            // Código para enviar mensaje al servidor
            break;
          case 8:
            // Código para leer mensajes
            break;
          case 9:
            // Código para enviar archivos al servidor
            break;
          case 10:
            // Código para listar archivos
            break;
          case 11:
            // Código para descargar archivos
            break;
          case 12:
            System.out.println("Nos vemos pronto!");
            sortir1 = true;
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
        while ((mensaje = in.readLine()) != null) {
          System.out.println("Mensaje recibido: " + mensaje);
        }
      } catch (IOException | SQLException e) {
        System.out.println(
          "Error en la conexión con el cliente: " + e.getMessage()
        );
      } finally {
        // No cierre la conexión aquí
      }
    }

    public void enviarMenuInicial() throws IOException, SQLException {
      out.println("1 - Crear usuario");
      out.println("2 - Acceder usuario");
      out.println("3 - Salir");
      out.println("Respuesta");

      String respuesta = in.readLine();
      System.out.println(respuesta);

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
