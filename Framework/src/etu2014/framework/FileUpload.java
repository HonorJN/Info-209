/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package etu1978.framework;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hoby
 */
public class FileUpload {
    String name;
    byte[] forFile;
    String Path;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getForFile() {
        return forFile;
    }

    public void setForFile(byte[] forFIle) {
        this.forFile = forFIle;
    }

    public String getPath() {
        return Path;
    }

    public void setPath(String Path) {
        this.Path = Path;
    }
    
    public FileUpload(String nom, byte[] filebytes){
        this.setName(nom);
        this.setForFile(filebytes);
    }
    
    public void writeFile() {
        int length = this.getForFile().length;
        byte[] message = new byte[length];
        try {
            FileOutputStream fos = new FileOutputStream("/home/rado/NetBeansProjects/Test/nouveauFichier.txt");
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bos.write(message,0, length);
            bos.flush();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileUpload.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileUpload.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
