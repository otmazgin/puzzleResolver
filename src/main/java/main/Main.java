package main;

import static org.opencv.imgproc.Imgproc.GaussianBlur;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;

import transformations.AffineTransformator;
import utillities.Utilities;
import assembler.PuzzleAssembler;

public class Main {
    public static void main(String[] args) throws Exception {
	System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

	// testAssembling();
	testAffineTransform();
    }

    private static void testAssembling() throws Exception {
	Mat puzzle = Utilities.readImage("/puzzle1.jpg");

	Mat piece1 = Utilities.readImage("/piece1.jpg");
	Mat piece2 = Utilities.readImage("/piece2.jpg");
	Mat piece3 = Utilities.readImage("/piece3.jpg");
	Mat piece4 = Utilities.readImage("/piece4.jpg");

	Map<Integer, Mat> puzzlePieces = new HashMap<>();

	GaussianBlur(piece1, piece1, new Size(3, 3), 3);
	// Utilities.writeImageToFile(piece1, "blurred.jpg");

	puzzlePieces.put(1, piece1);
	puzzlePieces.put(2, piece2);
	puzzlePieces.put(3, piece3);
	puzzlePieces.put(4, piece4);

	double[] backgroundColor = { 255, 255, 255 };
	PuzzleAssembler.instance.assemblePieces(puzzlePieces, puzzle, backgroundColor);
    }

    private static void testAffineTransform() throws Exception {
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
