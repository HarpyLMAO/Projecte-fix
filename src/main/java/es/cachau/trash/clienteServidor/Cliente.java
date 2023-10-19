/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.cachau.trash.clienteServidor;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Cliente {

    public static Scanner sc = new Scanner(System.in);
    
    
    //static User currentUser = new User(null, null);
    public static Socket sk = null;
    public static DataOutputStream out;

    public static BufferedReader in = null;

    public static void main(String[] args) throws IOException, SQLException {
        System.out.println("Inicia cliente");

        try {
            sk = new Socket("localhost", 8100); // Conéctate al servidor en localhost y puerto 8100
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Para leer del servidor
        in = new BufferedReader(new InputStreamReader(sk.getInputStream()));
        // Para escribir al servidor
        PrintWriter out = new PrintWriter(sk.getOutputStream(), true);

        //empezamos a escuchar
            escucharServidor();
            
            BufferedReader lectorConsola = new BufferedReader(new InputStreamReader(System.in));
            String lineaConsola;
            while ((lineaConsola = lectorConsola.readLine()) != null) {
                out.println(lineaConsola);
            }
            
            escucharServidor();
            
    }

    public static void escucharServidor() {
        try {
            while (true) {
                
                String mensajeServidor = in.readLine();
                if (mensajeServidor != null) {
                    if(mensajeServidor.equals("Respuesta")){
                        break;
                    }else{
                        System.out.println(mensajeServidor); // muestra el menú o cualquier otro mensaje del servidor
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
