package tut2.consumer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.Level;

import type.HearstAnnotation;

public class HearstWriter extends JCasConsumer_ImplBase {

	public static final String LF = System.getProperty("line.separator");

	// key is the name of hearst-type and the value the count
	public Map<String, Integer> mapHearstTypeCount;

	// Key has the concatenating hypernym@hyponym and value is count of that
	// combination
	public Map<String, Integer> mapCountHypernym;

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		mapCountHypernym = new HashMap<String, Integer>();
		mapHearstTypeCount = new HashMap<String, Integer>();

		StringBuilder sb = new StringBuilder();
		sb.append("-- Hearst Tags --");
		sb.append(LF);

		for (HearstAnnotation p : JCasUtil.select(jcas, HearstAnnotation.class)) {

			sb.append("[" + p.getType().getShortName() + "] ");
			sb.append("[Hyperonym] " + p.getHyperonym());
			sb.append("[Hyponym] " + p.getHyponym());
			sb.append(p.getCoveredText());

			sb.append(LF);

			// concatenate the hyperonym and hyponym as the key and the putting the count as
			// the value
			String key = p.getHyperonym() + "@" + p.getHyponym();
			// couting all hyperonym and hyponym pairs
			if (mapCountHypernym.containsKey(key))
				mapCountHypernym.replace(key, mapCountHypernym.get(key) + 1);
			else
				mapCountHypernym.put(key, 1);

			// couting hearst types
			if (mapHearstTypeCount.containsKey(p.getTypeOf()))
				mapHearstTypeCount.replace(p.getTypeOf(), mapHearstTypeCount.get(p.getTypeOf()) + 1);
			else
				mapHearstTypeCount.put(p.getTypeOf(), 1);

		}

		sb.append("count \t hyponym	\t hypernym");
		sb.append(LF);
		sb.append(LF);

		// Iterating over all couunts
		Iterator it = mapCountHypernym.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			String[] hypo_pair = pair.getKey().toString().split("@");
			sb.append(pair.getValue() + "\t" + hypo_pair[0] + "\t" + hypo_pair[1]);
			it.remove();
			sb.append(LF);
		}
		sb.append(LF);

		sb.append("count \t Hearst Pattern\n");

		it = mapHearstTypeCount.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			sb.append(pair.getValue() + "\t" + pair.getKey());
			it.remove();
			sb.append(LF);

		}

		sb.append(LF);

		getContext().getLogger().log(Level.INFO, sb.toString());
	}

}
