package org.laeq;

import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.transform.Threading;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.collection.ContainerModel;
import org.laeq.collection.ContainerView;
import org.laeq.service.MariaService;
import org.laeq.ui.DialogService;

import javax.annotation.Nonnull;
import javax.inject.Inject;

public class CRUDController<T> extends AbstractGriffonController {
    @MVCMember @Nonnull protected ContainerModel model;
    @MVCMember @Nonnull protected ContainerView view;
    @Inject protected MariaService dbService;
    @Inject protected DialogService dialogService;

    protected void alert(String key, String alertMsg){
        runInsideUISync(() -> dialogService.simpleAlert(key, alertMsg));
    }

    protected Boolean confirm(String key){
        return dialogService.confirm((key));
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void clear(){
        model.clearForm();
    }
}
