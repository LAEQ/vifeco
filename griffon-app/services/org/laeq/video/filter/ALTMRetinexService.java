package org.laeq.video.filter;

import griffon.core.artifact.GriffonService;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonService;
import org.isaac.models.ALTMRetinex;
import org.isaac.utils.ImgShow;
import org.laeq.editor.filter.AltmRetinexControls;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.awt.image.BufferedImage;
import java.io.File;

@javax.inject.Singleton
@ArtifactProviderFor(GriffonService.class)
public class ALTMRetinexService extends AbstractGriffonService {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private static final int r = 10;
    private static final double eps = 0.01;
    private static final double eta = 36.0;
    private static final double lambda = 100.0;
    private static final double krnlRatio = 0.01;

    public BufferedImage applyFilter(File file){
        Mat image = Imgcodecs.imread(file.getAbsolutePath(), Imgcodecs.IMREAD_ANYCOLOR);
        Mat result = ALTMRetinex.enhance(image, r, eps, eta, lambda, krnlRatio);

        return new ImgShow("").toBufferedImage(result);
    }

    public void controls(AltmRetinexControls controls){
        ALTMRetinex.rParam = controls.rParam.doubleValue();
        ALTMRetinex.gParam = controls.gParam.doubleValue();
        ALTMRetinex.bParam = controls.bParam.doubleValue();
    }

}
