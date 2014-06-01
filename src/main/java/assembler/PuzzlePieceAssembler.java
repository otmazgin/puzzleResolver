package assembler;

import assembler.templateMatcher.FastTemplateMatcher;
import assembler.templateMatcher.Match;
import assembler.templateMatcher.TemplateMatcher;
import entities.PuzzlePiece;
import org.opencv.core.Mat;

import java.util.concurrent.Callable;

class PuzzlePieceAssembler implements Callable<Match>
{
    private final PuzzlePiece puzzlePiece;
    private final Mat puzzle;
    private final boolean isWithInpainting;
    private final boolean matchingByPyramids;

    PuzzlePieceAssembler(PuzzlePiece puzzlePiece, Mat puzzle, boolean isWithInpainting, boolean matchingByPyramids)
    {
        this.puzzlePiece = puzzlePiece;
        this.puzzle = puzzle;
        this.isWithInpainting = isWithInpainting;
        this.matchingByPyramids = matchingByPyramids;
    }

    @Override
    public Match call() throws Exception
    {
        int pieceNumber = puzzlePiece.getPieceNumber();

        System.out.println("Started assembling piece number: " + pieceNumber);

        Mat transformedPieceMatrix = puzzlePiece.getTransformedPieceMatrix();

        if (isWithInpainting)
        {
            PuzzlePieceRestorer.instance.restoreMissingGaps(transformedPieceMatrix, puzzlePiece.getOriginalSource());
        }

        Match bestMatch;
        if (matchingByPyramids)
        {
            bestMatch = FastTemplateMatcher.instance.findBestMatch(puzzle, transformedPieceMatrix, 3);
        }
        else
        {
            bestMatch = TemplateMatcher.instance.findBestMatch(puzzle, transformedPieceMatrix);
        }

        puzzlePiece.setBestRotationAngle(0);

        int numOfRotations = 0;
        Mat rotatedPuzzlePiece = transformedPieceMatrix;
        Match rotationBestMatch;

        while (numOfRotations <= 3)
        {
            if (bestMatch.getMatchValue() > 0.99)
            {
                System.out.println("Finished assembling piece number: " + pieceNumber + " best match percentage: " + bestMatch.getMatchValue() * 100 + "%");
                puzzlePiece.setBestRotationAngle(numOfRotations * 90);
                return bestMatch;
            }

            rotatedPuzzlePiece = ImageRotator.instance.rotateLeft(rotatedPuzzlePiece);
            numOfRotations++;

            if (matchingByPyramids)
            {
                rotationBestMatch = FastTemplateMatcher.instance.findBestMatch(puzzle, rotatedPuzzlePiece, 3);
            }
            else
            {
                rotationBestMatch = TemplateMatcher.instance.findBestMatch(puzzle, rotatedPuzzlePiece);
            }

            if (rotationBestMatch.compareTo(bestMatch) > 0)
            {
                puzzlePiece.setBestRotationAngle(numOfRotations * 90);
                bestMatch = rotationBestMatch;
            }
        }

        System.out.println("Finished assembling piece number: " + pieceNumber + " best match percentage: " + bestMatch.getMatchValue() * 100 + "%");

        return bestMatch;
    }

    static Callable<Match> createAssembler(PuzzlePiece puzzlePiece, Mat puzzle, boolean isWithInpainting, boolean matchingByPyramids)
    {
        return new PuzzlePieceAssembler(puzzlePiece, puzzle, isWithInpainting, matchingByPyramids);
    }
}
