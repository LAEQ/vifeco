package org.laeq.video;

import javax.inject.Named;
import griffon.core.mvc.MVCGroup;
import org.codehaus.griffon.runtime.core.mvc.AbstractTypedMVCGroup;
import javax.annotation.Nonnull;

@Named("org.laeq.video-list")
public class VideoListMVCGroup extends AbstractTypedMVCGroup<VideoListModel, VideoListView, VideoListController> {
    public VideoListMVCGroup(@Nonnull MVCGroup delegate) {
        super(delegate);
    }
}