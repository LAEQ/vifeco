package org.laeq.statistic;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import javafx.util.Duration;
import org.apache.commons.io.FileUtils;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.TranslationService;
import org.laeq.db.CategoryDAO;
import org.laeq.db.PointDAO;
import org.laeq.db.VideoDAO;
import org.laeq.model.Category;
import org.laeq.model.Video;
import org.laeq.service.MariaService;
import org.laeq.service.statistic.StatisticException;
import org.laeq.service.statistic.StatisticService;
import org.laeq.settings.Settings;
import org.laeq.ui.DialogService;
import org.laeq.user.PreferencesService;
import org.laeq.video.ExportService;
import org.laeq.video.ImportService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.*;

@ArtifactProviderFor(GriffonController.class)
public class ContainerController extends AbstractGriffonController {
    @MVCMember @Nonnull private ContainerModel model;
    @MVCMember @Nonnull private ContainerView view;

    @Inject private MariaService dbService;
    @Inject private ImportService importService;
    @Inject private ExportService exportService;
    @Inject private PreferencesService preferenceService;
    @Inject private DialogService dialogService;

    private VideoDAO videoDAO;
    private PointDAO pointDAO;
    private CategoryDAO categoryDAO;
    private TranslationService translationService;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        videoDAO = dbService.getVideoDAO();
        pointDAO = dbService.getPointDAO();
        categoryDAO = dbService.getCategoryDAO();

        Set<Video> videos = videoDAO.findAll();

        videos.forEach(video -> {
            video.getPointSet().addAll(pointDAO.findByVideo(video));
            Set<Category> categorySet = categoryDAO.findByCollection(video.getCollection());
            video.getCollection().getCategorySet().addAll(categorySet);
        });

        model.setPrefs(preferenceService.getPreferences());
        model.addVideos(videos);
        view.init();

        getApplication().getEventRouter().addEventListener(listeners());
        setTranslationService();
    }

    @ControllerAction
    @Threading(Threading.Policy.OUTSIDE_UITHREAD)
    public void compare(){
        model.getVideos().parallelStream().forEach(video -> {
            Iterator it = FileUtils.iterateFiles(new File(Settings.imporPath), null, false);

            while (it.hasNext()){
                File file = (File) it.next();

                if(file.getName().contains(video.getName())){
                    try {

                        StatisticService statService = new StatisticService();

                        String content = FileUtils.readFileToString(file, "UTF-8");
                        Video importVideo = importService.execute(content);

                        String statFileName = String.format("%s%s%s-%s.json", Settings.statisticPath, File.separator, video.getName(), System.currentTimeMillis());

                        statService.setVideos(video, importVideo);
                        statService.setDurationStep(Duration.seconds(model.getDurationStep()));
                        statService.execute();

                        exportService.export(statService);

                    } catch (IOException | StatisticException e) {
                        getLog().error(e.getMessage());
                    }
                }
            }
        });

        runInsideUISync(() -> {
            dialogService.dialog(translationService.getMessage("org.laeq.statistic.compare.done"));
        });

    }

    private Map<String, RunnableWithArgs> listeners(){
        Map<String, RunnableWithArgs> list = new HashMap<>();
        list.put("change.language", objects -> {
            Locale locale = (Locale) objects[0];
            model.getPrefs().locale = locale;
            view.changeLocale();
        });

        return list;
    }

    public void savePreferences() {
        preferenceService.export(model.getPrefs());
    }

    private void setTranslationService(){
        try {
            translationService = new TranslationService(getClass().getClassLoader().getResourceAsStream("messages/messages.json"), model.getPrefs().locale);
        } catch (IOException e) {
            getLog().error("Cannot load file messages.json");
        }
    }
}