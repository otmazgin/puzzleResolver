package assembler.templateMatcher;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.imgproc.Imgproc.*;

public enum FastTemplateMatcher
{
    instance;

    public Match findBestMatch(Mat source, Mat template, int maxLevel)
    {
        List<Mat> references;
        List<Mat> tuples;
        List<Mat> results = new ArrayList<>();

        // Build Gaussian pyramid
        references = buildPyramid(source, maxLevel);
        tuples = buildPyramid(template, maxLevel);

        Mat reference;
        Mat tuple;
        Mat result = new Mat();

        // Process each level
        for (int level = maxLevel; level >= 0; level--)
        {
            reference = references.get(level);
            tuple = tuples.get(level);
            result = new Mat(reference.height() - 1 + tuple.height(), reference.width() - 1 + tuple.width(), CvType.CV_32FC1);

            if (level == maxLevel)
            {
                // On the smallest level, just perform regular template matching
                matchTemplate(reference, tuple, result, Imgproc.TM_CCORR_NORMED);
            } else
            {
                // On the next layers, template matching is performed on pre-defined
                // ROI areas.  We define the ROI using the template matching result
                // from the previous layer.

                Mat mask = new Mat();
                pyrUp(results.get(results.size() - 1), mask);

                Mat mask8u = new Mat();
                mask.convertTo(mask8u, CvType.CV_8U);

                // Find matches from previous layer
                List<MatOfPoint> contours = new ArrayList<>();
                Mat hierarchy = new Mat();
                findContours(mask8u, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);

                // Use the contours to define region of interest and
                // perform template matching on the areas
                for (MatOfPoint contour : contours)
                {
                    Rect r = boundingRect(contour);
                    matchTemplate
                            (
                                    reference.submat(r.height, r.height + tuple.height() - 1, r.width, r.width + tuple.width() - 1),
                                    tuple,
                                    result.submat(r),
                                    TM_CCORR_NORMED
                            );
                }
            }

            // Only keep good matches
            threshold(result, result, 0.94, 1., THRESH_TOZERO);
            results.add(result);
        }

        return extractBestMatch(result, template);
    }

    private List<Mat> buildPyramid(Mat source, int maxLevel)
    {
        List<Mat> destination = new ArrayList<>(maxLevel + 1);
        destination.add(0, source);

        Mat nextLayer;
        for (int i = 1; i <= maxLevel; i++)
        {
            Mat previousLayer = destination.get(i - 1);

            nextLayer = new Mat(previousLayer.height() / 2, previousLayer.width() / 2, previousLayer.type());

            pyrDown(previousLayer, nextLayer);

            destination.add(i, nextLayer);
        }

        return destination;
    }

    private Match extractBestMatch(Mat result, Mat template)
    {
        Core.MinMaxLocResult minMaxLocResult = Core.minMaxLoc(result);

        return Match.of(minMaxLocResult.maxLoc, minMaxLocResult.maxVal, template.width(), template.height());
    }
}
