import org.opencv.core.Core;
import org.opencv.core.Mat;
import utillities.Utilities;

import static org.opencv.imgproc.Imgproc.COLOR_BGR2GRAY;
import static org.opencv.imgproc.Imgproc.cvtColor;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat image = Utilities.readImage("/1.jpg");

        cvtColor(image, image, COLOR_BGR2GRAY);

        Utilities.writeImageToFile(image, "grayImage.png");
    }
}
