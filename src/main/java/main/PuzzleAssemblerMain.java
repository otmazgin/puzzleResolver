package main;

import assembler.PuzzleAssembler;
import assembler.PuzzleMatchesDrawer;
import assembler.templateMatcher.Match;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import utillities.Utilities;

import java.util.HashMap;
import java.util.Map;

import static org.opencv.imgproc.Imgproc.GaussianBlur;

public class PuzzleAssemblerMain
{
    public static void main(String[] args) throws Exception
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        testAssembling();
    }

    private static void testAssembling() throws Exception
    {
        Mat puzzle = Utilities.readImage("/p0.jpg");

        Mat piece1 = Utilities.readImage("/P1.jpg");
        Mat piece2 = Utilities.readImage("/P2.jpg");
        Mat piece3 = Utilities.readImage("/P3.jpg");

        Map<Integer, Mat> puzzlePieces = new HashMap<>();

        puzzlePieces.put(1, piece1);
        puzzlePieces.put(2, piece2);
        puzzlePieces.put(3, piece3);

        Map<Integer, Match> piecesMatches = PuzzleAssembler.instance.assemblePieces(puzzlePieces, puzzle, new double[]{101, 224, 180});

        PuzzleMatchesDrawer.instance.drawMatches(piecesMatches, puzzle);

        Utilities.writeImageToFile(puzzle, "matches.jpg");
    }
}
