package pieceRecognizer;

public class Corner implements Comparable<Corner>
{
    private int x;
    private int y;
    private double grade;

    public Corner(int x, int y, double grade)
    {
        super();
        this.x = x;
        this.y = y;
        this.grade = grade;
    }

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public double getGrade()
    {
        return grade;
    }

    public void setGrade(double grade)
    {
        this.grade = grade;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(grade);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Corner other = (Corner) obj;
        if (Double.doubleToLongBits(grade) != Double
                .doubleToLongBits(other.grade))
            return false;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        return true;
    }

    @Override
    public int compareTo(Corner o)
    {
        if (this.getGrade() > o.getGrade())
            return 1;
        if (this.getGrade() == o.getGrade())
            return 0;
        return -1;
    }

}
