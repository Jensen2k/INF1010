import java.util.Iterator;
import java.util.Arrays;

import java.io.*;
import java.util.Scanner;


class Oblig4
{
  public static void main(String[] args) {
    TestClass test = new TestClass();
    test.test();
  }
}


class MJReader {

  

  /**
   * Reads the relationship data from file
   * @return A container with all the relation-data
   */
  public MJContainer<String,MJContainer> readRelations()
  {
    // Location of file to read
    File file = new File("eierOgRepData.txt");
    MJContainer<String,MJContainer> data = new MJContainer<String,MJContainer>();

    data.add("EIERSKAP", new MJContainer<String, String[]>());
    data.add("REPARASJONER", new MJContainer<String, String[]>());
    try {
      Scanner scanner = new Scanner(file);
      int sectionOffset = 0;
      String currSection = "";

      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();

            // Check if we are entering a section
        if(data.contains(line)) {
          sectionOffset = 0;
          currSection = line;
          continue;
        }
        sectionOffset++;

        // Jump over the section count-line
        if(sectionOffset == 1) continue;

        if(currSection.equals("EIERSKAP"))
        {
          data.get(currSection).add(lineSegment(line,0), new String[]{ lineSegment(line,0), lineSegment(line,1) });
        }

        if(currSection.equals("REPARASJONER"))
        {
          data.get(currSection).add(lineSegment(line,0)+lineSegment(line,1), new String[]{ lineSegment(line,0), lineSegment(line,1) } );
        }

      }
      scanner.close();

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return data;
  }

  /**
   * Reads all data from personer.txt to a data-container
   * @return A data-container (MJContainer) with all app-data
   */
  public MJContainer<String,MJContainer> readPersons()
  {
      // Location of file to read
    File file = new File("personer.txt");
    MJContainer<String,MJContainer> data = new MJContainer<String,MJContainer>();

      // Array of the section we look for in the file
    String[] sections = new String[]{ "PERSONER", "MEKANIKERE", "PERSONBILER", "LASTEBILER", "BUSSER" };

    data.add("VEHICLES", new MJContainer<String, Vehicle>());
    data.add("PERSONS", new MJContainer<String, Person>());

    try {
      Scanner scanner = new Scanner(file);
      int sectionOffset = 0;
      String currSection = "";
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();

              // Check if we are entering a section
        if(Arrays.asList(sections).contains(line)) {
          sectionOffset = 0;
          currSection = line;
          continue;
        }
        sectionOffset++;

              // If it's the first line in the section, then it's just a count. No use.
        if(sectionOffset == 1) continue;

        if(currSection.equals("PERSONER"))
        {
          String name = line;
          Person p = new Person(name);
          data.get("PERSONS").add(name, p);
        }

        if(currSection.equals("MEKANIKERE"))
        {
          String name = line;
          Mechanic m = new Mechanic(name);
          data.get("PERSONS").add(name, m);
        }

        if(currSection.equals("PERSONBILER"))
        {
          Car car = new Car(lineSegment(line, 0), Integer.parseInt(lineSegment(line, 1)));
          data.get("VEHICLES").add(lineSegment(line, 0), car);
        }

        if(currSection.equals("LASTEBILER"))
        {
          Truck truck = new Truck(lineSegment(line, 0), Integer.parseInt(lineSegment(line, 1)));
          data.get("VEHICLES").add(lineSegment(line, 0), truck);
        }

        if(currSection.equals("BUSSER"))
        {
          Bus bus = new Bus(lineSegment(line, 0), Integer.parseInt(lineSegment(line, 1)));
          data.get("VEHICLES").add(lineSegment(line, 0), bus);
        }


      }
      scanner.close();

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    return data;

  }

  /**
   * Splits and returns a segment of a two-space-delimited string
   * @param line A line with segments separated by to spaces
   * @param offset What segment of the separated string should be returned
   * @return The segment of the offset specified
   */
  public String lineSegment(String line, int offset)
  {
    String[] split = line.split("  ");
    return split[offset];
  }


}

