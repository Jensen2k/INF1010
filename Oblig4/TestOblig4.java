import java.util.LinkedList;
import java.util.Iterator;
import java.util.Arrays;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

/**
 * Test class made to test part A and B of Oblig 4 for the course INF1010
 * @author Erlend Vestad
 */
class TestSamling {
    /**
     * Used in all tests requiring a collection with elements. Must be
     * initialized at the start of each testmethod. Use the populate()
     * method to initialize at the beginning of testmethods.
     * 
     * @see NAMES
     */
    private MJCollection<String, TestPerson> tb;
    
    /**
     * Used in all tests requiring an empty collection
     */
    private MJCollection<String, TestPerson> empty;
    
    /**
     * Used to store Test objects, and later retrieve them. When a new Test 
     * object is created it will automatically add itself to this list. 
     */
    private LinkedList<Test> tests = new LinkedList<Test>();
    
    /**
     * Used to create TestPersons and populate collections
     */
    private static String[] NAMES = { "Fry", "Bender", "Zoidberg", "Leela",
    "Professor", "Hermes" };

    /**
     * Used to check if collection is sorted
     */
    private static String[] sortedNames = Arrays.copyOf(NAMES, NAMES.length);

    /**
     * Used to check for non-existant objects in collection. Do not add
     * this element to collection
     */
    private TestPerson nonPerson = new TestPerson("IShouldNotBeHere");
    
    /**
     * IMPORTANT NOTE: Replace MJContainer in this method with the name of your own class
     * if your implementation has another classname
     * 
     * Used at the start of a testmethod to ensure predictable content.
     * Uses add() method to create a fresh collection filled with TestPersons. 
     * The names of the TestPersons are defined in the String[] NAMES array.
     * The new filled collection is stored in pointer "tb".
     * If method is called at the start of a testmethod this ensures that
     * testmethods does not change the collection from one testmethod to another.
     * 
     * Will also create an empty collection and store it in pointer "empty"
     * 
     * @see tb
     * @see empty
     */
    private void resetCollections() {
        tb = new MJContainer<String, TestPerson>();
        empty = new MJContainer<String, TestPerson>();
        
        //populate collection with TestPerson objects. This is used throughout testing
        for (TestPerson p: testPersoner()) {
            tb.add(p.navn, p);
        }
    }
    
    /**
     * Runs all tests
     */
    public boolean runTests() {
        boolean liInne = leggInnInneholder();
        boolean liHentN = leggInnHentNokkel();
        boolean liHentI = leggInnHentIndex();
        boolean liAnt = leggInnAntall();
        
        if (liHentN || liHentI || liInne)
            hentMinste("");
        else
            hentMinste("En av testene for leggInn maa passere foerst");
        
        if (liHentN || liHentI || liInne)
            hentStorste("");
        else
            hentStorste("En av testene for leggInn maa passere");
        
        if (liInne)
            fjernElement("");
        else
            fjernElement("Tester for leggInn og inneholer maa passere");
        
        if (liInne)
            fjernAlle("");
        else
            fjernAlle("Tester for leggInn og inneholder maa passere");
        
        if (liInne)
            tilArray("");
        else
            tilArray("Tester for leggInn og inneholder maa passere");
        
        if (liAnt)
            noDuplicates("");
        else
            noDuplicates("Tester for leggInn og antall maa passere");

        if (liInne && liAnt)
            iterator("");
        else
            iterator("Tester for leggInn, inneholder og antall maa passere");

        if (liInne && liAnt)
            iteratorRemove("");
        else
            iteratorRemove("Tester for leggInn, inneholder og antall maa passere");

        printResult();
        
        //easterEgg();
        return false;
    }
    
