package pieceRecognizer;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Properties;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import com.atul.JavaOpenCV.Imshow;

public class Main_test {



	public static void main (String args[]){
		Properties prop = new Properties(); 
		try {
			//load a properties file from class path, inside static method
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
			prop.load(new FileInputStream("parameterFile.txt"));


			int h_min = Integer.parseInt(prop.getProperty("background_h_min"));
			int s_min = Integer.parseInt(prop.getProperty("background_s_min"));
			int v_min = Integer.parseInt(prop.getProperty("background_v_min"));
			int h_max = Integer.parseInt(prop.getProperty("background_h_max"));
			int s_max = Integer.parseInt(prop.getProperty("background_s_max"));
			int v_max = Integer.parseInt(prop.getProperty("background_v_max"));

			int Haris_apertureSize = Integer.parseInt(prop.getProperty("Haris_apertureSize"));
			double Haris_k = Double.parseDouble(prop.getProperty("Haris_k"));
			int Haris_blockSize = Integer.parseInt(prop.getProperty("Haris_blockSize"));
			int Haris_threashHold  = Integer.parseInt(prop.getProperty("Haris_threashHold"));
			String file_location  = (prop.getProperty("src_pic"));
			Mat src = 	Highgui.imread (file_location);


			LinkedList<PuzzlePiece> Pices = new LinkedList<PuzzlePiece>();
			PuzzlePieceDetector.pieceDetector(
                    src,
                    h_min,
                    s_min,
                    v_min,
                    h_max,
                    s_max,
                    v_max,
                    Pices
            );
			




			for ( int i=0; i<Pices.size();i++){
				PuzzlePieceCornerDetector.corner_finder(
                        Haris_blockSize,
                        Haris_apertureSize,
                        Haris_k,
                        Haris_threashHold,
                        Pices.get(i).getMask(),
                        new Mat(),
                        Pices.get(i).getCornersList());
			}
			

			
			Imshow im = new Imshow("Title");
			Mat satImg = new Mat (src.size(), src.type());

			for( int i = 0; i< Pices.size(); i++ ){
				Pices.get(i).drawPice(satImg);
				Pices.get(i).drawcorners(satImg);
			}
			im.showImage(satImg);






		} 
		catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