/**
 * The main class responsible for all app-testing and glue
 */
class TestClass
{
  private MJContainer<String, MJContainer> data;
  private MJContainer<String, MJContainer> relationships;
  private MJContainer<String, Integer> statistics = new MJContainer<String, Integer>();

    /**
     * The main test-method for the test-class which is responsible for all app-logic
     * Is called from the Oblig4-class
     */
    public void test()
    {
      MJReader reader = new MJReader();
      data = reader.readPersons();

      relationships = reader.readRelations();
      mergeData(data, relationships);

      calculateTaxes();
      printStatistics();
    }

    /**
     * Merges all app-data by relation-definitions
     * @param data The data that should be merged
     * @param relations The relation-definitions determining what data should be merged
     */
    private void mergeData(MJContainer<String,MJContainer> data, MJContainer<String,MJContainer> relations)
    {

       /**
        * Merge persons with vehicles
        */
       MJContainer<String,Person> persons = data.get("PERSONS");
       MJContainer<String,String[]> vehicleRelations = relations.get("EIERSKAP");

       for (String[] person : vehicleRelations) {
        String identifier = person[0];
        String name = person[1];
        Vehicle vehicle = findVehicle(identifier);
        Person p = findPerson(name);

        vehicle.setOwner(p);
        p.vehicles.add(identifier, vehicle);
      }

        /**
         *  Merge vehicles with mechanics
         */
        MJContainer<String,Person> vehicles = data.get("PERSONS");
        MJContainer<String,String[]> vehicleRepairs = relations.get("REPARASJONER");

        for (String[] repair : vehicleRepairs) {
          String identifier = repair[0];
          String name = repair[1];

          Vehicle vehicle = findVehicle(identifier);
          Person p = findPerson(name);

          vehicle.repairs.add(p);
        }
      }

    /**
     * The method responsible for calling tax-calculation on vehicles
     * It also finds out what vehicles that should be de-licenced.
     *
     * Rule: All vehicles that have a owner (who is not a mechanic) that has done repair on his own vehicle.
     */
    private void calculateTaxes()
    {

      MJContainer<String, Vehicle> vehicles = data.get("VEHICLES");
      MJContainer<String, Person> persons = data.get("PERSONS");

      int unvalidCount = 0;
      int count = 0;
      Double maxTax = 0.0;
      Double minTax = 0.0;
      for(Vehicle v : vehicles)
      {
        Person owner = v.getOwner();
        if(v.repairs.contains(owner) && !Mechanic.class.isInstance(owner))
        {
          // Owner has repaired his own car, and is not mechanic
          vehicles.remove(v.getIdentifier());
          unvalidCount++;
        }
        if(v.calculateTax() > maxTax) maxTax = v.calculateTax();
        if(v.calculateTax() < minTax || count == 0) minTax = v.calculateTax();

        count++;
      }
      this.statistics.add("Antall avregistrert", unvalidCount);
      this.statistics.add("Høyste avgift", maxTax.intValue());
      this.statistics.add("Minste avgift", minTax.intValue());


    }

