package assembler;

import org.opencv.core.*;
import utillities.Utilities;

import static org.opencv.imgproc.Imgproc.COLOR_RGB2GRAY;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.photo.Photo.INPAINT_TELEA;
import static org.opencv.photo.Photo.inpaint;

enum PuzzlePieceRestorer
{
    instance;

    private static final int threshold = 5;

    void restoreMissingGaps(Mat puzzlePiece, Mat originalPiecesImage)
    {
        Mat maskOfGaps = new Mat(puzzlePiece.size(), CvType.CV_8U);

        Mat originalInGray = new Mat();
        cvtColor(originalPiecesImage, originalInGray, COLOR_RGB2GRAY);
        Utilities.writeImageToFile(originalInGray, "grayPieces.jpg");
        Mat puzzlePieceInGray = new Mat();
        cvtColor(puzzlePiece, puzzlePieceInGray, COLOR_RGB2GRAY);
        Utilities.writeImageToFile(puzzlePieceInGray, "grayPiece.jpg");

        double backgroundColor = BackgroundExtractor.instance.calcBackgroundFromSource(originalInGray);

        fillMaskAtLeftGap(puzzlePieceInGray, backgroundColor, maskOfGaps);
        fillMaskAtRightGap(puzzlePieceInGray, backgroundColor, maskOfGaps);
        fillMaskAtUpGap(puzzlePieceInGray, backgroundColor, maskOfGaps);
        fillMaskAtDownGap(puzzlePieceInGray, backgroundColor, maskOfGaps);

        inpaint(puzzlePiece, maskOfGaps, puzzlePiece, 1, INPAINT_TELEA);

        //Utilities.writeImageToFile(maskOfGaps, "mask" + (int) (100 * Math.random()) + ".jpg");
        //up
        Utilities.drawRect(new Rect(puzzlePiece.cols()/3, 0, puzzlePiece.cols()/3, puzzlePiece.rows()/3), puzzlePiece);
        //down
        Utilities.drawRect(new Rect(puzzlePiece.cols()/3, 2*(puzzlePiece.rows()/3), puzzlePiece.cols()/3, puzzlePiece.rows()/3), puzzlePiece);
        //left
        Utilities.drawRect(new Rect(0, puzzlePiece.rows()/3, puzzlePiece.cols()/3, puzzlePiece.rows()/3), puzzlePiece);
        //right
        Utilities.drawRect(new Rect(2*(puzzlePiece.cols()/3), puzzlePiece.rows()/3, puzzlePiece.cols()/3, puzzlePiece.rows()/3), puzzlePiece);
        Utilities.writeImageToFile(puzzlePiece, "inpainted" + (int) (100 * Math.random()) + ".jpg");
    }


    private void fillMaskAtLeftGap(Mat puzzlePieceInGray, double backgroundColor, Mat maskOfGaps)
    {
        if (Math.abs(puzzlePieceInGray.get(puzzlePieceInGray.height() / 2, 60)[0] - backgroundColor) <= threshold)
        {
            for (int i = puzzlePieceInGray.rows() / 3; i < 2 * (puzzlePieceInGray.rows() / 3); i++)
            {
                for (int j = 0; j < puzzlePieceInGray.cols() / 3; j++)
                {
                    maskOfGaps.put(i, j, 1);
                }
            }
            /*int row = puzzlePieceInGray.height() / 2;
            int column = 0;
            int index = 0;

            while (Math.abs(puzzlePieceInGray.get(row, column)[0] - backgroundColor) <= threshold &&
                    (column < puzzlePieceInGray.width() / 2))
            {
                while (Math.abs(puzzlePieceInGray.get(row, column)[0] - backgroundColor) <= threshold &&
                        (row + index < puzzlePieceInGray.height()))
                {
                    maskOfGaps.put(row - index, column, 1);
                    maskOfGaps.put(row + index, column, 1);
                    index++;
                }

                row = puzzlePieceInGray.height() / 2;
                column++;
            }*/
        }
    }

