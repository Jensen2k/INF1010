class Oblig3
{
  public static void main(String[] args) {

    TestClass test = new TestClass();
    test.test();
  }
}

class TestClass
{
  public void test()
  {
    List<Node> allPersons = new List<Node>();

    Person p1 = new Person("Mats");
    Person p2 = new Person("Martin");
    Person p3 = new Person("Tony");

    allPersons.add(p1);
    allPersons.add(p2);
    allPersons.add(p3);
    allPersons.printAll();

  }
}

class Node<K, T> { 

    K key;
    T data;
    Node next;

    public Node(T obj) { 
       data = obj;
    }

}

class List <T> { 

    Node<T> head;

    @SuppressWarnings("unchecked")
    public void add(Object item) 
    { 
        Node<T> node = head;
        if(node == null) { this.head = new Node(item); return; }
        while(node != null)
        {
          if(node.next == null)
          {
            node.next = new Node(item);
            break;
          }
          node = node.next;
        }
    }

    @SuppressWarnings("unchecked")
    public int count()
    {
      if(head == null) return 0;

      Node<T> tmpNode = head;
      int count = 0;
      while(tmpNode != null) 
      {
        tmpNode = tmpNode.next;
        count++;
      }
      return count;
    }



    @SuppressWarnings("unchecked")
    public void printAll() 
    {
      if(head == null) return;

      Node<T> tmpNode = head;
      while(tmpNode != null) 
      {
       // if(tmpNode.data.isAssignableFrom(Person))
        //{
          Person p = (Person)tmpNode.data;
          System.out.println(p.name());
        //}
          tmpNode = tmpNode.next;
      }
    }


}

class Person
{
  private String          name;
  private List<Node> friends = new List<Node>();

  /**
   * Denne metoden initialiserer og 
   * returnerer et person-objekt
   * @param n         Navn på personen vi oppretter
   * @return Person   Et nytt person-objekt.
   */
  Person(String n)
  {
    this.name = n;
  }

  public int numFriends()
  {
    return friends.count();
  }

  /**
   * En metode for å returnere variablen navn, som
   * holder objektets jeg-navn.
   * @return navn Navnet på personen i objektet
   */
  public void setName(String name)
  {
    this.name = name;
  }

  public String name()
  {
    return this.name;
  }

  public boolean isFriends() {

  }

  public void printMeAndFriends(String prefix) {
    System.out.println(prefix + name);
    if (prefix.equals("")) {
      
    }
  }

  /**
   * En metode for at vi skal bli kjent med en ny Person(p)
   * @param p Personen vi vil bli kjent med
   */
  public void meets(Person p)
  {
    if(p == this) return;
    this.friends.add(p);
  }





}