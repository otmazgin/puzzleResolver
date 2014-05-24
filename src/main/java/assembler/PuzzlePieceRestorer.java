package assembler;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import utillities.Utilities;

import java.awt.*;
import java.awt.Point;
import java.util.*;
import java.util.List;

import static org.opencv.imgproc.Imgproc.*;
import static org.opencv.photo.Photo.INPAINT_NS;
import static org.opencv.photo.Photo.INPAINT_TELEA;
import static org.opencv.photo.Photo.inpaint;

enum PuzzlePieceRestorer
{
    instance;

    private static final int distanceFromBackgroundColorThreshold = 10;

    void restoreMissingGaps(Mat puzzlePiece, double[] backgroundColor)
    {
        Mat maskOfGaps = new Mat(puzzlePiece.size(), CvType.CV_8U);

        findLeftMiddleGap(puzzlePiece, backgroundColor, maskOfGaps);
        findUpMiddleGap(puzzlePiece, backgroundColor, maskOfGaps);
        findDownMiddleGap(puzzlePiece, backgroundColor, maskOfGaps);
        findRightMiddleGap(puzzlePiece, backgroundColor, maskOfGaps);

        inpaint(puzzlePiece, maskOfGaps, puzzlePiece, 1, INPAINT_TELEA);

        //Utilities.writeImageToFile(puzzlePiece, "inpainted" + (int) (100 * Math.random()) + ".jpg");
    }

    private void findLeftMiddleGap(Mat puzzlePiece, double[] backgroundColor, Mat maskOfGaps)
    {
        if (euclideanDistance(puzzlePiece.get(puzzlePiece.height() / 2, 10), backgroundColor) <= distanceFromBackgroundColorThreshold)
        {
            inpaintGapIfExists(puzzlePiece, maskOfGaps, new Point(0, puzzlePiece.height() / 4), backgroundColor);
        }
    }

    private void findUpMiddleGap(Mat puzzlePiece, double[] backgroundColor, Mat maskOfGaps)
    {
        if (euclideanDistance(puzzlePiece.get(10, puzzlePiece.width() / 2), backgroundColor) <= distanceFromBackgroundColorThreshold)
        {
            inpaintGapIfExists(puzzlePiece, maskOfGaps, new Point(puzzlePiece.width() / 4, 0), backgroundColor);
        }
    }

    private void findDownMiddleGap(Mat puzzlePiece, double[] backgroundColor, Mat maskOfGaps)
    {
        if (euclideanDistance(puzzlePiece.get(puzzlePiece.height() - 10, puzzlePiece.width() / 2), backgroundColor) <= distanceFromBackgroundColorThreshold)
        {
            inpaintGapIfExists(puzzlePiece, maskOfGaps, new Point(puzzlePiece.width() / 4, puzzlePiece.height() / 2), backgroundColor);
        }
    }

    private void findRightMiddleGap(Mat puzzlePiece, double[] backgroundColor, Mat maskOfGaps)
    {
        if (euclideanDistance(puzzlePiece.get(puzzlePiece.height() / 2, puzzlePiece.width() - 10), backgroundColor) <= distanceFromBackgroundColorThreshold)
        {
            inpaintGapIfExists(puzzlePiece, maskOfGaps, new Point(puzzlePiece.width() / 2, puzzlePiece.height() / 4), backgroundColor);
        }
    }

    private void inpaintGapIfExists(Mat puzzlePiece, Mat maskOfGaps, Point suspectedGapStart, double[] backgroundColor)
    {
        //List<org.opencv.core.Point> chosenPoints = new ArrayList<>();

        for (int row = suspectedGapStart.y; row < suspectedGapStart.y + puzzlePiece.height() / 2; row++)
        {
            for (int column = suspectedGapStart.x; column < suspectedGapStart.x + puzzlePiece.width() / 2; column++)
            {
                if (euclideanDistance(puzzlePiece.get(row, column), backgroundColor) < 30)
                {
                    //chosenPoints.add(new org.opencv.core.Point(column, row));
                    maskOfGaps.put(row, column, 1);
                } else
                {
                    maskOfGaps.put(row, column, 0);
                }
            }
        }
/*        org.opencv.core.Point[] arrayOfPoints = new org.opencv.core.Point[chosenPoints.size()];
        Rect rect = boundingRect(new MatOfPoint(chosenPoints.toArray(arrayOfPoints)));
        Utilities.drawRect(rect, puzzlePiece);

        for (int row = rect.y - 10; row < rect.y + rect.height + 10; row++)
        {
            for (int column = rect.x - 10; column < rect.x + rect.width + 10; column++)
            {
                maskOfGaps.put(row, column, 1);
            }
        }*/
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