    /**
     * Iterates through tests and prints the results to stdout()
     */
    public void printResult() {
        System.out.println("\n*** Test Collection ***");
        String passed = "";
        String failed = "";
        String skipped = "";
        int p = 0;
        int f = 0;
        int s = 0;
        for (Test t: tests) {
            if (t.skipped()) {
                skipped = skipped + "\n** Testname: " + t.title + " - SKIPPED\nWhy: " +t.whySkipped + "\n";
                s++;
            } else if (t.passed) {
                p++;
                passed = passed + "\n** Testname: " + t.title + " - PASSED";
            } else {
                f++;
                failed = failed + "\n** Testname: " + t.title + " - FAILED\n" + t.description + "\n\nDetails: " + t.details + "\n";
            }
        }
        System.out.println("Failed: " + f + "  Passed: " + p + "  Skipped: " + s + "\n");
        System.out.println("leggInn() i rekkefoelgen:\t|\tSkal ende opp sortert som:");
        for (int i=0; i < NAMES.length; i++) {
            if (NAMES[i].length() > 6)
                System.out.println(NAMES[i] + "\t\t\t|\t" + sortedNames[i]);
            else
                System.out.println(NAMES[i] + "\t\t\t\t|\t" + sortedNames[i]);
        }
        
        System.out.print("\n------ Failed " + f + " tests ------" + failed);
        System.out.print("\n------ Passed " + p + " tests ------" + passed + "\n");
        System.out.print("\n------ Skipped " + s + " tests ------" + skipped + "\n");
        
        if (p == tests.size()) {
            System.out.println("\n\nAll tests pass, YOU WIN!\n\n");
            System.out.println("                     Tb.          Tb.                                \n                     :$$b.        $$$b.                              \n                     :$$$$b.      :$$$$b.                            \n                     :$$$$$$b     :$$$$$$b                           \n                      $$$$$$$b     $$$$$$$b                          \n                      $$$$$$$$b    :$$$$$$$b                         \n                      :$$$$$$$$b---^$$$$$$$$b                        \n                      :$$$$$$$$$b        \"\"^Tb                       \n                       $$$$$$$$$$b    __...__`.                      \n                       $$$$$$$$$$$b.g$$$$$$$$$pb                     \n                       $$$$$$$$$$$$$$$$$$$$$$$$$b                    \n                       $$$$$$$$$$$$$$$$$$$$$$$$$$b                   \n                       :$$$$$$$$$$$$$$$$$$$$$$$$$$;                  \n                       :$$$$$$$$$$$$$^T$$$$$$$$$$P;                  \n                       :$$$$$$$$$$$$$b  \"^T$$$$P\' :                  \n                       :$$$$$$$$$$$$$$b._.g$$$$$p.db                 \n                       :$$$$$$$$$$$$$$$$$$$$$$$$$$$$;                \n                       :$$$$$$$$\"\"\"^^T$$$$$$$$$$$$P^;                \n                       :$$$$$$$$       \"\"^^T$$$P^\'  ;                \n                       :$$$$$$$$    .\'       `\"     ;                \n                       $$$$$$$$;   /                :                \n                       $$$$$$$$;           .----,   :                \n                       $$$$$$$$;         ,\"          ;               \n                       $$$$$$$$$p.                   |               \n                      :$$$$$$$$$$$$p.                :               \n                      :$$$$$$$$$$$$$$$p.            .\'               \n                      :$$$$$$$$$$$$$$$$$$p...___..-\"                 \n                      $$$$$$$$$$$$$$$$$$$$$$$$$;                     \n   .db.          bug  $$$$$$$$$$$$$$$$$$$$$$$$$$                     \n  d$$$$bp.            $$$$$$$$$$$$$$$$$$$$$$$$$$;                    \n d$$$$$$$$$$pp..__..gg$$$$$$$$$$$$$$$$$$$$$$$$$$$                    \nd$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$p._            .gp. \n$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$p._.ggp._.d$$$$b\n");
        }
		}

		/**
     * Tests method leggInn() and inneholder(). This method is a prerequisite
     * for most of the other tests, seeing as the collection needs to contain 
     * elements if any other method is to be tested.
     */
    private boolean leggInnInneholder() {
        resetCollections();
        Test t = new Test("leggInn og inneholder", "Legger inn objekter i kolleksjonen og sjekker at inneholder(N n) returnerer true.", null);
        
        if (tb.contains(nonPerson.navn)) {
            t.failed("inneholder(N n) returnerer true paa en nokkel " + nonPerson.navn + " som ikke er lagt inn i kolleksjonen");
            return false;
        }
        
        for (String n: NAMES) {
            if (!tb.contains(n)) {
                t.failed("Har lagt inn objekt med noekkel \"" + n +  "\" i kolleksjonen, men inneholder(\"" + n + "\") returnerer false");
                return false;
            }
        }
        
        return t.passed;
    }
    
