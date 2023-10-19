/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.cachau.trash.database;

import es.cachau.trash.clienteServidor.Cliente;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alberto
 */
public class MySQLconexion {

  static Connection cn;
  static Statement st;

  public static void conexionBBDD() {
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException ex) {
      System.out.println("No trobem el Driver MySQL para JDBC.");
      return;
    }

    try {
      cn =
        DriverManager.getConnection(
          "jdbc:mysql://localhost:3306/bbdd_projectexat",
          "root",
          "Alumne2022"
        );
      st = cn.createStatement();
    } catch (SQLException ex) {
      Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
      return;
    }
  }
}
