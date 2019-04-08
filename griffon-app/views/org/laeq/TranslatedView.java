package org.laeq;


import griffon.core.i18n.MessageSource;
import griffon.transform.Threading;
import javafx.scene.control.Labeled;
import javafx.scene.control.TableColumn;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.ui.DialogService;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TranslatedView extends AbstractJavaFXGriffonView {
    protected final Map<Labeled, String> textFields = new HashMap<>();
    protected final Map<TableColumn, String> columnsMap = new HashMap<>();
    @Inject private DialogService dialogService;

    @Override
    public void initUI() {

    }

    protected void translate(Labeled node, String key){
        node.setText(key);
    }

    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    public void alert(String key, String message){
        dialogService.simpleAlert(key, message);
    }
}
