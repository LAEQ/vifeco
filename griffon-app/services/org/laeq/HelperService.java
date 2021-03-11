package org.laeq;

import griffon.core.artifact.GriffonService;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@javax.inject.Singleton
@ArtifactProviderFor(GriffonService.class)
public class HelperService extends AbstractGriffonService {

    public static boolean validTimeString(String time){
        Pattern pattern = Pattern.compile("[0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}");
        Matcher matcher = pattern.matcher(time);

        if(matcher.find()){
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }


}