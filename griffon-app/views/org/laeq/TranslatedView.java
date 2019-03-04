package org.laeq;


import javafx.scene.control.Labeled;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TranslatedView extends AbstractJavaFXGriffonView {

    protected Map<Labeled, String> textFields = new HashMap<>();

    @Override
    public void initUI() {

    }

    protected void translate(){
        textFields.entrySet().forEach(t -> {
            t.getKey().setText(t.getValue());
        });
    }

    private String getText(String key){
        return getApplication().getMessageSource().getMessage(key, Locale.CANADA);
    }
}
