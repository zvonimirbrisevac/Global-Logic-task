/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package global_logic;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Class for a solution to a task given by GlobalLogic. :)
 * @author zvoni
 */

public class GlobalLogicTask {

    private HashMap<Pattern, Integer> patternFreq;
    private String inputSentence;
    private String keyWord;
    private int numOfChar;
    private int numOfKeyWordChars;

    private final String specialCharsRegex = " |!|\"|#|\\$|%|&|'|\\(|\\)|\\*|\\+|,|-|\\.|/|:|;|<|=|>|\\?|@|[|\\|]|^|_|`|\\{|\\}|~";
    private static final String defaultKeyWord = "LOGIC";
    private static final String defaultInputSentence = "I love to work in global logic!";

    /**
     * Class that contains pattern and its number of occurence in sentence.
     *  
     */
    private static class Pair implements Comparable<Pair> {

        private Pattern pattern;
        private int frequency;
        
        /**
         * Creates a pair of pattern and its number of occurence.
         * @param pattern - pattern
         * @param frequency - number of occurence
         */
        public Pair(Pattern pattern, int frequency) {
            this.pattern = pattern;
            this.frequency = frequency;
        }
        
        /**
         * Fetches array of characters in pattern.
         * @return array of characters in pattern
         */
        public char[] getPatternChars() {
            return pattern.getCharPattern();
        }
        
        /**
         * Fetches number of occurence of pattern.
         * @return number of occurence of pattern.
         */
        public int getFrequency() {
            return frequency;
        }
        
        /**
         * Function for sorting collections of pairs.
         * First it compares them by their frequencies 
         * If they are the same, then by the length of their patterns 
         * character sequence.
         * If they too are the same, then it compares those character sequences
         * alphabeticly.
         * @param otherPair - pair to be compared against
         * @return 
         */
        @Override
        public int compareTo(Pair otherPair) {
            if (otherPair.getFrequency() != frequency) {
                return frequency - otherPair.getFrequency();
            }

            int otherPatternLength = otherPair.getPatternChars().length;
            int patternLength = pattern.getCharPattern().length;
            if (otherPatternLength != patternLength) {
                return patternLength - otherPatternLength;
            }

            for (int i = 0; i < patternLength; i++) {
                char patternChar = pattern.getCharPattern()[i];
                char otherChar = otherPair.getPatternChars()[i];
                if (patternChar != otherChar) {
                    return Character.compare(patternChar, otherChar);
                }
            }
            return 0;
        }
        
        /**
         * 
         * @return string representaion of Pair fot printing 
         */
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{(");
            for (int i = 0; i < pattern.getCharPattern().length - 1; i++) {
                sb.append(pattern.getCharPattern()[i]).append(", ");
            }
            sb.append(pattern.getCharPattern()[pattern.getCharPattern().length - 1]);
            sb.append("), ");
            sb.append(pattern.getWordLength());
            sb.append("} ");
            return sb.toString();
        }
    }
    
    /**
     * Class that contains characters sequence and length of word in which
     * it appears.
     */
    private static class Pattern {

        private final char[] charPattern;
        private final int wordLength;
        
        /**
         * Creates new pattern.
         * @param charPattern - sequence of characters
         * @param wordLength - length of word in which they appeared
         */
        public Pattern(char[] charPattern, int wordLength) {
            this.charPattern = charPattern;
            this.wordLength = wordLength;
        }
        
        /**
         * 
         * @return array of characters in pattern. 
         */
        public char[] getCharPattern() {
            return charPattern;
        }
        
        /**
         * 
         * @return length of word in which characters appeared. 
         */
        public int getWordLength() {
            return wordLength;
        }
        
        /**
         * Hashes Pattern object by its character array and word length
         * @return hash of Pattern class 
         */
        @Override
        public int hashCode() {
            String s = String.copyValueOf(charPattern);
            return Objects.hash(s, wordLength);
        }
        
        /**
         * Compares if object is equal to Pattern object.
         * @param other - object being compared with this object
         * @return true if they are equal, false otherwise
         */
        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (other == null) {
                return false;
            }
            if (getClass() != other.getClass()) {
                return false;
            }
            return Arrays.equals(charPattern, ((Pattern) other).getCharPattern())
                    && ((Pattern) other).getWordLength() == this.wordLength;
        }
    }
    
