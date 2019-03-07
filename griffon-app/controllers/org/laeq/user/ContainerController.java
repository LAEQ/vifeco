package org.laeq.user;

import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import org.laeq.CRUDController;
import org.laeq.db.DAOException;
import org.laeq.db.UserDAO;
import org.laeq.model.User;
import org.laeq.service.MariaService;
import org.laeq.ui.DialogService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.sql.SQLException;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class ContainerController extends CRUDController<User> {
    @MVCMember @Nonnull private ContainerModel model;
    @MVCMember @Nonnull private ContainerView view;

    @Inject private MariaService dbService;
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
            alert("key.to_implement", String.format("Some fields are invalid: \n%s", model.getErrors()));
        }
    }

    private void insertUser(User user) {
        try {
            userDAO.insert(user);
            model.addUser(user);
            model.clear();
        } catch (DAOException e) {
            String message = String.format("Cannot save user: \n%s", user);
            alert("key.to_implemetent", message);

            getLog().error(message);
        }
    }

    private void updateUser(User user) {
        try {
            userDAO.update(user);
            model.update(user);
            model.clear();
        } catch (DAOException | SQLException e) {
            String message = String.format("Cannot save user: \n%s", model.getUser());
            alert("key.to_implement", message);

            getLog().error(message);
        }
    }

    public void delete(User user) {
        Boolean confirmation = confirm("org.laeq.user.delete.confirmation");

        if(confirmation){
            try {
                userDAO.delete(user);
                model.delete(user);
            } catch (DAOException e) {
                String message = getMessage("org.laeq.delete.default_user.error");
                alert("key.to_implement", message);

                getLog().error(String.format("UserDAO: failed to delete %s.", user));
            }
        }
    }

    public void clear(){
        model.clear();
    }
}