    /**
     * Does a pretty-print of app-statistics
     */
    private void printStatistics()
    {
      MJContainer<String, Person> persons = data.get("PERSONS");
      int maxVehicles = 0;
      Person maxVehiclesPerson = null;
      for(Person p : persons)
      {
        if(p.vehicles.count() > maxVehicles) {
          maxVehicles = p.vehicles.count();
          maxVehiclesPerson = p;
        }
      }
      System.out.println("");
      System.out.println(" _____   __       ___                    __ __      ");
      System.out.println("/\\  __`\\/\\ \\     /\\_ \\    __            /\\ \\\\ \\     ");
      System.out.println("\\ \\ \\/\\ \\ \\ \\____\\//\\ \\  /\\_\\     __    \\ \\ \\\\ \\    ");
      System.out.println(" \\ \\ \\ \\ \\ \\ '__`\\ \\ \\ \\ \\/\\ \\  /'_ `\\   \\ \\ \\\\ \\_  ");
      System.out.println("  \\ \\ \\_\\ \\ \\ \\L\\ \\ \\_\\ \\_\\ \\ \\/\\ \\L\\ \\   \\ \\__ ,__\\");
      System.out.println("   \\ \\_____\\ \\_,__/ /\\____\\\\ \\_\\ \\____ \\   \\/_/\\_\\_/");
      System.out.println("    \\/_____/\\/___/  \\/____/ \\/_/\\/___L\\ \\     \\/_/  ");
      System.out.println("                                  /\\____/           ");
      System.out.println("                                  \\_/__/            ");

      System.out.println("Gjennomkjøring av data fullført.");
      System.out.println("");
      System.out.println("-------------");
      System.out.println("Statistikk:");
      System.out.println("-------------");
      System.out.println("Person med flest kjøretøy: "+maxVehiclesPerson.getName()+" med "+maxVehiclesPerson.vehicles.count()+" kjøretøy");
      this.statistics.printAll();
      System.out.println("");
      System.out.println("");
      System.out.println("                    _______");
      System.out.println("         .--.      [Oblig 5]");
      System.out.println("    .----'   '--.      |");
      System.out.println(" ***'-()-----()-'      |");
      System.out.println("");
      System.out.println("");

    }

    /**
     * Looks up and returns a Person-object from the main person-container, by a name-key
     * @param name: The name to lookup in the container
     * @return A person-object
     */
    private Person findPerson(String name)
    {
      MJContainer<String, Person> persons = data.get("PERSONS");
      return persons.get(name);
    }