    /**
     * Creates new task.
     * @param keyWord - word containing characters which are being searched for
     * @param inputSentence - sentence in which characters from key word are 
     * being searhed.
     */
    public GlobalLogicTask(String keyWord, String inputSentence) {
        this.keyWord = keyWord;
        this.inputSentence = inputSentence;
        this.numOfChar = 0;
        this.numOfKeyWordChars = 0;
        this.patternFreq = new HashMap<>();
    }
    
    /**
     * 
     * @return total number of characters (without special characters) 
     */
    public int getNumOfChar() {
        return numOfChar;
    }
    
    /**
     * 
     * @return number of key word characters occurence in sentence  
     */
    public int getNumOfKeyWordChar() {
        return numOfKeyWordChars;
    }
    
    /**
     * Analyses number of key word characters occurences in each word of the sentence.
     * It creates pattern based on which characters occured in which word and on
     * the length of that word. It puts patterns in a map which has those patterns
     * as keys and its values are number of occurences of key word characters
     * in each word that has the same length as patterns word length.
     * After that it creates pairs of those keys and its values and it puts
     * those pairs in a list that is then sorted and returned.
     * @return sorted list of pairs
     */
    public ArrayList<Pair> analyzeFrequency() {
        String[] allWords = inputSentence.split("\t| ");
        for (String word : allWords) {
            if (word.equals("")) {
                continue;
            }
            word = word.replaceAll(specialCharsRegex, "");
            word = word.toLowerCase();
            numOfChar += word.length();
            String sb = "";
            char[] keyChars = keyWord.toLowerCase().toCharArray();
            char[] wordChars = word.toCharArray();
            int numOfOccurence = 0;
            for (int i = 0; i < keyChars.length; i++) {
                if (sb.indexOf(keyChars[i]) != -1) {
                    continue;
                }
                for (int j = 0; j < wordChars.length; j++) {
                    if (keyChars[i] == wordChars[j]) {
                        numOfKeyWordChars++;
                        numOfOccurence++;
                        if (sb.indexOf(keyChars[i]) == -1) {
                            sb = sb.concat(Character.toString(keyChars[i]));
                        }
                    }
                }
            }
            if (!sb.equals("")) {
                Pattern pattern = new Pattern(sb.toCharArray(), word.length());
                int count = patternFreq.containsKey(pattern) ? patternFreq.get(pattern) : 0;
                patternFreq.put(pattern, count + numOfOccurence);
            }
        }

        ArrayList<Pair> pairList = new ArrayList<>();
        for (Map.Entry<Pattern, Integer> entry : patternFreq.entrySet()) {
            pairList.add(new Pair(entry.getKey(), entry.getValue()));
        }
        Collections.sort(pairList);
        return pairList;
    }
    
    /**
     * Runs program and does all of printing.
     * @param args - there can only be 2 optional arguments:
     * 1. key word (default: LOGIC)
     * 2. sentence (default: "I love to work in global logic!")
     */
    public static void main(String[] args) {
        String userKeyWord = defaultKeyWord;
        String userInputSentence = defaultInputSentence;

        if (args.length == 2) {
            userKeyWord = args[0];
            userInputSentence = args[1];
        }
        if (args.length != 0 && args.length != 2) {
            throw new IllegalArgumentException("Illegal number of arguments!");
        }

        System.out.println("Key word: " + userKeyWord);
        System.out.println("Sentence: " + userInputSentence);

        GlobalLogicTask task = new GlobalLogicTask(userKeyWord, userInputSentence);
        ArrayList<Pair> listFreq = task.analyzeFrequency();

        int numOfKeyChars = task.getNumOfKeyWordChar();
        int numOfTotalChars = task.getNumOfChar();
        DecimalFormat df = new DecimalFormat("#.##");
        for (Pair pair : listFreq) {
            double percentage = ((double) pair.getFrequency()) / numOfKeyChars;
            System.out.println(pair + "= " + df.format(percentage)
                    + " (" + pair.getFrequency() + "/" + numOfKeyChars + ")");

        }
        double totalPercentage = 0;
        if (numOfTotalChars != 0) {
            totalPercentage = ((double) numOfKeyChars) / numOfTotalChars;
        }
        System.out.println("TOTAL Frequency: " + df.format(totalPercentage)
                + " (" + numOfKeyChars + "/" + numOfTotalChars + ")");

    }

}
