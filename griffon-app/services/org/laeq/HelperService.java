package org.laeq;

import griffon.core.artifact.GriffonService;
import griffon.metadata.ArtifactProviderFor;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonService;
import org.laeq.helper.GitRelease;
import org.laeq.helper.JsonBodyHandler;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.Duration;
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

    public static String fetchLatestRelease(String url) throws IOException, InterruptedException {
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(
                URI.create(url))
                .header("accept", "application/json")
                .build();


        var response = client.send(request, new JsonBodyHandler<>(GitRelease.class));
        return response.body().get().getName();
    }

    public static String formatDuration(Duration duration){
        return DurationFormatUtils.formatDuration((long) duration.toSeconds(), "HH:mm:ss");
    }

    public static Object formatDuration(javafx.util.Duration duration) {
        return DurationFormatUtils.formatDuration((long) duration.toSeconds(), "HH:mm:ss");
    }
}