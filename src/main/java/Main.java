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
        Mat template = Utilities.readImage("/lips_rotated.jpg");

        List<Mat> puzzlePieces = new ArrayList<>();
        puzzlePieces.add(template);

        PuzzleAssembler.instance.assemblePieces(puzzlePieces, image);
    }
}
