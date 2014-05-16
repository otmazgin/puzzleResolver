package assembler.templateMatcher;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import static org.opencv.imgproc.Imgproc.TM_CCORR_NORMED;
import static org.opencv.imgproc.Imgproc.matchTemplate;

public enum TemplateMatcher
{
    instance;

    public Match findBestMatch(Mat image, Mat template)
    {
        int result_cols = image.cols() - template.cols() + 1;
        int result_rows = image.rows() - template.rows() + 1;
        Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);

        matchTemplate(image, template, result, TM_CCORR_NORMED);

        return getBestMatch(result, template);
    }

    private Match getBestMatch(Mat result, Mat template)
    {
        Core.MinMaxLocResult minMaxLocResult = Core.minMaxLoc(result);

        return Match.of(minMaxLocResult.maxLoc, minMaxLocResult.maxVal, template.width(), template.height());
    }
}
