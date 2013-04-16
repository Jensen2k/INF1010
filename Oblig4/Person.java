class Person {
	private String name;
	public MJContainer<String,Vehicle> vehicles = new MJContainer<String,Vehicle>();

	Person(String n)
	{
		this.name = n;
	}

	public String getName()
	{
		return this.name;
	}

}

class Mechanic extends Person
{
	Mechanic(String name)
	{
		super(name);
	}
}


