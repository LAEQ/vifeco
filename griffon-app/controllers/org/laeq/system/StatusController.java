package org.laeq.system;

import griffon.core.artifact.GriffonController;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.db.CollectionDAO;
import org.laeq.db.DAOException;
import org.laeq.db.UserDAO;
import org.laeq.model.Collection;
import org.laeq.model.User;
import org.laeq.service.MariaService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.sql.SQLException;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class StatusController extends AbstractGriffonController {
    @MVCMember @Nonnull private StatusModel model;
    @MVCMember @Nonnull private StatusView view;
    @Inject private MariaService dbService;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        try {
            dbService.getManager().getConnection();
            model.setConnectionStatus("OK");
        } catch (SQLException e) {
            model.setConnectionStatus("Connection failed");
        }

        try {
            dbService.getTableStatus();
            model.setTableStatus("OK");
        } catch (SQLException | DAOException e) {
            model.setTableStatus("Some table are missing");
        }

        try{
            User user= dbService.getUserDAO().findDefault();
            if(user == null){
                throw new DAOException("No active user");
            } else {
                model.setUserStatus(String.format("OK"));
            }
        } catch (DAOException | SQLException e) {
            model.setUserStatus("No default user found.");
        }

        try{
            Collection collection = dbService.getCollectionDAO().findDefault();
            if(collection == null){
                throw new DAOException("No default collection");
            } else{
                model.setCollectionStatus(String.format("OK"));
            }
        } catch (DAOException | SQLException e) {
            model.setCollectionStatus("No default collection found or must have at least one.");
        }
    }
}