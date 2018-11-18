package tut2.writer;

import java.util.*;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.teaching.general.type.BIToken;
import type.Sentence;

// Consumer class which counts the number of tokens in every sentence
// and adds it to a list
public class TokensPerSentenceWriter extends JCasConsumer_ImplBase {

	public static final String LF = System.getProperty("line.separator");
	
	List<Integer> numOfTokens = new ArrayList<Integer>();
	public void process(JCas jcas) throws AnalysisEngineProcessException {
    	System.out.println("TokensPerSentence called");

		for (Sentence s: JCasUtil.select(jcas, Sentence.class))  {

            	List<BIToken> tokens = new ArrayList<BIToken>();
            	tokens = JCasUtil.selectCovered(jcas, BIToken.class, s);
            	numOfTokens.add(tokens.size());
            }

	}

}