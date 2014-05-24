package assembler;

import assembler.templateMatcher.Match;
import org.opencv.core.*;
import utillities.Utilities;

import java.util.Map;

import static org.opencv.core.Core.putText;
import static org.opencv.imgproc.Imgproc.boundingRect;

public enum PuzzleMatchesDrawer
{
    instance;

    public void drawMatches(Map<Integer, Match> matches, Mat puzzle)
    {
        for (Map.Entry<Integer, Match> match : matches.entrySet())
        {
            drawMatchNumberOnTheMiddle(match, puzzle);
            drawPieceBoundingRectangle(match.getValue(), puzzle);
        }
    }

    private void drawMatchNumberOnTheMiddle(Map.Entry<Integer, Match> matches, Mat puzzle)
    {
        Match match = matches.getValue();
        Point matchPoint = match.getMatchPoint();
        Point pieceCenter = new Point(matchPoint.x + (match.getWidth() / 2) - 40, matchPoint.y + (match.getHeight() / 2) + 40);

        putText(puzzle, matches.getKey().toString(), pieceCenter, 1, 10, new Scalar(0, 0, 255), 10);
    }

    private void drawPieceBoundingRectangle(Match pieceMatch, Mat puzzle)
    {
        Utilities.drawRect(new Rect(pieceMatch.getMatchPoint(), new Size(pieceMatch.getWidth(), pieceMatch.getHeight())), puzzle);
    }
}
