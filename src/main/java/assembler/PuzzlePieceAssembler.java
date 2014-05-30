package assembler;

import assembler.templateMatcher.FastTemplateMatcher;
import assembler.templateMatcher.Match;
import assembler.templateMatcher.TemplateMatcher;
import entities.PuzzlePiece;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;

import java.util.concurrent.Callable;

class PuzzlePieceAssembler implements Callable<Match>
{
    private final PuzzlePiece puzzlePiece;
    private final Mat puzzle;
    private double[] backgroundColor;

    PuzzlePieceAssembler(PuzzlePiece puzzlePiece, Mat puzzle, double[] backgroundColor)
    {
        this.puzzlePiece = puzzlePiece;
        this.puzzle = puzzle;
        this.backgroundColor = backgroundColor;
    }

    @Override
    public Match call() throws Exception
    {
        int pieceNumber = puzzlePiece.getPieceNumber();

        System.out.println("Started assembling piece number: " + pieceNumber);

        Mat transformedPieceMatrix = puzzlePiece.getTransformedPieceMatrix();

        PuzzlePieceRestorer.instance.restoreMissingGaps(transformedPieceMatrix, backgroundColor);

        //Match bestMatch = TemplateMatcher.instance.findBestMatch(puzzle, puzzlePiece);
        Match bestMatch = FastTemplateMatcher.instance.findBestMatch(puzzle, transformedPieceMatrix, 3);

        int numOfRotations = 0;
        Mat rotatedPuzzlePiece = transformedPieceMatrix;
        Match rotationBestMatch;

        while (numOfRotations <= 3)
        {
            if (bestMatch.getMatchValue() > 0.99)
            {
                System.out.println("Finished assembling piece number: " + pieceNumber);
                return bestMatch;
            }

            rotatedPuzzlePiece = ImageRotator.instance.rotateLeft(rotatedPuzzlePiece);

            rotationBestMatch = TemplateMatcher.instance.findBestMatch(puzzle, rotatedPuzzlePiece);

            if (rotationBestMatch.compareTo(bestMatch) > 0)
            {
                bestMatch = rotationBestMatch;
            }

            numOfRotations++;
        }

        System.out.println("Finished assembling piece number: " + pieceNumber);

        return bestMatch;
    }

    static Callable<Match> createAssembler(PuzzlePiece puzzlePiece, Mat puzzle, double[] backgroundColor)
    {
        return new PuzzlePieceAssembler(puzzlePiece, puzzle, backgroundColor);
    }
}
