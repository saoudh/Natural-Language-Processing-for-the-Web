package tut2.tokenizer;


import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.jcas.JCas;

import type.Sentence;

import java.text.BreakIterator;

public class SentenceSplitter extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {

		String document = jcas.getDocumentText();
		int start = 0;
		int end = 0;
		// Use BreakIterator for iteration over sentences 
		BreakIterator boundary_sentence = BreakIterator.getSentenceInstance();
		boundary_sentence.setText(document);
		start = boundary_sentence.first();
		// loop over all sentences and set the beginning and ending index of the sentence
		for (end = boundary_sentence.next(); end != BreakIterator.DONE; start = end, end = boundary_sentence.next()) {

			Sentence tokenAnnotation = new Sentence(jcas);
			tokenAnnotation.setBegin(start);
			tokenAnnotation.setEnd(end);

			tokenAnnotation.addToIndexes();

		}

	}
}
