package es.cachau.managers;

import es.cachau.Client;
import es.cachau.Servidor;
import es.cachau.managers.entities.Group;
import es.cachau.managers.entities.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ChatManager {

  private final Scanner scanner;

  private final PrintWriter out;

  private final BufferedReader in;

  public ChatManager(Socket socket, PrintWriter out) throws IOException {
    this.out = out;
    this.in =
      new BufferedReader(new InputStreamReader(socket.getInputStream()));
    this.scanner = Client.getScanner();

    out.println("Registered Chat manager");
  }

  public void createUser() throws SQLException, IOException {
    out.println("Nom usuari: ");
    String name = in.readLine();

    out.println("Contrasenya: ");
    String password = in.readLine();

    out.println("Alias: ");
    String alias = in.readLine();

    new User(name, alias, password);
    Client.setCurrentUser(User.getByName(name));
  }

  public void createGroup() throws SQLException, IOException {
    out.println("Nom grup: ");
    String name = in.readLine();

    new Group(name, Group.groups.size() + 1);
  }

  public void deleteGroup() throws SQLException, IOException {
    out.println("Nom grup: ");
    String name = in.readLine();

    if (Group.getByName(name) == null) {
      out.println(name);
      return;
    }

    Group.deleteGroup(Group.getByName(name));
  }

  public void iniciaSessio() throws IOException, SQLException {
    out.println("Nom usuari: ");
    String name = in.readLine();

    out.println("Contrasenya: ");
    String password = in.readLine();

    String query =
      "SELECT * FROM usuaris WHERE nom_usuari = '" +
      name +
      "' AND password_usuari = '" +
      password +
      "';";
    ResultSet resultSet = Servidor.database.statement.executeQuery(query);

    if (resultSet.next()) {
      Client.setCurrentUser(User.getByName(name));
      out.println("Sessió iniciada correctament.");
    } else {
      out.println("Usuari o contrasenya incorrectes.");
    }
  }

  public void llistaUsuaris() throws SQLException, IOException {
    String query = "SELECT nom_usuari,alias_usuari FROM usuaris";
    ResultSet resultSet = Servidor.database.statement.executeQuery(query);

    while (resultSet.next()) {
      out.println(
        resultSet.getString("nom_usuari") + " " + resultSet.getString(2)
      );
    }
  }
}
