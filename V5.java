package mainPackage;

import java.util.Scanner;
//V5 the standard char set of passwords, with variable length, and a rough estimate of time required to brute force a given password
public class V5 {
	
	

	public static void main(String[] args) {
		//Variable Declarations
		String userPassword = "";
        Scanner scanner = new Scanner(System.in);
        long initialTime = 0;
        long finalTime = 0;
        long numberOfGuesses=0;
        long totalTime = 0;
        

        //Gather user inputs
        System.out.print("Please enter your example password to be cracked: ");
        userPassword = scanner.nextLine();
        
        System.out.println("Valid input. You entered: " + userPassword);
        scanner.close();
             
        //Fetch charset
        char[] charset = gatherCharset();
        
        //Calculate Estimate of time ~ assumes current rate of 54 million per second
        long secondsRequired = estimateBruteForceTime(charset, userPassword.length());
        System.out.println("Your current password can take a maximum of "+secondsRequired+" seconds, which equates to "+(secondsRequired/3600)+" hours, or "+((secondsRequired/3600)/24)+" days at the current hashrate of 54,000,000 per second.");
        
        //Time logging
        initialTime = getCurrentSystemTime();
        
        //Call method
        numberOfGuesses=alphabetCracker(userPassword,charset);
        finalTime = getCurrentSystemTime();
        totalTime = (finalTime-initialTime);
        //Outputs
        System.out.println("Process took: "+(finalTime-initialTime)+ " seconds.");
        System.out.println("Algorithm worked at an attempt rate of "+(numberOfGuesses/totalTime)+" attempts per second.");
	}
	
	
	
	
	
	public static long alphabetCracker(String userPassword, char[] charset) {
	    String currentGuess = "";
	    int  passWordLength   =  userPassword.length();
	    long totalGuesses = 0;
	    int charSetLength = charset.length;
	    while (!currentGuess.equals(userPassword)) {
	        //Reset current guess
	        currentGuess = "";
	        //First Letter
	        for (int i=0;i<charSetLength;i++) {
	            for (int j=0;j<charSetLength;j++) {
	                for (int k=0;k<charSetLength;k++) {
	                    for (int l=0;l<charSetLength;l++) {
	                        for (int m=0;m<charSetLength;m++) {
                            totalGuesses+=1;
                            currentGuess = Character.toString(charset[i])+Character.toString(charset[j])+Character.toString(charset[k])+Character.toString(charset[l])+Character.toString(charset[m]);
                            if (currentGuess.equals(userPassword)) {
                                System.out.println("Your Password has been cracked: "+currentGuess+" after "+totalGuesses+" attempts");
                                return totalGuesses; // terminate the method after finding the password, return number of guesses
                            	}
	                        }
	                    }
	                }
	            }
	        }
	        //Temporary Termination to prevent infinite loop
	        currentGuess = userPassword;
	    }   
	    return 0;
	}

	
	public static long getCurrentSystemTime() {
		long currentSystemTimeInSeconds = System.currentTimeMillis()/1000;
		return currentSystemTimeInSeconds;
	}
	
	
	public static char[] gatherCharset() {
	    char[] lowerCaseAlphabet = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
	    char[] upperCaseAlphabet = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
	    char[] digits = {'0','1','2','3','4','5','6','7','8','9'};
	    char[] symbols = {'!','@','#','$','%','^','&','*','(',')','<','>','?','{','}','+','-','[',']'};
	    
	    int mergedLength = lowerCaseAlphabet.length + upperCaseAlphabet.length + digits.length+symbols.length;
	    
	    char[] mergedCharset = new char[mergedLength];
	    
	    // Copy elements from lowerCaseAlphabet to mergedCharset
	    System.arraycopy(lowerCaseAlphabet, 0, mergedCharset, 0, lowerCaseAlphabet.length);
	    // Copy elements from upperCaseAlphabet to mergedCharset
	    System.arraycopy(upperCaseAlphabet, 0, mergedCharset, lowerCaseAlphabet.length, upperCaseAlphabet.length);
	    // Copy elements from digits to mergedCharset
	    System.arraycopy(digits, 0, mergedCharset, lowerCaseAlphabet.length + upperCaseAlphabet.length, digits.length);
	    // Copy elements from symbols to mergedCharset
	    System.arraycopy(symbols, 0, mergedCharset, lowerCaseAlphabet.length + upperCaseAlphabet.length + digits.length,symbols.length);
	    
	    return mergedCharset;
	}
	
	
	public static long estimateBruteForceTime(char[] charset, int lengthOfPassword) {
		return ((long) (Math.pow(charset.length, lengthOfPassword))/54000000);
	}

}
