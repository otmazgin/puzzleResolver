package utillities;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;

import java.io.*;
import java.net.URLDecoder;
import java.util.List;
import java.util.SortedMap;

public class Utilities
{
	public static String readResource(String resourceName) {
		String resource = resourceName;

		// this will replace %20 with spaces
		if (resource.startsWith("/", 0)) {
			resource = resource.replaceFirst("/", "");
		}

		try {
			return URLDecoder.decode(resource, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static Mat readImage(String fileName) throws Exception
    {
        Mat image = Highgui.imread(Utilities.readResource(fileName));

        if (image.size().equals(new Size(0, 0)))
        {
            throw new Exception("Could not read the image: " + fileName);
        }

        return image;
	}

	public static void drawDetectedParts(MatOfRect detectedParts, Mat image, String outputFileName) {
		// Draw a bounding box around each face.
		for (Rect rect : detectedParts.toArray()) {
			drawRect(rect, image);
		}

		writeImageToFile(image, outputFileName);
	}

	public static void drawRectAndStore(Rect rect, Mat image, String outputFileName) {
		drawRect(rect, image);

		writeImageToFile(image, outputFileName);
	}

	public static void drawRect(Rect rect, Mat image) {
		Core.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 0, 0));
	}

	public static void drawPoint(Point point, Mat image) {
		Core.line(image, point, new Point(point.x, point.y + 1), new Scalar(0, 0, 0), 5);
	}

	public static void drawLine(Point first, Point second, Mat image) {
		Core.line(image, first, second, new Scalar(0, 0, 0));
	}

	public static void writeImageToFile(Mat image, String fileName) {
		System.out.println(String.format("Writing %s", fileName));
		Highgui.imwrite(fileName, image);
	}

	public static void drawCollectionLineOf(Mat image, List<Point> points) {
		for (Point point : points) {
			Utilities.drawPoint(point, image);
		}

		int i = 0;
		int j = 1;
		while (j < points.size()) {
			Utilities.drawLine(points.get(i++), points.get(j++), image);
		}
	}

	/**
	 * @throws java.io.IOException
	 * */
	public static void writeObjectToFile(SortedMap<Integer, double[][]> pulseSamples, String fileName) throws IOException {
		File file = new File(fileName);

		if (file.exists()) {
			file.delete();
		}

		file.createNewFile();

		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
			out.writeObject(pulseSamples);
		}
	}

	/**
	 * @throws Exception
	 * */
	public static Object readObjectFromFile(String fileName) throws Exception {

		Object obj = null;
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
			obj = ois.readObject();
		}

		return obj;
	}
}
