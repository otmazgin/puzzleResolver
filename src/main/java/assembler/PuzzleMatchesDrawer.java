package assembler;

import assembler.templateMatcher.Match;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;

import java.util.Map;

import static org.opencv.core.Core.putText;

public enum PuzzleMatchesDrawer
{
    instance;

    public void drawMatches(Map<Integer, Match> matches, Mat puzzle)
    {
        for (Map.Entry<Integer, Match> futureMatch : matches.entrySet())
        {
            drawMatchNumberOnTheMiddle(futureMatch, puzzle);
        }
    }

    private void drawMatchNumberOnTheMiddle(Map.Entry<Integer, Match> matches, Mat puzzle)
    {
        Match match = matches.getValue();
        Point matchPoint = match.getMatchPoint();
        Point pieceCenter = new Point(matchPoint.x + (match.getWidth() / 2) - 30, matchPoint.y + (match.getHeight() / 2));

        putText(puzzle, matches.getKey().toString(), pieceCenter, 1, 10, new Scalar(0, 0, 255), 15);
    }
}
