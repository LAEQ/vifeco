package org.isaac.filters;

import org.isaac.models.DarkChannelPriorDehaze;
import org.isaac.models.FusionEnhance;
import org.isaac.utils.ImgShow;
import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.util.Arrays;

public class FusionEnhanceTest {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private static final int level = 10;

    @Test
    public void testFusionEnhance(){
        String directoryPath = "src/test/resources/images/underwater_images";
        File directory = new File(directoryPath);
        File[] allFiles = directory.listFiles();

        Arrays.stream(allFiles).forEach(file -> applyFilter(file));

        File tuna = new File("src/test/resources/images/tuna/original.png");
        applyFilter(tuna);
    }

    private void applyFilter(File file){
        Mat image = Imgcodecs.imread(file.getAbsolutePath(), Imgcodecs.IMREAD_ANYCOLOR);
        Mat fusion = FusionEnhance.enhance(image, level);
        fusion.convertTo(fusion, CvType.CV_8UC1);

        String destination = file.getAbsolutePath().replace(".", "fusion_enhance.");
        new ImgShow("original").writeImage(fusion, destination);
    }
}
