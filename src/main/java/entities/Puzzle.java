package entities;

import java.util.Collections;
import java.util.List;

import org.opencv.core.Mat;

public class Puzzle {

    public final Mat       completePuzzle;
    public final List<Mat> pieces;

    public Puzzle(Mat puzzle, List<Mat> pieces) {
	this.completePuzzle = puzzle;
	this.pieces = Collections.unmodifiableList(pieces);
    }

    public Mat getCompletePuzzle() {
	return this.completePuzzle;
    }

    public List<Mat> getPieces() {
	return this.pieces;
    }

}
