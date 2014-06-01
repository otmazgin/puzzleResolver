package assembler;

import assembler.templateMatcher.Match;
import entities.PuzzlePiece;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import utillities.Utilities;

import java.util.*;

import static org.opencv.core.Core.putText;
import static org.opencv.imgproc.Imgproc.*;
import static org.opencv.photo.Photo.fastNlMeansDenoising;

public enum PuzzleMatchesDrawer
{
    instance;

    public void drawMatches(Map<PuzzlePiece, Match> matches, Mat puzzle)
    {
        for (Map.Entry<PuzzlePiece, Match> matchEntry : matches.entrySet())
        {
            drawPieceNumberOnThePiecesImage(matchEntry.getKey());

            drawPiecesOnCompletePuzzle(puzzle, matchEntry);
        }

        Utilities.writeImageToFile(matches.entrySet().iterator().next().getKey().getOriginalSource(), "pieces.jpg");
        Utilities.writeImageToFile(puzzle, "matches.jpg");
    }

    private void drawPieceNumberOnThePiecesImage(PuzzlePiece puzzlePiece)
    {
        Rect rect = boundingRect(puzzlePiece.getcontoure());

        Point pieceCenter = new Point(rect.x + (rect.width / 2) - 40, rect.y + (rect.height / 2) + 40);

        putText(puzzlePiece.getOriginalSource(), puzzlePiece.getPieceNumber() + "", pieceCenter, 1, 10, new Scalar(0, 0, 255), 10);
    }

    private void drawPiecesOnCompletePuzzle(Mat puzzle, Map.Entry<PuzzlePiece, Match> match)
    {
        drawPieceNumberOnTheCompletePuzzle(match, puzzle);
        drawPieceContourOnTheCompletePuzzle(match, puzzle);
    }

    private void drawPieceNumberOnTheCompletePuzzle(Map.Entry<PuzzlePiece, Match> matchEntry, Mat puzzle)
    {
        Match match = matchEntry.getValue();
        Point matchPoint = match.getMatchPoint();

        Point pieceCenter = new Point(matchPoint.x + (match.getWidth() / 2) - 40, matchPoint.y + (match.getHeight() / 2) + 40);

        putText(puzzle, matchEntry.getKey().getPieceNumber() + "", pieceCenter, 1, 10, new Scalar(0, 0, 255), 10);
    }

    private void drawPieceContourOnTheCompletePuzzle(Map.Entry<PuzzlePiece, Match> matchEntry, Mat puzzle)
    {
        PuzzlePiece puzzlePiece = matchEntry.getKey();
        Match pieceMatch = matchEntry.getValue();

        double bestRotationAngle = puzzlePiece.getBestRotationAngle();

        System.out.println("Piece number: " + puzzlePiece.getPieceNumber() + " rotation angle: " + bestRotationAngle);

        Mat rotatedPiece = ImageRotator.instance.rotate(puzzlePiece.getRotatedImage(), bestRotationAngle);

        double backgroundColor = extractBackgroundColorFrom(puzzlePiece);

        Mat pieceInBinary = binaryQuantizePuzzlePiece(backgroundColor, rotatedPiece);

        //Utilities.writeImageToFile(pieceInBinary, "binary" + puzzlePiece.getPieceNumber() + ".jpg");

        findAndDrawContours(puzzle, pieceMatch, pieceInBinary);
    }

    private double extractBackgroundColorFrom(PuzzlePiece puzzlePiece)
    {
        Mat originalInGray = new Mat();
        cvtColor(puzzlePiece.getOriginalSource(), originalInGray, COLOR_RGB2GRAY);
        return AverageColorCalculator.instance.averageBackgroundOfOneDimension(originalInGray);
    }

    private Mat binaryQuantizePuzzlePiece(double backgroundColor, Mat puzzlePiece)
    {
        Mat puzzlePieceInBinary = new Mat();
        cvtColor(puzzlePiece, puzzlePieceInBinary, COLOR_RGB2GRAY);

        for (int row = 0; row < puzzlePieceInBinary.rows(); row++)
        {
            for (int column = 0; column < puzzlePieceInBinary.cols(); column++)
            {
                if (Math.abs(puzzlePieceInBinary.get(row, column)[0] - backgroundColor) < 50 || puzzlePieceInBinary.get(row, column)[0] == 0)
                {
                    puzzlePieceInBinary.put(row, column, 0);
                } else
                {
                    puzzlePieceInBinary.put(row, column, 255);
                }
            }
        }

        //delete the rectangle border:
        for (int row = 0; row < puzzlePieceInBinary.rows(); row++)
        {
            for (int column = 0; column < puzzlePieceInBinary.cols(); column++)
            {
                if (puzzlePieceInBinary.get(row, column)[0] == 255)
                {
                    int down = (row < puzzlePieceInBinary.rows() - 1 && puzzlePieceInBinary.get(row + 1, column)[0] == 0) ? 1 : 0;
                    int downLeft = (row < puzzlePieceInBinary.rows() - 1 && column > 0 && puzzlePieceInBinary.get(row + 1, column - 1)[0] == 0) ? 1 : 0;
                    int downRight = (row < puzzlePieceInBinary.rows() - 1 && column < puzzlePieceInBinary.cols() - 1 && puzzlePieceInBinary.get(row + 1, column + 1)[0] == 0) ? 1 : 0;
                    int up = (row > 0 && puzzlePieceInBinary.get(row - 1, column)[0] == 0) ? 1 : 0;
                    int upLeft = (row > 0 && column > 0 && puzzlePieceInBinary.get(row - 1, column - 1)[0] == 0) ? 1 : 0;
                    int upRight = (row > 0 && column < puzzlePieceInBinary.cols() - 1 && puzzlePieceInBinary.get(row - 1, column + 1)[0] == 0) ? 1 : 0;
                    int right = (column < puzzlePieceInBinary.cols() - 1 && puzzlePieceInBinary.get(row, column + 1)[0] == 0) ? 1 : 0;
                    int left = (column > 0 && puzzlePieceInBinary.get(row, column - 1)[0] == 0) ? 1 : 0;

                    int sumOfBlackPixelsAround = down + downLeft + downRight + up + upLeft + upRight + left + right;

                    if (sumOfBlackPixelsAround > 4)
                    {
                        puzzlePieceInBinary.put(row, column, 0);
                    } else
                    {
                        puzzlePieceInBinary.put(row, column, 255);
                    }
                }
            }
        }

        return puzzlePieceInBinary;
    }

    private void findAndDrawContours(Mat puzzle, Match pieceMatch, Mat binaryPuzzlePiece)
    {
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        findContours(binaryPuzzlePiece, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0));

        for (int i = 0; i < contours.size(); i++)
        {
            Imgproc.drawContours
                    (
                            puzzle,
                            contours,
                            i,
                            new Scalar(0, 0, 255),
                            1,
                            BORDER_ISOLATED,
                            hierarchy,
                            0,
                            new Point(pieceMatch.getMatchPoint().x - pieceMatch.getWidth(), pieceMatch.getMatchPoint().y - pieceMatch.getHeight())
                    );

        }
    }
}
