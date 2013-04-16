/**
* Denne klassen initialiserer den andre oblig-en i INF1010
* @author Martin Jensen (martjens)
* @version 0.1
*	@comment Verdt å merke seg ved oppgaven:
*						- Jeg har valgt å skrive det meste på engelsk, mener dette er enklere
*						- I metoden der det sjekkes om noen vil ha en bok, returnerer jeg boolsk-verdi isteden for boken, mener dette er enklere å sjekke mot, og mer korrekt.
*/


/**
* Nå som Facebook starter å miste brukere
* vil jeg introdusere Friendbook.
**/
class Friendbook
{
	/**
   * Denne metoden setter opp datastrukturen for
   * friendbook, en klasse som holder styr på
   * forbindelser mellom Person-objekter
   */
	private Person[] arr;


	public void setup() 
	{
		Person martin = new Person("Martin", 3, "poesi");
		Person emil = new Person("Emil", 3, "sport");
		Person lisa = new Person("Lisa", 3, "krim");
		Person ramzi = new Person("Ramzi", 3, "mat");

		arr = new Person[4];
		arr[0] = martin;
		arr[1] = emil;
		arr[2] = lisa;
		arr[3] = ramzi;



		// Meg først!
		martin.meets(lisa);

		// Lag koblinger for Lisa
		lisa.meets(ramzi);

		// Lag koblinger for Ramzi
		ramzi.meets(emil);

		// Sjekk bøker
		this.checkBooks();

		// Skriv ut all info om alle
		martin.printAll();

	}

	private void checkBooks() {
		checkBook(new Book("Java")); 
		checkBook(new Book("krim")); 
		checkBook(new Book("historie")); 
		checkBook(new Book("sport")); 
		checkBook(new Book("poesi")); 
		checkBook(new Book("sport")); 
		checkBook(new Book("poesi")); 
		checkBook(new Book("baby")); 
		checkBook(new Book("poesi"));
	}

	private void checkBook(Book b)
	{
		// Sort the array around
		Person[] bookChecks = new Person[4];
		bookChecks[0] = arr[1];
		bookChecks[1] = arr[3];
		bookChecks[2] = arr[2];
		bookChecks[3] = arr[0];

		for (Person p: bookChecks) 
		{
			if (p.wantsBook(b))
				break;
		}

	}

}

class Person
{
	private String 					name;
	private Person[] 				friends;
	private Person 					likerikke;
	private Person 					forelsketi;
	private Book 						book;
	private Container<Book>	library;

	/**
	 * Denne metoden initialiserer og 
	 * returnerer et person-objekt
   * @param n 				Navn på personen vi oppretter
   * @param lengde 		Størrelse på arrayet som holder kjenninger
   * @return Person 	Et nytt person-objekt.
   */
	Person(String n, int length, String kat)
	{
		this.friends 	= new Person[length];
		this.name 		= n;
		this.book 		= new Book(kat);
		this.library 	= new Container<Book>();
	}

	/**
   * En metode for å returnere variablen navn, som
   * holder objektets jeg-navn.
   * @return navn Navnet på personen i objektet
   */
	public String name()
	{
		return this.name;
	}

	/**
   * En metode for å se om denne Personen er kjent med
   * personen som vi sender inn som parameter
   * @param p Personen vi vil vite om vi er kjent med.
   * @return Boolean En bool som forteller oss om vi er kjente
   */
	public boolean hasMet(Person p)
	{
		for (Person person : this.friends) 
		{
			if (person.equals(p))
				return true;
		}
		return false;
	}

	/**
   * En metode for at vi skal bli kjent med en ny Person(p)
   * Det loopes over kjenner-arrayet og vi finner den første
   * "tomme" plassen, og setter Person(p) inn på den posisjonen.
   * @param p Personen vi vil bli kjent med
   */
	public void meets(Person p)
	{
		if (p.equals(this)) { return; }
		for (int i = 0; i < this.friends.length; i++) 
		{
			if (this.friends[i] == null) 
			{
				this.friends[i] = p;
				return;
			}
		}
	}

	/**
   * En metode for å sette en Person(p) til likerikke-klassevariabelen
   * Dette forteller oss hvem vi "ikke liker".
   * @param p Personen vi vil bli uvenn med
   */
	public void blirUvennMed(Person p)
	{
		if (p.equals(this)) { return; }
		this.likerikke = p;
	}

	/**
   * @see erKjentMed(Person p)
   */
	public boolean isFriends(Person p)
	{
		return this.hasMet(p);
	}

	/**
   * @see blirKjentMed(Person p)
   */
	public void addFriend(Person p)
	{
		this.meets(p);
	}


	/**
	*		En ny kommentar
	**/
	public void printFriends ()
	{
		for (Person p : this.friends) {
			if (!p.equals(this.likerikke)) {
				System.out.println(p.name());
			}
		}
	}
	public Person bestFriend()
	{
		return this.friends[0];
	}

	public Person[] known()
	{
		return this.friends;
	}

	public void printKnown () { 
		for (Person p: this.friends)
				if ( p!=null) System.out.print(p.name() + " "); 
			System.out.println("");
	}

	public String bestFriendName() 
	{
		if (this.bestFriend() == null) 
			return "Ingen";

		return this.bestFriend().name();				

	}

	public void printMe() {
		System.out.println(name + " er venn med " + bestFriendName());
		System.out.println(name + " liker " + this.book.category() + "-bøker og har " + this.library.count() + " av dem.");
	}

	public void printAll() { 
		printMe();
		if(this.bestFriend() != null) bestFriend().printAll();
	}

	public boolean hasInterest(Book b)
	{
		if (this.book.category().equals(b.category()))
			return true;

		return false;
	}


	/*
	*	Følte det her var mer korrekt å returnere en BOOL enn et bok-objekt.
	*/
	public boolean wantsBook(Book b)
	{
		if (this.hasInterest(b)) 
		{
			this.library.put(b);
			return true;
		}
		return false;

			
	}

}

class Container <T> {

	private T[] objects = (T[]) new Object[100]; 
	private int count = 0;

	public void put(T obj) 
	{
		objects[count] = obj;
		count ++; 
	}
	
	public T get() 
	{
		count--;
		return objects[count]; 
	}

	public int count() 
	{
		return count;
	}
}

class Book {
	private String category;

	Book(String cat)
	{
		this.category = cat;
	}

	public String category()
	{
		return this.category;
	}
}