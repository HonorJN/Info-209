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
    HashMap<String,Object> sessions;
    boolean isJson;

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

    public boolean isIsJson() {
        return isJson;
    }

    public void setIsJson(boolean isJson) {
        this.isJson = isJson;
    }

    public HashMap<String, Object> getSessions() {
        return sessions;
    }

    public void setSessions(HashMap<String, Object> sessions) {
        this.sessions = sessions;
    }
    
    public ModelView(String view,boolean json) {
        this.setView(view);
        this.setIsJson(json);
        HashMap<String,Object> data = new HashMap<>();
        HashMap<String,Object> session = new HashMap<>();
        this.setData(data);
        this.setSessions(session);
    }
    
    public void addItem(String cle, Object objet){
        this.getData().put(cle, objet);
    }
    
    public void addSession(String cle, Object objet){
        this.getSessions().put(cle, objet);
    }
}
