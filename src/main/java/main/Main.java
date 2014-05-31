package main;

import java.util.List;
import java.util.Map;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;

import pieceRecognizer.PuzzlePieceCornerDetector;
import pieceRecognizer.PuzzlePieceDetector;
import transformations.PuzzlePieceTransformer;
import utillities.Utilities;
import assembler.PuzzleAssembler;
import assembler.PuzzleMatchesDrawer;
import assembler.templateMatcher.Match;
import entities.Puzzle;
import entities.PuzzlePiece;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat piecesImage = Utilities.readImage("test20.jpg");
        Mat puzzleImg = Utilities.readImage("IMG_1062.jpg");
        puzzleImg = new Mat(puzzleImg, new Rect(new Point(249, 2277), new Point(3062, 308)));
        int harisBlockSize = 2;
        int harisApertureSize = 5;
        double harisK = 0.05;
        int harisThreashHold = 0;

        List<PuzzlePiece> pieces = PuzzlePieceDetector.pieceDetector(piecesImage, 0, 0, 0, 0, 0, 0,
                Boolean.TRUE);

        int count = 1;
        for (PuzzlePiece puzzlePiece : pieces)
        {
            PuzzlePieceCornerDetector.cornerFindeByQuaters(harisBlockSize, harisApertureSize, harisK,
                    harisThreashHold, puzzlePiece);
            Utilities.writeImageToFile(puzzlePiece.getPiece(), "BP" + count + ".jpg");
            count++;
        }

        Puzzle puzzle = PuzzlePieceTransformer.instance.transformPieces(puzzleImg, pieces, 5, 4);

        writePiecesAndPuzzle(puzzle);

        assembleAndDraw(puzzle);
    }

    private static void writePiecesAndPuzzle(Puzzle puzzle)
    {
        Utilities.writeImageToFile(puzzle.getCompletePuzzle(), "p0.jpg");
        int i = 1;

        for (PuzzlePiece puzzlePiece : puzzle.getPieces())
        {
            Utilities.writeImageToFile(puzzlePiece.getTransformedPieceMatrix(), "P" + i + ".jpg");
            Utilities.writeImageToFile(puzzlePiece.getRotatedImage(), "R" + i + ".jpg");
            i++;
        }
    }

    private static void assembleAndDraw(Puzzle puzzle) throws Exception
    {
        Map<PuzzlePiece, Match> piecesMatches = PuzzleAssembler.instance.assemble(puzzle);

        PuzzleMatchesDrawer.instance.drawMatches(piecesMatches, puzzle.getCompletePuzzle());
    }
}
