package org.isaac.filters;

import org.isaac.models.ALTMRetinex;
import org.isaac.models.DarkChannelPriorDehaze;
import org.isaac.utils.ImgShow;
import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.util.Arrays;

public class DarkChannelPriorDehazeTest {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    // Paper url: https://www.robots.ox.ac.uk/~vgg/rg/papers/hazeremoval.pdf
    private static final double krnlRatio = 0.01; // set kernel ratio
    private static final double minAtmosLight = 240.0; // set minimum atmospheric light
    private static final double eps = 0.000001;


    @Test
    public void testDarkChannelPriorDehaze(){
        String directoryPath = "src/test/resources/images/haze_images";

        File directory = new File(directoryPath);
        File[] allFiles = directory.listFiles();

        Arrays.stream(allFiles).forEach(file -> applyFilter(file));

        File tuna = new File("src/test/resources/images/tuna/original.png");
        applyFilter(tuna);
    }

    private void applyFilter(File file){
        Mat image = Imgcodecs.imread(file.getAbsolutePath(), Imgcodecs.IMREAD_ANYCOLOR);
        Mat result = DarkChannelPriorDehaze.enhance(image, krnlRatio, minAtmosLight, eps);

        String destination = file.getAbsolutePath().replace(".png", "dark_channel_dehaze.png");
        new ImgShow("original").writeImage(result, destination);
    }
}
