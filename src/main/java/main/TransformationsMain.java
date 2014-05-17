package main;

import java.util.Arrays;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;

import pieceRecognizer.PuzzlePiece;
import pieceRecognizer.PuzzlePieceCornerDetector;
import pieceRecognizer.PuzzlePieceDetector;
import transformations.AffineTransformator;
import transformations.PuzzlePieceTransformer;
import utillities.Utilities;
import entities.Puzzle;

public class TransformationsMain {
    public static void main(String[] args) throws Exception {
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
	for (PuzzlePiece puzzlePiece : pieces) {
	    PuzzlePieceCornerDetector.cornerFindeByQuaters(harisBlockSize, harisApertureSize, harisK,
		    harisThreashHold, puzzlePiece);
	    Utilities.writeImageToFile(puzzlePiece.getPiece(), "BP" + count + ".jpg");
	    count++;
	}

	Puzzle puzzle = PuzzlePieceTransformer.instance.transformPieces(puzzleImg, pieces, 5, 4);

	Utilities.writeImageToFile(puzzle.getCompletePuzzle(), "p0.jpg");
	int i = 1;
	for (Mat piece : puzzle.getPieces()) {
	    Utilities.writeImageToFile(piece, "P" + i + ".jpg");
	    i++;
	}

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
