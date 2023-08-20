package org.isaac.filters;

import org.isaac.models.FusionEnhance;
import org.isaac.models.RemoveBackScatter;
import org.isaac.utils.ImgShow;
import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.util.Arrays;

public class RemoveBackScatterTest {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private static final int blkSize = 10 * 10;
    private static final int patchSize = 8;
    private static final double lambda = 10;
    private static final double gamma = 1.7;
    private static final int r = 10;
    private static final double eps = 1e-6;
    private static final int level = 5;

    @Test
    public void testRemoveBackScatter(){
        String directoryPath = "src/test/resources/images/underwater_images";
        File directory = new File(directoryPath);
        File[] allFiles = directory.listFiles();

//        Arrays.stream(allFiles).forEach(file -> applyFilter(file));

        File tuna = new File("src/test/resources/images/tuna/original.png");
        applyFilter(tuna);
    }

    private void applyFilter(File file){
        Mat image = Imgcodecs.imread(file.getAbsolutePath(), Imgcodecs.IMREAD_ANYCOLOR);
        Mat fusion = RemoveBackScatter.enhance(image, blkSize, patchSize, lambda, gamma, r, eps, level);
        fusion.convertTo(fusion, CvType.CV_8UC1);

        String destination = file.getAbsolutePath().replace(".", "_back_scatter.");
        new ImgShow("original").writeImage(fusion, destination);
    }
}
