package assembler;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import utillities.Optional;

import java.util.concurrent.Callable;

class PuzzlePieceAssembler implements Callable<Rect>
{
    private final Mat puzzlePiece;
    private final Mat puzzle;
    private double[] backgroundColor;

    PuzzlePieceAssembler(Mat puzzlePiece, Mat puzzle, double[] backgroundColor)
    {
        this.puzzlePiece = puzzlePiece;
        this.puzzle = puzzle;
        this.backgroundColor = backgroundColor;
    }

    @Override
    public Rect call() throws Exception
    {
        PuzzlePieceRestorer.instance.restoreMissingGaps2(puzzlePiece, backgroundColor);

        Optional<Point> matchingPoint = TemplateMatcher.instance.findBestMatching(puzzle, puzzlePiece);

        int numOfDirectionsTested = 1;
        Mat rotatedPuzzlePiece = puzzlePiece;

        while (!matchingPoint.isPresent() && numOfDirectionsTested <= 4)
        {
            rotatedPuzzlePiece = ImageRotator.instance.rotateLeft(rotatedPuzzlePiece);

            matchingPoint = TemplateMatcher.instance.findBestMatching(puzzle, rotatedPuzzlePiece);

            numOfDirectionsTested++;
        }

        if (matchingPoint.isPresent())
        {
            if (numOfDirectionsTested % 2 == 0)
            {
                return new Rect(matchingPoint.get(), new Size(puzzlePiece.rows(), puzzlePiece.cols()));
            }
            else
            {
                return new Rect(matchingPoint.get(), new Size(puzzlePiece.cols(), puzzlePiece.rows()));
            }
        }
        else
        {
            throw new RuntimeException("Match not found!");
        }
    }

    static Callable<Rect> createAssembler(Mat puzzlePiece, Mat puzzle, double[] backgroundColor)
    {
        return new PuzzlePieceAssembler(puzzlePiece, puzzle, backgroundColor);
    }
}
