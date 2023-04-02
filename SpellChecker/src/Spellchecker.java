/*
    The Spellchecker class stores a dictionary of words (src/dictionary.txt)
    in the form of a binary tree, then allows you to spellcheck a
    sample (src/sample.txt) against the dictionary.
*/


/*      IMPORTS     */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


/*      PROGRAM     */

public class Spellchecker {

    Node rNode;
    int numTerm = 0;

    /*
        A Node represents a single character. It carries information
        about its value, its child Nodes, and whether it is a valid
        ending for a word.
    */
    public class Node {
        Character nodeVal;
        boolean isTerminator;
        ListOfNode children = new ListOfNode(); // contains all direct children of this node

        public Node(){this(null, false);}

        /*
         Constructor initialises node with its nVal, it's character value and
         isTerminator, which determines if a word can end on that character
        */
        public Node(Character nVal, boolean isTerminator){
            //constructor
            this.nodeVal = nVal;
            this.isTerminator = isTerminator;
        }

        /*
         makeTerm() allows words to terminate on this node.
        */
        public void makeTerm(){
            this.isTerminator = true;
        }
    }

    public class ListOfNode extends ArrayList<Node>{

        /*
         contains() checks all children of the current node against the Character argument, val.
        */
        public boolean contains(Character val){

            for (Node member : this) {

                if (val == member.nodeVal) {

                    return true;

                }
            }

            return false; //return false if node does not exist in list
        }

        public int getIndex(Character val) {

            for (Node member : this) {

                if (val == member.nodeVal) {

                    return indexOf(member);

                }
            }

            return -1; //return -1 if node does not exist in list
        }

    }

    public Spellchecker(){
        rNode = new Node();
    }

    /*
    addWord adds a String 'word' provided as an argument to the dictionary tree.
     */
    public void addWord(String word){

        ListOfNode children = rNode.children; // starting with children of root node;
        char[] chArray = word.toCharArray(); //convert String word to array of chars;
        boolean term;

        for (int i = 0; i < chArray.length; i++) {

            Node child;
            term = ((i + 1) == chArray.length); // current character is last char in word, it is a valid terminator;
            Character ch = chArray[i];

            if(children.contains(ch)){ // if child exists in list of children;

                int index = children.getIndex(ch);

                child = children.get(index);

            } else {

                children.add(child = new Node(chArray[i], term));

                if (term) {

                    numTerm++; //increment counter of new words in tree;

                }
            }

            if(term){

                //if child exists and should be terminator, convert to terminator;
                child.makeTerm();
            }

            children = child.children;
        }
    }

    /*
     containsWord() matches a String word with the dictionary tree.
    */
    public boolean containsWord(String word){

        ListOfNode children = rNode.children; // starting with children of root node;
        char[] chArray = word.toCharArray(); //convert String word to array of chars;
        boolean term;
        Node child;
        for (int i = 0; i < chArray.length; i++) {
            term = ((i + 1) == chArray.length); //if last char in word, it is terminator;

            int index;

            if(children.contains(chArray[i])) {
                index = children.getIndex(chArray[i]);

                child = children.get(index);

                if (term){
                    return child.isTerminator; //if char from string is terminator, check if child is terminator;
                }

                children = child.children;
            }

            else {  // if child does not exist in list of string, return false;

                return false;

            }
        }

        return true;
    }

    /*
     addFile() adds all the words in a dictionary file 'filename' to the dictionary binary tree.
    */
    public void addFile(String filename){

        try {

            Scanner input = new Scanner(new File(filename));

            while (input.hasNextLine()){

                String word = input.nextLine().strip();
                addWord(word);

            }
        } catch (FileNotFoundException e) {

            e.printStackTrace(); //if you want to see errors
            System.out.println("File not found.");
        }
    }

    /*
     checkFile() checks each word in the File 'filename', and returns
     a count of incorrect words. Each incorrect word is also printed to
     the console.
    */
    public int checkFile(String filename){

        int counter = 0;

        try {
            Scanner input = new Scanner(new File(filename));

            while (input.hasNextLine()){

                //converted to lowercase
                String line = input.nextLine().toLowerCase();
                String[] words = line.split("[^a-z]+");

                for (String word: words){
                    if(!containsWord(word)){
                        counter++;
                        System.out.println(word);
                    }
                }
            }

        } catch (FileNotFoundException e) {

            e.printStackTrace(); // if you want to see errors

            System.out.println("File not found.");
        }
        return counter;
    }

    public static void main(String[] args) {

        Spellchecker tree = new Spellchecker();

        tree.addFile("src/dictionary.txt");

        System.out.println("Incorrect words: " + tree.checkFile("src/sample.txt"));

    }
}