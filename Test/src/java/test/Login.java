/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

import annotation.Url;
import etu1978.framework.ModelView;

/**
 *
 * @author hoby
 */
public class Login {
    String name, pass;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Login(String u_name, String pass) {
        this.setName(u_name);
        this.setPass(pass);
    }

    public Login() {
    }
    
    @Url(path="login")
    public ModelView login(){
        String view = "save-emp.jsp";
        ModelView mv = new ModelView(view,false);
        if(this.getName().equals("Ihobiana") && this.getPass().equals("admin")){
            mv.addSession("isConnected", true);
            mv.addSession("profil", 11);
        } else if(this.getName().equals("Koto") && this.getPass().equals("manga")){
            mv.addSession("isConnected", true);
            mv.addSession("profil", 1);
        }
        return mv;
    }
}
