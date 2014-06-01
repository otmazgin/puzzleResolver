package main;

import java.io.FileInputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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

        Properties prop = new Properties();
        // load a properties file from class path, inside static method
        prop.load(new FileInputStream("parameterFile.txt"));

        int hueMin = Integer.parseInt(prop.getProperty("background_h_min"));
        int saturationMin = Integer.parseInt(prop.getProperty("background_s_min"));
        int valueMin = Integer.parseInt(prop.getProperty("background_v_min"));
        int hueMax = Integer.parseInt(prop.getProperty("background_h_max"));
        int saturationMax = Integer.parseInt(prop.getProperty("background_s_max"));
        int valueMax = Integer.parseInt(prop.getProperty("background_v_max"));

        int harisApertureSize = Integer.parseInt(prop.getProperty("Haris_apertureSize"));
        double harisK = Double.parseDouble(prop.getProperty("Haris_k"));
        int harisBlockSize = Integer.parseInt(prop.getProperty("Haris_blockSize"));
        int harisThreashHold = Integer.parseInt(prop.getProperty("Haris_threashHold"));
        boolean auto_color_detect = Boolean.parseBoolean(prop.getProperty("auto_color_detect"));
        boolean isWithInpainting = Boolean.parseBoolean(prop.getProperty("with_inpainting"));
        boolean matchingByPyramids = Boolean.parseBoolean(prop.getProperty("matching_by_pyramids"));

        Mat puzzleImage = Utilities.readImage(args[0]);
        Mat piecesImage = Utilities.readImage(args[1]);

        //puzzleImage = new Mat(puzzleImage, new Rect(new Point(249, 2277), new Point(3062, 308)));

        List<PuzzlePiece> pieces = PuzzlePieceDetector.pieceDetector
                (piecesImage, hueMin, saturationMin, valueMin, hueMax, saturationMax, valueMax, auto_color_detect);

        //int count = 1;
        for (PuzzlePiece puzzlePiece : pieces)
        {
            PuzzlePieceCornerDetector.cornerFindeByQuaters
                    (harisBlockSize, harisApertureSize, harisK, harisThreashHold, puzzlePiece);
            //Utilities.writeImageToFile(puzzlePiece.getPiece(), "BP" + count + ".jpg");
            //count++;
        }

        Puzzle puzzle = PuzzlePieceTransformer.instance.transformPieces(puzzleImage, pieces, 5, 4);
        //writePiecesAndPuzzle(puzzle);

        Map<PuzzlePiece, Match> piecesMatches = PuzzleAssembler.instance.assemble(puzzle, isWithInpainting, matchingByPyramids);

        PuzzleMatchesDrawer.instance.drawMatches(piecesMatches, puzzle.getCompletePuzzle());
    }

    /*private static void writePiecesAndPuzzle(Puzzle puzzle)
    {
        Utilities.writeImageToFile(puzzle.getCompletePuzzle(), "p0.jpg");
        int i = 1;

        for (PuzzlePiece puzzlePiece : puzzle.getPieces())
        {
            Utilities.writeImageToFile(puzzlePiece.getTransformedPieceMatrix(), "P" + i + ".jpg");
            Utilities.writeImageToFile(puzzlePiece.getRotatedImage(), "R" + i + ".jpg");
            i++;
        }
    }*/
}
