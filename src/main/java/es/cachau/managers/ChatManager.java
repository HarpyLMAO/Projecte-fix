package es.cachau.managers;

import es.cachau.Client;
import es.cachau.Servidor;
import es.cachau.managers.entities.Group;
import es.cachau.managers.entities.User;
import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ChatManager {

  private final Scanner scanner;

  private final DataOutputStream out;
  private final DataInputStream in;

  public ChatManager(Socket socket, DataOutputStream out) throws IOException {
    this.out = out;
    this.in = new DataInputStream(socket.getInputStream());
    this.scanner = Client.getScanner();

    out.writeUTF("XAT");
  }

  public void createUser() throws SQLException, IOException {
    out.writeUTF("Nom usuari: ");
    String name = in.readUTF();

    out.writeUTF("Contrasenya: ");
    String password = in.readUTF();

    out.writeUTF("Alias: ");
    String alias = in.readUTF();

    User user = new User(name, alias, password);
    user.create(name, alias, password);

    Client.setCurrentUser(User.getByName(name));
  }

  public void createGroup() throws SQLException, IOException {
    out.writeUTF("Nom grup: ");
    String name = in.readUTF();

    new Group(name, Group.groups.size() + 1);
  }

  public void deleteGroup() throws SQLException, IOException {
    out.writeUTF("Nom grup: ");
    String name = in.readUTF();

    if (Group.getByName(name) == null) {
      out.writeUTF(name);
      return;
    }

    Group.deleteGroup(Group.getByName(name));
  }

  public void iniciaSessio() throws IOException, SQLException {
    out.writeUTF("Nom usuari: ");
    String name = in.readUTF();

    out.writeUTF("Contrasenya: ");
    String password = in.readUTF();

    String query = "SELECT * FROM usuaris WHERE nom_usuari = '" + name + "' AND password_usuari = '" + password + "';";
    ResultSet resultSet = Servidor.database.statement.executeQuery(query);

    if (resultSet.next()) {
      Client.setCurrentUser(User.getByName(name));
      out.writeUTF("Sessió iniciada correctament.");
    } else {
      out.writeUTF("Usuari o contrasenya incorrectes.");
    }
  }

  public void llistaUsuaris() throws SQLException, IOException {
    String query = "SELECT nom_usuari,alias_usuari FROM usuaris";
    ResultSet resultSet = Servidor.database.statement.executeQuery(query);

    while (resultSet.next()) {
      out.writeUTF(resultSet.getString("nom_usuari") + " " + resultSet.getString("alias_usuari"));
    }
  }
}
