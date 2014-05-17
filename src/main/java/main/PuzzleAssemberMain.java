package main;

import assembler.PuzzleAssembler;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import utillities.Utilities;

import java.util.HashMap;
import java.util.Map;

import static org.opencv.imgproc.Imgproc.GaussianBlur;

public class PuzzleAssemberMain
{
    public static void main(String[] args) throws Exception
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        testAssembling();
    }

    private static void testAssembling() throws Exception
    {
        Mat puzzle = Utilities.readImage("/puzzle1.jpg");

        Mat piece1 = Utilities.readImage("/piece1.jpg");
        Mat piece2 = Utilities.readImage("/piece2.jpg");
        Mat piece3 = Utilities.readImage("/piece3.jpg");
        Mat piece4 = Utilities.readImage("/piece4.jpg");

        Map<Integer, Mat> puzzlePieces = new HashMap<>();

        GaussianBlur(piece1, piece1, new Size(3, 3), 3);
        // Utilities.writeImageToFile(piece1, "blurred.jpg");

        //puzzlePieces.put(1, piece1);
        //puzzlePieces.put(2, piece2);
        puzzlePieces.put(3, piece3);
        puzzlePieces.put(4, piece4);

        double[] backgroundColor = {255, 255, 255};
        PuzzleAssembler.instance.assemblePieces(puzzlePieces, puzzle, backgroundColor);
    }

}
