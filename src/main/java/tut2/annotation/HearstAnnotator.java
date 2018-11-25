package tut2.annotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import type.HearstAnnotation;

public class HearstAnnotator extends JCasAnnotator_ImplBase {
	List<String> list_regex;
	// sorted according to list_regex in process()
	int hearstTypeCount[] = new int[6];

	// Key has the concatenating hypernym@hyponym and value is count of that
	// combination
	public Map<String, Integer> mapCountHypernym;

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {

		HearstAnnotation ha = new HearstAnnotation(jcas);
		for (Sentence sentence : JCasUtil.select(jcas, Sentence.class)) {
			mapCountHypernym = new HashMap<String, Integer>();

			// all possible patterns of Hearst
			list_regex = new ArrayList<>();
			list_regex.add("NP\\s+such\\s+as\\s+NP");
			list_regex.add("such\\s+NP\\s+as\\s+NP");
			list_regex.add("NP\\s+and\\s+other\\s+NP");
			list_regex.add("NP\\s+or\\s+other\\s+NP");
			list_regex.add("NP\\s+,\\s+including\\s+NP");
			list_regex.add("NP\\s+,\\s+especially\\s+NP");

			// sb_tagged: for recognizing the pattern
			// list_tagged: list of the tokens with the noun tagged as NP
			// list_original: list of the tokens
			StringBuilder sb_tagged = new StringBuilder();
			StringBuilder sb_original = new StringBuilder();
			List<String> list_tagged = new ArrayList<String>();
			List<String> list_original = new ArrayList<String>();

			for (Annotation a : JCasUtil.selectCovered(jcas, Annotation.class, sentence)) {
				String pos = a.getType().toString();
				String word = a.getCoveredText();

				if (!pos.contains("pos"))
					continue;
				list_original.add(word);

				sb_original.append(word);

				if (pos.contains("NOUN")) {
					list_tagged.add("NP");
					sb_tagged.append("NP");
				} else {
					list_tagged.add(word);
					sb_tagged.append(word);
				}
				sb_tagged.append(" ");
			}

			// check if sentence contains the Hearst Pattern
			for (int i = 0; i < list_regex.size() - 1; i++) {
				Pattern pattern = Pattern.compile(list_regex.get(i));
				Matcher matcher = pattern.matcher(sb_tagged.toString());
				// if a regex pattern matches, it will be processed according to its pattern
				// type
				if (matcher.find()) {
					// increment count of the regex type
					hearstTypeCount[i]++;
					processAnnotating(i, list_tagged, list_original, jcas);
					// if pattern is found and processed, then leave it
					break;
				}
			}

		}

	}

	public void processAnnotating(int index, List<String> list_tagged, List<String> list_original, JCas jcas) {
		String hypernym = "", hyponym = "";
		int i;
		// every Hearst Pattern case has a different location of the hypernym and
		// hyponym
		// and have to be processed individually
		// the third regex has Hyponym as the first noun and hypernym as the second noun
		switch (index) {
		case 0:
			i = list_original.indexOf("such");
			hyponym = list_original.get(i - 1);
			hypernym = list_original.get(i + 2);
			break;
		case 1:
			i = list_original.indexOf("such");
			hyponym = list_original.get(i + 1);
			i = list_original.indexOf("as");
			hypernym = list_original.get(i + 1);
			break;
		case 2:
			i = list_original.indexOf("other");
			hyponym = list_original.get(i + 1);
			hypernym = list_original.get(i - 2);
			break;
		case 3:
			i = list_original.indexOf("other");
			hyponym = list_original.get(i + 1);
			hypernym = list_original.get(i - 2);
			break;
		case 4:
			i = list_original.indexOf("including");
			hyponym = list_original.get(i - 2);
			hypernym = list_original.get(i + 1);
			break;
		case 5:
			i = list_original.indexOf("especially");
			hyponym = list_original.get(i - 2);
			hypernym = list_original.get(i + 1);
			break;
		}
		String key = hypernym + "@" + hyponym;
		if (mapCountHypernym.containsKey(key))
			mapCountHypernym.replace(key, mapCountHypernym.get(key) + 1);
		else
			mapCountHypernym.put(key, 1);

		// add the information to the annotations
		HearstAnnotation ha = new HearstAnnotation(jcas);
		ha.setHyperonym(hypernym);
		ha.setHyponym(hyponym);
		ha.setTypeOf(list_regex.get(index));
		ha.addToIndexes();

	}

}