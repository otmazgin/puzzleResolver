package utillities;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;

import java.io.*;
import java.net.URLDecoder;
import java.util.List;
import java.util.SortedMap;

public class Utilities
{
    public static String readResource(String resourceName)
    {
        String resource = resourceName;

        // this will replace %20 with spaces
        if (resource.startsWith("/", 0))
        {
            resource = resource.replaceFirst("/", "");
        }

        try
        {
            return URLDecoder.decode(resource, "UTF-8");
        } catch (UnsupportedEncodingException e)
        {
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

    public static void drawRect(Rect rect, Mat image)
    {
        Core.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 0, 255));
    }

    public static void writeImageToFile(Mat image, String fileName)
    {
        System.out.println(String.format("Writing %s", fileName));
        Highgui.imwrite(fileName, image);
    }
}
