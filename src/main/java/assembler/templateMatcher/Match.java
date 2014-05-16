package assembler.templateMatcher;

import org.opencv.core.Point;

public class Match implements Comparable<Match>
{
    private final Point matchPoint;
    private final Double matchValue;
    private final double width;
    private final double height;

    public static Match of(Point matchPoint, Double matchValue, double width, double height)
    {
        return new Match(matchPoint, matchValue, width, height);
    }

    private Match(Point matchPoint, double matchValue, double width, double height)
    {
        this.matchPoint = matchPoint;
        this.matchValue = matchValue;
        this.width = width;
        this.height = height;
    }

    public Point getMatchPoint()
    {
        return matchPoint;
    }

    public Double getMatchValue()
    {
        return matchValue;
    }

    public double getWidth()
    {
        return width;
    }

    public double getHeight()
    {
        return height;
    }

    @Override
    public int compareTo(Match other)
    {
        int matchValueComparisonResult = getMatchValue().compareTo(other.getMatchValue());

        return matchValueComparisonResult == 0 ? 1 : matchValueComparisonResult;
    }
}
