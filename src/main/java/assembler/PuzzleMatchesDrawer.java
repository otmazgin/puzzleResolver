package assembler;

import assembler.templateMatcher.Match;
import com.google.common.collect.ImmutableList;
import entities.PuzzlePiece;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import utillities.Utilities;

import java.util.*;

import static org.opencv.core.Core.putText;
import static org.opencv.imgproc.Imgproc.*;

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

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();

        double bestRotationAngle = puzzlePiece.getBestRotationAngle();

        Mat rotatedPiece = ImageRotator.instance.rotate(puzzlePiece.getRotatedImage(), bestRotationAngle);

        cvtColor(rotatedPiece, rotatedPiece, COLOR_RGB2GRAY);

        Mat originalInGray = new Mat();
        cvtColor(puzzlePiece.getOriginalSource(), originalInGray, COLOR_RGB2GRAY);
        double backgroundColor = BackgroundExtractor.instance.calcBackgroundFromSource(originalInGray);

        for (int row = 0; row < rotatedPiece.rows(); row++)
        {
            for (int column = 0; column < rotatedPiece.cols(); column++)
            {
                if (Math.abs(rotatedPiece.get(row, column)[0] - backgroundColor) < 50 || rotatedPiece.get(row, column)[0]<=127)
                {
                    rotatedPiece.put(row, column, 0);
                }
                else
                {
                    rotatedPiece.put(row, column, 255);
                }
            }

        }
        Utilities.writeImageToFile(rotatedPiece, "binary" + puzzlePiece.getPieceNumber() + ".jpg");

        findContours(rotatedPiece, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0));

        for (int i = 0; i<contours.size(); i++)
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
