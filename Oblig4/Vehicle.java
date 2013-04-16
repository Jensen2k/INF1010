import java.util.ArrayList;

abstract class Vehicle {
	private String identifier;
	public int tax;
	private Person owner;
	ArrayList<Person> repairs = new ArrayList<Person>();

	Vehicle(String identifier, int tax) {
		this.identifier = identifier;
		this.tax = tax;
	}

    /**
     * The identifier for the vehicle (also called license plate)
     * @return The identifier as a string
     */
    public String getIdentifier()
    {
    	return this.identifier;
    }

    /**
     * Get the owner of the vehicle
     * @return A Person-object
     */
    public Person getOwner()
    {
    	return this.owner;
    }

    /**
     * Set's the owner of the vehicle
     * @param owner
     */
    public void setOwner(Person owner)
    {
    	this.owner = owner;
    }

    /**
     * Returns the tax-ratio for this vehicle (see subclass)
     * @return A double representing a 0 to 1-value of the tax-ratio
     */
    private double taxRatio() {
    	return 0.0;
    }

    /**
     * Calculates the tax from the tax and tax ratio
     * @return A double representing the tax
     */
    public double calculateTax()
    {
    	return tax*taxRatio();
    }

    /**
     * Loops all repairs and finds out the ratio of mechanics to non-mechanics (0 to 1)
     * @return A double from 0 to 1 representing the ratio
     */
    public double authorizedRepairRatio()
    {
    	double total = this.repairs.size();
    	double count = 0;
    	for (Person p : this.repairs) {
    		if (Mechanic.class.isInstance(p)) {
    			count++;
    		}
    	}

    	if(total == 0 || count == 0) return 0;

    	double result = (1/(total/count));
    	return (double)Math.round(result * 100) / 100;
    }

  }

  class Bus extends Vehicle
  {
  	Bus(String identifier, int tax)
  	{
  		super(identifier, tax);
  	}

  	private double taxRatio()
  	{
  		if(authorizedRepairRatio() == 1.0 || repairs.size() == 0)
  		{
  			return 0.034;
  		}
  		return 0.12;
  	}

  	public double calculateTax()
  	{
  		return tax*taxRatio();
  	}

  }

  class Truck extends Vehicle
  {
  	Truck(String identifier, int tax)
  	{
  		super(identifier, tax);
  	}

  	private double taxRatio()
  	{
  		if(authorizedRepairRatio() == 1.0 || repairs.size() == 0)
  		{
  			return 0.034;
  		}
  		return 0.12;
  	}

  	public double calculateTax()
  	{
  		return tax*taxRatio();
  	}

  }

  class Car extends Vehicle
  {
  	Car(String identifier, int tax)
  	{
  		super(identifier, tax);
  	}

  	private double taxRatio()
  	{
  		if(authorizedRepairRatio() == 1.0 || repairs.size() == 0)
  		{
  			return 0.05;
  		}
  		if(authorizedRepairRatio() >= 0.5)
  		{
  			return 0.075;
  		}

  		return 0.12;
  	}

  	public double calculateTax()
  	{
  		return tax*taxRatio();
  	}

  }