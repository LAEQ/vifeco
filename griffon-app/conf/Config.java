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
                    .e("model", "org.laeq.VifecoModel")
                    .e("view", "org.laeq.VifecoView")
                    .e("controller", "org.laeq.VifecoController")
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
            );
    }
}