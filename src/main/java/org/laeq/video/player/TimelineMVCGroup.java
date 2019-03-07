package org.laeq.video.player;

import griffon.core.mvc.MVCGroup;
import org.codehaus.griffon.runtime.core.mvc.AbstractTypedMVCGroup;

import javax.annotation.Nonnull;
import javax.inject.Named;

@Named("timeline")
public class TimelineMVCGroup extends AbstractTypedMVCGroup<TimelineModel, TimelineView, TimelineController> {
    public TimelineMVCGroup(@Nonnull MVCGroup delegate) {
        super(delegate);
    }
}