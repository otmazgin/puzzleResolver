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

        Utilities.writeImageToFile(rotatedPiece, "rotated" + puzzlePiece.getPieceNumber() + ".jpg");

        cvtColor(rotatedPiece, rotatedPiece, COLOR_RGB2GRAY);

        threshold( rotatedPiece, rotatedPiece, 120, 200, THRESH_BINARY );
        //Canny(rotatedPiece, rotatedPiece, 100, 200);

        Utilities.writeImageToFile(rotatedPiece, "rotatedCanny" + puzzlePiece.getPieceNumber() + ".jpg");

        findContours( rotatedPiece, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0) );

/*        double maxArea = 0;
        double secondMaxArea = 0;
        MatOfPoint pieceContour = null;
        for (MatOfPoint contour : contours)
        {
            double contourArea = Imgproc.contourArea(contour);

            if (contourArea > maxArea)
            {
                maxArea = contourArea;
            }
            else
            {
                if (contourArea > secondMaxArea)
                {
                    pieceContour = contour;
                    secondMaxArea = contourArea;
                }
            }
        }
        Collections.sort(contours, new Comparator<MatOfPoint>()
        {
            @Override
            public int compare(MatOfPoint o1, MatOfPoint o2)
            {
                double area1 = contourArea(o1);
                double area2 = contourArea(o2);
                return area1 < area2 ? 1 : area1 > area2 ? -1 : 0;
            }
        });*/

        for (MatOfPoint contour : contours)
        {
            Imgproc.drawContours
                    (
                            puzzle,
                            Arrays.asList(contour),
                            0,
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
