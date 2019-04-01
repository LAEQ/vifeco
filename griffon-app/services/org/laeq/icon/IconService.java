package org.laeq.icon;

import griffon.core.artifact.GriffonService;
import griffon.metadata.ArtifactProviderFor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.transcoder.TranscoderException;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


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

    public IconService(){
        String svgPath = Settings.svgPath;

        Path path = Paths.get(svgPath);

        if(!Files.exists(path)){
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                getLog().error(e.getMessage());
            }
        }
    }

    public void createPNG(Category category) throws IOException, TranscoderException {
        JPEGTranscoder t = new JPEGTranscoder();
        t.addTranscodingHint(JPEGTranscoder.KEY_QUALITY,  new Float(.8));

        PNGTranscoder transcoder = new PNGTranscoder();
        transcoder.addTranscodingHint(PNGTranscoder.KEY_WIDTH, 80f);
        transcoder.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, 80f);

        TranscoderInput input = new TranscoderInput(buildDocument(category, 40,2.2, category.getColor(), "white"));
        String exportPath = getImagePath(category, "_off");
        File newFile = new File(exportPath);
        OutputStream ostream = new FileOutputStream(newFile);
        TranscoderOutput output = new TranscoderOutput(ostream);
        transcoder.transcode(input, output);
        ostream.flush();
        ostream.close();

        input = new TranscoderInput(buildDocument(category, 40,2.2, "white", category.getColor()));
        exportPath = getImagePath(category, "_on");
        newFile = new File(exportPath);
        ostream = new FileOutputStream(newFile);
        output = new TranscoderOutput(ostream);
        transcoder.transcode(input, output);
        ostream.flush();
        ostream.close();

        transcoder = new PNGTranscoder();
        transcoder.addTranscodingHint(PNGTranscoder.KEY_WIDTH, 20f);
        transcoder.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, 20f);

        input = new TranscoderInput(buildDocument(category, 10, category.getColor(), "white"));
        exportPath = getImagePath(category, "_small_off");
        newFile = new File(exportPath);
        ostream = new FileOutputStream(newFile);
        output = new TranscoderOutput(ostream);
        transcoder.transcode(input, output);
        ostream.flush();
        ostream.close();

        input = new TranscoderInput(buildDocument(category, 10, "white", category.getColor()));
        exportPath = getImagePath(category, "_small_on");
        newFile = new File(exportPath);
        ostream = new FileOutputStream(newFile);
        output = new TranscoderOutput(ostream);
        transcoder.transcode(input, output);
        ostream.flush();
        ostream.close();
    }

    public String getImagePath(Category category, String mode){
        return String.format("%s%s%s%s.png", Settings.svgPath, File.separator, category.getName().toLowerCase().replace(" ", "_"), mode);
    }

    public Image[] getImageView(Category category) throws IOException, TranscoderException {
        String imagePath = getImagePath(category, "_off");
        File file = new File(getImagePath(category, "_off"));
        Image image;
        ImageView imageView;

        if(! file.exists()){
            createPNG(category);
        }

        Image[] result = new Image[4];

        file = new File(getImagePath(category, "_off"));
        image = new Image(file.getCanonicalFile().toURI().toString());
        result[0] = image;

        file = new File(getImagePath(category, "_on"));
        image = new Image(file.getCanonicalFile().toURI().toString());
        result[1] = image;

        file = new File(getImagePath(category, "_small_off"));
        image = new Image(file.getCanonicalFile().toURI().toString());
        result[2] = image;

        file = new File(getImagePath(category, "_small_on"));
        image = new Image(file.getCanonicalFile().toURI().toString());
        result[3] = image;

        return result;
    }

    public Map<Category, Image[]> getImageViews(Set<Category> categorySet) throws IOException, TranscoderException {
        Map<Category, Image[]> result = new HashMap<>();

        for (Category category: categorySet) {
            result.put(category, getImageView(category));
        }

        return result;
    }

    private Document buildDocument(Category category, int width, double scale, String color1, String color2) {
        final DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        final String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
        final Document doc = impl.createDocument(svgNS, "svg", null);

        final Element svgRoot = doc.getDocumentElement();

        final Element element = doc.createElementNS(svgNS, "path");
        element.setAttribute("d", category.getIcon());

        element.setAttribute("style", "fill:" + color1 + ";");
        element.setAttribute("transform", "translate(11 , 11) scale( " + scale + " , " + scale + " )");
        svgRoot.appendChild(circle(doc ,width, width, width, color2 ));
        svgRoot.appendChild(element);

        return doc;
    }

    private Document buildDocument(Category category, int width, String color1, String color2) {
        final DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        final String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
        final Document doc = impl.createDocument(svgNS, "svg", null);

        final Element svgRoot = doc.getDocumentElement();

        final Element element = doc.createElementNS(svgNS, "path");
        element.setAttribute("d", category.getIcon());

        element.setAttribute("style", "fill:" + color1 + ";");
        element.setAttribute("transform", "translate(1.6 , 1.6) scale(0.7, 0.7)");
        svgRoot.appendChild(circle(doc, width, width, width, color2));
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
