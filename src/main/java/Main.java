import assembler.PuzzleAssembler;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import utillities.Utilities;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.imgproc.Imgproc.COLOR_RGB2GRAY;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.photo.Photo.INPAINT_TELEA;
import static org.opencv.photo.Photo.inpaint;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat image = Utilities.readImage("/1.jpg");

        /*Mat lips = Utilities.readImage("/lips.jpg");
        Mat rotatedLips = Utilities.readImage("/lips_rotated.jpg");
        Mat leftEye = Utilities.readImage("/leftEye.jpg");
        Mat nose = Utilities.readImage("/nose.jpg");
        Mat hair = Utilities.readImage("/hair.jpg");
        Mat dash = Utilities.readImage("/dash.jpg");
*/
        Mat eyePiece = Utilities.readImage("/eyePiece.jpg");
        //Mat lipsPiece = Utilities.readImage("/lipsPiece.jpg");

        List<Mat> puzzlePieces = new ArrayList<>();
        /*puzzlePieces.add(lips);
        puzzlePieces.add(leftEye);
        puzzlePieces.add(nose);
        puzzlePieces.add(hair);
        puzzlePieces.add(lips);
        puzzlePieces.add(dash);
        puzzlePieces.add(rotatedLips);
*/
        inpaintPuzzlePieceHoles(eyePiece);
        puzzlePieces.add(eyePiece);

       PuzzleAssembler.instance.assemblePieces(puzzlePieces, image);
    }

    public static void inpaintPuzzlePieceHoles(Mat puzzlePiece)
    {
        Mat maskOfGaps = new Mat();
        cvtColor(puzzlePiece, maskOfGaps, COLOR_RGB2GRAY);

        for (int row = 0; row < puzzlePiece.rows(); row++)
        {
            for (int column = 0; column < puzzlePiece.cols(); column++)
            {
                if (maskOfGaps.get(row, column)[0]>200)
                {
                    maskOfGaps.put(row, column, 1);
                }
                else
                {
                    maskOfGaps.put(row, column, 0);
                }
            }
        }

        inpaint(puzzlePiece, maskOfGaps, puzzlePiece, 1, INPAINT_TELEA);
        Utilities.writeImageToFile(puzzlePiece, "inpainted.jpg");
    }
}
