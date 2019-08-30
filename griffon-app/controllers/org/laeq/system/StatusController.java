package org.laeq.system;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.TranslationService;
import org.laeq.db.*;
import org.laeq.model.Category;
import org.laeq.model.Collection;
import org.laeq.model.User;
import org.laeq.model.Video;
import org.laeq.service.MariaService;
import org.laeq.settings.Settings;
import org.laeq.ui.DialogService;
import org.laeq.user.PreferencesService;
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
    @Inject private DialogService dialogService;
    @Inject private PreferencesService preferencesService;


    private TranslationService translationService;

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
        setTranslationService();
    }


    private Map<String, RunnableWithArgs> listeners() {
        Map<String, RunnableWithArgs> list = new HashMap<>();

        list.put("change.language", objects -> {
            runInsideUISync(() -> view.translateText());
        });

        return list;
    }

    @ControllerAction
    @Threading(Threading.Policy.OUTSIDE_UITHREAD)
    public void export(){
        VideoDAO videoDAO = dbService.getVideoDAO();
        CategoryDAO categoryDAO = dbService.getCategoryDAO();
        PointDAO pointDAO = dbService.getPointDAO();
        Set<Video> videoList = videoDAO.findAll();

        for(Video video : videoList){
            Set<Category> categories = categoryDAO.findByCollection(video.getCollection());
            video.getCollection().getCategorySet().addAll(categories);

            video.getPointSet().addAll(pointDAO.findByVideo(video));

            String filename = exportService.export(video);
        }

        alert(translationService.getMessage("org.laeq.model.invalid_fields"));
    }


    private void alert(String alertMsg){
        dialogService.simpleAlert(translationService.getMessage("org.laeq.title.error") ,alertMsg);
    }

    @ControllerAction
    @Threading(Threading.Policy.OUTSIDE_UITHREAD)
    public void archive() {
        VideoDAO videoDAO = dbService.getVideoDAO();
        CategoryDAO categoryDAO = dbService.getCategoryDAO();
        UserDAO userDAO = dbService.getUserDAO();
        PointDAO pointDAO = dbService.getPointDAO();

        Set<Video> videoList = videoDAO.findAll();

        List<String> srcFiles = new ArrayList<>();

        for(Video video : videoList){
            Set<Category> categories = categoryDAO.findByCollection(video.getCollection());
            video.getCollection().getCategorySet().addAll(categories);

            video.getPointSet().addAll(pointDAO.findByVideo(video));

            String fileName = exportService.export(video);
            srcFiles.add(fileName);
        }

        String userDefault = "";
        try {
            userDefault = userDAO.findDefault().toString();
        } catch (Exception e) {
            userDefault = UUID.randomUUID().toString().substring(0, 8);
        }

        String zipFileName = String.format("%s-%s.zip", userDefault, System.currentTimeMillis());

        try {
            FileOutputStream fos = new FileOutputStream(getPathExport(zipFileName));
            ZipOutputStream zipOut = new ZipOutputStream(fos);
            for (String srcFile : srcFiles) {
                File fileToZip = new File(srcFile);
                FileInputStream fis = new FileInputStream(fileToZip);
                ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
                zipOut.putNextEntry(zipEntry);

                byte[] bytes = new byte[1024];
                int length;
                while ((length = fis.read(bytes)) >= 0) {
                    zipOut.write(bytes, 0, length);
                }
                fis.close();
            }

            zipOut.close();
            fos.close();
        } catch (IOException e ){
            getLog().error(e.getMessage());
        } finally {
            for(String srcFile : srcFiles){
                File file = new File(srcFile);
                file.delete();
            }
        }
    }

    private String getPathExport(String filename) {
        return String.format("%s/%s", Settings.exportPath, filename);
    }

    private void setTranslationService(){
        try {
            translationService = new TranslationService(getClass().getClassLoader().getResourceAsStream("messages/messages.json"), preferencesService.getPreferences().locale);
        } catch (IOException e) {
            getLog().error("Cannot load file messages.json");
        }
    }

}