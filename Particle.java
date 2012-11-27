/*
 * This class has been modified for use in the MadBlueBalls game.
 * 
 * Modifications:
 * 	velocity constructor -- for setting initial velocity
 * 
 *
 */
public class Particle extends VectorObject {
	private double lifespan;
	
	// Constructor has been modified
	public Particle(int x, int y, double vX, double vY, double mass, double damping, double lifespan)
	{
		super();
		setLocation(new Vector(x,y));
		setVelocity(new Vector(vX, vY));
		setMass(mass);
		setDamping(damping);
		this.lifespan = lifespan;
	}
	
	public double getLifespan()
	{
		return lifespan;
	}
	
	public void setLifespan(double l)
	{
		lifespan = l;
	}
	
	public boolean isDead()
	{
		return lifespan<0.0;
	}
}
