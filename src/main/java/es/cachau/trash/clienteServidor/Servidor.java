/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.cachau.trash.clienteServidor;

import static es.cachau.trash.clienteServidor.Cliente.sc;

import es.cachau.trash.funcionalidades.Funcionalidades;
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

  static int n = 0; //contador de conexiones
  //esto lo que hace es un array list de los clientes(hilos) conectados
  private static List<Server> clientes = Collections.synchronizedList(
    new ArrayList<>()
  );
  static final int PUERTO = 8100;

  public static void main(String[] args) throws IOException {
    ServerSocket servidor = null;
    Socket sc = null;

    try {
      //iniciamos servidor
      servidor = new ServerSocket(PUERTO);
      System.out.println("Servidor listo para aceptar conexiones...");

      while (true) {
        //aqui se acepta la conexion con el nuevo cliente
        Socket socket = servidor.accept();
        System.out.println("Nuevo cliente conectado");
        //creamos el hilo del cliente
        Server nuevoCliente = new Server(socket, clientes);
        //Se añade a nuestra array list de clientes
        clientes.add(nuevoCliente);
        //ejecutamos el hilo
        new Thread(nuevoCliente).start();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static class Server implements Runnable {

    private Socket sk;
    private PrintWriter out;
    private BufferedReader in;
    private List<Server> clientes;

    public Server(Socket socket, List<Server> clientes) throws IOException {
      this.sk = socket;
      this.clientes = clientes;
      this.out = new PrintWriter(socket.getOutputStream(), true);
      this.in =
        new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public static void menu() throws IOException, SQLException {
      boolean sortir1 = false;
      System.out.println("");
      System.out.println("Benvingut al Xat.");
      System.out.println("Que vols fer?");
      System.out.println("1 - Connectar al servidor");
      System.out.println("2 - Llistar usuaris");
      System.out.println("3 - Crear grup");
      System.out.println("4 - Eliminar grup");
      System.out.println("5 - Administrar grup");
      System.out.println("6 - Transmissió d'un fitxer");
      System.out.println("7 - Enviar missatge al servidor");
      System.out.println("8 - Llegir missatges");
      System.out.println("9 - Enviar fitxers al servidor");
      System.out.println("10 - Llistar fitxers");
      System.out.println("11 - Descarregar fitxers");
      System.out.println("12 - Sortir");

      do {
        int menu = sc.nextInt();
        sc.nextLine();
        switch (menu) {
          case 1:
            //sk = new Socket("localhost", 8100); //acepta una conexión

            break;
          case 2:
            Funcionalidades.llistaUsuaris();
            break;
          case 3:
            Funcionalidades.crearGrup();
            break;
          case 4:
            Funcionalidades.eliminaGrup();
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

    public void enviarMenuInicial() throws IOException {
      this.out.println("1 - Crear usuario");
      this.out.println("2 - Acceder usuario");
      this.out.println("3 - Salir");

      this.out.println("Respuesta");

      String respuesta = in.readLine();
      System.out.println(respuesta);

      switch (respuesta) {
        case "1":
          //creaUsuari();
          break;
        case "2":
          //iniciaSessio();
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

    @Override
    public void run() {
      try {
        enviarMenuInicial();
        String mensaje;

        while ((mensaje = in.readLine()) != null) {
          System.out.println("Mensaje recibido: " + mensaje);
        }
      } catch (IOException e) {
        System.out.println(
          "Error en la conexión con el cliente: " + e.getMessage()
        );
      } finally {
        cerrarConexion();
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
}
