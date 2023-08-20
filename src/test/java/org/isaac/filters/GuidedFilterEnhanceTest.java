package org.isaac.filters;

import org.isaac.models.RemoveBackScatter;
import org.isaac.utils.ImgShow;
import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GuidedFilterEnhanceTest {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    @Test
    public void testGuidedFilterEnhance(){
        String directoryPath = "src/test/resources/dcp_images/enhancement";
        File directory = new File(directoryPath);
        File[] allFiles = directory.listFiles();

        Arrays.stream(allFiles).forEach(file -> applyFilter(file));

        File tuna = new File("src/test/resources/images/tuna/original.png");
        applyFilter(tuna);
    }

    private void applyFilter(File file){
        Mat image = Imgcodecs.imread(file.getAbsolutePath(), Imgcodecs.IMREAD_ANYCOLOR);
        image.convertTo(image, CvType.CV_32F);
        List<Mat> img = new ArrayList<>();
        Core.split(image, img);
        int r = 46;
        double eps = 0.25;

        Mat q_r = Filters.GuidedImageFilter(img.get(0), img.get(0), r, eps);
        Mat q_g = Filters.GuidedImageFilter(img.get(1), img.get(1), r, eps);
        Mat q_b = Filters.GuidedImageFilter(img.get(2), img.get(2), r, eps);

        Mat q = new Mat();
        Core.merge(new ArrayList<>(Arrays.asList(q_r, q_g, q_b)), q);
        q.convertTo(q, CvType.CV_8UC1);
        String destination = file.getAbsolutePath().replace(".", "_guided_filter_enhance.");

        new ImgShow("original").writeImage(q, destination);
    }
}
