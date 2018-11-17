package tut2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.uima.UimaContext;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.fit.component.JCasCollectionReader_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class WebpageReader extends JCasCollectionReader_ImplBase {

	private List<String> paragraphs;
	private int idx;
	private Document doc;
	//Configuration-Parameters which are accessed in MyPipeline by their names 
	public static final String PARAM_LANGUAGE = "TextEncoding"; 
	public static final String PARAM_URL = "MyURL"; 
	@ConfigurationParameter(
			   name = PARAM_LANGUAGE,
			   description = "Sets the web language",
			   mandatory = true,
			   defaultValue = "en")
			private String webLanguage;
	
	@ConfigurationParameter(
			   name = PARAM_URL,
			   description = "Sets the web URL",
			   mandatory = true,
			   defaultValue = "https://en.wikipedia.org/wiki/Natural_language_processing")
			private String myURL;
	
	@Override
	public void initialize(UimaContext context)
	{
		try {
			super.initialize(context);
		} catch (ResourceInitializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			this.doc = Jsoup.connect(myURL).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.paragraphs=new ArrayList<String>();
		this.idx=0;
		//String title=this.doc.title();
		for(Element e: this.doc.select("p"))
		{
			// add every paragraph to the list
			this.paragraphs.add(e.ownText());
		}
		
	}
	
	@Override
	public boolean hasNext() throws IOException, CollectionException {
		// return bool whether there are more paragraphs in the list
		return this.idx<this.paragraphs.size();
	}

	@Override
	public Progress[] getProgress() {
		
		return new Progress[] {new ProgressImpl(this.idx+1, this.paragraphs.size(), Progress.ENTITIES)};
	}

	@Override
	public void getNext(JCas jCas) throws IOException, CollectionException {
		// get current paragraph and set doc-language
		jCas.setDocumentText(this.paragraphs.get(this.idx));
		jCas.setDocumentLanguage(webLanguage);
		this.idx++;
	}

}
