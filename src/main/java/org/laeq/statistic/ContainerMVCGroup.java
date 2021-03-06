package org.laeq.statistic;

import griffon.core.mvc.MVCGroup;
import org.codehaus.griffon.runtime.core.mvc.AbstractTypedMVCGroup;
import org.laeq.StatisticController;
import org.laeq.StatisticModel;
import org.laeq.StatisticView;

import javax.annotation.Nonnull;
import javax.inject.Named;

@Named("container")
public class ContainerMVCGroup extends AbstractTypedMVCGroup<StatisticModel, StatisticView, StatisticController> {
    public ContainerMVCGroup(@Nonnull MVCGroup delegate) {
        super(delegate);
    }
}