package org.laeq;

import javax.inject.Named;
import griffon.core.mvc.MVCGroup;
import org.codehaus.griffon.runtime.core.mvc.AbstractTypedMVCGroup;
import javax.annotation.Nonnull;

@Named("test")
public class TestMVCGroup extends AbstractTypedMVCGroup<TestModel, TestView, TestController> {
    public TestMVCGroup(@Nonnull MVCGroup delegate) {
        super(delegate);
    }
}