/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reminderfx.model;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleBooleanProperty;

/**
 *
 * @author tengi12
 */
public class Reminder implements Serializable{
    private List<Activity> activities;
    private static final String todoListFileName = "todo-list.xml";
    
    private static Reminder instance;
    
    private SimpleBooleanProperty synchronize;
    
    
    public Reminder(){
        instance = this;
        synchronize = new SimpleBooleanProperty(false);
        activities = new LinkedList();
        load();
    }

    public static Reminder getInstance() {
        if(instance == null){
            instance = new Reminder();
        }
        return instance;
    }

    /**
     * Tells if you must synchronize the view with the Reminder
     * @return 
     */
    public SimpleBooleanProperty getSynchronize() {
        return synchronize;
    }

    public void setSynchronize(SimpleBooleanProperty synchronize) {
        this.synchronize = synchronize;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }
    
    public void save(){
        File activitiesFile = getActivitiesFile();
        try (XMLEncoder enc = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(activitiesFile)))) {
            enc.writeObject(activities);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Reminder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void load(){
        List<Activity> l;
        File todoListFile = getActivitiesFile();
        
        if(todoListFile.length() == 0){
            System.out.println("Il file è vuoto, non carico alcuna attività");
            return;
        }
        
        try (XMLDecoder d = new XMLDecoder(new BufferedInputStream(new FileInputStream(todoListFile)))) {
            l = (LinkedList) d.readObject();
            activities.addAll(l);
        } catch (FileNotFoundException ex) {
            // Qui non si dovrebbe mai arrivare, perche' se il file non esiste, 
            // vuol dire che c'è stato qualche problema prima.
        }
    }
    
    /**
     * Questa funzione si occupa di ottenere un puntatore ad un file reale, creato
     * su filesystem. Se non esiste, lo crea.
     * @return il puntatore al {@link File} dove vengono salvate le attività
     */
    private File getActivitiesFile(){
        File result;
        String appdata = System.getenv("APPDATA");
        
        if(appdata == null){
            System.out.println("Errore! Non dichiarata la variabile APPDATA");
            return null;
        }
        
        File appdirectory = new File(appdata + "\\ReminderFX");
        if(!appdirectory.exists()){
            if(!appdirectory.mkdir()){
                System.out.println("Errore durante la creazione della directory " + appdirectory.getAbsolutePath());
                return null;
            }
        }
        
        result = new File(appdirectory.getAbsolutePath() + "\\" + todoListFileName);
        if(!result.exists()){
            try {
                result.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Reminder.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
        
        
        return result;
    }
    
    public void deleteActivity(int index){
        activities.remove(index);
        synchronize.set(true);
    }
}
