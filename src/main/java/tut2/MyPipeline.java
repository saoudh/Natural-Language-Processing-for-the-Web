package tut2;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReader;

import java.io.IOException;

import org.apache.uima.UIMAException;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;

import org.apache.uima.collection.CollectionReader;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.jazzy.JazzyChecker;
import de.tudarmstadt.ukp.dkpro.core.tokit.BreakIteratorSegmenter;
import org.apache.uima.analysis_engine.AnalysisEngine;

import de.tudarmstadt.ukp.dkpro.core.tokit.BreakIteratorSegmenter;

public class MyPipeline {


	
	public static void main(String[] args) throws UIMAException, IOException
	{
		
		// load page by given url and doc language as config-parameter
		CollectionReader reader=createReader(WebpageReader.class,WebpageReader.PARAM_URL,"https://en.wikipedia.org/wiki/Natural_language_processing",WebpageReader.PARAM_LANGUAGE,"en");
		// segment sentences
		AnalysisEngine seg = createEngine(BreakIteratorSegmenter.class);
		
		AnalysisEngine tokenizer = createEngine(WhitespaceTokenizer.class);
		
		

		// spell checker
		AnalysisEngine jazzy=createEngine(JazzyChecker.class,
				JazzyChecker.PARAM_MODEL_LOCATION,"src/main/resources/eng_com.dic");
		// output tokens and sentences
		AnalysisEngine writer=createEngine(MyCASConsumer.class);
		// for every paragraph the following the pipeline runs iteratively 
		SimplePipeline.runPipeline(reader,seg,tokenizer,jazzy,writer);
		
		
	}
}