    /**
     * Looks up and returns a Vehicle-object from the main vehicle-container, by identifier-key
     * @param identifier
     * @return A vehicle-object
     */
    private Vehicle findVehicle(String identifier)
    {
      MJContainer<String, Vehicle> vehicles = data.get("VEHICLES");
      return vehicles.get(identifier);
    }

  }

  interface MJCollection<K extends Comparable<K>, V> extends Iterable<V>
  {
    void add(K key, V v);

    int count();

    V get(K key);

    V get(int n);

    V getLowest();

    V getLargest();

    boolean contains(K key);

    boolean remove(K key);

    void empty();

    V[] toArray(V[] a);
  }

  class Node<K, V> {

    K key;
    V data;
    Node next;

    public Node(K k, V obj) {
      key = k;
      data = obj;
    }

    /**
     * Returns the generic key for the node
     * @return The key referencing to the value for the node
     */
    public K getKey() {
      return key;
    }

    /**
     * Get the value
     * @return Generic value object
     */
    public V getValue()
    {
      return data;
    }

  }


  class MJContainer<K extends Comparable<K>, V> implements MJCollection<K,V> {

    Node <K,V> head;

    public void add(K key, V v)
    {
      Node<K,V> node = head;
      Node<K,V> prev = head;

      if(contains(key)) { return; }
      Node<K,V> newNode = new Node(key, v);

      if(node == null) { this.head = newNode; return; }
      while(node != null)
      {

        if(newNode.getKey().compareTo(node.getKey()) < 0) {
          if(node == head) {
            head = newNode;
            newNode.next = node;
          } else {
            prev.next = newNode;
            newNode.next = node;
          }
          break;
        }
        if(node.next == null) {
          node.next = new Node(key, v); return;
        }

        prev = node;
        node = node.next;

      }
    }

    public void printAll()
    {
      if(head == null) return;
      Node<K,V> tmpNode = head;
      while(tmpNode != null)
      {
        System.out.println(tmpNode.getKey()+": "+tmpNode.getValue());
        tmpNode = tmpNode.next;
      }
    }

    public int indexOf(K key)
    {
      if(head == null) return 0;

      Node<K,V> tmpNode = head;
      int count = 0;
      while(tmpNode != null)
      {
        if(tmpNode.key.equals(key)) { return count; }
        tmpNode = tmpNode.next;
        count++;
      }
      return 0;
    }

    public int count()
    {
      if(head == null) return 0;

      Node<K,V> tmpNode = head;
      int count = 0;
      while(tmpNode != null)
      {
        tmpNode = tmpNode.next;
        count++;
      }
      return count;
    }

    public V get(K key)
    {
      if(head == null) return null;

      Node<K,V> tmpNode = head;
      while(tmpNode != null)
      {
        if(tmpNode.key.equals(key)) { return tmpNode.getValue(); }
        tmpNode = tmpNode.next;
      }
      return null;
    }

    public V get(int n)
    {
      if(head == null) return null;

      Node<K,V> tmpNode = head;
      int count = 1;
      while(tmpNode != null)
      {
        if(count == n) { return tmpNode.data; }
        tmpNode = tmpNode.next;
        count++;
      }
      return null;
    }

    public V getLowest()
    {
      if(head == null) return null;

      Node<K,V> tmpNode = head;
      K lowest = head.getKey();
      while(tmpNode != null)
      {
        if(tmpNode.getKey().compareTo(lowest) < 0) {
          lowest = tmpNode.getKey();
        }
        tmpNode = tmpNode.next;
      }
      return get(lowest);
    }

    public V getLargest()
    {
      if(head == null) return null;

      Node<K,V> tmpNode = head;
      K highest = head.getKey();
      while(tmpNode != null)
      {
        if(tmpNode.getKey().compareTo(highest) > 0) {
          highest = tmpNode.getKey();
        }
        tmpNode = tmpNode.next;
      }
      return get(highest);
    }

    public boolean contains(K key)
    {
      if(head == null) return false;

      Node<K,V> tmpNode = head;
      while(tmpNode != null)
      {
        if(tmpNode.getKey().equals(key)) {
          return true;
        }
        tmpNode = tmpNode.next;
      }
      return false;
    }

    public boolean remove(K key)
    {
      if(head == null) return false;

      Node<K,V> tmpNode = head;
      Node<K,V> prevNode = head;
      while(tmpNode != null)
      {
        if(tmpNode.getKey().equals(key)) {
          if(tmpNode == head) {
            head = tmpNode.next;
            return true;
          }
          prevNode.next = tmpNode.next;
          return true;
        }
        prevNode = tmpNode;
        tmpNode = tmpNode.next;
      }
      return false;
    }

    public void empty()
    {
      head = null;
    }

    public V[] toArray(V[] a)
    {
      if(head == null) return a;

      Node<K,V> tmpNode = head;
      int count = 0;
      while(tmpNode != null)
      {
        a[count] = tmpNode.getValue();
        tmpNode = tmpNode.next;
        count++;
      }
      return a;
    }

    public String toString()
    {
      String str = "";
      if(head == null) return str;

      Node<K,V> tmpNode = head;
      while(tmpNode != null)
      {
        str += tmpNode.getValue().toString();
        str += "\n";
        tmpNode = tmpNode.next;
      }
      return str;
    }

    public Iterator<V> iterator() {
      return new CollectionIterator();
    }

    private class CollectionIterator implements Iterator<V> {
      Node curr;
      Node prev;
      Node prevPrev;

      CollectionIterator() {
        curr = head;
        prev = head;
        prevPrev = head;
      }

      public boolean hasNext()
      {
        return curr != null;
      }

      public V next() {
        V v = (V)curr.getValue();
        if(curr != head && prev.next == curr) {
          prev = curr;
          if(prev != null){
            prevPrev = prev;
          }
        }
        curr = curr.next;
        return v;
      }

      public void remove() {
        if(head == null) return;

        Node<K,V> tmpNode = head;
        Node<K,V> prevNode = head;
        while(tmpNode != null)
        {
          if(tmpNode.equals(prevNode)) {
            if(tmpNode == head) {
              head = tmpNode.next;
              return;
            }
            prevNode.next = tmpNode.next;
            return ;
          }
          prevNode = tmpNode;
          tmpNode = tmpNode.next;
        }
        }
    }
  }


