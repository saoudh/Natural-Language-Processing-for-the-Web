package tut2.writer;

import java.util.ArrayList;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.util.Level;
import de.tudarmstadt.ukp.teaching.general.type.BIToken;

// This Writer writes every Annotation and also the number of tokens in a sentence

public class ExtendedAnnotationsWriter extends JCasConsumer_ImplBase {

	public static final String LF = System.getProperty("line.separator");

	public void process(JCas jcas) throws AnalysisEngineProcessException {
		StringBuilder sb = new StringBuilder();
		sb.append("=== CAS ===");
		sb.append(LF);
		sb.append("-- Document Text --");
		sb.append(LF);
		sb.append(jcas.getDocumentText());
		sb.append(LF);
		sb.append("-- Annotations --");
		sb.append(LF);
	
        for (Annotation a : JCasUtil.select(jcas, Annotation.class)) {
            sb.append("[" + a.getType().getShortName() + "] ");
            sb.append("(" + a.getBegin() + ", " + a.getEnd() + ") ");
            sb.append(a.getCoveredText());
            sb.append(LF);
            sb.append(LF);
            
            // output number of tokens in a sentence after outputting the sentence
            if(a.getType().getShortName().equals("Sentence")){
	            List<BIToken> tokens = new ArrayList<BIToken>();
	        	tokens = JCasUtil.selectCovered(jcas, BIToken.class, a);
	        	sb.append("number of tokens in the sentence: " + tokens.size());
            }
            sb.append(LF);
            sb.append(LF);
    
        }

		sb.append(LF);

		getContext().getLogger().log(Level.INFO, sb.toString());
	}

}