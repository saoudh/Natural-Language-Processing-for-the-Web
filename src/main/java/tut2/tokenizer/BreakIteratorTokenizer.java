package tut2.tokenizer;

import java.util.StringTokenizer;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.teaching.general.type.BIToken;

import java.text.BreakIterator;

public class BreakIteratorTokenizer extends JCasAnnotator_ImplBase {

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
		while (tok.hasMoreTokens()) {
			System.out.println(tok.nextElement());
		}
	}

	// Problem 2: Implement a tokenizer using java.text.BreakIterator.
	// What are the improvements over the previous approach? What issues
	// still remain?
	private static void BreakIteratorTokenize(String document) {
		BreakIterator boundary = BreakIterator.getWordInstance();
		boundary.setText(document);
		int start = boundary.first();
		for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next()) {
			System.out.println(document.substring(start, end));
		}
	}

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		String document = jcas.getDocumentText();
		int start = 0;
		int end = 0;
		BreakIterator boundary = BreakIterator.getWordInstance();
		boundary.setText(document);
		start = boundary.first();
		for (end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next()) {
			BIToken tokenAnnotation = new BIToken(jcas);
			tokenAnnotation.setBegin(start);
			tokenAnnotation.setEnd(end);
			tokenAnnotation.addToIndexes();

		}

	}
}
