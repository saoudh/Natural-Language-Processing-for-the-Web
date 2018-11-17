//package de.tudarmstadt.ukp.teaching.nlp4web.tutorial.annotator;
package tut2;

import java.util.StringTokenizer;
import java.text.BreakIterator;

public class TokenizerExample {

	public static void main(String[] args) {
		String s = "I saw a man.  \"I'm Sam,\" he said.";

		System.out.println("--- WhiteSpaceTokenize ---");
		WhitespaceTokenize(s);

		System.out.println("--- BreakIteratorTokenize ---");
		BreakIteratorTokenize(s);
	}

	// Problem 1: Examine and run the following tokenizer. What problems
	// do you see in the output?
	private static void WhitespaceTokenize(String document) {
		StringTokenizer tok = new StringTokenizer(document);
		while(tok.hasMoreTokens()){
			System.out.println(tok.nextElement());
		}
	}

	// Problem 2: Implement a tokenizer using java.text.BreakIterator.
	// What are the improvements over the previous approach? What issues
	// still remain?
	private static void BreakIteratorTokenize(String document) {
		BreakIterator boundary = BreakIterator.getWordInstance();
        boundary.setText(document);
        printEachForward(boundary, document);
        //print each sentence in reverse order
      //  boundary = BreakIterator.getSentenceInstance(Locale.US);
      //  boundary.setText(document);
    //    printEachBackward(boundary, document);
    //    printFirst(boundary, document);
     //   printLast(boundary, document);
	}
	 public static void printEachForward(BreakIterator boundary, String source) {
		 System.out.println("boundary-first="+boundary.first());
		 System.out.println("boundary-next="+boundary.next());

	     int start = boundary.first();
	     for (int end = boundary.next();

	          end != BreakIterator.DONE;
	          start = end, end = boundary.next()) {
	          System.out.println(source.substring(start,end));
	    	 
	    	 
	     }
	}
}
