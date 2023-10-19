/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.cachau.trash.funcionalidades;

import es.cachau.trash.database.bbdd_usuarios;

import static es.cachau.trash.clienteServidor.Cliente.out;
import static es.cachau.trash.clienteServidor.Cliente.sc;
import static es.cachau.trash.clienteServidor.Cliente.sk;
import static es.cachau.trash.clienteServidor.Servidor.Server.menu;

import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author hugo2
 */
public class Funcionalidades {

    public static void creaUsuari() {
            System.out.print("Nom usuari: ");
            String nom_usuari = sc.nextLine();

            System.out.print("Contraseña usuari: ");
            String contrasena_usuari = sc.nextLine();

            System.out.print("Alias: ");
            String alias_usuari = sc.nextLine();

            bbdd_usuarios.creaUsuariQuery(nom_usuari,contrasena_usuari,alias_usuari);
    }

    public static void crearGrup() {
        System.out.print("Nom grup: ");
        String nom_grup = sc.nextLine();

        String sql3 = "INSERT INTO grups SET nom_grup = '" + nom_grup + "';";
        enviarAlServidor(sql3);
        System.out.println("Grup afegit correctament.");
    }

    public static void eliminaGrup() {
        try {
            String sql = "SELECT nom_grup FROM grups;";
            ResultSet resultSet = bbdd_usuarios.st.executeQuery(sql);

            System.out.println("Lista de grupos:");
            while (resultSet.next()) {
                String nombreGrupo = resultSet.getString("nom_grup");
                System.out.println("- " + nombreGrupo);
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar los grupos: " + ex.toString());
        }
        System.out.print("Quin grup vols eliminar?");
        String nom_grup = sc.nextLine();
        try {
            String delGrp = "DELETE FROM grups WHERE nom_grup = '" + nom_grup + "';";
            bbdd_usuarios.st.executeUpdate(delGrp);
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.toString());
            return;
        }
        System.out.println("Grup eliminat correctament.");
    }

    public static void iniciaSessio() throws IOException {
        try {
            System.out.print("Nom usuari: ");
            String nom_usuari = sc.nextLine();
            System.out.print("Contraseña: ");
            String password = sc.nextLine();

            String sql = "SELECT * FROM usuaris WHERE nom_usuari = '" + nom_usuari + "' AND password_usuari = '" + password + "';";
            ResultSet resultSet = bbdd_usuarios.st.executeQuery(sql);

            if (resultSet.next()) {
                System.out.println("Sessió iniciada amb èxit per l'usuari: " + nom_usuari);

                //currentUser.setNomUsuari(nom_usuari);
                //currentUser.setPassword(password);
                menu();

            } else {
                System.out.println("Error: Usuari o contrasenya incorrectes.");
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.toString());
        }
    }

    public static void llistaUsuaris() {
        //Consulta
        try {

            // Codi SQL
            String strSql = "SELECT nom_usuari,alias_usuari FROM usuaris";

           //Resultats select
            ResultSet rs = bbdd_usuarios.st.executeQuery(strSql);
            enviarAlServidor(strSql);

            while (rs.next()) {
                System.out.println(rs.getString("nom_usuari") + " " + rs.getString(2));
            }

            rs.close();

        } catch (SQLException ex) {
            System.out.println("Error: " + ex.toString());
            return;
        }
    }

    public static void enviarAlServidor(String mensaje) {
        try {
            if (sk.isClosed()) {
                System.out.println("El socket está cerrado. No se puede enviar el mensaje.");
                return;
            }
            out = new DataOutputStream(sk.getOutputStream());
            out.writeUTF(mensaje);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
