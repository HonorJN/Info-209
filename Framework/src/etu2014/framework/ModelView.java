/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package etu1978.framework;

import java.util.HashMap;

/**
 *
 * @author hoby
 */
public class ModelView {
    String view;
    HashMap<String,Object> data;

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public HashMap<String, Object> getData() {
        return data;
    }

    public void setData(HashMap<String, Object> dictionnaries) {
        this.data = dictionnaries;
    }
    
    public ModelView(String view) {
        this.setView(view);
        HashMap<String,Object> data = new HashMap<>();
        this.setData(data);
    }
    
    public void addItem(String cle, Object objet){
        this.getData().put(cle, objet);
    }
}
