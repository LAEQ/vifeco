import griffon.util.AbstractMapResourceBundle;

import javax.annotation.Nonnull;
import java.util.Map;

import static java.util.Arrays.asList;
import static griffon.util.CollectionUtils.map;

public class Config extends AbstractMapResourceBundle {
    @Override
    protected void initialize(@Nonnull Map<String, Object> entries) {
        map(entries)
            .e("application", map()
                .e("title", "vifeco")
                .e("startupGroups", asList("vifeco"))
                .e("autoShutdown", true)
            )
            .e("mvcGroups", map()
                .e("vifeco", map()
                    .e("view", "org.laeq.VifecoView")
                )
                .e("menu", map()
                    .e("model", "org.laeq.MenuModel")
                    .e("view", "org.laeq.MenuView")
                    .e("controller", "org.laeq.MenuController")
                )
                .e("menu2", map()
                        .e("model", "org.laeq.SubMenuModel")
                        .e("view", "org.laeq.SubMenuView")
                        .e("controller", "org.laeq.SubMenuController")
                )
                .e("bottom", map()
                        .e("model", "org.laeq.BottomModel")
                        .e("view", "org.laeq.BottomView")
                        .e("controller", "org.laeq.BottomController")
                )
                .e("videoList", map()
                        .e("model", "org.laeq.video.VideoListModel")
                        .e("view", "org.laeq.video.VideoListView")
                        .e("controller", "org.laeq.video.VideoListController")
                )
                .e("player", map()
                        .e("model", "org.laeq.video.PlayerModel")
                        .e("view", "org.laeq.video.PlayerView")
                        .e("controller", "org.laeq.video.PlayerController")
                )
                .e("controls", map()
                        .e("model", "org.laeq.video.ControlsModel")
                        .e("view", "org.laeq.video.ControlsView")
                        .e("controller", "org.laeq.video.ControlsController")
                )
                .e("category", map()
                        .e("model", "org.laeq.video.CategoryModel")
                        .e("view", "org.laeq.video.category.CategoryView")
                        .e("controller", "org.laeq.video.CategoryController")
                )
            );
    }
}