    private void fillMaskAtRightGap(Mat puzzlePieceInGray, double backgroundColor, Mat maskOfGaps)
    {
        if (Math.abs(puzzlePieceInGray.get(puzzlePieceInGray.height() / 2, puzzlePieceInGray.width() - 60)[0] - backgroundColor) <= threshold)
        {
            for (int i = puzzlePieceInGray.rows() / 3; i < 2 * (puzzlePieceInGray.rows() / 3); i++)
            {
                for (int j = 2 * (puzzlePieceInGray.cols() / 3); j < puzzlePieceInGray.cols(); j++)
                {
                    maskOfGaps.put(i, j, 1);
                }
            }
            /*int row = puzzlePieceInGray.height() / 2;
            int column = puzzlePieceInGray.width();
            int index = 0;

            while (Math.abs(puzzlePieceInGray.get(row, column)[0] - backgroundColor) <= threshold &&
                    (column > puzzlePieceInGray.width() / 2))
            {
                while (Math.abs(puzzlePieceInGray.get(row + index, column)[0] - backgroundColor) <= threshold &&
                        (row + index < puzzlePieceInGray.height()))
                {
                    maskOfGaps.put(row - index, column, 1);
                    maskOfGaps.put(row + index, column, 1);
                    index++;
                }

                row = puzzlePieceInGray.height() / 2;
                column--;
            }*/
        }
    }

    private void fillMaskAtUpGap(Mat puzzlePieceInGray, double backgroundColor, Mat maskOfGaps)
    {
        if (Math.abs(puzzlePieceInGray.get(60, puzzlePieceInGray.width() / 2)[0] - backgroundColor) <= threshold)
        {
            for (int i = 0; i < puzzlePieceInGray.rows() / 3; i++)
            {
                for (int j = puzzlePieceInGray.cols() / 3; j < 2 * (puzzlePieceInGray.cols() / 3); j++)
                {
                    maskOfGaps.put(i, j, 1);
                }
            }

            /*int row = 0;
            int column = puzzlePieceInGray.width() / 2;
            int index = 0;

            while (Math.abs(puzzlePieceInGray.get(row, column)[0] - backgroundColor) <= threshold &&
                    (row < puzzlePieceInGray.height() / 2))
            {
                while (Math.abs(puzzlePieceInGray.get(row, column + index)[0] - backgroundColor) <= threshold &&
                        (column + index < puzzlePieceInGray.width()))
                {
                    maskOfGaps.put(row, column - index, 1);
                    maskOfGaps.put(row, column + index, 1);
                    Utilities.drawPoint(new Point(column - index, row), puzzlePieceInGray);
                    Utilities.drawPoint(new Point(column + index, row), puzzlePieceInGray);

                    index++;
                }

                column = puzzlePieceInGray.width() / 2;
                row++;
            }*/
        }
    }

    private void fillMaskAtDownGap(Mat puzzlePieceInGray, double backgroundColor, Mat maskOfGaps)
    {
        if (Math.abs(puzzlePieceInGray.get(puzzlePieceInGray.height() - 60, puzzlePieceInGray.width() / 2)[0] - backgroundColor) <= threshold)
        {
            for (int i = 2 * (puzzlePieceInGray.rows() / 3); i < puzzlePieceInGray.rows(); i++)
            {
                for (int j = puzzlePieceInGray.cols() / 3; j < 2 * (puzzlePieceInGray.cols() / 3); j++)
                {
                    maskOfGaps.put(i, j, 1);
                }
            }
            /*int row = puzzlePieceInGray.height();
            int column = puzzlePieceInGray.width() / 2;
            int index = 0;

            while (Math.abs(puzzlePieceInGray.get(row, column)[0] - backgroundColor) <= threshold &&
                    (row > puzzlePieceInGray.height() / 2))
            {
                while (Math.abs(puzzlePieceInGray.get(row, column + index)[0] - backgroundColor) <= threshold &&
                        (column + index < puzzlePieceInGray.width()))
                {
                    maskOfGaps.put(row, column - index, 1);
                    maskOfGaps.put(row, column + index, 1);
                    index++;
                }

                column = puzzlePieceInGray.width() / 2;
                row--;
            }*/
        }
    }
}
