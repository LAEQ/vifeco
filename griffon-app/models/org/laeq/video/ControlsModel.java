package org.laeq.video;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonModel;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Observable;
import javafx.beans.property.*;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

@ArtifactProviderFor(GriffonModel.class)
public class ControlsModel extends AbstractGriffonModel {
    private SimpleStringProperty rate;

    public ControlsModel(){
        this.rate = new SimpleStringProperty(this, "rate", "1");
    }

    @Nonnull
    public final SimpleStringProperty rateProperty() {return rate;}
    public String getRate() {return rate.get(); }
    public void setRate(String rate) {this.rate.set(rate); }

    public void increaseRate() {
        getLog().info("Increase rate");
//        if(getRate() < 10){
//            setRate(rate.add(0.1).doubleValue());
//        }
    }

    public void decreateRate() {
        getLog().info("Increase rate");
//        if(getRate() > 0.1){
//            this.setRate(getRate() - 0.1);
//        }
    }
}