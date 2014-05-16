package pieceRecognizer;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import com.atul.JavaOpenCV.Imshow;

public class MainTest {
    public static void main(String args[]) {

	System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	try {
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

	    String filePath = (prop.getProperty("src_pic"));
	    Mat src = Highgui.imread(filePath);

	    List<PuzzlePiece> pieces = PuzzlePieceDetector.pieceDetector(src, hueMin, saturationMin,
		    valueMin, hueMax, saturationMax, valueMax,auto_color_detect);

	    for (PuzzlePiece puzzlePiece : pieces) {
		PuzzlePieceCornerDetector.cornerFindeByQuaters(harisBlockSize, harisApertureSize, harisK,
			harisThreashHold, puzzlePiece);

		/*
		 * PuzzelPiceCornerDetector.corner_finder( Haris_blockSize , Haris_apertureSize, Haris_k ,
		 * Haris_threashHold, Pices.get(i).getMask(), new Mat() , Pices.get(i).getCornersList());
		 */

	    }

	    Imshow im = new Imshow("Title");
	    Mat satImg = new Mat(src.size(), src.type());

	    for (PuzzlePiece puzzlePiece : pieces) {
		puzzlePiece.drawPiece(satImg);
		puzzlePiece.drawCorners(satImg);

	    }

	    im.showImage(satImg);

	} catch (IOException ex) {
	    ex.printStackTrace();
	}
    }
}
