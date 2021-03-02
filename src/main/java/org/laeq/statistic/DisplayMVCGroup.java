package org.laeq.statistic;

import griffon.core.mvc.MVCGroup;
import org.codehaus.griffon.runtime.core.mvc.AbstractTypedMVCGroup;
import org.laeq.StatisticController;
import org.laeq.StatisticModel;
import org.laeq.StatisticView;

import javax.annotation.Nonnull;
import javax.inject.Named;

@Named("display")
public class DisplayMVCGroup extends AbstractTypedMVCGroup<StatisticModel, StatisticView, StatisticController> {
    public DisplayMVCGroup(@Nonnull MVCGroup delegate) {
        super(delegate);
    }
}