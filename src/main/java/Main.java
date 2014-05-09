import assembler.PuzzleAssembler;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import utillities.Utilities;

import java.util.ArrayList;
import java.util.List;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat image = Utilities.readImage("/1.jpg");
        Mat lips = Utilities.readImage("/lips.jpg");
        Mat rotatedLips = Utilities.readImage("/lips_rotated.jpg");
        Mat leftEye = Utilities.readImage("/leftEye.jpg");
        Mat nose = Utilities.readImage("/nose.jpg");
        Mat hair = Utilities.readImage("/hair.jpg");
        Mat dash = Utilities.readImage("/dash.jpg");

        List<Mat> puzzlePieces = new ArrayList<>();
        puzzlePieces.add(lips);
        puzzlePieces.add(leftEye);
        puzzlePieces.add(nose);
        puzzlePieces.add(hair);
        puzzlePieces.add(lips);
        puzzlePieces.add(dash);
        puzzlePieces.add(rotatedLips);

        PuzzleAssembler.instance.assemblePieces(puzzlePieces, image);
    }
}
