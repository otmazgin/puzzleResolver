package pieceRecognizer;

public class Corner implements Comparable<Corner> {
	
	private int i;
	private int j;
	private double grade;
	public Corner(int i, int j, double grade) {
		super();
		this.i = i;
		this.j = j;
		this.grade = grade;
	}
	public int getI() {
		return i;
	}
	public void setI(int i) {
		this.i = i;
	}
	public int getJ() {
		return j;
	}
	public void setJ(int j) {
		this.j = j;
	}
	public double getGrade() {
		return grade;
	}
	public void setGrade(double grade) {
		this.grade = grade;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(grade);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + i;
		result = prime * result + j;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
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
		if (i != other.i)
			return false;
		if (j != other.j)
			return false;
		return true;
	}
	@Override
	public int compareTo(Corner o) {
		if (this.getGrade() > o.getGrade())
		return 1;
		if (this.getGrade() == o.getGrade())
		return 0;
		return -1;
	}

}
