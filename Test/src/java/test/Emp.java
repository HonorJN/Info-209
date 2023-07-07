/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;
import annotation.Authentification;
import annotation.Parametre;
import annotation.ResteAPI;
import annotation.Url;
import etu1978.framework.FileUpload;
import etu1978.framework.ModelView;
import etu1978.framework.myAnnotation.Singleton;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Vector;
/**
 *
 * @author hoby
 */
@Singleton(is_singleton = true)
public class Emp {
    int id;
    int id_dept;
    String nom;
    String prenom;
    String[] check;
    double salaire;
    
    Date dateNaissance;
    FileUpload uploadFichier;

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

    public String[] getCheck() {
        return check;
    }

    public void setCheck(String[] check) {
        this.check = check;
    }
    
    public double getSalaire() {
        return salaire;
    }

    public void setSalaire(double salaire) {
        this.salaire = salaire;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public FileUpload getUploadFichier() {
        return uploadFichier;
    }

    public void setUploadFichier(FileUpload uploadFichier) {
        this.uploadFichier = uploadFichier;
    }
    
    public Emp(int id, int id_dept, String nom, String prenom, double salaire, String dtn, String[] chk) {
        this.id = id;
        this.id_dept = id_dept;
        this.nom = nom;
        this.prenom = prenom;
        this.salaire = salaire;
        this.dateNaissance = Date.valueOf(dtn);
        this.setCheck(chk);
    }
    
    public Emp(){
        
    }
    
    @Authentification(value = "admin",reference = 11)
    @Url(path="emp-all")
    public ModelView getEmp(){
        String view = "emp-all.jsp";
        ModelView mv = new ModelView(view,false);
        mv.addItem("employes", this.lists());
        return mv;
    }
    
    @Authentification(value = "",reference = 1)
    @Url(path="emp-save")
    public ModelView getAllEmp(){
        String view = "emp-all.jsp";
        ModelView mv = new ModelView(view,false);
        String daty = this.getDateNaissance().toString();
        Emp vao2 = new Emp(this.getId(),this.getId_dept(),this.getNom(),this.getPrenom(),this.getSalaire(),daty,this.getCheck());
        vao2.setUploadFichier(this.getUploadFichier());
        //this.getUploadFichier().writeFile();
        Vector<Emp> liste = this.lists();
        liste.add(vao2);
        mv.addItem("employes", liste);
        return mv;
    }
    
    @Authentification(value = "admin", reference = 11)
    @Url(path="emp-details")
    public ModelView FindbyId(@Parametre(name = "id") Integer id){
        String view = "emp-details.jsp";
        ModelView mv = new ModelView(view,true);
        Vector<Emp> liste = this.lists();
        for (Emp emp : liste){
            if(emp.getId() == (int)id){
                Emp vao2 = emp;
                mv.addItem("select", vao2);
            }
        }
        return mv;
    }
    
    public Vector<Emp> lists(){
        String[] table1 = new String[2];
        table1[0] = "Gestion";
        table1[1] = "Comptabilite";
        String[] table2 = new String[2];
        table2[0] = "Sociologie";
        table2[1] = "Be resaka";
        Emp emp = new Emp(1,1,"Rakoto","Jean",200000,"1998-07-12",table1);
        Emp emp1 = new Emp(2,1,"Randria","Bema",600000,"1999-02-24",table2);
        Vector<Emp> list = new Vector<>();
        list.add(emp);
        list.add(emp1);
        return list;
    }
    
    @ResteAPI(isresteApi = true)
    @Url(path="resteapi")
    public Emp[] liste(){
        Emp[] emp = new Emp[this.lists().size()];
        for (int i = 0; i < emp.length; i++) {
            emp[i] = this.lists().get(i);
        }
        return emp;
    }
    
    /*@Url(path="index")
    public ModelView saveEmp(){
        String view = "index.jsp";
        ModelView mv = new ModelView(view,false);
        return mv;
    }*/
}
