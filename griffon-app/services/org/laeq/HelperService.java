package org.laeq;

import griffon.core.artifact.GriffonService;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonService;
import org.laeq.helper.GitRelease;
import org.laeq.helper.JsonBodyHandler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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

}