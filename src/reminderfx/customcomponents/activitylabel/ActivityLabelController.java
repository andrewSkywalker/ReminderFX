/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reminderfx.customcomponents.activitylabel;

import java.io.IOException;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import reminderfx.model.Activity;
import reminderfx.model.Reminder;
import reminderfx.model.UrgencyLevel;

/**
 * FXML Controller class
 *
 * @author tengi12
 */
public class ActivityLabelController extends HBox {
    
    private final Activity activity;
    @FXML Label descriptionLabel;
    @FXML ImageView doneImageView;
    @FXML HBox activityLabel;
    @FXML TextField descriptionEditorTextField;
    
    private final Image doneImage = new Image(getClass().getResource("check-done.png").toString());
    private final Image todoImage = new Image(getClass().getResource("check-todo.png").toString());
    private final SimpleBooleanProperty isEditing;
    
    private final int orderNumber;
    
    /**
     * Costruttore. Il parametro e' necessario, perché questa label è fatta per
     * mostrare una {@link Activity}.
     * @param a l'attività da renderizzare
     * @param orderNumber il numero d'ordine per identificare la label da eliminare
     * in caso di un'eventuale elminazione di attività
     */
    public ActivityLabelController(Activity a, int orderNumber){
        //----------------------------------------------------------------------
        //
        //  Attività preliminari
        //
        //----------------------------------------------------------------------
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ActivityLabel.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        this.orderNumber = orderNumber;

        //----------------------------------------------------------------------
        //
        //  Caricamento elemento FXML
        //
        //----------------------------------------------------------------------
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        
        //----------------------------------------------------------------------
        //
        //  Settaggio attività e collegamento con i listeners
        //
        //----------------------------------------------------------------------
        this.activity = a;
        a.getIsCompleteProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                somethingChanged();
            }
        });
        
        a.getUrgencyLevelString().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                somethingChanged();
            }
        });
        
        a.getDescriptionProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                descriptionLabel.setText(t1);
            }
        });
        
        //----------------------------------------------------------------------
        //
        //  Ultimi settaggi
        //
        //----------------------------------------------------------------------
        descriptionLabel.setText(activity.getDescription());
        isEditing = new SimpleBooleanProperty();
        isEditing.set(false);
        isEditing.addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                if(t1){
                    descriptionEditorTextField.setText(activity.getDescription());
                    getChildren().clear();
                    getChildren().add(descriptionEditorTextField);
                    descriptionEditorTextField.requestFocus();
                }
                else{
                    onEditFinished();
                }
            }
        });
        somethingChanged();
        
        //----------------------------------------------------------------------
        //
        //  Si toglie dai figli il text editor, mentre si lascia la label
        //
        //----------------------------------------------------------------------
        getChildren().remove(0);
        descriptionLabel.setText(activity.getDescription());
    }
    
    @FXML private void onMouseEntered(MouseEvent e){
        doneImageView.setVisible(true);
        e.consume();
    }
    
    @FXML private void onMouseExited(MouseEvent e){
        if(!activity.isCompleted()){
            doneImageView.setVisible(false);
        }
        e.consume();
    }
    
    @FXML private void activityDone(MouseEvent e){
        if(e.getButton() == MouseButton.PRIMARY){
            if(activity.isCompleted()){
                activity.setCompleted(false);
            }
            else{
                activity.setCompleted(true);
            }
            e.consume();
        }
    }
    
    @FXML private void setNonUrgent(){
        activity.setUrgencyLevel(UrgencyLevel.NONE);
    }
    
    @FXML private void setLittleUrgent(){
        activity.setUrgencyLevel(UrgencyLevel.LITTLE);
    }
    
    @FXML private void setMediumUrgent(){
        activity.setUrgencyLevel(UrgencyLevel.MEDIUM);
    }
    
    @FXML private void setGreatUrgent(){
        activity.setUrgencyLevel(UrgencyLevel.GREAT);
    }
    
    @FXML private void setMaximumUrgent(){
        activity.setUrgencyLevel(UrgencyLevel.MAXIMUM);
    }
    
    @FXML private void onEditDescriptionRequested(){
        isEditing.set(true);
    }
    
    @FXML private void onKeyTyped(KeyEvent e){
        if(e.getCharacter().equals("\r") || e.getCharacter().equals("\n")){
            isEditing.set(false);
        }
    }
    
    private void onEditFinished(){
        if(descriptionEditorTextField.getText().length() != 0){
            activity.setDescription(descriptionEditorTextField.getText());
        }
        getChildren().clear();
        getChildren().add(descriptionLabel);
        getChildren().add(doneImageView);
        somethingChanged();
    }
    
    /**
     * Questo metodo gestisce il cambiamento delle proprietà della label, sia che
     * si cambi la proprietà completed, che quelle sull'urgenza.
     */
    private void somethingChanged(){
        if (activity.isCompleted()) {
            activityLabel.getStyleClass().clear();
            activityLabel.getStyleClass().add("activityLabelDone");
            descriptionLabel.setTextFill(Color.GREY);
            doneImageView.setImage(doneImage);
            doneImageView.setVisible(true);
        } else {
            doneImageView.setImage(todoImage);
            activityLabel.getStyleClass().clear();
            descriptionLabel.setTextFill(Color.BLACK);
            switch (activity.getUrgencyLevel()) {
                case NONE:
                    activityLabel.getStyleClass().add("activityLabelUrgencyNone");
                    break;
                case LITTLE:
                    activityLabel.getStyleClass().add("activityLabelUrgencyLittle");
                    break;
                case MEDIUM:
                    activityLabel.getStyleClass().add("activityLabelUrgencyMedium");
                    break;
                case GREAT:
                    activityLabel.getStyleClass().add("activityLabelUrgencyGreat");
                    break;
                case MAXIMUM:
                    activityLabel.getStyleClass().add("activityLabelUrgencyMaximum");
                    break;
            }
        }
    }
    
    public void setIsEditing(boolean value){
        isEditing.set(value);
        descriptionEditorTextField.requestFocus();
    }
    
    @FXML public void onDeleteActivity(){
        Reminder.getInstance().deleteActivity(orderNumber);
    }
}
