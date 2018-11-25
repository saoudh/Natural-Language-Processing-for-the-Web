package tut2.pipelines;
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
import tut2.reader.WebpageReader;
import tut2.tokenizer.BreakIteratorTokenizer;
import tut2.tokenizer.SentenceSplitter;
import tut2.tokenizer.WhitespaceTokenizer;
import tut2.writer.ExtendedAnnotationsWriter;
import tut2.writer.MyCASConsumer;
import tut2.writer.TokensPerSentenceWriter;

import org.apache.uima.analysis_engine.AnalysisEngine;

import de.tudarmstadt.ukp.dkpro.core.tokit.BreakIteratorSegmenter;

public class PipelineTutorial3 {


	
	public static void main(String[] args) throws UIMAException, IOException
	{
		
		// load page by given url and doc language as config-parameter
		CollectionReader reader=createReader(WebpageReader.class,WebpageReader.PARAM_URL,"https://en.wikipedia.org/wiki/Natural_language_processing",WebpageReader.PARAM_LANGUAGE,"en", WebpageReader.PARAM_SELECTOR,"p");
		// segment sentences into tokens/words while removing punctuation like dots and comma
		AnalysisEngine seg = createEngine(BreakIteratorSegmenter.class);
		AnalysisEngine sentenceSplitter = createEngine(SentenceSplitter.class);

		AnalysisEngine whiteSpaceTokenizer = createEngine(WhitespaceTokenizer.class);
		AnalysisEngine breakIteratorTokenizer = createEngine(BreakIteratorTokenizer.class);

		// spell checker
		AnalysisEngine jazzy=createEngine(JazzyChecker.class,
				JazzyChecker.PARAM_MODEL_LOCATION,"src/main/resources/eng_com.dic");
		// output tokens and sentences
		AnalysisEngine writer=createEngine(MyCASConsumer.class);
		
		AnalysisEngine tokensPerSentenceWriter = createEngine(TokensPerSentenceWriter.class);
		AnalysisEngine annotationWriter=createEngine(ExtendedAnnotationsWriter.class);

		// for every paragraph the following the pipeline runs iteratively 
		SimplePipeline.runPipeline(reader,sentenceSplitter,breakIteratorTokenizer,tokensPerSentenceWriter,annotationWriter);
		
		
	}
}
