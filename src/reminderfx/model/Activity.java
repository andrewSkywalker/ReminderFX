/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reminderfx.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author tengi12
 */
public class Activity implements Serializable{
    private final SimpleStringProperty description;
    private Date insertDate;
    private Date endDate;
    private final SimpleBooleanProperty completed;
    private final SimpleStringProperty urgencyLevelString;
    private UrgencyLevel urgencyLevel;

    public Activity() {
        Calendar c = Calendar.getInstance();
        description = new SimpleStringProperty();
        insertDate = c.getTime();
        c.add(Calendar.DAY_OF_MONTH, 1);
        endDate = c.getTime();
        completed = new SimpleBooleanProperty(false);
        urgencyLevel = UrgencyLevel.MEDIUM;
        urgencyLevelString = new SimpleStringProperty(urgencyLevel.name());
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isCompleted() {
        return completed.get();
    }

    public void setCompleted(boolean completed) {
        this.completed.set(completed);
    }

    public UrgencyLevel getUrgencyLevel() {
        return urgencyLevel;
    }

    public void setUrgencyLevel(UrgencyLevel urgencyLevel) {
        this.urgencyLevel = urgencyLevel;
        urgencyLevelString.setValue(urgencyLevel.name());
    }
    
    public SimpleStringProperty getUrgencyLevelString(){
        return urgencyLevelString;
    }
    
    public SimpleStringProperty getDescriptionProperty(){
        return description;
    }
    
    public SimpleBooleanProperty getIsCompleteProperty(){
        return completed;
    }
}
