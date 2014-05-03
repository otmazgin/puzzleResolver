package assembler;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import utillities.Optional;

import static org.opencv.imgproc.Imgproc.TM_CCORR_NORMED;
import static org.opencv.imgproc.Imgproc.matchTemplate;

enum TemplateMatcher
{
    instance;

    Optional<Point> findBestMatching(Mat image, Mat template)
    {
        int result_cols = image.cols() - template.cols() + 1;
        int result_rows = image.rows() - template.rows() + 1;
        Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);

        matchTemplate(image, template, result, TM_CCORR_NORMED);

        return getMatchingPoint(result);
    }

    private Optional<Point> getMatchingPoint(Mat result)
    {
        Core.MinMaxLocResult minMaxLocResult = Core.minMaxLoc(result);

        if (minMaxLocResult.maxVal > 0.999)
        {
            return Optional.of(minMaxLocResult.maxLoc);
        }

        return Optional.absent();
    }
}