    /**
     * Tests if leggInn method adds elements and retrieves them with the right key.
     */
    private boolean leggInnHentNokkel() {
        resetCollections();
        Test t = new Test("leggInn og hent paa nokkel", "Legger inn objekter i kolleksjonen og sjekker at get(N n) returnerer riktig objekt.", null);
        
        TestPerson shouldBeNull = tb.get(nonPerson.navn);
        if (shouldBeNull != null) {
            t.failed("get(\"" + nonPerson.navn + "\") returnerer " + shouldBeNull + " paa en nokkel som ikke er lagt inn i kolleksjonen");
            return false;
        }
        
        for (String n: NAMES) {
            TestPerson testperson = tb.get(n);
            if (testperson == null) {
                t.failed("Har lagt inn objekt med noekkel \"" + n +  "\" i kolleksjonen, men get(\"" + n + "\") returnerer null");
                return false;
            } else {
                if (!testperson.navn.equals(n)) {
                    t.failed("Har lagt inn objekt med nokkel \"" + n + "\", men get(\"" + n + "\") returnerer " + testperson);
                    return false;
                }
            }
        }
        
        return t.passed;
    }
    
    /**
     * Tests if the collection adds elements with leggInn and retrieves them with
     * get(int nr) on the right index. Checks if collection is sorted
     */
    private boolean leggInnHentIndex() {
        resetCollections();
        Test t = new Test("add() og get(int nr) paa indeks", "Legger inn objekter i kolleksjonen og sjekker at get(int nr) returnerer objektene i riktig rekkefoelge", null);
        
        TestPerson shouldBeNull = empty.get(1);
        if (shouldBeNull != null) {
            t.failed("get(1) returnerer " + shouldBeNull + " selv om kolleksjonen ikke inneholder noen elementer. Denne metoden burde returnere null");
            return false;
        }
        
        for (int i = 1; i < sortedNames.length; i++) {
            TestPerson testperson = tb.get(i);
            if (testperson == null) {
                t.failed("get(" + i + ") returnerer null selv om " + new TestPerson(sortedNames[i-1]) + " skal ligge paa denne plassen");
                return false;
            } else {
                if (!testperson.navn.equals(sortedNames[i-1])) {
                    t.failed("get(" + i + ")  returnerer " + testperson + ". Ved riktig sortering skal den returnere " + new TestPerson(sortedNames[i-1]));
                    return false;
                }
            }
        }
        
        TestPerson outOfBounds = tb.get(NAMES.length+1);
        if (outOfBounds != null) {
            t.failed("get(int nr) med et nr som er stoerre enn antall elementer lagt inn i samlingen returnerer " + outOfBounds + ". Skal returnere null om element med indeks ikke eksisterer");
            return false;
        }
        
        return t.passed;
    }
    
    /**
     * Check if antall is correct after adding elements
     */
    private boolean leggInnAntall() {
        resetCollections();
        Test t = new Test("add() og count()", "Legger inn objekter og sjekker at count() returnerer riktig antall elementer i samlingen", null);
        
        if (empty.count() != 0) {
            t.failed("count() returnerer " + empty.count() + " paa en tom kolleksjon");
        }
        
        for (int i = 1; i < NAMES.length-1; i++) {
            empty.add(NAMES[i], new TestPerson(NAMES[i]));
            if (empty.count() != i) {
                t.failed("Legger til element nr " + i + " i samlingen, men count() returnerer " + empty.count());
                return false;
            }
        }
        
        return t.passed;
    }
    
    /**
     * Check if hentMinste returns the element sorted as the smallest key in the collection
     * @param skippedReason  A string representation of why this test is skipped
     */
    private boolean hentMinste(String skippedReason) {
        resetCollections();
        Test t = new Test("hentMinste()", "Sjekker at samlingen returnerer elementet med den minste nokkelen", skippedReason);
        
        if (t.skipped()) {
            return false;
        }
        
        TestPerson shouldBeNull = empty.getLowest();
        if (shouldBeNull != null) {
            t.failed("getLowest() returnerer " + shouldBeNull + " selv om kolleksjonen ikke inneholder noen elementer. Denne metoden burde returnere null");
            return false;
        }
        
        TestPerson tp = tb.getLowest();
        if (tp == null) {
            t.failed("getLowest() skal returnere " + new TestPerson(sortedNames[0]) + " men returnerer " + tp);
            return false;
        }
        
        if (!tp.navn.equals(sortedNames[0])) {
            t.failed("getLowest() skal returnere " + new TestPerson(sortedNames[0]) + " men returnerer " + tp);
            return false;
        }
        
        return t.passed;
    }
    
