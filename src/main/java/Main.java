import org.opencv.core.*;
import utillities.Utilities;

import static org.opencv.imgproc.Imgproc.TM_CCORR_NORMED;
import static org.opencv.imgproc.Imgproc.matchTemplate;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat image = Utilities.readImage("/1.jpg");
        Mat template = Utilities.readImage("/lips.jpg");

        int result_cols = image.cols() - template.cols() + 1;
        int result_rows = image.rows() - template.rows() + 1;
        Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);

        matchTemplate(image, template, result, TM_CCORR_NORMED);

        Core.MinMaxLocResult minMaxLocResult = Core.minMaxLoc(result);

        Point maxLocation = minMaxLocResult.maxLoc;
        Utilities.drawRect(new Rect(maxLocation, new Size(template.cols(), template.rows())), image);

        Utilities.writeImageToFile(image, "match.jpg");
    }
}
