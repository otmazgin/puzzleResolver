package assembler;

import org.opencv.core.*;
import utillities.Utilities;

import java.awt.*;
import java.awt.Point;

import static org.opencv.imgproc.Imgproc.COLOR_RGB2GRAY;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.photo.Photo.INPAINT_TELEA;
import static org.opencv.photo.Photo.inpaint;

enum PuzzlePieceRestorer
{
    instance;

    private static final int threshold = 30;
    private static int epsilon = 10;

    void restoreMissingGaps(Mat puzzlePiece, Mat originalPiecesImage)
    {
        Mat maskOfGaps = new Mat(puzzlePiece.size(), CvType.CV_8U);

        Mat originalInGray = new Mat();
        cvtColor(originalPiecesImage, originalInGray, COLOR_RGB2GRAY);
        //Utilities.writeImageToFile(originalInGray, "grayPieces.jpg");
        Mat puzzlePieceInGray = new Mat();
        cvtColor(puzzlePiece, puzzlePieceInGray, COLOR_RGB2GRAY);
        //Utilities.writeImageToFile(puzzlePieceInGray, "grayPiece.jpg");

        double backgroundColor = AverageColorCalculator.instance.averageBackgroundOfOneDimension(originalInGray);

        fillMaskAtLeftGap(puzzlePieceInGray, backgroundColor, maskOfGaps);
        fillMaskAtRightGap(puzzlePieceInGray, backgroundColor, maskOfGaps);
        fillMaskAtUpGap(puzzlePieceInGray, backgroundColor, maskOfGaps);
        fillMaskAtDownGap(puzzlePieceInGray, backgroundColor, maskOfGaps);

        inpaint(puzzlePiece, maskOfGaps, puzzlePiece, 1, INPAINT_TELEA);

        Utilities.writeImageToFile(puzzlePiece, "inpainted" + (int) (100 * Math.random()) + ".jpg");
    }


    private void fillMaskAtLeftGap(Mat puzzlePieceInGray, double backgroundColor, Mat maskOfGaps)
    {
        double averageAround = AverageColorCalculator.instance.averageAround
                (
                        puzzlePieceInGray,
                        new Point(60, puzzlePieceInGray.height() / 2)
                );

        if (Math.abs(averageAround - backgroundColor) <= threshold)
        {
            for (int i = puzzlePieceInGray.rows() / 3 - epsilon; i < 2 * (puzzlePieceInGray.rows() / 3) + epsilon; i++)
            {
                for (int j = 0; j < puzzlePieceInGray.cols() / 3 + 10; j++)
                {
                    maskOfGaps.put(i, j, 1);
                }
            }
        }
    }

    private void fillMaskAtRightGap(Mat puzzlePieceInGray, double backgroundColor, Mat maskOfGaps)
    {
        double averageAround = AverageColorCalculator.instance.averageAround
                (
                        puzzlePieceInGray,
                        new Point(puzzlePieceInGray.width() - 60, puzzlePieceInGray.height() / 2)
                );

        if (Math.abs(averageAround - backgroundColor) <= threshold)
        {
            for (int i = puzzlePieceInGray.rows() / 3 - epsilon; i < 2 * (puzzlePieceInGray.rows() / 3) + epsilon; i++)
            {
                for (int j = 2 * (puzzlePieceInGray.cols() / 3) - 10; j < puzzlePieceInGray.cols(); j++)
                {
                    maskOfGaps.put(i, j, 1);
                }
            }
        }
    }

    private void fillMaskAtUpGap(Mat puzzlePieceInGray, double backgroundColor, Mat maskOfGaps)
    {
        double averageAround = AverageColorCalculator.instance.averageAround
                (
                        puzzlePieceInGray,
                        new Point(puzzlePieceInGray.width() / 2, 60)
                );

        if (Math.abs(averageAround - backgroundColor) <= threshold)
        {
            for (int i = 0; i < puzzlePieceInGray.rows() / 3 + epsilon; i++)
            {
                for (int j = puzzlePieceInGray.cols() / 3 - epsilon; j < 2 * (puzzlePieceInGray.cols() / 3) + epsilon; j++)
                {
                    maskOfGaps.put(i, j, 1);
                }
            }
        }
    }

    private void fillMaskAtDownGap(Mat puzzlePieceInGray, double backgroundColor, Mat maskOfGaps)
    {
        double averageAround = AverageColorCalculator.instance.averageAround
                (
                        puzzlePieceInGray,
                        new Point(puzzlePieceInGray.width() / 2, puzzlePieceInGray.height() - 60)
                );

        if (Math.abs(averageAround - backgroundColor) <= threshold)
        {
            for (int i = 2 * (puzzlePieceInGray.rows() / 3) - epsilon; i < puzzlePieceInGray.rows(); i++)
            {
                for (int j = puzzlePieceInGray.cols() / 3 - epsilon; j < 2 * (puzzlePieceInGray.cols() / 3) + epsilon; j++)
                {
                    maskOfGaps.put(i, j, 1);
                }
            }
        }
    }
}
