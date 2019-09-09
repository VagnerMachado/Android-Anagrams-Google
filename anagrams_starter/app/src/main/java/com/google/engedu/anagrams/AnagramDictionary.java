/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 4;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private final String TAG = "vagnerDebug";
    ArrayList<String> wordList = new ArrayList();
    HashSet<String> wordSet = new HashSet<>();
    HashMap<String, ArrayList<String>>lettersToWord = new HashMap<>();  //hash maps where key is
    HashMap<Integer, ArrayList<String>> sizeToWords = new HashMap<>();    //to save all the words with the same length
    int wordLength  = DEFAULT_WORD_LENGTH;


    public AnagramDictionary(Reader reader) throws IOException {
        Log.i(TAG, "Inside the AnagramDictionary constructor");
        BufferedReader in = new BufferedReader(reader);
        String line;
        int keyCounter = 0;
        int dupCounter = 0;
        while((line = in.readLine()) != null)
        {
           String word = line.trim();
             wordList.add(word);                      //old version with arrayList still used for pick a good word
             wordSet.add(word);                         //not sure if this has to go here

            // work on the map with the array listst with buckets
            String sortedWord = sortedLetters(word);
            if(lettersToWord.containsKey(sortedWord))
            {
                lettersToWord.get(sortedWord).add(word);
                dupCounter++;
            }
            else
            {
                ArrayList<String> temp1 = new ArrayList<>();
                temp1.add(word);
                lettersToWord.put(sortedWord, temp1);
                keyCounter++;
            }

           // work on the hash map with array lists of words with the same length
//            if(sizeToWords.containsKey(word.length()))
//                sizeToWords.get(word.length()).add(word);
//            else
//            {
//                ArrayList<String> temp2 = new ArrayList<>();
//                temp2.add(word);
//                sizeToWords.put(word.length(), temp2);
//            }
        }
        // test how many different keys
        Log.i(TAG, "There are " + keyCounter + " keys");

        // test how many different keys
        Log.i(TAG, "There are " + dupCounter + " duplicates");

       ArrayList<String> tester = lettersToWord.get(sortedLetters("skate"));
        // test for a known word with anagram
        Log.i(TAG, "skate has " + tester.size() + " anagrams");

        //confirms the size fo the input 62995
       Log.i(TAG, "File should have 62995 words: " + (keyCounter + dupCounter));

        //confirms that the sortedLetters works
        Log.i(TAG, "Sorted 'horseAROundtheTRee' is: " + sortedLetters("horseAROundtheTRee".toLowerCase()));

        //test for is good word
        Log.i(TAG, "is good word??? word: poster -- base: post  -->" + (isGoodWord("poster", "post")));
        Log.i(TAG, "is good word??? word: nonstop -- base: post -->" + (isGoodWord("nonstop", "post")));

        //test for get anagram with one more
        //getAnagramsWithOneMoreLetter("post");
    }
    public String sortedLetters(String word)
    {
    char [] test = word.toCharArray();
    Arrays.sort(test);
    return String.valueOf(test);
    }

    public boolean isGoodWord(String word, String base)                                               ////check it
    {
        if(wordSet.contains(word))
        {
            if(!word.contains(base))
                return true;
        }
        return false;
    }

    public List<String> getAnagrams(String targetWord)
    {
        ArrayList<String> result = new ArrayList<String>();

        // method with array list
//        String sortedTarget = sortedLetters(targetWord);
//        for(int i = 0; i < wordList.size(); i++)
//        {
//            String temp = wordList.get(i);
//            if(targetWord.length() == temp.length())
//            {
//                if (sortedLetters(temp).equals(sortedTarget))
//                    result.add(temp);
//            }
//        }

        result = lettersToWord.get(sortedLetters(targetWord));
        //prints the anagrams of target word
        Log.i(TAG, "Printing the anagrams for the targetword: ");
        for (String x : result)
            Log.i(TAG, x + "\n");
        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for(int i = 'a'; i <= 'z'; i++)
        {
            StringBuilder temp = new StringBuilder();
            String plusOne = temp.append((char)i).toString();
            plusOne += word; //add word to character
            plusOne = sortedLetters(plusOne);
            if(lettersToWord.containsKey(plusOne))
                result.addAll(lettersToWord.get(plusOne));
        }

        Log.i(TAG, word +" plus one:");
        for (String res: result)
        {
           Log.i(TAG, res);
        }
        return result;
    }

    public String pickGoodStarterWord()
    {
        int rand = random.nextInt(wordList.size() - 1);
        for (int i = rand;  ; i++)
        {
           String word = wordList.get(i);
          //

            Log.i(TAG, "Does it meet the length?? "   +  (word.length()  == wordLength));
           if(lettersToWord.get(sortedLetters(word)).size() >= MIN_NUM_ANAGRAMS &&  word.length() == wordLength)
           {
               if(wordLength != MAX_WORD_LENGTH)
                    wordLength++;
               return word;
           }
           if(i == wordList.size() - 1)
               i = 0;
        }
        //return "skate";
    }
}
