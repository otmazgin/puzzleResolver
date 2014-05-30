package assembler;

import assembler.templateMatcher.Match;
import entities.PuzzlePiece;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import utillities.Utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
        drawPieceBoundingRectangleOnTheCompletePuzzle(match, puzzle);
    }

    private void drawPieceNumberOnTheCompletePuzzle(Map.Entry<PuzzlePiece, Match> matchEntry, Mat puzzle)
    {
        Match match = matchEntry.getValue();
        Point matchPoint = match.getMatchPoint();

        Point pieceCenter = new Point(matchPoint.x + (match.getWidth() / 2) - 40, matchPoint.y + (match.getHeight() / 2) + 40);

        putText(puzzle, matchEntry.getKey().getPieceNumber() + "", pieceCenter, 1, 10, new Scalar(0, 0, 255), 10);
    }

    private void drawPieceBoundingRectangleOnTheCompletePuzzle(Map.Entry<PuzzlePiece, Match> matchEntry, Mat puzzle)
    {
        PuzzlePiece puzzlePiece = matchEntry.getKey();
        Match pieceMatch = matchEntry.getValue();

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();

        double bestRotationAngle = puzzlePiece.getBestRotationAngle();

        Mat rotatedPiece = ImageRotator.instance.rotate(puzzlePiece.getTransformedPieceMatrix(), bestRotationAngle);

        cvtColor(rotatedPiece, rotatedPiece, COLOR_RGB2GRAY);

        Imgproc.findContours(rotatedPiece, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0));

        Imgproc.drawContours
                (
                        puzzle,
                        Arrays.asList(contours.iterator().next()),
                        0,
                        new Scalar(0, 0, 255),
                        1,
                        BORDER_ISOLATED,
                        hierarchy,
                        0,
                        pieceMatch.getMatchPoint()
                );
    }
}
