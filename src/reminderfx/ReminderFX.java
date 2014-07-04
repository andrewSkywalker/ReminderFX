/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reminderfx;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import reminderfx.model.Activity;
import reminderfx.model.Reminder;
import reminderfx.model.UrgencyLevel;
import reminderfx.views.ReminderViewController;

/**
 *
 * @author tengi12
 */
public class ReminderFX extends Application {
    
    private ReminderViewController r;
    
    @Override
    public void start(Stage stage) throws Exception {
        r = new ReminderViewController();
        
        Scene scene = new Scene(r);
        stage.setScene(scene);
        
        //---------------------------------------
        //
        //  Setting position and dimensions
        //
        //---------------------------------------
        stage.setTitle("Reminder");
        stage.setHeight(Screen.getPrimary().getVisualBounds().getHeight());
        stage.setWidth(425);
        stage.setX(0);
        stage.setY(0);
        
        //---------------------------------------
        //
        //  SETTING HANDLER PER SALVATAGGIO
        //
        //---------------------------------------
        
        stage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                r.saveReminder();
            }
        });
        
        //---------------------------------------
        //
        //  Setting application icon
        //
        //---------------------------------------
        stage.getIcons().add(new Image(getClass().getResource("images/post-it-orange.png").toString()));
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
        
        /*
        Questa porzione di codice la metto qui, ma non viene mai utilizzata. Mi
        servirà solamente come promemoria.
        */
//        try {
//            Calendar c = Calendar.getInstance();
//            Date oggi = c.getTime();
//            c.add(Calendar.DAY_OF_MONTH, 1);
//            Date domani = c.getTime();
//            Reminder r = new Reminder();
//            
//            try (XMLEncoder e = new XMLEncoder(new BufferedOutputStream(new FileOutputStream("provaReminder.xml")))) {
//                List<Activity> l = new LinkedList<>();
//                for(long i = 0; i < 70000; i++){
//                    Activity a = new Activity();
//                    a.setCompleted(false);
//                    a.setDescription("Attività di prova " + i);
//                    a.setEndDate(domani);
//                    a.setInsertDate(oggi);
//                    a.setUrgencyLevel(UrgencyLevel.MEDIUM);
//                    l.add(a);
//                }
//                r.setActivities(l);
//                
//                e.writeObject(r);
//            }
//                    
//            
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(ReminderFX.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        try{
//            XMLDecoder d = new XMLDecoder(new BufferedInputStream(new FileInputStream("prova.xml")));
//            List<Activity> l = (LinkedList) d.readObject();
//            for(Activity a : l){
//                System.out.println(a.getDescription());
//            }
//        }
//        catch(FileNotFoundException ex){
//            Logger.getLogger(ReminderFX.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
    }
    
}
