package org.laeq;

import javax.inject.Named;
import griffon.core.mvc.MVCGroup;
import org.codehaus.griffon.runtime.core.mvc.AbstractTypedMVCGroup;
import javax.annotation.Nonnull;

@Named("vifeco")
public class VifecoMVCGroup extends AbstractTypedMVCGroup<VifecoModel, VifecoView, VifecoController> {
    public VifecoMVCGroup(@Nonnull MVCGroup delegate) {
        super(delegate);
    }
}