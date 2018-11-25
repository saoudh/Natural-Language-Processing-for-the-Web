package tut2.pipelines;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordLemmatizer;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordParser;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordPosTagger;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;
import tut2.annotation.HearstAnnotator;
import tut2.consumer.ChunkWriter;
import tut2.consumer.HearstWriter;
import tut2.consumer.LemmaWriter;
import tut2.consumer.POSWriter;

public class PipelineTutorial4
{

    public static void main(String[] args) throws UIMAException, IOException
    {

        JCas jcas;

        // Segmenter
        AnalysisEngine seg = createEngine(StanfordSegmenter.class);

        // Lemmatizer
        AnalysisEngine lem = createEngine(StanfordLemmatizer.class);

        // Pos tagger
        AnalysisEngine pos = createEngine(StanfordPosTagger.class);

        // Parser
        // StanfordParser is kind of heavy. You might want to increase the heap space.
        // e.g. add -Xmx1500m to the VM arguments in the Run Configurations arguments tab.
        // Also better start with not too much text to process.
        AnalysisEngine parse = createEngine(StanfordParser.class);

        // Consumers
        AnalysisEngine posWriter = createEngine(POSWriter.class);
        AnalysisEngine lemmaWriter = createEngine(LemmaWriter.class);
        AnalysisEngine chunkWriter = createEngine(ChunkWriter.class);
        AnalysisEngine hearstAnnotator=createEngine(HearstAnnotator.class);
        AnalysisEngine hearstWriter = createEngine(HearstWriter.class);

        /*
        // Print POS tags
        jcas = createDoc();
        SimplePipeline.runPipeline(jcas, seg, pos, posWriter);

        // Print lemmas
        jcas = createDoc();
        SimplePipeline.runPipeline(jcas, seg, pos, lem, lemmaWriter);

        // Print chunks
        jcas = createDoc();
        SimplePipeline.runPipeline(jcas, seg, pos, lem, parse, chunkWriter);
        */
        // Print chunks
        jcas = createDoc();
        // outputting the counts for hearst pattern
        SimplePipeline.runPipeline(jcas, seg, pos, hearstAnnotator, hearstWriter);
        

    }

    static JCas createDoc()
    {
        // No real reader needed for a simple demo.
        JCas jcas = null;

        try {
            jcas = JCasFactory.createJCas();
            // Make sure that the document language is set to "en". Check your reader(s)!
            jcas.setDocumentLanguage("en");

            jcas.setDocumentText(
                    "I like mammals such as cats and the sillier mice. I saw a lot of mammals, including zebras. You should be nice to dogs and other mammals.");
        }
        catch (UIMAException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return jcas;
    }
}
