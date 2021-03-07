import griffon.util.AbstractMapResourceBundle;

import javax.annotation.Nonnull;
import java.util.Map;

import static griffon.util.CollectionUtils.map;
import static java.util.Arrays.asList;

public class Config extends AbstractMapResourceBundle {
    @Override
    protected void initialize(@Nonnull Map<String, Object> entries) {
        map(entries)
            .e("application", map()
                .e("title", "vifeco")
                .e("startupGroups", asList("vifeco"))
                .e("autoShutdown", true)
            ).e("windowManager", map()
            )
            .e("mvcGroups", map()
                .e("vifeco", map()
                    .e("controller", "org.laeq.VifecoController")
                    .e("model", "org.laeq.VifecoModel")
                    .e("view", "org.laeq.VifecoView")
                )
                .e("menu", map()
                    .e("model", "org.laeq.menu.MenuModel")
                    .e("view", "org.laeq.menu.MenuView")
                    .e("controller", "org.laeq.menu.MenuController")
                )
                .e("bottom", map()
                        .e("model", "org.laeq.menu.BottomModel")
                        .e("view", "org.laeq.menu.BottomView")
                        .e("controller", "org.laeq.menu.BottomController")
                )
                .e("user", map()
                        .e("model", "org.laeq.UserModel")
                        .e("view", "org.laeq.UserView")
                        .e("controller", "org.laeq.UserController")
                )
                .e("video", map()
                        .e("model", "org.laeq.VideoModel")
                        .e("view", "org.laeq.VideoView")
                        .e("controller", "org.laeq.VideoController")
                )
                .e("category", map()
                        .e("model", "org.laeq.CategoryModel")
                        .e("view", "org.laeq.CategoryView")
                        .e("controller", "org.laeq.CategoryController")
                )
                .e("collection", map()
                        .e("model", "org.laeq.CollectionModel")
                        .e("view", "org.laeq.CollectionView")
                        .e("controller", "org.laeq.CollectionController")
                )
                .e("statistic", map()
                        .e("model", "org.laeq.StatisticModel")
                        .e("view", "org.laeq.StatisticView")
                        .e("controller", "org.laeq.StatisticController")
                )
                .e("statistic_display", map()
                        .e("model", "org.laeq.statistic.DisplayModel")
                        .e("view", "org.laeq.statistic.DisplayView")
                        .e("controller", "org.laeq.statistic.DisplayController")
                )
                .e("about", map()
                        .e("model", "org.laeq.info.AboutModel")
                        .e("view", "org.laeq.info.AboutView")
                        .e("controller", "org.laeq.info.AboutController")
                )
                .e("import", map()
                        .e("model", "org.laeq.tools.ImportModel")
                        .e("view", "org.laeq.tools.ImportView")
                        .e("controller", "org.laeq.tools.ImportController")
                )
                .e("editor", map()
                        .e("model", "org.laeq.editor.PlayerModel")
                        .e("view", "org.laeq.editor.PlayerView")
                        .e("controller", "org.laeq.editor.PlayerController")
                )
                .e("display", map()
                        .e("model", "org.laeq.editor.DisplayModel")
                        .e("view", "org.laeq.editor.DisplayView")
                        .e("controller", "org.laeq.editor.DisplayController")
                )
                .e("controls", map()
                        .e("model", "org.laeq.editor.ControlsModel")
                        .e("view", "org.laeq.editor.ControlsView")
                        .e("controller", "org.laeq.editor.ControlsController")
                )
            );
    }
}