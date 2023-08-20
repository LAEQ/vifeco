package org.isaac.filters;

import org.isaac.models.FusionEnhance;
import org.isaac.models.OptimizedContrastEnhance;
import org.isaac.utils.ImgShow;
import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.util.Arrays;

public class OptimizedContrastEnhanceTest {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private static final int blkSize = 100; // block size
    private static final int patchSize = 4; // patch size
    private static final double lambda = 5.0; // control the relative importance of contrast loss and information loss
    private static final double eps = 1e-8;
    private static final int krnlSize = 10;

    @Test
    public void testOptimizedContrastEnhanceTest(){
        String directoryPath = "src/test/resources/images/haze_images";
        File directory = new File(directoryPath);
        File[] allFiles = directory.listFiles();

        Arrays.stream(allFiles).forEach(file -> applyFilter(file));

        File tuna = new File("src/test/resources/images/tuna/original.png");
        applyFilter(tuna);
    }

    private void applyFilter(File file){
        Mat image = Imgcodecs.imread(file.getAbsolutePath(), Imgcodecs.IMREAD_ANYCOLOR);
        Mat result = OptimizedContrastEnhance.enhance(image, blkSize, patchSize, lambda, eps, krnlSize);
        result.convertTo(result, CvType.CV_8UC1);


        String destination = file.getAbsolutePath().replace(".", "optimized_contrast_enhance.");
        new ImgShow("original").writeImage(result, destination);
    }

}
