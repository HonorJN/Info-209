/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package etu1978.framework.servlet;

import annotation.Authentification;
import annotation.Url;
import annotation.Parametre;
import annotation.ResteAPI;
import com.google.gson.Gson;
import etu1978.framework.myAnnotation.Singleton;
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
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Date;
import jakarta.servlet.ServletConfig; 
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
/**
 *
 * @author hoby
 */
@WebServlet(name = "FrontServlet", urlPatterns = {"/"})
@MultipartConfig
public class FrontServlet extends HttpServlet {
   
    HashMap<String, Mapping> MappingUrls;
    HashMap<String, Object> classInstances;
    HashMap<String, Object> sessions;

    public HashMap<String, Mapping> getMappingUrls() {
        return MappingUrls;
    }

    public void setMappingUrls(HashMap<String, Mapping> MappingUrls) {
        this.MappingUrls = MappingUrls;
    }

    public HashMap<String, Object> getClassInstances() {
        return classInstances;
    }

    public void setClassInstances(HashMap<String, Object> classInstances) {
        this.classInstances = classInstances;
    }

    public HashMap<String, Object> getSessions() {
        return sessions;
    }

    public void setSessions(HashMap<String, Object> sessions) {
        this.sessions = sessions;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        String sourceFile = config.getInitParameter("sourceFile");
        String[] parts = sourceFile.split("-");
        Enumeration sessionNames = config.getInitParameterNames();
        HashMap<String, Object> session = new HashMap<>();
        while(sessionNames.hasMoreElements()){
            String name = (String) sessionNames.nextElement();
            if(!name.equals("sourceFile")){
                String parameterValue = config.getInitParameter(name);
                session.put(name, parameterValue);
            }
        }
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
        HashMap<String, Object> singleton = new HashMap<>();
        for(String classeName : classeNames){
            Class<?> classe = null;
            try {
                classe = Class.forName(pack+"."+classeName);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.setClassInstances(singleton);
            this.checkSingleton(classe);
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
        this.setSessions(session);
        this.setMappingUrls(mappingurl);
    }
    
    public void checkSingleton(Class classToChecked) {
        if (classToChecked.isAnnotationPresent(Singleton.class)) {
            String className = classToChecked.getName();
            this.classInstances.put(className, null);
        }
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

    public void reset(Object object) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            String fieldNameUp = upperFirst(field.getName());
            Method methodSet = null;
            try {
                methodSet = object.getClass().getMethod("set" + fieldNameUp, field.getType());
            } catch (Exception e) {
                continue;
            }
            if (field.getType().equals(int.class)) {
                methodSet.invoke(object, 0);
            }
            if (field.getType().equals(double.class)) {
                methodSet.invoke(object, 0);
            }
            if (field.getType().equals(float.class)) {
                methodSet.invoke(object, 0);
            }
            if (field.getType().equals(Object.class)) {
                methodSet.invoke(object, (Object) null);
            }
        }
    }
    
    public ModelView check(String url,PrintWriter out , HttpServletRequest req) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, UrlInconue, InstantiationException{
        if(this.getMappingUrls().containsKey(url)){
            String classname = this.getMappingUrls().get(url).getClassName();
            String methode = this.getMappingUrls().get(url).getMethod();
            Class<?> classe = Class.forName(classname);
            Method method = classe.getDeclaredMethod(methode);
            ModelView mv = null;
            try {
                checkAuthorisation(method, req, out);
                Object objet = classe.newInstance();
                mv = (ModelView)method.invoke(objet);
            } catch (Exception ex) {
                out.print(ex.getMessage());
            }
            return mv;
        }else{
            throw new UrlInconue();
        }
    }
    
    public Object getInClassInstance(String classname, Class<?> classe) throws InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException{
        Object objet = null;
        if(this.getClassInstances().containsKey(classname)){
            Object ob = this.getClassInstances().get(classname);
            if(ob == null){
                ob = classe.newInstance();
                reset(ob);
                objet = ob;
                this.classInstances.put(classname, objet);
            }else{
                reset(ob);
                objet = ob;
            }
        }
        else{
            objet = classe.newInstance();
        }
        return objet;
    }
    
    public ModelView det(String url,String[] params,String[] values,PrintWriter out, HttpServletRequest req) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, UrlInconue, InstantiationException{
        String classname = this.getMappingUrls().get(url).getClassName();
        String methode = this.getMappingUrls().get(url).getMethod();
        Class<?> classe = Class.forName(classname);
        //Object objet = classe.newInstance();
        Object objet = getInClassInstance(classname, classe);
        Method method = classe.getDeclaredMethod(methode, Integer.class);
        try {
            checkAuthorisation(method, req, out);
        } catch (Exception ex) {
            out.print(ex.getMessage());
        }
        Parameter[] de = method.getParameters();
        Object[] type = new Object[de.length];
        for(Parameter param : de){
            String nom = param.getName();
            String name = param.getAnnotation(Parametre.class).name();
            out.print("<h3>"+nom+"</h3>");
            for(int i = 0; i < params.length; i++){
                if(params[i].equals(name)){
                    if(param.getType()==Integer.class)
                        type[i] = Integer.valueOf(values[0]);
                    else if (param.getType() == String.class) {
                        type[i] = Integer.valueOf(values[i]);
                    } else if (param.getType() == Double.class) {
                        type[i] = Double.valueOf(values[i]);
                    } else if (param.getType() == boolean.class || param.getType() == Boolean.class) {
                        type[i] = Boolean.valueOf(values[i]);
                    } else if (param.getType() == Date.class) {
                        type[i] = Date.valueOf(values[i]);
                    }
                }
            }
        }
        ModelView mv = (ModelView)method.invoke(objet,type);
        return mv;
    }
    
    public ModelView save(String url,String[] params,String[] values,String[] checkBox,FileUpload fup,PrintWriter out, HttpServletRequest req) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, UrlInconue, InstantiationException{
        String classname = this.getMappingUrls().get(url).getClassName();
        out.print("1");
        String methode = this.getMappingUrls().get(url).getMethod();
        Class<?> classe = Class.forName(classname);
        //Object objet = classe.newInstance();
        Object objet = getInClassInstance(classname, classe);
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
                    }
                    //allparm = Arrays.copyOf(allparm,allparm.length + 1);
                    try{
                        out.print("allparm:"+type.getClass().getName());
                    }catch(Exception e){
                        out.print(e.getMessage());
                    }
                    //allparm[allparm.length - 1] = type.getClass().getName();
                    setobject.invoke(objet, type);
                }
            }
        }
        Method method = classe.getDeclaredMethod(methode);
        try {
            checkAuthorisation(method, req, out);
        }catch(Exception ex){
            out.print(ex.getMessage());
        }
        ModelView mv = (ModelView)method.invoke(objet);  
        return mv;
    }
    
    public void fillSessions(HttpServletRequest req, HashMap<String, Object> sessionsFromDataObject) {
        for (Map.Entry<String, Object> entry : sessionsFromDataObject.entrySet()) {
            if (sessions.containsValue(entry.getKey())) {
                req.getSession().setAttribute(entry.getKey(), entry.getValue());
            }
        }
    }
    
    public void checkAuthorisation(Method m, HttpServletRequest req,PrintWriter out) throws Exception {
        if (m.isAnnotationPresent(Authentification.class)) {
            int reference = m.getAnnotation(Authentification.class).reference();
            String sessionProfilName = (String) this.sessions.get("sessionName");
            String sessionProfil = (String) this.sessions.get("sessionProfil");
            if (req.getSession().getAttribute(sessionProfilName) == null) {
                throw new Exception("Vous devriez vous connecter");
            }
            int userProfil = (int) req.getSession().getAttribute(sessionProfil);
            if (reference > userProfil) {
                String exceptionMessage = "Vous n'etes pas en mesure d'appeller cette fonction";
                throw new Exception(exceptionMessage);
            }
        }
    }
    
    public String upperFirst(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
    
    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        String[] elements = contentDisposition.split(";");
        for (String element : elements) {
            if (element.trim().startsWith("filename")) {
                return element.substring(element.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
    
    private byte[] getBytesFromFile(String filePath) throws IOException {
        Path path = Path.of(filePath);
        return Files.readAllBytes(path);
    }
    
    public String getLastPart(String input) {
        String[] parts = input.split("/");
        return parts[parts.length - 1];
    }
    
    public void objectTojson(String url, PrintWriter out) throws ClassNotFoundException, InstantiationException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, UrlInconue{
        if(this.getMappingUrls().containsKey(url)){
            String classname = this.getMappingUrls().get(url).getClassName();
            String methode = this.getMappingUrls().get(url).getMethod();
            Class<?> classe = Class.forName(classname);
            Method method = classe.getDeclaredMethod(methode);
            if(checkresteAPI(method)){
                Object objet = classe.newInstance();
                Object mv = method.invoke(objet);
                Gson gson = new Gson();
                String jsonString = gson.toJson(mv);
                out.print(jsonString);
            }else{
                return;
            }
        }else{
            throw new UrlInconue();
        }
    }
    
    boolean checkresteAPI(Method m){
        if(m.isAnnotationPresent(ResteAPI.class))return true;
        return false;
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
                objectTojson(url, out);
                if (this.getMappingUrls().containsKey(url) && url.contains("save") || url.contains("login")){
                    Enumeration<String> parametres = request.getParameterNames();
                    String[] attributs = new String[0];
                    String[] values = new String[0];
                    String[] forCheckBox = new String[0];
                    FileUpload upload = null;
                    //out.print(this.getClassInstances().get);
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
                            out.print("Ihobiana 1");
                            //String value = request.getParameter(Parametre);
                            // Obtient le part correspondant au fichier uploadé
                            
                            Part filePart = request.getPart(Parametre);
                            String fileName = getFileName(filePart);
                            out.print("Ihobiana 2");
                            // Obtient le nom du fichier

                            // Obtient le chemin absolu du répertoire de l'application web
                            String appPath = request.getServletContext().getRealPath("");

                            // Définit le répertoire de destination des fichiers uploadés
                            String uploadDir = "/uploads";

                            // Crée le chemin absolu du répertoire de destination
                            String uploadPath = appPath + uploadDir;

                            // Vérifie si le répertoire de destination existe, sinon le crée
                            File uploadDirFile = new File(uploadPath);
                            if (!uploadDirFile.exists()) {
                                uploadDirFile.mkdirs();
                            }

                            // Crée le chemin absolu du fichier uploadé
                            String filePath = uploadPath + File.separator + fileName;

                            // Déplace le fichier vers le répertoire de destination
                            filePart.write(filePath);
                            // Obtient les bytes du fichier
                            byte[] fileBytes = getBytesFromFile(filePath);  
                            
                            upload = new FileUpload(fileName, fileBytes);
                            /*Part filePart = null;
                            try{
                                filePart = request.getPart(value);
                            }catch(Exception e){
                                out.print(e.getMessage());
                            }
                            InputStream inputStream = filePart.getInputStream();
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                            byte[] buffer = new byte[4096];
                            int bytesRead = -1;
                            out.print("Miditra ato ve ee?");
                            while ((bytesRead = inputStream.read(buffer)) != -1) {
                                outputStream.write(buffer, 0, bytesRead);
                            }
                            upload = new FileUpload(value, buffer);*/
                            /*Path path = Paths.get(value);
                            //out.print("<h3>"+Parametre+" ato tsika :</h3><p>"+path.toAbsolutePath()+"</p>");
                            try{
                                byte[] fileBytes = Files.readAllBytes(path);
                                upload = new FileUpload(value,fileBytes);
                            }catch(Exception io){
                                out.print(io.getMessage());
                            }*/
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
                    mv = this.save(url,attributs,values,forCheckBox,upload,out,request);
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
                        //out.println("<h2>" + value + "</h2>");
                        //out.println("<h2>" + Parametre + "</h2>");
                    }
                    mv = this.det(url, attributs, values, out, request);
                    out.println(mv.getView());
                }
                else mv = this.check(url, out, request);
                if (!mv.getSessions().isEmpty()) {
                    fillSessions(request, mv.getSessions());
                }
                if(mv.isIsJson()){
                    Gson gson = new Gson();
                    String jsonString = gson.toJson(mv.getData());
                    out.print(jsonString);
                    //out.flush();
                }else{
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
                }
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