/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reminderfx.customcomponents;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import reminderfx.model.Reminder;

/**
 *
 * @author tengi12
 */
public class DatePicker extends VBox{
    @FXML Label dateLabel;
    @FXML Label dayOfWeekLabel;
    @FXML TextField dateEditorTextField;
    @FXML HBox dateSelectionBox;
    @FXML Button prevMonthButton;
    @FXML Button prevDayButton;
    @FXML Button nextDayButton;
    @FXML Button nextMonthButton;
    private Date currentDate;
    private SimpleStringProperty dateLabelText;
    private SimpleStringProperty dayOfWeek;
    private final SimpleDateFormat dateFormat;
    

    public DatePicker(){
        //----------------------------------------------------------------------
        //
        //  LOADING FXML
        //
        //----------------------------------------------------------------------
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DatePicker.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        
        //----------------------------------------------------------------------
        //
        //  SETTAGGIO DATA CORRENTE
        //
        //----------------------------------------------------------------------
        currentDate = Calendar.getInstance().getTime();
        
        dayOfWeek = new SimpleStringProperty();
        dayOfWeek.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                Calendar c = Calendar.getInstance();
                c.setTime(currentDate);
                dayOfWeekLabel.setText(dayOfWeek.get());
            }
        });
        setDayOfWeek(Calendar.getInstance());
        
        //----------------------------------------------------------------------
        //
        //  SETTAGGIO RENDER MODE DELLA DATE LABEL
        //
        //----------------------------------------------------------------------
        dateLabelText = new SimpleStringProperty();
        dateLabelText.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                dateLabel.setText(dateLabelText.get());
            }
        });
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateLabelText.setValue(dateFormat.format(currentDate));
        
        //----------------------------------------------------------------------
        //
        //  ELIMINAZIONE DAI CHILDREN DEL TEXT FIELD
        //
        //----------------------------------------------------------------------
        dateSelectionBox.getChildren().remove(2); // il text field è il secondo
        
        //----------------------------------------------------------------------
        //
        //  SETTAGGIO TOOLTIP DEI BUTTONS
        //
        //----------------------------------------------------------------------
        prevMonthButton.setTooltip(new Tooltip("Vai al mese precedente"));
        prevDayButton.setTooltip(new Tooltip("vai al giorno precedente"));
        nextDayButton.setTooltip(new Tooltip("vai al giorno successivo"));
        nextMonthButton.setTooltip(new Tooltip("vai al mese successivo"));
        dateLabel.setTooltip(new Tooltip("clicca per cambiare la data"));
    }
    
    public SimpleStringProperty getDateLabelText(){
        return dateLabelText;
    }
    
    public void setDate(Date date){
        currentDate = date;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        dateLabelText.set(sdf.format(currentDate));
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        setDayOfWeek(c);
    }
    
    @FXML
    public void prevMonthButtonOnAction(){
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.MONTH, -1);
        setDate(c.getTime());
        Reminder.getInstance().getSynchronize().set(true);
    }
    
    @FXML
    public void prevDayButtonOnAction(){
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.DAY_OF_MONTH, -1);
        setDate(c.getTime());
        Reminder.getInstance().getSynchronize().set(true);
    }
    
    @FXML
    public void nextDayButtonOnAction(){
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.DAY_OF_MONTH, 1);
        setDate(c.getTime());
        Reminder.getInstance().getSynchronize().set(true);
    }
    
    @FXML
    public void nextMonthButtonOnAction(){
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.MONTH, 1);
        setDate(c.getTime());
        Reminder.getInstance().getSynchronize().set(true);
    }
    
    @FXML public void onDateSelectionCalled(){
        dateSelectionBox.getChildren().remove(2); // faccio sparire la label
        dateSelectionBox.getChildren().add(2, dateEditorTextField);
        dateEditorTextField.requestFocus();
    }
    
    @FXML public void onKeyPressed(KeyEvent e){
        if(e.getCharacter().equals("\r") || e.getCharacter().equals("\n")){
            if(dateEditorTextField.getText().length() != 0){
                try {
                    dateFormat.parse(dateEditorTextField.getText());
                    setDate(dateFormat.parse(dateEditorTextField.getText()));
                } catch (ParseException ex) {
                    
                    System.out.println("Errore! data non valida!!");
                    dateEditorTextField.setText(dateFormat.format(currentDate));
                    return;
                }
            }
            dateSelectionBox.getChildren().remove(2); // elimino il textEditor
            dateSelectionBox.getChildren().add(2, dateLabel);
            Reminder.getInstance().getSynchronize().set(true);
        }
    }
    
    private void setDayOfWeek(Calendar c){
        switch(c.get(Calendar.DAY_OF_WEEK)){
            case 1:
                dayOfWeekLabel.setText("Domenica");
                break;
            case 2:
                dayOfWeekLabel.setText("Lunedì");
                break;
            case 3:
                dayOfWeekLabel.setText("Martedì");
                break;
            case 4:
                dayOfWeekLabel.setText("Mercoledì");
                break;
            case 5:
                dayOfWeekLabel.setText("Giovedì");
                break;
            case 6:
                dayOfWeekLabel.setText("Venerdì");
                break;
            case 7:
                dayOfWeekLabel.setText("Sabato");
                break;
        }
    }
    
    public Date getCurrentDate(){
        return currentDate;
    }
}