     /**
     * Check if hentStorste returns the element sorted as the largest key in the collection
     * @param skippedReason  A string representation of why this test is skipped
     */
     private boolean hentStorste(String skippedReason) {
        resetCollections();
        Test t = new Test("getLargest()", "Sjekker at samlingen returnerer elementet med den storste nokkelen", skippedReason);
        
        if (t.skipped()) {
            return false;
        }
        
        TestPerson shouldBeNull = empty.getLargest();
        if (shouldBeNull != null) {
            t.failed("hentMinste() returnerer " + shouldBeNull + " selv om kolleksjonen ikke inneholder noen elementer. Denne metoden burde returnere null");
            return false;
        }
        
        TestPerson tp = tb.getLargest();
        if (tp == null) {
            t.failed("getLargest() skal returnere " + new TestPerson(sortedNames[sortedNames.length-1]) + " men returnerer " + tp);
            return false;
        }
        
        if (!tp.navn.equals(sortedNames[sortedNames.length-1])) {
            t.failed("getLargest() skal returnere " + new TestPerson(sortedNames[sortedNames.length-1]) + " men returnerer " + tp);
            return false;
        }
        
        return t.passed;
    }
    
    /**
     * Checks that fjernElement(N n) removes the correct 
     * element and antall() is correct afterwards
     * @param skippedReason  A string representation of why this test is skipped
     */
    private boolean fjernElement(String skippedReason) {
        resetCollections();
        Test t = new Test("fjernElement()", "Sjekker at fjernElement(N n) fjerner riktig element fra samlingen og har riktig antall etterpaa", skippedReason);
        
        if (t.skipped()) {
            return false;
        }
        try {
            if (empty.remove("BarrackObama")) {
                t.failed("fjernElement(N, n) returnerer true selv man kaller den paa en tom samling. Skal gi false");
                return false;
            }      
            
        } catch(RuntimeException e) {
            StackTraceElement[] st = e.getStackTrace();
            String mes = e.getClass().getName();
            if (e.getMessage() != null)
                mes = mes + ": " + e.getMessage();

            for (int i=0; i < st.length-3; i++) {
                mes = mes + "\n\tat " + st[i];
            }
            
            t.failed("Kraesjer paa linje " + st[0].getLineNumber() + " ved fjernElement(N n) paa en tom samling:\n" + mes);
            return false;
        }
        
        try {
            if (tb.remove("God_does_not_exist_yolo")) {
                t.failed("fjernElement(N, n) returnerer true selv man kaller fjernElement paa en nokkel som aldri har blitt lagt til i samlingen. Skal gi false");
                return false;
            }      
            
        } catch(RuntimeException e) {
            StackTraceElement[] st = e.getStackTrace();
            String mes = e.getClass().getName();
            if (e.getMessage() != null)
                mes = mes + ": " + e.getMessage();

            for (int i=0; i < st.length-3; i++) {
                mes = mes + "\n\tat " + st[i];
            }
            
            t.failed("Kraesjer paa linje " + st[0].getLineNumber() + " ved kall paa fjernElement(N n) en nokkel som ikke er lagret i samlingen:\n" + mes);
            return false;
        }
        
        String removed = "Foer dette har det blitt gjort kall paa:\n";
        for (int i = 0; i < NAMES.length; i++) {
            if (tb.contains(NAMES[i])) {
                boolean res = tb.remove(NAMES[i]);
                removed = removed + "fjernElement(\"" + NAMES[i] + "\")\n";
                if (!res) {
                    t.failed("fjernElement(\"" + NAMES[i] + "\") returnerer false selv om samlingen inneholder element lagt inn paa nokkel \"" + NAMES[i] + "\"");
                    return false;
                }
                
                int nr = tb.count();
                int riktigNr = NAMES.length -1 -i;
                if (nr != riktigNr) {
                    t.failed("count() returnerer " + nr + " selv om det bare er fjernet " + (i+1) + " elementer.\nAntall skulle returnert " + riktigNr + "\n\n" + removed);
                    return false;
                }
            } else {
                t.failed("inneholder(\"" + NAMES[i] + "\") returnerer false selv om fjernElement(\"" + NAMES[i] + "\") aldri har blitt kalt.\nDette betyr at et tidligere kall paa fjernElement har fjernet feil element i samlingen, eller at et tidligere kall har slettet flere elementer enn det skulle\n\n" + removed);
                return false;
            }
        }
        
        return t.passed;
    }
    
