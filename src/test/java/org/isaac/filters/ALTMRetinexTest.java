package org.isaac.filters;

import org.isaac.models.ALTMRetinex;
import org.isaac.utils.ImgShow;
import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.util.Arrays;

public class ALTMRetinexTest {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    // Local Adaptation Parameters
    private static final int r = 10;
    private static final double eps = 0.01;
    private static final double eta = 36.0;
    private static final double lambda = 100.0;
    private static final double krnlRatio = 0.01;

    @Test
    public void testRetinex(){
        String directoryPath = "src/test/resources/images/dark_images";

        File directory = new File(directoryPath);
        File[] allFiles = directory.listFiles();

        Arrays.stream(allFiles).forEach(file -> applyFilter(file));

        File tuna = new File("src/test/resources/images/tuna/original.png");
        applyFilter(tuna);
    }

    private void applyFilter(File file){
        Mat image = Imgcodecs.imread(file.getAbsolutePath(), Imgcodecs.IMREAD_ANYCOLOR);
        Mat result = ALTMRetinex.enhance(image, r, eps, eta, lambda, krnlRatio);

        String destination = file.getAbsolutePath().replace(".png", "altm_retinex.png");
        new ImgShow("original").writeImage(result, destination);
    }
}
