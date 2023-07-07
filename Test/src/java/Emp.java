/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import annotation.Url;
import etu1978.framework.ModelView;
import java.util.Vector;
/**
 *
 * @author hoby
 */
public class Emp {
    int id;
    int id_dept;
    String nom;
    String prenom;
    double salaire;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_dept() {
        return id_dept;
    }

    public void setId_dept(int id_dept) {
        this.id_dept = id_dept;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public double getSalaire() {
        return salaire;
    }

    public void setSalaire(double salaire) {
        this.salaire = salaire;
    }

    public Emp(int id, int id_dept, String nom, String prenom, double salaire) {
        this.id = id;
        this.id_dept = id_dept;
        this.nom = nom;
        this.prenom = prenom;
        this.salaire = salaire;
    }
    
    public Emp(){
        
    }
    
    @Url(path="/emp-all")
    public ModelView getEmp(){
        String view = "emp-all.jsp";
        ModelView mv = new ModelView(view);
        mv.addItem("employes", this.lists());
        return mv;
    }
    
    public Vector<Emp> lists(){
        Emp emp = new Emp(1,1,"Rakoto","Jean",200000);
        Emp emp1 = new Emp(2,1,"Randria","Bema",600000);
        Vector<Emp> list = new Vector<>();
        list.add(emp);
        list.add(emp1);
        return list;
    }
}
