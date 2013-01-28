/**
* Denne klassen initialiserer den første oblig-en i INF1010
* @author Martin Jensen
* @version 0.1
*/
class Oblig1
{
	public static void main(String[] args) {
		Friendbook friendster = new Friendbook();
		friendster.setup();
	}
}

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
	public void setup() 
	{
		Person martin = new Person("Martin", 3);
		Person emil = new Person("Emil", 3);
		Person lisa = new Person("Lisa", 3);
		Person ramzi = new Person("Ramzi", 3);

		// Meg først!
		martin.blirKjentMed(emil);
		martin.blirKjentMed(lisa);
		martin.blirKjentMed(ramzi);

		// Lag koblinger for Emil
		emil.blirKjentMed(martin);
		emil.blirKjentMed(ramzi);
		emil.blirForelsketI(martin);
		emil.blirUvennMed(lisa);

		// Lag koblinger for Lisa
		lisa.blirKjentMed(emil);
		lisa.blirKjentMed(ramzi);
		lisa.blirForelsketI(ramzi);
		lisa.blirUvennMed(martin);

		// Lag koblinger for Ramzi
		ramzi.blirKjentMed(martin);
		ramzi.blirKjentMed(emil);
		ramzi.blirKjentMed(lisa);
		ramzi.blirUvennMed(emil);
		ramzi.blirForelsketI(lisa);

		// Skriv ut all info om alle
		martin.skrivUtAlt();
		emil.skrivUtAlt();
		lisa.skrivUtAlt();
		ramzi.skrivUtAlt();

		// Denne skal gi utskriften "Martin"
		Person [] lisaKjenner = lisa.hentKjenninger();
		System.out.println(lisaKjenner[1].hentBestevenn().hentNavn());

	}

}

class Person
{
	private String navn;
	private Person [] kjenner;
	private Person likerikke;
	private Person forelsketi;

	/**
	 * Denne metoden initialiserer og 
	 * returnerer et person-objekt
   * @param n 				Navn på personen vi oppretter
   * @param lengde 		Størrelse på arrayet som holder kjenninger
   * @return Person 	Et nytt person-objekt.
   */
	Person(String n, int lengde)
	{
		this.kjenner = new Person[lengde];
		this.navn = n;
	}

	/**
   * En metode for å returnere variablen navn, som
   * holder objektets jeg-navn.
   * @return navn Navnet på personen i objektet
   */
	public String hentNavn()
	{
		return this.navn;
	}

	/**
   * En metode for å se om denne Personen er kjent med
   * personen som vi sender inn som parameter
   * @param p Personen vi vil vite om vi er kjent med.
   * @return Boolean En bool som forteller oss om vi er kjente
   */
	public boolean erKjentMed(Person p)
	{
		for (Person person : this.kjenner) {
			if (person.equals(p)) {
				return true;
			}
		}
		return false;
	}

	/**
   * En metode for at vi skal bli kjent med en ny Person(p)
   * Det loopes over kjenner-arrayet og vi finner den første
   * "tomme" plassen, og setter Person(p) inn på den posisjonen.
   * @param p Personen vi vil bli kjent med
   */
	public void blirKjentMed(Person p)
	{
		if (p.equals(this)) { return; }
		for (int i = 0; i < this.kjenner.length; i++) {
			if (this.kjenner[i] == null) {
				this.kjenner[i] = p;
				return;
			}
		}
	}

	/**
   * En metode for å sette en Person(p) til forelsket-klassevariabelen
   * Dette forteller oss hvem vi er forelsket i.
   * @param p Personen vi vil bli forelsket i
   */
	public void blirForelsketI(Person p)
	{
		if (p.equals(this)) { return; }
		this.forelsketi = p;
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
	public boolean erVennMed(Person p)
	{
		return this.erKjentMed(p);
	}

	/**
   * @see blirKjentMed(Person p)
   */
	public void blirVennMed(Person p)
	{
		this.blirKjentMed(p);
	}


	public void skrivUtVenner ()
	{
		for (Person p : this.kjenner) {
			if (!p.equals(this.likerikke)) {
				System.out.println(p.hentNavn());
			}
		}
	}
	public Person hentBestevenn()
	{
		return this.kjenner[0];
	}

	public Person[] hentKjenninger()
	{
		return this.kjenner;
	}

	public void skrivUtKjenninger () { 
		for (Person p: kjenner)
				if ( p!=null) System.out.print(p.hentNavn() + " "); 
			System.out.println("");
	}

	public void skrivUtAlt() { 
		System.out.print(navn + " kjenner: "); 
		skrivUtKjenninger();
		if ( forelsketi != null) 
			System.out.println(navn +
				" er forelsket i " + forelsketi.hentNavn()); 
		if (likerikke != null)
			System.out.println(navn +
				" liker ikke " + likerikke .hentNavn());
	}

}