package main;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import transformations.AffineTransformator;
import utillities.Utilities;

import java.util.Arrays;
import java.util.List;

public class TransformationsMain
{
    public static void main(String[] args) throws Exception
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        testAffineTransform();
    }

    private static void testAffineTransform() throws Exception
    {
        String turtlePiecePath = ClassLoader.getSystemResource("turtle_one_piece.png").getFile();
        String turtlePuzzlePath = ClassLoader.getSystemResource("turtle_puzzle.png").getFile();

        Mat puzzlePiece = Utilities.readImage(turtlePiecePath);
        Mat puzzle = Utilities.readImage(turtlePuzzlePath);

        int puzzleWidth = 400;
        int puzzleHeight = 300;
        Mat dstPuzzleImage = new Mat(new Size(puzzleWidth, puzzleHeight), puzzle.type());
        List<Point> srcPuzzlePoints = Arrays.asList(new Point(0, 0), new Point(0, puzzle.height()),
                new Point(puzzle.width(), puzzle.height()));
        List<Point> dstPuzzlePoints = Arrays.asList(new Point(0, 0), new Point(0, dstPuzzleImage.height()),
                new Point(dstPuzzleImage.width(), dstPuzzleImage.height()));
        Mat transformedPuzzle = AffineTransformator.instance.transform(puzzle, dstPuzzleImage,
                srcPuzzlePoints, dstPuzzlePoints);

        Mat dstPieceImage = new Mat(new Size(puzzleHeight / 2, puzzleWidth / 3), puzzlePiece.type());
        List<Point> srcPiecePoints = Arrays.asList(new Point(7, 9), new Point(128, 9), new Point(128, 136));

        List<Point> dstPiecePoints = Arrays.asList(new Point(0, 0), new Point(dstPieceImage.width(), 0),
                new Point(dstPieceImage.width(), dstPieceImage.height()));
        ;
        Mat transformedPiece = AffineTransformator.instance.transform(puzzlePiece, dstPieceImage,
                srcPiecePoints, dstPiecePoints);

        Utilities.writeImageToFile(dstPuzzleImage, "puzz_trans.png");
        Utilities.writeImageToFile(dstPieceImage, "piece_rans.png");
    }
}
