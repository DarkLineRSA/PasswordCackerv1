package mainPackage;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicLong;

public class V6 {

    private static volatile boolean passwordFound = false;

    public static void main(String[] args) {
        // Variable Declarations
        String userPassword = "";
        Scanner scanner = new Scanner(System.in);
        long initialTime = 0;
        long finalTime = 0;
        long totalTime = 0;

        // Gather user inputs
        System.out.print("Please enter your example password to be cracked: ");
        userPassword = scanner.nextLine();

        System.out.println("Valid input. You entered: " + userPassword);
        scanner.close();

        // Fetch charset
        char[] charset = gatherCharset();

        // Time logging
        initialTime = getCurrentSystemTime();

        // Call method
        AtomicLong totalGuesses = new AtomicLong(0);
        Thread[] threads = alphabetCracker(userPassword, charset, totalGuesses);
        
        // Wait for all threads to finish
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        finalTime = getCurrentSystemTime();
        totalTime = (finalTime - initialTime);
        // Outputs after all threads have finished
        System.out.println("Process took: " + (finalTime - initialTime) + " seconds.");
        System.out.println("Algorithm worked at an attempt rate of " + (totalGuesses.get() / totalTime) + " attempts per second.");
        System.out.println("Total guesses made by all threads: " + totalGuesses.get());
    }

    public static Thread[] alphabetCracker(String userPassword, char[] charset, AtomicLong totalGuesses) {
        int charSetLength = charset.length;
        int numThreads = 8; // Number of threads to use for multi-threading
        Thread[] threads = new Thread[numThreads];

        // Split the charset into equal chunks for each thread
        int chunkSize = charSetLength / numThreads;

        // Start threads
        for (int i = 0; i < numThreads; i++) {
            final int start = i * chunkSize;
            final int end = (i == numThreads - 1) ? charSetLength : (i + 1) * chunkSize;
            threads[i] = new Thread(() -> {
                crackInRange(userPassword, charset, start, end, totalGuesses);
            });
            threads[i].start();
        }

        return threads;
    }

    private static void crackInRange(String userPassword, char[] charset, int start, int end, AtomicLong totalGuesses) {
        String currentGuess = "";
        int charSetLength = charset.length;
        long guesses = 0;
        while (!passwordFound) {
            for (int i = start; i < end; i++) {
                for (int j = 0; j < charSetLength; j++) {
                    for (int k = 0; k < charSetLength; k++) {
                        for (int l = 0; l < charSetLength; l++) {
                            for (int m = 0; m < charSetLength; m++) {
                            	for (int n = 0; n < charSetLength; n++) {
                            		if (passwordFound) {
                            			totalGuesses.addAndGet(guesses);
                            			return;
                            		}
                            		guesses += 1;
                            		currentGuess = Character.toString(charset[i]) + Character.toString(charset[j]) + Character.toString(charset[k]) + Character.toString(charset[l]) + Character.toString(charset[m])+ Character.toString(charset[n]);
                            		if (currentGuess.equals(userPassword)) {
                            			passwordFound = true;
                            			System.out.println("Your Password has been cracked: " + currentGuess);
                            			totalGuesses.addAndGet(guesses);
                            			return;
                            		}
                            	}
                            }
                        }
                    }
                }
            }
        } 
    }


    public static long getCurrentSystemTime() {
        return System.currentTimeMillis() / 1000;
    }


    public static char[] gatherCharset() {
        char[] lowerCaseAlphabet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        char[] upperCaseAlphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        char[] symbols = {'!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '<', '>', '?', '{', '}', '+', '-', '[', ']'};

        int mergedLength = lowerCaseAlphabet.length + upperCaseAlphabet.length + digits.length + symbols.length;

        char[] mergedCharset = new char[mergedLength];

        // Copy elements from lowerCaseAlphabet to mergedCharset
        System.arraycopy(lowerCaseAlphabet, 0, mergedCharset, 0, lowerCaseAlphabet.length);
        // Copy elements from upperCaseAlphabet to mergedCharset
        System.arraycopy(upperCaseAlphabet, 0, mergedCharset, lowerCaseAlphabet.length, upperCaseAlphabet.length);
        // Copy elements from digits to mergedCharset
        System.arraycopy(digits, 0, mergedCharset, lowerCaseAlphabet.length + upperCaseAlphabet.length, digits.length);
        // Copy elements from symbols to mergedCharset
        System.arraycopy(symbols, 0, mergedCharset, lowerCaseAlphabet.length + upperCaseAlphabet.length + digits.length, symbols.length);

        return mergedCharset;
    }


    public static long estimateBruteForceTime(char[] charset, int lengthOfPassword) {
        return ((long) (Math.pow(charset.length, lengthOfPassword)) / 54000000);
    }

}
