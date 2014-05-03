package assembler;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import utillities.Optional;
import utillities.Utilities;

import java.util.concurrent.Callable;

class PuzzlePieceAssembler implements Callable<Point>
{
    private final Mat puzzlePiece;
    private final Mat puzzle;

    PuzzlePieceAssembler(Mat puzzlePiece, Mat puzzle)
    {
        this.puzzlePiece = puzzlePiece;
        this.puzzle = puzzle;
    }

    @Override
    public Point call() throws Exception
    {
        Optional<Point> matchingPoint = TemplateMatcher.instance.findBestMatching(puzzle, puzzlePiece);
        int numOfDirectionsTested = 1;

        Mat rotatedPuzzlePiece = puzzlePiece;

        while (!matchingPoint.isPresent() && numOfDirectionsTested <= 4)
        {
            rotatedPuzzlePiece = ImageRotator.instance.rotateToLeft(rotatedPuzzlePiece);

            matchingPoint = TemplateMatcher.instance.findBestMatching(puzzle, rotatedPuzzlePiece);

            numOfDirectionsTested++;
        }

        if (matchingPoint.isPresent())
        {
            if (numOfDirectionsTested % 2 == 0)
            {
                Utilities.drawRectAndStore(new Rect(matchingPoint.get(), new Size(puzzlePiece.rows(), puzzlePiece.cols())), puzzle, "match.jpg");
            }
            else
            {
                Utilities.drawRectAndStore(new Rect(matchingPoint.get(), new Size(puzzlePiece.cols(), puzzlePiece.rows())), puzzle, "match.jpg");
            }
        }
        else
        {
            throw new RuntimeException("Match not found!");
        }

        return matchingPoint.get();
    }

    static Callable<Point> createAssembler(Mat puzzlePiece, Mat puzzle)
    {
        return new PuzzlePieceAssembler(puzzlePiece, puzzle);
    }
}
