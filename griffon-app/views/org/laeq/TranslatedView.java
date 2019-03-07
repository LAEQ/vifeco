package org.laeq;


import griffon.core.i18n.MessageSource;
import javafx.scene.control.Labeled;
import javafx.scene.control.TableColumn;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TranslatedView extends AbstractJavaFXGriffonView {
    protected Map<Labeled, String> textFields = new HashMap<>();
    protected Map<TableColumn, String> columnsMap = new HashMap<>();
    private MessageSource messageSource;

    @Override
    public void initUI() {

    }

    protected void translate(){
        textFields.entrySet().forEach(t -> {
            t.getKey().setText(getTranslation(t.getValue()));
        });

        columnsMap.entrySet().forEach( t -> {
            t.getKey().setText(getTranslation(t.getValue()));
        });
    }

    protected void translate(Labeled node, String key){
        node.setText(getTranslation(key));
    }

    private String getTranslation(String key){
        return getApplication().getMessageSource().getMessage(key, Locale.CANADA);
    }
}