    /**
     * Checks if fjernAlle method removes all elements and antall() is 0
     * @param skippedReason  A string representation of why this test is skipped
     */
    private boolean fjernAlle(String skippedReason) {
        resetCollections();
        Test t = new Test("fjernAlle()", "Sjekker at inneholder() ikke returnerer elementer etter at fjernAlle() er kalt. Sjekker ogsaa at count() gir 0", skippedReason);        
        
        if (t.skipped()) {
            return false;
        }
        
        tb.empty();
        
        for (int i=0; i < NAMES.length; i++) {
            if (tb.contains(NAMES[i])) {
                t.failed("empty() er kalt men inneholder(\"" + NAMES[i] + "\") returnerer true");
                return false;
            }
        }
        
        int ant = tb.count();
        if (ant != 0) {
            t.failed("empty() har blitt kalt, men count() returnerer " + ant + ". Skal vaere 0");
            return false;
        }
        
        return t.passed;
    }
    
    /**
     * Checks if tilArray(V[] a) returns all elements in collection in sorted order (by key N)
     * @param skippedReason  A string representation of why this test is skipped
     */
    private boolean tilArray (String skippedReason) {
        resetCollections();
        Test t = new Test("toArray", "Sjekker om toArray(V[] a) returnerer alle elementene i samlingen sortert paa nokkel N.", skippedReason);
        
        if (t.skipped()) {
            return false;    
        }
        
        TestPerson res[];
        try {
            res = tb.toArray(new TestPerson[NAMES.length]);
        } catch(RuntimeException e) {
            e.printStackTrace();
            StackTraceElement[] st = e.getStackTrace();
            String mes = e.getClass().getName();
            if (e.getMessage() != null)
                mes = mes + ": " + e.getMessage();

            for (int i=0; i < st.length-3; i++) {
                mes = mes + "\n\tat " + st[i];
            }
            
            t.failed("Kraesjer paa linje " + st[0].getLineNumber() + " ved kall paa " + st[st.length-3].getMethodName() + "(new TestPerson[" + NAMES.length + "]):\n" + mes);
            return false;
        }
        
        for (int i=0; i < sortedNames.length; i++) {
            if (res[i] == null) {
                t.failed("toArray(v[] a) returnerer en array hvor indeks " + i + " i arrayet == null, forventet element: TestPerson: " + sortedNames[i]);
                return false;
            }
            
            if (!res[i].navn.equals(sortedNames[i])) {
                String s = "toArray(V[] a) returnerer ikke elementene sortert fra minste til storste nokkel:\nReturnert array:\t\t|\tRiktig resultat skal vaere:\n";
                for (int j=0; j < res.length; j++) {
                    if (res[j].navn.length() > 4)
                        s = s + res[j] + "\t\t|\t" + "TestPerson: " + sortedNames[j] + "\n";
                    else
                        s =  s + res[j] + "\t\t\t|\t" + "TestPerson: " + sortedNames[j] + "\n";
                }
                t.failed(s);
                return false;
            }
        }
        
        return t.passed;
    }
    
