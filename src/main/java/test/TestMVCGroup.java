package test;

import griffon.core.mvc.MVCGroup;
import org.codehaus.griffon.runtime.core.mvc.AbstractTypedMVCGroup;

import javax.annotation.Nonnull;
import javax.inject.Named;

@Named("test")
public class TestMVCGroup extends AbstractTypedMVCGroup<TestModel, TestView, TestController> {
    public TestMVCGroup(@Nonnull MVCGroup delegate) {
        super(delegate);
    }
}