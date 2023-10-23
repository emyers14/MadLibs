/*
 * Ethan Myers
 * Object Oriented Programming
 * Madlib
 * 10/23/23
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

/**
 * This class represents a simple Mad Libs game that generates random stories
 * using wordlists provided by the user.
 */
public class MadlibsGame {

    public static void main(String[] args) {
        // Use a try-with-resources block to automatically close the Scanner
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("**************************************************************");
            System.out.println("*                   Welcome to Madlibs V1.                   *");
            System.out.println("**************************************************************");
            System.out.println("This program generates random stories using wordlists you supply.");
            

        // Prompt the user for the folder location (default is "c:\temp\ml")
        System.out.print("Enter the name of the folder where the stories and wordlists are, or press Enter for the default location: ");
        String folderLocation = scanner.nextLine().trim();
        if (folderLocation.isEmpty()) {
            folderLocation = "c:\\temp\\ml";
        }

        Map<String, String> wordLists = loadWordLists(folderLocation);
        if (wordLists == null) {
            System.out.println("Failed to load wordlists. Make sure the folder and files exist.");
            return;
        }

        while (true) {
            System.out.print("Enter a story number or 'q' to quit: ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("q")) {
                break;
            }

            try {
                int storyNumber = Integer.parseInt(input);
                String storyFileName = folderLocation + "\\story" + storyNumber + ".txt";

                String madlibStory = generateMadlibStory(storyFileName, wordLists);
                System.out.println("Here is your Madlib:");
                System.out.println(madlibStory);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid story number or 'q' to quit.");
            }
        }

        System.out.println("Thank you for using this program.");
    }
}
/**
     * Loads wordlists from text files located in the specified folder.
     *
     * @param folderLocation The folder location where wordlist files are stored.
     * @return A map of wordlist names to their content, or null if an error occurs.
     */

    private static Map<String, String> loadWordLists(String folderLocation) {
        Map<String, String> wordLists = new HashMap<>();

        String[] wordListFiles = {
                "adj.txt",
                "adv.txt",
                "singnoun.txt",
                "plunoun.txt",
                "singverb.txt",
                "pluverb.txt",
                "pastverb.txt"
        };

        try {
            for (String wordListFile : wordListFiles) {
                String fileName = folderLocation + "\\" + wordListFile;
                BufferedReader reader = new BufferedReader(new FileReader(fileName));
                StringBuilder wordList = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    wordList.append(line).append("\n");
                }
                reader.close();
                wordLists.put(wordListFile, wordList.toString());
            }
        } catch (IOException e) {
            return null;
        }

        return wordLists;
    }
    /**
     * Generates a Madlib story by replacing placeholders in a story file with random words from wordlists.
     *
     * @param storyFileName The file name of the story template.
     * @param wordLists A map of wordlists with names and their content.
     * @return The Madlib story, or null if an error occurs.
     */
    private static String generateMadlibStory(String storyFileName, Map<String, String> wordLists) {
        StringBuilder madlibStory = new StringBuilder();
        Random random = new Random();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(storyFileName));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split(" ");
                for (String word : words) {
                    if (word.startsWith("<") && word.endsWith(">")) {
                        String placeholder = word.substring(1, word.length() - 1);
                        if (wordLists.containsKey(placeholder)) {
                            String[] wordArray = wordLists.get(placeholder).split("\n");
                            String randomWord = wordArray[random.nextInt(wordArray.length)];
                            madlibStory.append(randomWord).append(" ");
                        } else {
                            madlibStory.append(word).append(" ");
                        }
                    } else {
                        madlibStory.append(word).append(" ");
                    }
                }
                madlibStory.append("\n");
            }
            reader.close();
        } catch (IOException e) {
            return null;
        }

        return madlibStory.toString();
    }
}
