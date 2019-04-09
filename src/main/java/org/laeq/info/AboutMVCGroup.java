package org.laeq.info;

import javax.inject.Named;
import griffon.core.mvc.MVCGroup;
import org.codehaus.griffon.runtime.core.mvc.AbstractTypedMVCGroup;
import javax.annotation.Nonnull;

@Named("about")
public class AboutMVCGroup extends AbstractTypedMVCGroup<AboutModel, AboutView, AboutController> {
    public AboutMVCGroup(@Nonnull MVCGroup delegate) {
        super(delegate);
    }
}