    /**
     * Checks if iterator returns all elements in collection in sorted order (sorted on key N)
     * @param skippedReason  A string representation of why this test is skipped
     */
    @SuppressWarnings("unchecked")
    public boolean iterator(String skippedReason) {
        resetCollections();
        Test t;
        
        Method iteratorMethod = null;
        try {
            iteratorMethod = tb.getClass().getMethod("iterator", (Class<?>[]) null);
            t = new Test("iterator() hasNext() og next()", "sjekker om iterator() gir tilbake et Iterator objekt som inneholder alle elementene i samlingen", skippedReason);
            if (iteratorMethod != null) {
                try {
                    Object o = iteratorMethod.invoke(tb,(Object[]) null);
                    Iterator<TestPerson> iter = (Iterator<TestPerson>) o;
                    
                    try {
                        int counter = 0;
                        while (iter.hasNext()) {
                            TestPerson v= iter.next();
                            counter++;
                            
                            if (counter > tb.count()) {
                                t.failed("har iterert gjennom alle elementene med next(), men hasNext() gir true.");
                                return false;
                            }
                        }
                        
                    } catch (RuntimeException e) {
                        StackTraceElement[] st = e.getStackTrace();
                        String mes = e.getClass().getName();
                        if (e.getMessage() != null)
                            mes = mes + ": " + e.getMessage();

                        for (int i=0; i < st.length-6; i++) {
                            mes = mes + "\n\tat " + st[i];
                        }
                        t.failed("iterator() kraesjer naar man itererer gjennom alle elementer med next()" + mes);
                        return false;
                    }
                    
                    if (tb.count() != NAMES.length) {
                        t.failed("iterasjon gjennom samlingen har oedelagt samlingen. Etter iterasjon av samlingen er antall " + tb.count() + ". Antall elementer i samlingen skulle vaert " + NAMES.length);
                        return false;
                    }
                    
                    for (String n: NAMES) {
                        if (!tb.contains(n)) {
                            t.failed("iterasjon gjennom samlingen har oedelagt samlingen. Etter aa ha iterert gjennom alle elementer mangler TestPerson: " + n);
                            return false;
                        }
                    }
                    
                    Object iterObj = iteratorMethod.invoke(tb,(Object[]) null);
                    iter = (Iterator<TestPerson>) iterObj;
                    int counter = 0;
                    if (iter != null) {
                        boolean stop = false;
                        while (!stop) {
                            if (iter.hasNext()) {
                                TestPerson tp = iter.next();
                                if (tp == null) {
                                    t.failed("hasNext() returnerer true, men next() returnerer null. Naar hasNext() gir true betyr det at iteratoren skal inneholde flere elementer");
                                    return false;
                                }
                                
                                if (!tp.navn.equals(sortedNames[counter])) {
                                    t.failed("iteratoren returnerer ikke elementene i sortert rekkefoelge. next() returnerte " + tp + ". Ved riktig sorteringe skulle den returnert TestPerson: " + sortedNames[counter]);
                                    return false;
                                }
                                counter++;
                                
                            } else {
                                stop = true;
                                if (counter != sortedNames.length) {
                                    t.failed("iteratoren itererer ikke gjennom alle elementene. hasNext() returnerer false selv om iteratoren skulle inneholdt " + (sortedNames.length - counter) + " flere elementer.");
                                    return false;
                                }
                            }
                        }
                    } else {
                        t.failed("Iterator method returns null");
                        return false;
                    }
                    
                    Object obj = iteratorMethod.invoke(empty, (Object[]) null);
                    Iterator<TestPerson> iterempty = (Iterator<TestPerson>) obj;
                    if (iterempty != null) {
                        if (iterempty.hasNext()) {
                            t.failed("har kalt iterator() paa en tom samling. Kall paa hasNext() skal da gi false, men returnerer true");
                            return false;
                        }
                        
                    } else {
                        t.failed("iterator() kalt paa en tom samling gir null. Skal returnere et Iterator objekt");
                        return false;
                    }

                } catch (IllegalAccessException e) {
                    StackTraceElement[] st = e.getStackTrace();
                    String mes = e.getClass().getName();
                    if (e.getMessage() != null)
                        mes = mes + ": " + e.getMessage();

                    for (int i=0; i < st.length-3; i++) {
                        mes = mes + "\n\tat " + st[i];
                    }
                    
                    t = new Test("iterator() hasNext() og next()", "sjekker om iterator() gir tilbake et Iterator objekt som inneholder alle elementene i samlingen", "iterator() method is inaccesible, make sure the method is declared with public access control\n" + mes);
                    return false;
                } catch (IllegalArgumentException e) {
                    StackTraceElement[] st = e.getStackTrace();
                    String mes = e.getClass().getName();
                    if (e.getMessage() != null)
                        mes = mes + ": " + e.getMessage();

                    for (int i=0; i < st.length-3; i++) {
                        mes = mes + "\n\tat " + st[i];
                    }
                    
                    t = new Test("iterator() hasNext() og next()", "sjekker om iterator() gir tilbake et Iterator objekt som inneholder alle elementene i samlingen", "Illegal parameters when trying to invoke iterator() method, iterator() should take no parameters\n" + mes);
                    return false;
                } catch (InvocationTargetException e) {
                    Throwable target = e.getTargetException();
                    StackTraceElement[] st = target.getStackTrace();
                    String mes = target.getClass().getName();
                    if (target.getMessage() != null)
                        mes = mes + ": " + target.getMessage();

                    for (int i=0; i < st.length-6; i++) {
                        mes = mes + "\n\tat " + st[i];
                    }
                    
                    t = new Test("iterator() hasNext() og next()", "sjekker om iterator() gir tilbake et Iterator objekt som inneholder alle elementene i samlingen", null);
                    t.failed("Iterator crashed while iterating through the elements:\n" + mes);
                    return false;
                }
            }
        } catch (NoSuchMethodException e) {
            t = new Test("iterator() hasNext() og next()", "sjekker om iterator() gir tilbake et Iterator objekt som inneholder alle elementene i samlingen", "iterator() metoden er ikke implementert");
            return false;
        }
        
        return t.passed;
    }

