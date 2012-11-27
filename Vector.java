
public class Vector {
	double x;
	double y;

	public Vector()
	{
		x = 0;
		y = 0;
	}
	
	public Vector(double x2, double y2)
	{
		this.x = x2;
		this.y = y2;
	}
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}
	
	public void limit(double x)
	{
		if (magnitude()>x)
		{
			normalize();
			mult(x);
		}
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public void invertY()
	{
		y = y * -1;
	}
	
	public void invertX()
	{
		x = x * -1;
	}
	
	public void add(Vector v)
	{
		this.x += v.x;
		this.y += v.y;
	}
	
	public void sub(Vector v)
	{
		this.x -= v.x;
		this.y -= v.y;
	}

	public static Vector sub(Vector v, Vector w)
	{
		double x = v.x - w.x;
		double y = v.y - w.y;
		return new Vector(x,y);
	}
	
	public void mult(double v)
	{
		this.x *= v;
		this.y *= v;
	}
	
	public void div(double v)
	{
		this.x /= v;
		this.y /= v;
	}
	
	public double magnitude()
	{
		return Math.sqrt(x*x+y*y);
	}
	
	public void normalize()
	{
		double mag = magnitude();
		if (mag != 0)
		{
			div(mag);
		}
	}
	
	public Vector get()
	{
		return new Vector(x, y);
	}
	
	public boolean isZero()
	{
		return (x==0 && y==0);
	}
	
	public String toString(){
		return "("+x+", "+y+")";
	}
	

}
