
import java.io.*;
import java.util.Iterator;

/**
 * Class to find anagrams of a word.
 * @author Armaan Thakker
 * @version 1.0 December 17, 2023
 */

public class AnagramFinder {

    /**
     * Uses the insertion sort algorithm to sort the characters in a word lexicographically
     * @param word  word of the element to return
     * @return       the sorted word
     */
    public static String insertionSort(String word) {
        //converts the word to a lowercase character array.
        char characterArray[] = word.toLowerCase().toCharArray();
        String sorted = "";

        //modifies insertion sort to sort the characters in the character array.
        for (int i = 1; i < characterArray.length; ++i) {
            int k;
            char current = characterArray[i];
            //iterates through the loop to find the lexicographically correct insertion space.
            for (k = i - 1; k >= 0 && characterArray[k] > current; --k) {
                characterArray[k + 1] = characterArray[k];
            }
            //inserts the word in the correct position in the linked list.
            characterArray[k + 1] = current;
        }

        //converts the character array back to a string to return a String form of the sorted word.
        for (int i=0;i<characterArray.length;i++)
        {
            sorted+=characterArray[i];
        }
        return sorted;
    }

    /**
     * Uses the insertion sort algorithm to sort the words in a linked list lexicographically
     * @param unsortedWordList  linked list of anagrams words that are unsorted
     * @return       the lexicographically sort linked list of words
     */
    public static MyLinkedList<String> insertionSort(MyLinkedList<String> unsortedWordList) {
        /*modifies the insertion sort to sort the words in an unsorted
         linked list in lexographical order.
         */
        for (int i = 1; i < unsortedWordList.size(); ++i) {
            int k;
            String current = unsortedWordList.get(i);
            //uses compareTo operator for Strings in order to maintain lexicographical order.
            for (k = i - 1; k >= 0 && unsortedWordList.get(k).compareTo(current)>0; --k) {
                //iterates through the loop to find the lexicographically correct insertion space.
                unsortedWordList.set(k+1,unsortedWordList.get(k));
            }
            //inserts the word in the correct position in the linked list.
            unsortedWordList.set(k+1,current);
        }
        return unsortedWordList;
    }


    public static void main(String[] args) {


        String inputtedWord = args[0];
        String sorted_inputtedWord = insertionSort(args[0]);
        String inputtedFile = args[1];
        File dictionaryFile = new File(inputtedFile);
        String inputtedDataStructure = args[2];

        //checks to make sure the arguments meet all necessary requirements
        /*checks to see if there are three arguments. If not prints an error to stderr and
        exits in failure.
         */
        if (args.length != 3) {
            System.err.println("Usage: java AnagramFinder <word> <dictionary file> <bst|avl|hash>");
            System.exit(1);
        }

            /*checks to see if the dictionary file exits. If not prints an error to stderr and
            exits in failure.
            */
        if (!dictionaryFile.exists()) {
            System.err.println("Error: Cannot open file '" + dictionaryFile + "' for input.");
            System.exit(1);
        }

            /*checks to see if the inputted data structure equals the string "bst","avl", or "hash".
            If not prints an error to stderr and exits in failure.
            */
        if ((!(inputtedDataStructure.equals("bst"))) && (!(inputtedDataStructure.equals("avl"))) && (!(inputtedDataStructure.equals("hash")))) {
            System.err.println("Error: Invalid data structure '" + args[2] + "' received.");
            System.exit(1);
        }
        

        //Decleares the initial map of MyMap type and sets it to null.
        MyMap<String, MyLinkedList<String>> map = null;
        //if the user inputs bst as the data structure, initializes the map to a BSTMap.
        if (inputtedDataStructure.equals("bst")) {
            map = new BSTMap<>();
        }
        //if the user inputs avl as the data structure, initializes the map to a AVLTreeMap.
        if (inputtedDataStructure.equals("avl")) {
            map = new AVLTreeMap<>();
        }
        //if the user inputs hash as the data structure, initializes the map to a MyHashMap.
        if (inputtedDataStructure.equals("hash")) {
            map = new MyHashMap<>();
        }

        //creates a buffered reader object to read the inputted file.
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(inputtedFile))) {
            String line;
            //checking to see that there is a word on the line.
            while ((line = bufferedReader.readLine()) != null) {
                String sorted = insertionSort(line);

                //adding anagrams to the data structure
                /*initially if value associated with key in the map is null, creates
                a new linked list and adds the current word being read by the reader
                into the list. Then puts the list in the map.
                 */
                if (map.get(sorted) == null) {
                    MyLinkedList<String> tracker = new MyLinkedList<>();
                    tracker.add(line);
                    map.put(sorted, tracker);
                } else {
                    /*if the file reader finds the value(linked list of anagrams)
                    associated with the key, then add the current line to the
                    linked list.
                     */
                    map.get(sorted).add(line);
                }

            }
        }
        //Print an error to the stderr if fail to initialize the buffered reader object.
        catch (IOException e) {
            System.err.println("Error: An I/O error occurred reading '" + args[1] + "'.");
        }


        //getting the value (linked list of words) associated with the key (inputted word)


        MyList<String> list = map.get(sorted_inputtedWord);
        /*if the linked list is null or the only word in the linked list is the inputted word
        itself, print "No anagrams found."
         */
        if (list == null || list.isEmpty() || (list.size() == 1 && list.get(0).equals(inputtedWord))) {
            System.out.println("No anagrams found.");
        } else {
            //sort the linked list of anagrams using the insertion sort above.
            insertionSort(map.get(sorted_inputtedWord));
            //initialize an iterator and use it to print every word but the inputted word.
            Iterator<String> iter = list.iterator();
            while (iter.hasNext()) {
                String curr = iter.next();
                if (!curr.equals(args[0])) {
                    System.out.println(curr);
                }
            }
        }

    }

}
