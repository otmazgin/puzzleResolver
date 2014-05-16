package main;

import assembler.PuzzleAssembler;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import utillities.Utilities;

import java.util.HashMap;
import java.util.Map;

import static org.opencv.imgproc.Imgproc.GaussianBlur;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat image = Utilities.readImage("/1.jpg");

	    Mat eyePiece = Utilities.readImage("/eyePiece.jpg");

        Map<Integer, Mat> puzzlePieces = new HashMap<>();

        GaussianBlur(eyePiece, eyePiece, new Size(1, 1), 3);
        Utilities.writeImageToFile(eyePiece, "blurred.jpg");

        puzzlePieces.put(1, eyePiece);

        double[] backgroundColor = {255, 255, 255};
        PuzzleAssembler.instance.assemblePieces(puzzlePieces, image, backgroundColor);
    }

    private void testAffineTransform() throws Exception
    {
        Mat puzzlePiece = Utilities.readImage("turtle_one_piece.png");
    }
}
