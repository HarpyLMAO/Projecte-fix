/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.cachau.trash.entidades;

/**
 *
 * @author hugo2
 */
public class User {
    
    
    //tiene que tener los mismos campos con el mismo nombre que la base de datos
    private String nomUsuari;
    private String password;
    
    public User(String nomUsuari, String password) {
        this.nomUsuari = nomUsuari;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNomUsuari() {
        return nomUsuari;
    }

    public void setNomUsuari(String nomUsuari) {
        this.nomUsuari = nomUsuari;
    }
}