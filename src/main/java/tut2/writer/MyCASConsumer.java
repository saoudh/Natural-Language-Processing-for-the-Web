package tut2.writer;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.util.Level;
import org.apache.uima.util.Logger;

public class MyCASConsumer extends JCasConsumer_ImplBase {
    public static final String LF = System.getProperty("line.separator");

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		// for every paragraph the following is run
		StringBuilder sb = new StringBuilder();
		sb.append("=== CAS ==="); sb.append(LF);
		sb.append("-- Document Text --"); sb.append(LF);
		sb.append(aJCas.getDocumentText()); sb.append(LF);
		sb.append("-- Annotations --"); sb.append(LF);
		
		// output all tokens/sentences of a paragraph with the beginning and final index
		// of the token in the current sentence
		for(Annotation a: JCasUtil.select(aJCas, Annotation.class))
		{
			// Type: [Token] or [Sentence]; (begin, end) of the sentence or token like "(1,2)";
			// coveredText of the token/sentence "("  
			sb.append("[" + a.getType().getShortName() + "] ");
			sb.append("(" + a.getBegin() + ", " + a.getEnd() + ") ");
			sb.append(a.getCoveredText());
			sb.append(LF);
			
		}
		sb.append(LF);
		getContext().getLogger().log(Level.INFO, sb.toString());;
	}

}
