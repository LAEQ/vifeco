package org.laeq.system;

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
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@ArtifactProviderFor(GriffonController.class)
public class StatusController extends AbstractGriffonController {
    @MVCMember @Nonnull private StatusModel model;
    @MVCMember @Nonnull private StatusView view;
    @Inject private MariaService dbService;
    @Inject private ExportService exportService;

    private String slackToken = "xoxp-521832980311-520675523571-611242433713-7b82ae9a38b8eba1e5a65ef7e15e5030";

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
            System.out.println("export: " + filename);
        }


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

        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        Date now = new Date(System.currentTimeMillis());

        String userDefault = "";
        try {
            userDefault = userDAO.findDefault().toString();
        } catch (Exception e) {
            userDefault = UUID.randomUUID().toString().substring(0, 8);
        }

        String zipFileName = String.format("%s-%s.zip", userDefault, formatter.format(now));

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




    private String getPathExport(String filename){
        return String.format("%s/%s", Settings.exportPath, filename);
    }

}