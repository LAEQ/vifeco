package org.laeq.info;

import griffon.core.mvc.MVCGroup;
import org.codehaus.griffon.runtime.core.mvc.AbstractTypedMVCGroup;

import javax.annotation.Nonnull;
import javax.inject.Named;

@Named("about")
public class AboutMVCGroup extends AbstractTypedMVCGroup<AboutModel, AboutView, AboutController> {
    public AboutMVCGroup(@Nonnull MVCGroup delegate) {
        super(delegate);
    }
}