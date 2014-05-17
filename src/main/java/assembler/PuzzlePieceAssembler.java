package assembler;

import assembler.templateMatcher.Match;
import assembler.templateMatcher.TemplateMatcher;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;

import java.util.concurrent.Callable;

class PuzzlePieceAssembler implements Callable<Match>
{
    private final int pieceNumber;
    private final Mat puzzlePiece;
    private final Mat puzzle;
    private double[] backgroundColor;

    PuzzlePieceAssembler(int pieceNumber, Mat puzzlePiece, Mat puzzle, double[] backgroundColor)
    {
        this.pieceNumber = pieceNumber;
        this.puzzlePiece = puzzlePiece;
        this.puzzle = puzzle;
        this.backgroundColor = backgroundColor;
    }

    @Override
    public Match call() throws Exception
    {
        System.out.println("Started assembling piece number: " + pieceNumber);

        PuzzlePieceRestorer.instance.restoreMissingGaps(puzzlePiece, backgroundColor);

        Match bestMatch = TemplateMatcher.instance.findBestMatch(puzzle, puzzlePiece);

        int numOfRotations = 0;
        Mat rotatedPuzzlePiece = puzzlePiece;
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

    static Callable<Match> createAssembler(int pieceNumber, Mat puzzlePiece, Mat puzzle, double[] backgroundColor)
    {
        return new PuzzlePieceAssembler(pieceNumber, puzzlePiece, puzzle, backgroundColor);
    }
}
