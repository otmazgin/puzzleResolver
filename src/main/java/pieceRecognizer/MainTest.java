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
	Properties prop = new Properties();
	try {
	    // load a properties file from class path, inside static method
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
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
	    String filePath = (prop.getProperty("src_pic"));
	    Mat src = Highgui.imread(filePath);

	    List<PuzzlePiece> Pieces = PuzzlePieceDetector.pieceDetector(src, hueMin, saturationMin, valueMin,
		    hueMax, saturationMax, valueMax);

	    for (int i = 0; i < Pieces.size(); i++) {

		PuzzlePieceCornerDetector.cornerFindeByQuaters(harisBlockSize, harisApertureSize, harisK,
			harisThreashHold, Pieces.get(i).getMask(), Pieces.get(i).getRect(), new Mat(), Pieces
				.get(i).getCornersList()); /*
							    * PuzzelPiceCornerDetector.corner_finder(
							    * Haris_blockSize , Haris_apertureSize, Haris_k ,
							    * Haris_threashHold, Pices.get(i).getMask(), new
							    * Mat() , Pices.get(i).getCornersList());
							    */
	    }

	    Imshow im = new Imshow("Title");
	    Mat satImg = new Mat(src.size(), src.type());

	    for (int i = 0; i < Pieces.size(); i++) {
		Pieces.get(i).drawPice(satImg);
		Pieces.get(i).drawCorners(satImg);
	    }
	    im.showImage(satImg);

	} catch (IOException ex) {
	    ex.printStackTrace();
	}
    }
}
