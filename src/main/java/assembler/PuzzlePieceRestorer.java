package assembler;

import org.opencv.core.*;
import utillities.Utilities;

import java.awt.*;
import java.awt.Point;

import static org.opencv.photo.Photo.INPAINT_TELEA;
import static org.opencv.photo.Photo.inpaint;

enum PuzzlePieceRestorer
{
    instance;

    private static final int distanceFromBackgroundColorThreshold = 5;

    private void restoreMissingGaps2(Mat puzzlePiece, double[] backgroundColor)
    {
        Mat maskOfGaps = new Mat(puzzlePiece.size(), CvType.CV_8U);

        for (int row = 0; row < puzzlePiece.height(); row++)
        {
            for (int column = 0; column < puzzlePiece.width(); column++)
            {
                if (euclideanDistance(puzzlePiece.get(row, column), backgroundColor) < 70)
                {
                    maskOfGaps.put(row, column, 1);
                }
                else
                {
                    maskOfGaps.put(row, column, 0);
                }
            }
        }

        inpaint(puzzlePiece, maskOfGaps, puzzlePiece, 1, INPAINT_TELEA);

        Utilities.writeImageToFile(puzzlePiece, "inpainted.jpg");

    }

    void restoreMissingGaps(Mat puzzlePiece, double[] backgroundColor)
    {
        Mat maskOfGaps = new Mat(puzzlePiece.size(), CvType.CV_8U);

        inpaintLeftMiddleSuspectedGap(puzzlePiece, backgroundColor, maskOfGaps);
        inpaintUpMiddleSuspectedGap(puzzlePiece, backgroundColor, maskOfGaps);
        inpaintDownMiddleSuspectedGap(puzzlePiece, backgroundColor, maskOfGaps);
        inpaintRightMiddleSuspectedGap(puzzlePiece, backgroundColor, maskOfGaps);

        inpaint(puzzlePiece, maskOfGaps, puzzlePiece, 1, INPAINT_TELEA);

        Utilities.writeImageToFile(puzzlePiece, "inpainted.jpg");
    }

    private void inpaintLeftMiddleSuspectedGap(Mat puzzlePiece, double[] backgroundColor, Mat maskOfGaps)
    {
        //Utilities.drawPoint(new org.opencv.core.Point(10, puzzlePiece.height() / 2), puzzlePiece);

        if (euclideanDistance(puzzlePiece.get(puzzlePiece.height() / 2, 10), backgroundColor) <= distanceFromBackgroundColorThreshold)
        {
            inpaintGapIfExists(puzzlePiece, maskOfGaps, new Point(0, puzzlePiece.height() / 4), backgroundColor);
        }
    }

    private void inpaintUpMiddleSuspectedGap(Mat puzzlePiece, double[] backgroundColor, Mat maskOfGaps)
    {
        //Utilities.drawPoint(new org.opencv.core.Point(puzzlePiece.height() / 2, 10), puzzlePiece);

        if (euclideanDistance(puzzlePiece.get(10, puzzlePiece.width() / 2), backgroundColor) <= distanceFromBackgroundColorThreshold)
        {
            inpaintGapIfExists(puzzlePiece, maskOfGaps, new Point(puzzlePiece.width() / 4, 0), backgroundColor);
        }
    }

    private void inpaintDownMiddleSuspectedGap(Mat puzzlePiece, double[] backgroundColor, Mat maskOfGaps)
    {
        //Utilities.drawPoint(new org.opencv.core.Point(puzzlePiece.width() / 2, puzzlePiece.height() - 10), puzzlePiece);

        if (euclideanDistance(puzzlePiece.get(puzzlePiece.height() - 10, puzzlePiece.width() / 2), backgroundColor) <= distanceFromBackgroundColorThreshold)
        {
            inpaintGapIfExists(puzzlePiece, maskOfGaps, new Point(puzzlePiece.width() / 4, puzzlePiece.height() / 2), backgroundColor);
        }
    }

    private void inpaintRightMiddleSuspectedGap(Mat puzzlePiece, double[] backgroundColor, Mat maskOfGaps)
    {
        //Utilities.drawPoint(new org.opencv.core.Point(puzzlePiece.width() - 10, puzzlePiece.height() / 2), puzzlePiece);

        if (euclideanDistance(puzzlePiece.get(puzzlePiece.height() / 2, puzzlePiece.width() - 10), backgroundColor) <= distanceFromBackgroundColorThreshold)
        {
            inpaintGapIfExists(puzzlePiece, maskOfGaps, new Point(puzzlePiece.width() / 2, puzzlePiece.height() / 4), backgroundColor);
        }
    }

    private void inpaintGapIfExists(Mat puzzlePiece, Mat maskOfGaps, Point suspectedGapStart, double[] backgroundColor)
    {
        //Utilities.drawRect(new Rect(suspectedGapStart.x, suspectedGapStart.y, puzzlePiece.width() / 2, puzzlePiece.height() / 2), puzzlePiece);

        for (int row = suspectedGapStart.y; row < suspectedGapStart.y + puzzlePiece.height() / 2; row++)
        {
            for (int column = suspectedGapStart.x; column < suspectedGapStart.x + puzzlePiece.width() / 2; column++)
            {
                if (euclideanDistance(puzzlePiece.get(row, column), backgroundColor) < 70)
                {
                    maskOfGaps.put(row, column, 1);
                } else
                {
                    maskOfGaps.put(row, column, 0);
                }
            }
        }
    }

    private double euclideanDistance(double[] first, double[] second)
    {
        if (first.length != second.length)
        {
            throw new RuntimeException("Euclidean distance: color dimensions are not the same");
        }

        int sumSquares = 0;
        for (int dimension = 0; dimension < first.length; dimension++)
        {
            sumSquares += Math.pow(first[dimension] - second[dimension], 2);
        }

        return Math.sqrt(sumSquares);
    }
}
