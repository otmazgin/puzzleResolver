import org.opencv.core.*;
import utillities.Optional;
import utillities.Utilities;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat image = Utilities.readImage("/1.jpg");
        Mat template = Utilities.readImage("/lips_rotated.jpg");

        Optional<Point> matchingPoint = TemplateMatcher.instance.findBestMatching(image, template);
        int numOfDirectionsTested = 1;
        
        Mat rotatedTemplate = template;
        
        while (!matchingPoint.isPresent() && numOfDirectionsTested <= 4)
        {
            rotatedTemplate = ImageRotator.instance.rotateToLeft(rotatedTemplate);

            matchingPoint = TemplateMatcher.instance.findBestMatching(image, rotatedTemplate);

            numOfDirectionsTested++;
        }
        
        if (matchingPoint.isPresent())
        {
            if (numOfDirectionsTested % 2 == 0)
            {
                Utilities.drawRectAndStore(new Rect(matchingPoint.get(), new Size(template.rows(), template.cols())), image, "match.jpg");
            }
            else
            {
                Utilities.drawRectAndStore(new Rect(matchingPoint.get(), new Size(template.cols(), template.rows())), image, "match.jpg");
            }
        }
        else
        {
            throw new RuntimeException("Match not found!");
        }
    }
}