    /**
    * Checks if iterators remove() can remove all elements after iterating with next()
    */
    @SuppressWarnings("unchecked")
    private boolean iteratorRemove(String skippedReason) {
        resetCollections();
        Test t;
        
        Method iteratorMethod = null;
        try {
            iteratorMethod = tb.getClass().getMethod("iterator", (Class<?>[]) null);
            t = new Test("iterate and remove() with iterator", "itererer gjennom alle elementene i iterator() med next() og kaller remove etter hver next()", skippedReason);        
            try {
                Object or = iteratorMethod.invoke(tb, (Object[]) null);
                Iterator<TestPerson> iterRemove = (Iterator<TestPerson>) or;
                if (iterRemove != null) {
                    int remc = 0;
                    while (iterRemove.hasNext()) {
                        TestPerson tp = iterRemove.next();
                        if (tp == null) {
                            t.failed(".next() returned null instead of " + "TestPerson: " + sortedNames[remc]);
                            return false;
                        } else {
                            iterRemove.remove();
                                    //if (tb.inneholder(tp.navn)) {
                            if (tb.contains(sortedNames[remc])) {
                                System.out.println(tp);
                                t.failed("iteratorens remove() metode fjerner ikke fra selve samlingen. kalte next() som returnerte TestPerson: " + sortedNames[remc] + ", og deretter remove() i iteratoren(). Men kall paa inneholder(\"" + sortedNames[remc] + "\") i samlingen returnerer true.");
                                return false;
                            }
                        }
                        remc++;
                    }

                    if (tb.count() != 0) {
                        t.failed("Har fjernet alle elementene i samlingen med iteratorens remove(), men antall() returnerer " + tb.count());
                        return false;
                    }

                } else {
                    t.failed("iterator() returnerer null. Skal returnere et Iterator objekt");
                    return false;
                }
            } catch (RuntimeException e) {
                StackTraceElement[] st = e.getStackTrace();
                String mes = e.getClass().getName();
                if (e.getMessage() != null)
                    mes = mes + ": " + e.getMessage();

                for (int i=0; i < st.length-6; i++) {
                    mes = mes + "\n\tat " + st[i];
                }
                t.failed("iterator() kraesjer ved kall paa iteratorens remove() metode" + mes);
                return false;
            }
        } catch (IllegalAccessException e) {
            StackTraceElement[] st = e.getStackTrace();
            String mes = e.getClass().getName();
            if (e.getMessage() != null)
                mes = mes + ": " + e.getMessage();

            for (int i=0; i < st.length-3; i++) {
                mes = mes + "\n\tat " + st[i];
            }

            t = new Test("iterate and remove() with iterator", "itererer gjennom alle elementene i iterator() med next() og kaller remove etter hver next()", "iterator() method is inaccesible, make sure the method is declared with public access control\n" + mes);
            return false;
        } catch (IllegalArgumentException e) {
            StackTraceElement[] st = e.getStackTrace();
            String mes = e.getClass().getName();
            if (e.getMessage() != null)
                mes = mes + ": " + e.getMessage();

            for (int i=0; i < st.length-3; i++) {
                mes = mes + "\n\tat " + st[i];
            }

            t = new Test("iterate and remove() with iterator", "itererer gjennom alle elementene i iterator() med next() og kaller remove etter hver next()", "Illegal parameters when trying to invoke iterator() method, iterator() should take no parameters\n" + mes);
            return false;
        } catch (InvocationTargetException e) {
            Throwable target = e.getTargetException();
            StackTraceElement[] st = target.getStackTrace();
            String mes = target.getClass().getName();
            if (target.getMessage() != null)
                mes = mes + ": " + target.getMessage();

            for (int i=0; i < st.length-6; i++) {
                mes = mes + "\n\tat " + st[i];
            }

            t = new Test("iterate and remove() with iterator", "itererer gjennom alle elementene i iterator() med next() og kaller remove etter hver next()", skippedReason);        
            t.failed("Iterator crashed while iterating through the elements:\n" + mes);
            return false;
        } catch (NoSuchMethodException e) {
            t = new Test("iterator()", "sjekker om iterator() gir tilbake et Iterator objekt som inneholder alle elementene i samlingen", "iterator() metoden er ikke implementert");
            return false;
        }

        return t.passed;
    }

