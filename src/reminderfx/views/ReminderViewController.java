/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reminderfx.views;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import reminderfx.customcomponents.activitylabel.ActivityLabelController;
import reminderfx.customcomponents.DatePicker;
import reminderfx.model.Activity;
import reminderfx.model.Reminder;
import reminderfx.model.UrgencyLevel;

/**
 *
 * @author tengi12
 */
public class ReminderViewController extends VBox{
    @FXML DatePicker datePicker;
    @FXML VBox activityLabelsPanel;
    @FXML ImageView addActivityButton;
    private Reminder reminder;
    
    public ReminderViewController(){
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ReminderView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

//        datePicker = new DatePicker();
//        datePicker.setDate(Calendar.getInstance().getTime());
        
        reminder = Reminder.getInstance();
        reminder.getSynchronize().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                if(t1){
                    reload();
                    reminder.getSynchronize().set(false);
                }
            }
        });
        
//        for(int i = 0; i < 20; i++){
//            Activity act = new Activity();
//            act.setDescription("Attività di prova " + i);
//            act.setUrgencyLevel(getRandomUrgencyLevel());
////            act.setCompleted(true);
//            reminder.getActivities().add(act);
////            ActivityLabelController a = new ActivityLabelController(act);
////            this.getChildren().add(a);
//        }
        
        int i = 0;
        for(Activity act: reminder.getActivities()){
            ActivityLabelController a = new ActivityLabelController(act, i);
            activityLabelsPanel.getChildren().add(a);
            i++;
        }
    }
    
    private UrgencyLevel getRandomUrgencyLevel(){
        int i = (int) (Math.random() * 5);
        
        switch(i){
            case 0:
                return UrgencyLevel.NONE;
            case 1:
                return UrgencyLevel.LITTLE;
            case 2:
                return UrgencyLevel.MEDIUM;
            case 3:
                return UrgencyLevel.GREAT;
            case 4:
                return UrgencyLevel.MAXIMUM;
        }
        
        return UrgencyLevel.NONE;
    }
    
    public void saveReminder(){
        reminder.save();
    }
    
    @FXML public void onAddActivity(){
        Activity act = new Activity();
        act.setDescription("Nuova attività");
        act.setInsertDate(datePicker.getCurrentDate());
        reminder.getActivities().add(act);
        ActivityLabelController alc = new ActivityLabelController(act, reminder.getActivities().size() - 1);
        activityLabelsPanel.getChildren().add(alc);
        alc.setIsEditing(true);
    }
    
    private void reload(){
        activityLabelsPanel.getChildren().clear();
        int i = 0;
        for(Activity a : reminder.getActivities()){
            if(afterEquals(datePicker.getCurrentDate(), a.getInsertDate())){
                System.out.println(" Attività inserita");
                ActivityLabelController alc = new ActivityLabelController(a, i);
                activityLabelsPanel.getChildren().add(alc);
                i++;
            }
            else {
                System.out.println(" Attività non inserita");
            }
        }
    }
    
    private boolean afterEquals(Date date1, Date date2){
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(date2);
        
        System.out.print("1: " + c1.getTime() + " - 2: " + c2.getTime());
        
        if(c1.get(Calendar.YEAR) > c2.get(Calendar.YEAR)){
            return true;
        }
        else if (c1.get(Calendar.YEAR) < c2.get(Calendar.YEAR)){
            return false;
        }
        
        if(c1.get(Calendar.MONTH) > c2.get(Calendar.MONTH)){
            return true;
        }
        else if (c1.get(Calendar.MONTH) < c2.get(Calendar.MONTH)){
            return false;
        }
        
        return c1.get(Calendar.DAY_OF_MONTH) >= c2.get(Calendar.DAY_OF_MONTH);
    }
}
