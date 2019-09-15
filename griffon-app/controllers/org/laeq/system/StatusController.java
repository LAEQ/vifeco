package org.laeq.system;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.db.*;
import org.laeq.model.Category;
import org.laeq.model.Collection;
import org.laeq.model.User;
import org.laeq.model.Video;
import org.laeq.service.MariaService;
import org.laeq.settings.Settings;
import org.laeq.video.ExportService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@ArtifactProviderFor(GriffonController.class)
public class StatusController extends AbstractGriffonController {
    @MVCMember @Nonnull private StatusModel model;
    @MVCMember @Nonnull private StatusView view;
    @Inject private MariaService dbService;
    @Inject private ExportService exportService;

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

        model.setVideoTotal(dbService.getVideoDAO().findAll().size());
        getApplication().getEventRouter().addEventListener(listeners());
    }


    private Map<String, RunnableWithArgs> listeners() {
        Map<String, RunnableWithArgs> list = new HashMap<>();

        list.put("change.language", objects -> {
            runInsideUISync(() -> view.translateText());
        });

        return list;
    }

    private String getPathExport(String filename){
        return String.format("%s/%s", Settings.exportPath, filename);
    }

}