    /**
     * Checks that the collection does not add duplicate keys
     */
    public boolean noDuplicates(String skipReason) {
        resetCollections();
        Test t = new Test("ingen duplikate noekler", "sjekker at samlingen ikke legger til to objekter med lik noekkel. To objekter med lik nokkel kan ikke begge vaere i samlingen", skipReason);

        empty.add("Ola", new TestPerson("Ola Twotimes"));
        empty.add("Ola", new TestPerson("Ola Twotimes"));
        
        if (empty.count() != 1) {
            t.failed("leggTil(\"Ole\", new TestPerson(\"Ole\")) har blitt kalt to ganger paa en tom samling. antall() skulle returnert 1, men returnerer " + empty.count());  
            return false;
        }

        return t.passed;
    }
    
    public void easterEgg() {
        //Congratulations, you actually read the code :O
        String jokeOne = "\n\nA man flying in a hot air balloon suddenly realizes he\u2019s lost. He reduces height and spots a man down below. He lowers the balloon further and shouts to get directions, \"Excuse me, can you tell me where I am?\"\n\nThe man below says: \"Yes. You\'re in a hot air balloon, hovering 30 feet above this field.\"\n\n\"You must work in Information Technology,\" says the balloonist.\n\n\"I do\" replies the man. \"How did you know?\"\n\n\"Well,\" says the balloonist, \"everything you have told me is technically correct, but It\'s of no use to anyone.\"\n\nThe man below replies, \"You must work in management.\"\n\n\"I do,\" replies the balloonist, \"But how\'d you know?\"*\n\n\"Well\", says the man, \"you don\u2019t know where you are or where you\u2019re going, but you expect me to be able to help. You\u2019re in the same position you were before we met, but now it\u2019s my fault.\"";
        String jokeTwo = "\n\nA SQL query goes into a bar, walks up to two tables and asks, \"Can I join you?\"";
        System.out.println(Math.random() > 0.5 ? jokeOne: jokeTwo);
    }
    
    TestSamling() {
        //sort array sortedNames
        Arrays.sort(sortedNames);   
    }
    
    /**
     * Internal test class.
     * When a Test object is created it will automatically add
     * itself to the list "tests".
     * 
     * @see tests
     */
    class Test {
        private boolean passed;
        private String title, description, details, whySkipped;
        String[] dependencies;
        
        Test(String key, String dc, String skipReason) {
            title = key;
            description = dc;
            passed = true;
            details = "";
            whySkipped = skipReason;
            tests.add(this);
        }
        
        /**
        * Flags the test as failed
        * @param reason An explanation of why the test failed
        * @return boolean 
        */
        public void failed(String reason) {
            passed = false;
            details = reason;
        }


        /**
        * Will flag Test as skipped if whySkipped is not null or empty
        * @return boolean true if Test should be skippped.
        */
        public boolean skipped() {
            if (whySkipped != null && whySkipped.length() > 0) {
                passed = false;
                details = whySkipped;
                return true;
            } else
            return false;
        }
    }
    
    /**
     * Use to create an array of testPersons
     */
    TestPerson[] testPersoner() {
        TestPerson[] tp = new TestPerson[NAMES.length];
        for (int i=0; i<tp.length; i++) {
            tp[i] = new TestPerson(NAMES[i]);
        }
        return tp;
    }
}

/**
* Used as the V object in collections in the test class
*/
class TestPerson {
    String navn;

    TestPerson(String n) {
        navn = n;
    }

    public String toString() {
        return "TestPerson: " + navn;
    }
}

class TestOblig4 {
    public static void main(String[] args) {
        TestSamling tests = new TestSamling();
        tests.runTests();
    }
}