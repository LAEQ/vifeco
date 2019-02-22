package org.laeq.user;

import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;

import org.laeq.db.DAOException;
import org.laeq.db.DatabaseService;
import org.laeq.db.UserDAO;
import org.laeq.model.User;
import org.laeq.ui.DialogService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.sql.SQLException;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class ContainerController extends AbstractGriffonController {
    @MVCMember @Nonnull private ContainerModel model;
    @Inject private DatabaseService dbService;
    @Inject private DialogService dialogService;

    private UserDAO userDAO;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        userDAO = dbService.getUserDAO();
        model.getUserList().addAll(userDAO.findAll());
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void save(){
        if(model.valid()){
            User user = model.generateUser();
            if(model.getId() == 0) {
                insertUser(user);
            }else {
                updateUser(user);
            }
        } else {
            alert(String.format("Some fields are invalid: \n%s", model.getErrors()));
        }
    }

    private void insertUser(User user) {
        try {
            userDAO.insert(user);
            model.addUser(user);
            model.clear();
        } catch (DAOException e) {
            alert(String.format("Cannot save user: \n%s", user));
        }
    }

    private void updateUser(User user) {
        try {
            userDAO.update(user);
            model.update(user);
            model.clear();
        } catch (DAOException | SQLException e) {
            alert(String.format("Cannot save user: \n%s", model.getUser()));
        }
    }

    private void alert(String alertMsg){
        dialogService.simpleAlert(alertMsg);
    }
}