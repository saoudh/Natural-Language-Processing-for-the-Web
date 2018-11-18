package tut2.tokenizer;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.teaching.general.type.WSToken;

public class WhitespaceTokenizer
    extends JCasAnnotator_ImplBase
{
    // Adapt the whitespace tokenizer into a UIMA annotator
    @Override
    public void process(JCas jcas)
        throws AnalysisEngineProcessException
    {
        String document = jcas.getDocumentText();
        int len = document.length();
        int start = 0;
        int end = 0;
        System.out.println("WhitespaceTokenizer-Document="+document);

        // as long as the sentence is not finished, loop
        while (start < len) {
        	// increment the start-index as long as there are white spaces to get the 
        	// start index of the first word
            while (start < len && Character.isSpaceChar(document.charAt(start))) {
                start++;
            }
            // loop at the latest until the sentence ends or a White Space is encountered
            // to get the end-index of the word
            for (end = start; end < len && !Character.isSpaceChar(document.charAt(end)); end++);
            
            // as long as we didn't arrive at the end of the sentence annotate the words
            if (start < len) {
                // The following code creates an annotation and adds it to
                // the index. You'll need to execute it for each token you find.
                WSToken tokenAnnotation = new WSToken(jcas);
                tokenAnnotation.setBegin(start);
                tokenAnnotation.setEnd(end);
                tokenAnnotation.addToIndexes();
            }
            start = end + 1;
        }
    }
}
