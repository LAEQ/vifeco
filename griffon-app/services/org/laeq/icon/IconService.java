package org.laeq.icon;

import griffon.core.artifact.GriffonService;
import griffon.metadata.ArtifactProviderFor;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonService;
import org.laeq.model.Category;
import org.laeq.model.Icon;
import org.laeq.settings.Settings;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@javax.inject.Singleton
@ArtifactProviderFor(GriffonService.class)
public class IconService extends AbstractGriffonService {
    private int width = 100;
    private int height = 100;
    private float opacity = 0.65f;
    private int duration = 10;
    private int size = 100;
    private String fillColor = "#EEEEEE";
    private String defaultPath = "icons/iconmonstr-help-3-64.png";

    private final String[] icons = new String[]{
            "icons/truck-mvt-blk-64.png",
            "icons/truck-mvt-red-64.png",
            "icons/icon-bicycle-mvt-64.png",
            "icons/icon-car-co2-black-64.png",
            "icons/icon-constr-black-64.png",
            "icons/iconmonstr-car-23-64.png",
            "icons/icon-car-elec-blk-64.png",
    };

    public void createPNG(Category category){
        String svgFolder = Settings.svgPath;

        try {
            JPEGTranscoder t = new JPEGTranscoder();
            t.addTranscodingHint(JPEGTranscoder.KEY_QUALITY,  new Float(.8));

            PNGTranscoder transcoder = new PNGTranscoder();
            transcoder.addTranscodingHint(PNGTranscoder.KEY_WIDTH, 80f);
            transcoder.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, 80f);

            TranscoderInput input = new TranscoderInput(buildDocument(category));

            String exportPath = String.format("%s/%s.png", svgFolder, category.getName().toLowerCase().replace(" ", "_"));
            File newFile = new File(exportPath);
            OutputStream ostream = new FileOutputStream(newFile);
            TranscoderOutput output = new TranscoderOutput(ostream);
            transcoder.transcode(input, output);
            ostream.flush();
            ostream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public IconService(){
        String svgFolder = Settings.svgPath;
        Path path = Paths.get(svgFolder);

        if(!Files.exists(path)){
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                getLog().error(e.getMessage());
            }
        }

        try {
            JPEGTranscoder t = new JPEGTranscoder();
            t.addTranscodingHint(JPEGTranscoder.KEY_QUALITY,  new Float(.8));

            PNGTranscoder transcoder = new PNGTranscoder();
            transcoder.addTranscodingHint(PNGTranscoder.KEY_WIDTH, 80f);
            transcoder.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, 80f);

            TranscoderInput input = new TranscoderInput(buildDocument());

            String exportPath = String.format("%s/image.png", svgFolder);
            File newFile = new File(exportPath);
            OutputStream ostream = new FileOutputStream(newFile);
            TranscoderOutput output = new TranscoderOutput(ostream);
            transcoder.transcode(input, output);
            ostream.flush();
            ostream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Document buildDocument(Category category) {
        final DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        final String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
        final Document doc = impl.createDocument(svgNS, "svg", null);

        final Element svgRoot = doc.getDocumentElement();

        final Element element = doc.createElementNS(svgNS, "path");
        element.setAttribute("d", category.getIcon());

        element.setAttribute("style", "fill:black;");
        element.setAttribute("transform", "translate(7 , 8)");
        svgRoot.appendChild(circle(doc,40, 40, 40, "lightgray" ));
        svgRoot.appendChild(element);

        return doc;
    }

    private Document buildDocument() {
        final DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        final String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
        final Document doc = impl.createDocument(svgNS, "svg", null);

        final Element svgRoot = doc.getDocumentElement();

        final Element element = doc.createElementNS(svgNS, "path");
        element.setAttribute("d", "M 12.5 27.5 L 12.5 30 L 32.5 30 L 32.5 12.5 L 7.5 12.5 L 7.5 10 C 7.5 8.621094 8.621094 7.5 10 7.5 L 35 7.5 C 36.378906 7.5 37.5 8.621094 37.5 10 L 37.5 15 L 49.167969 15 C 51.960938 15 53.167969 16.441406 54.007812 17.765625 C 55.492188 20.117188 57.847656 23.847656 59.28125 26.210938 C 59.75 26.992188 60 27.886719 60 28.800781 L 60 40 C 60 42.722656 58.140625 45 55 45 L 52.5 45 C 52.5 49.140625 49.140625 52.5 45 52.5 C 40.859375 52.5 37.5 49.140625 37.5 45 L 27.5 45 C 27.5 49.140625 24.140625 52.5 20 52.5 C 15.859375 52.5 12.5 49.140625 12.5 45 L 10 45 C 8.621094 45 7.5 43.878906 7.5 42.5 L 7.5 27.5 L 2.5 27.5 L 2.5 22.5 L 20 22.5 L 20 27.5 Z M 20 42 C 21.65625 42 23 43.34375 23 45 C 23 46.65625 21.65625 48 20 48 C 18.34375 48 17 46.65625 17 45 C 17 43.34375 18.34375 42 20 42 Z M 45 42 C 46.65625 42 48 43.34375 48 45 C 48 46.65625 46.65625 48 45 48 C 43.34375 48 42 46.65625 42 45 C 42 43.34375 43.34375 42 45 42 Z M 37.5 35 L 12.5 35 L 12.5 40 L 14.414062 40 C 15.785156 38.464844 17.78125 37.5 20 37.5 C 22.21875 37.5 24.214844 38.464844 25.585938 40 L 39.414062 40 C 40.785156 38.464844 42.78125 37.5 45 37.5 C 47.21875 37.5 49.214844 38.464844 50.585938 40 L 55 40 L 55 28.5625 L 50.722656 21.238281 C 50.273438 20.472656 49.453125 20 48.566406 20 L 37.5 20 Z M 40 22.5 L 40 30 L 52.5 30 L 48.933594 23.761719 C 48.488281 22.980469 47.660156 22.5 46.761719 22.5 Z M 0 15 L 20 15 L 20 20 L 0 20 Z M 0 15 ");

        element.setAttribute("style", "fill:black;");
        element.setAttribute("transform", "translate(7 , 8)");
        svgRoot.appendChild(circle(doc,40, 40, 40, "lightgray" ));
        svgRoot.appendChild(element);

        return doc;
    }

    public Element circle(Document doc, double cx,double cy, double radius, String color) {
        Element circle = doc.createElementNS(SVGDOMImplementation.SVG_NAMESPACE_URI, "circle");
        circle.setAttributeNS(null, "cx", q(cx));
        circle.setAttributeNS(null, "cy", q(cy));
        circle.setAttributeNS(null, "r", q(radius));
        circle.setAttributeNS(null, "style", "fill:"+color);
        return circle;
    }

    private static String q(double i) {
        return Double.toString(i);
    }

    public Icon generateIcon(int rand)  {
        return generateIcon(new Category("", icons[rand], "#000000", ""), this.size, this.opacity);
    }

    public Icon generateIcon(Category category) {
      return generateIcon(category, this.size, this.opacity);
    }

    public Icon generateIcon(Category category, int size, double opacity) {
        return new Icon(category, size);
    }
}
