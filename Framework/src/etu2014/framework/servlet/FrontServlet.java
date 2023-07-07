320005/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package etu1978.framework.servlet;

import annotation.Url;
import annotation.Parametre;
import etu1978.framework.FileUpload;
import etu1978.framework.ModelView;
import etu1978.framework.Mapping;
import exception.UrlInconue;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import javax.servlet.ServletConfig;

/**
 *
 * @author hoby
 */
@WebServlet(name = "FrontServlet", urlPatterns = {"/"})
public class FrontServlet extends HttpServlet {
   
    HashMap<String, Mapping> MappingUrls;

    public HashMap<String, Mapping> getMappingUrls() {
        return MappingUrls;
    }

    public void setMappingUrls(HashMap<String, Mapping> MappingUrls) {
        this.MappingUrls = MappingUrls;
    }


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        String sourceFile = config.getInitParameter("sourceFile");
        String[] parts = sourceFile.split("-");
        String pack = null;
        String path = null;

    // Stockage des parties découpées dans les attributs package et pathDir
        if (parts.length >= 1) {
            pack = parts[0];
        }
        if (parts.length >= 2) {
            path = parts[1];
        }
        //this.setDirectoryPath(sourceFile);
        String[] classeNames = this.getClassList(path);
        HashMap<String, Mapping> mappingurl = new HashMap<>();
        for(String classeName : classeNames){
            Class<?> classe = null;
            try {
                classe = Class.forName(pack+"."+classeName);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
            }

            Method[] methods = classe.getDeclaredMethods();
            for (Method method : methods){
                Url url = method.getAnnotation(Url.class);
                if (url != null){
                    Class<?> conteneurClass = method.getDeclaringClass();
                    /*this.setMethod(method.getName());
                    this.setAnnotation(url.path());
                    this.setClassObj(conteneurClass.getName());*/
                    Mapping mapping = new Mapping(conteneurClass.getName(),method.getName());
                    mappingurl.put(url.path(), mapping);
                }
            }
        }
        this.setMappingUrls(mappingurl);
    }
    
    
     public static String[] getClassList(String path)
    {
        File file=new File(path);

        File[] f=file.listFiles();
        FilenameFilter textFilefilter = new FilenameFilter(){
            public boolean accept(File dir, String name) {
//        String lowercaseName = name.toLowerCase();
                if (name.endsWith(".java")) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        String [] listefile=file.list(textFilefilter);
        for (int i = 0; i < listefile.length; i++) {
            listefile[i]=listefile[i].split(".java")[0];
        }    
        return listefile;
    }

  
    
    public ModelView check(String url) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, UrlInconue, InstantiationException{
        if(this.getMappingUrls().containsKey(url)){
            String classname = this.getMappingUrls().get(url).getClassName();
            String methode = this.getMappingUrls().get(url).getMethod();
            Class<?> classe = Class.forName(classname);
            Method method = classe.getDeclaredMethod(methode);
            Object objet = classe.newInstance();
            ModelView mv = (ModelView)method.invoke(objet);
            return mv;
        }else{
            throw new UrlInconue();
        }
    }
    
    public ModelView det(String url,String[] params,String[] values,PrintWriter out) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, UrlInconue, InstantiationException{
        String classname = this.getMappingUrls().get(url).getClassName();
        String methode = this.getMappingUrls().get(url).getMethod();
        Class<?> classe = Class.forName(classname);
        Object objet = classe.newInstance();
        Method method = classe.getDeclaredMethod(methode, Integer.class);
        Parameter[] de = method.getParameters();
        Object type = null;
        for(Parameter param : de){
            String nom = param.getName();
            String name = param.getAnnotation(Parametre.class).name();
            out.print("<h3>"+nom+"</h3>");
            for(int i = 0; i < params.length; i++){
                if(params[i].equals(name)){
                    if(param.getType()==Integer.class)
                        type = Integer.valueOf(values[0]);
                    else if (param.getType() == String.class) {
                        type = Integer.valueOf(values[i]);
                    } else if (param.getType() == Double.class) {
                        type = Double.valueOf(values[i]);
                    } else if (param.getType() == boolean.class || param.getType() == Boolean.class) {
                        type = Boolean.valueOf(values[i]);
                    } else if (param.getType() == Date.class) {
                        type = Date.valueOf(values[i]);
                    }
                }
            }
        }
        ModelView mv = (ModelView)method.invoke(objet,type);
        return mv;
    }
    
    public ModelView save(String url,String[] params,String[] values,String[] checkBox,FileUpload fup,PrintWriter out) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, UrlInconue, InstantiationException{
        String classname = this.getMappingUrls().get(url).getClassName();
        out.print("ATOOOOOOOOO");
        String methode = this.getMappingUrls().get(url).getMethod();
        Class<?> classe = Class.forName(classname);
        Object objet = classe.newInstance();
        Field[] fields = classe.getDeclaredFields();
        String[] allparm = new String[0];
        out.println("ATOOOOOOOOO");
        for(Field field : fields){
            for(int i = 0;i < params.length;i++){
                if(params[i].equals(field.getName())){
                    Method setobject = classe.getMethod("set"+this.upperFirst(params[i]),field.getType());
                    Object type = null;
                    if (field.getType() == String.class) {
                        type = values[i];
                    } else if (field.getType() == int.class || field.getType() == Integer.class) {
                        type = Integer.valueOf(values[i]);
                    } else if (field.getType() == double.class || field.getType() == Double.class) {
                        type = Double.valueOf(values[i]);
                    } else if (field.getType() == boolean.class || field.getType() == Boolean.class) {
                        type = Boolean.valueOf(values[i]);
                    } else if (field.getType() == Date.class) {
                        type = Date.valueOf(values[i]);
                        out.println("DATY");
                    }else if (field.getType() == String[].class) {
                        type = checkBox;
                        out.println("STRING[]");
                    } else if (field.getType() == FileUpload.class) {
                        type = fup;
                        out.println("FILEUPLOAD");
                    }
                    //allparm = Arrays.copyOf(allparm,allparm.length + 1);
                    try{
                        out.print("allparm:"+type.getClass().getName());
                    }catch(Exception e){
                        out.print(e.getMessage());
                    }
                    //allparm[allparm.length - 1] = type.getClass().getName();
                    setobject.invoke(objet, type);
                    out.println("METY VEE");
                }
            }
        }
        out.print("ATOOOOOOOOO AMBANYYYY");
        Method method = classe.getDeclaredMethod(methode);
        ModelView mv = (ModelView)method.invoke(objet);
        return mv;
    }
    
    public String upperFirst(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
    
    public String getLastPart(String input) {
        String[] parts = input.split("/");
        return parts[parts.length - 1];
    }
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        response.setContentType("text/html;charset=UTF-8");
        String url = getLastPart(request.getServletPath());
        ModelView mv;
        int k = 0;
        try (PrintWriter out = response.getWriter()) {
            try {
                if (this.getMappingUrls().containsKey(url) && url.contains("save")){
                    Enumeration<String> parametres = request.getParameterNames();
                    String[] attributs = new String[0];
                    String[] values = new String[0];
                    String[] forCheckBox = new String[0];
                    FileUpload upload = null;
                    while(parametres.hasMoreElements()){
                        String Parametre = parametres.nextElement();
                        if(Parametre.equals("check")){
                            String[] checks = request.getParameterValues(Parametre);
                            attributs = Arrays.copyOf(attributs,attributs.length + 1);
                            attributs[attributs.length - 1] = Parametre;
                            values = Arrays.copyOf(values,values.length + 1);
                            values[values.length - 1] = "none"; 
                            for(String check : checks){
                                out.print("<h3>"+Parametre+" :</h3><p>"+check+"</p>");
                                forCheckBox = Arrays.copyOf(forCheckBox,forCheckBox.length + 1);
                                forCheckBox[forCheckBox.length - 1] = check;
                            }
                        }else if(Parametre.equals("uploadFichier")){
                            String value = request.getParameter(Parametre);
                            Path path = Paths.get(value);
                            out.print("<h3>"+Parametre+" ato tsika :</h3><p>"+path.toAbsolutePath()+"</p>");
                            try{
                                byte[] fileBytes = Files.readAllBytes(path);
                                upload = new FileUpload(value,fileBytes);
                            }catch(Exception io){
                                out.print(io.getMessage());
                            }
                            attributs = Arrays.copyOf(attributs,attributs.length + 1);
                            attributs[attributs.length - 1] = Parametre;
                            values = Arrays.copyOf(values,values.length + 1);
                            values[values.length - 1] = "none"; 
                            //out.print("<h3>"+Parametre+" ato tsika :</h3><p>"+path.toAbsolutePath()+"</p>");
                        }else{
                            String value = request.getParameter(Parametre);
                            out.print("<h3>"+Parametre+" :</h3><p>"+value+"</p>");
                            attributs = Arrays.copyOf(attributs,attributs.length + 1);
                            attributs[attributs.length - 1] = Parametre;
                            values = Arrays.copyOf(values,values.length + 1);
                            values[values.length - 1] = value;   
                        }
                    }
                    out.print("Atoo");
                    mv = this.save(url,attributs,values,forCheckBox,upload,out);
                    out.print("Tafiditra ato ambany");
                }else if (this.getMappingUrls().containsKey(url) && url.contains("details")){
                    Enumeration<String> parametres = request.getParameterNames();
                    String[] attributs = new String[0];
                    String[] values = new String[0];
                    while(parametres.hasMoreElements()){
                        String Parametre = parametres.nextElement();
                        String value = request.getParameter(Parametre);
                        attributs = Arrays.copyOf(attributs,attributs.length + 1);
                        attributs[attributs.length - 1] = Parametre;
                        values = Arrays.copyOf(values,values.length + 1);
                        values[values.length - 1] = value;
                        out.println("<h2>" + value + "</h2>");
                        out.println("<h2>" + Parametre + "</h2>");
                    }
                    mv = this.det(url, attributs, values, out);
                    out.print(mv.getView());
                }
                else mv = this.check(url);
                RequestDispatcher dispatcher = request.getRequestDispatcher(mv.getView());
                for (Map.Entry<String, Object> entry : mv.getData().entrySet()) {
                    Object key = entry.getKey();
                    Object val = entry.getValue();
                    out.print(key.toString());
                    out.print(val.getClass());
                    request.setAttribute("cle",(String)key);
                    request.setAttribute((String)key,val);
                }
                dispatcher.forward(request, response);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchMethodException ex) {
                Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UrlInconue ex) {
                out.println("<h1>" + ex.getMessage() + "</h1>");;
            } catch (InstantiationException ex) {
                Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }catch(Exception e){
            
        }
    }
    


    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}