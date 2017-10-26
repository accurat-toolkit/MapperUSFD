package shef.ac.uk;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import com.shef.uk.util.TextProcessingUtilEnglish;
import com.shef.uk.util.Util;


public class LinkEntry {
	private TextProcessingUtilEnglish util = new TextProcessingUtilEnglish();
	
	public Map<String, Double> alignNE(String anEnglishText, String aForeignText, String aTaggingInformation, String aForeignLanguage, double aThreshold) throws IOException {
    	if (aTaggingInformation.equalsIgnoreCase("NE1")) {
        	String englishSentences[] = util.detectSentences(anEnglishText);
        	String foreignSentences[] = util.detectSentences(aForeignText);
    		return getNameMappingWithForeignNETagged(englishSentences, foreignSentences, aForeignLanguage, aThreshold);
    	} else if (aTaggingInformation.equalsIgnoreCase("NE0")){
        	String englishSentences[] = util.detectSentences(anEnglishText);
        	String foreignSentences[] = util.detectSentences(aForeignText);
    		return getNameMappingWithoutNETagging(englishSentences, foreignSentences, aForeignLanguage, aThreshold);
    	} else if (aTaggingInformation.equalsIgnoreCase("NE2")) {
        	String englishSentences[] = anEnglishText.split("\\n");
        	String foreignSentences[] = aForeignText.split("\\n");
    		return getNameMappingWithBothInputNETagged(englishSentences, foreignSentences, aForeignLanguage, aThreshold);    		
    	}
    	return null;
	}
	
	public Map<String, Double> alignTE(String anEnglishText, String aForeignText, String aTaggingInformation, String aSourceLanguage, String aForeignLanguage, double aThreshold) throws Exception {
		boolean isToUseTranslation = false;
		if (aTaggingInformation.toLowerCase().contains("-trans")) {
			isToUseTranslation = true;
		}
		if (isToUseTranslation) {
			Dictionary.init(aForeignLanguage, aSourceLanguage);
		}
		if (aTaggingInformation.equalsIgnoreCase("T0")) {
			return getTermLinkWithTE(anEnglishText, aForeignText, aForeignLanguage, aThreshold, isToUseTranslation);			
		} else {
			return getTermLinkWithBothTE(anEnglishText, aForeignText, aForeignLanguage, aThreshold, isToUseTranslation);
		}
	}

	private Map<String, Double> getTermLinkWithTE(String aEnglishText, String aForeignText, String aForeignLanguage, double aThreshold, boolean isToUseTranslation) throws Exception {

		Map<String, String> elLangMap = new HashMap<String, String>();
		if ("el".equals(aForeignLanguage)) {
			elLangMap = Util.getFileContentAsMap(aForeignLanguage + "-en.txt", "=");
		}

		Map<String, String> foreignTerms = getExistingTE(aForeignText, isToUseTranslation);
		Vector<String> enTerms =  util.detectTE(aEnglishText);
				
		Map<String, Double> termLinkScored = new HashMap<String, Double>();
		
		Iterator<String> it = null;
		Iterator<String> foreignTermsIt = foreignTerms.keySet().iterator();
		while (foreignTermsIt.hasNext()) {
			String foreignTermOri = foreignTermsIt.next();
			String foreignTerm = foreignTerms.get(foreignTermOri);
			Vector<String> foreignCharacterList = getUniGramChracter(foreignTerm);
			
			String foreignTermToUse = foreignTerm;
			if ("el".equalsIgnoreCase(aForeignLanguage) && !isToUseTranslation) {
				foreignTermToUse = getELTransliteration(elLangMap,
						foreignCharacterList);
			}

			it = enTerms.iterator();
			while (it.hasNext()) {
				String enTerm = it.next().trim();
				Vector<String> enCharacterList = getUniGramChracter(enTerm);
				double totalScore = 0;
				if (enTerm.toLowerCase().equals(foreignTermOri.toLowerCase())) {
					totalScore = 1;
				} else {					
					foreignCharacterList = getUniGramChracter(foreignTermToUse);
					totalScore = getTotalScore(foreignTermToUse, foreignCharacterList, enTerm, enCharacterList);
				}
				
				if (totalScore > aThreshold && !termLinkScored.containsKey(enTerm + "\t" + foreignTermOri)) {
					termLinkScored.put(enTerm + "\t" + foreignTermOri, totalScore);
				}
				
			}
			
		}
		
		return termLinkScored;
	}

	private Map<String, Double> getTermLinkWithBothTE(String aEnglishText, String aForeignText, String aForeignLanguage, double aThreshold, boolean isToUseTranslation) throws Exception {

		Map<String, String> elLangMap = new HashMap<String, String>();
		if ("el".equals(aForeignLanguage)) {
			elLangMap = Util.getFileContentAsMap(aForeignLanguage + "-en.txt", "=");
		}

		Map<String, String> foreignTerms = getExistingTE(aForeignText, isToUseTranslation);
		
		Map<String, String> enTerms =  getExistingTE(aEnglishText, false);
				
		Map<String, Double> termLinkScored = new HashMap<String, Double>();
		
		Iterator<String> it = null;
		Iterator<String> foreignTermsIt = foreignTerms.keySet().iterator();
		while (foreignTermsIt.hasNext()) {
			String foreignTermOri = foreignTermsIt.next();
			String foreignTerm = foreignTerms.get(foreignTermOri);
			Vector<String> foreignCharacterList = getUniGramChracter(foreignTerm);
			String foreignTermToUse = foreignTerm;
			if ("el".equalsIgnoreCase(aForeignLanguage) && !isToUseTranslation) {
				foreignTermToUse = getELTransliteration(elLangMap,foreignCharacterList);
			}

			it = enTerms.keySet().iterator();
			while (it.hasNext()) {
				String enTermOri = it.next().trim();
				String enTerm = enTerms.get(enTermOri);
				//System.out.println("en " + enTerm);
				Vector<String> enCharacterList = getUniGramChracter(enTerm);
				double totalScore = 0;
				if (enTerm.toLowerCase().equals(foreignTerm.toLowerCase())) {
					totalScore = 1;
				} else {					
					foreignCharacterList = getUniGramChracter(foreignTermToUse);
					totalScore = getTotalScore(foreignTermToUse, foreignCharacterList, enTerm, enCharacterList);
				}
				
				if (totalScore > aThreshold && !termLinkScored.containsKey(enTermOri + "\t" + foreignTermOri)) {
					termLinkScored.put(enTermOri + "\t" + foreignTermOri, totalScore);
					//System.out.println(termLinkScored);
				}
				
			}
			
		}
		
		return termLinkScored;
	}

	private Map<String, Double> itsForeignNEs = new HashMap<String, Double>();
	private Map<String, Double> itsENNEs = new HashMap<String, Double>();
	
	
	
	public final Map<String, Double> getForeignNEs() {
		return itsForeignNEs;
	}

	public final void setForeignNEs(Map<String, Double> aForeignNEs) {
		itsForeignNEs = aForeignNEs;
	}

	public final Map<String, Double> getENNEs() {
		return itsENNEs;
	}

	public final void setENNEs(Map<String, Double> aEs) {
		itsENNEs = aEs;
	}

	private Map<String, Double> getNameMappingWithoutNETagging(String aEnglishSentenceArray[], String aForeignSentenceArray[], String aForeignLanguage, double aThreshold) throws IOException {
		itsForeignNEs = new HashMap<String, Double>();
		itsENNEs = new HashMap<String, Double>();
		Map<String, String> langMap = new HashMap<String, String>();
		if ("el".equals(aForeignLanguage) || "lv".equals(aForeignLanguage) || "lt".equals(aForeignLanguage)
				|| "et".equals(aForeignLanguage) || "ro".equals(aForeignLanguage) || "sl".equals(aForeignLanguage) 
				|| "hr".equals(aForeignLanguage)) {
			langMap = Util.getFileContentAsMap(aForeignLanguage + "-en.txt", "=");
		}

		Vector<String> foreignNEs = getUpperCaseNE(aForeignSentenceArray);
		for (int i = 0; i < foreignNEs.size(); i++) {
			String ne = foreignNEs.get(i);
			Double count = itsForeignNEs.get(ne);
			if (count == null) {
				count = new Double(0);
			}
			count = new Double(1 + count.intValue());
			itsForeignNEs.put(ne, count);
		}
		Map<String, String> enNEs = new HashMap<String, String>();
		
		for (int i = 0; i < aEnglishSentenceArray.length; i++) {
			String englishSentence = aEnglishSentenceArray[i];
			Map<String, String> nes = util.detectNamedEntities(englishSentence);
			enNEs.putAll(nes);
		}
		
		Map<String, Double> neLinkScored = new HashMap<String, Double>();
		Iterator<String> it = null;
		it = enNEs.keySet().iterator();
		while (it.hasNext()) {
			String ne = it.next().trim();
			Double count = itsENNEs.get(ne);
			if (count == null) {
				count = new Double(0);
			}
			count = new Double(1 + count.intValue());
			itsENNEs.put(ne, count);
		}

		
		
		for (int i = 0; i < foreignNEs.size(); i++) {
			String foreignNE = foreignNEs.get(i).trim();
			foreignNE = foreignNE.replaceAll("\"", "");
			foreignNE = foreignNE.replaceAll("\\.", "");
			foreignNE = foreignNE.replaceAll("„", "");
			foreignNE = foreignNE.replaceAll("“", "");
			foreignNE = foreignNE.replaceAll("«", "");
			foreignNE = foreignNE.replaceAll("!", "");
			foreignNE = foreignNE.replaceAll("\\?", "");
			foreignNE = foreignNE.replaceAll("»", "");
			foreignNE = foreignNE.replaceAll("\\)", "");
			foreignNE = foreignNE.replaceAll("\\(", "").trim();
			
			Vector<String> foreignCharacterList = getUniGramChracter(foreignNE);
			
			String foreignNEToUse = foreignNE;
			if ("el".equals(aForeignLanguage) || "lv".equals(aForeignLanguage) || "lt".equals(aForeignLanguage)
					|| "et".equals(aForeignLanguage) || "ro".equals(aForeignLanguage) || "sl".equals(aForeignLanguage) 
					|| "hr".equals(aForeignLanguage)) {
				foreignNEToUse = getELTransliteration(langMap,foreignCharacterList);
			}

			it = enNEs.keySet().iterator();
			while (it.hasNext()) {
				String enNE = it.next().trim();
				
				enNE = enNE.replaceAll("\"", "");
				enNE = enNE.replaceAll("\\.", "");
				enNE = enNE.replaceAll("„", "");
				enNE = enNE.replaceAll("“", "");
				enNE = enNE.replaceAll("«", "");
				enNE = enNE.replaceAll("!", "");
				enNE = enNE.replaceAll("\\?", "");
				enNE = enNE.replaceAll("»", "");
				enNE = enNE.replaceAll("\\)", "");
				enNE = enNE.replaceAll("\\(", "").trim();

				String enValues[] = enNE.split(" ");
				String foreignValues[] = foreignNE.split(" ");
				if (enValues.length != foreignValues.length) {
					continue;
				}

				Vector<String> enCharacterList = getUniGramChracter(enNE);
				if (enNE.toLowerCase().equals(enNE)) {
					continue;
				}
				double totalScore = 0;
				if (enNE.toLowerCase().equals(foreignNE.toLowerCase())) {
					totalScore = 1;
				} else {
					foreignCharacterList = getUniGramChracter(foreignNEToUse);
					totalScore = getTotalScore(foreignNEToUse, foreignCharacterList, enNE, enCharacterList);
				}
				
				if (totalScore > aThreshold && !neLinkScored.containsKey(enNE + "\t" + foreignNE)) {
					neLinkScored.put(enNE + "\t" + foreignNE, totalScore);
				}
				
			}
			
		}
		
		return neLinkScored;
	}

	static Map<String, String> foreign = new HashMap<String, String>();
	static Map<String, String> english = new HashMap<String, String>();
	private Map<String, Double> getNameMappingWithForeignNETagged(String aEnglishSentenceArray[], String aForeignSentenceArray[], String aForeignLanguage, double aThreshold) throws IOException {
		Map<String, String> elLangMap = new HashMap<String, String>();
		if ("el".equals(aForeignLanguage)) {
			elLangMap = Util.getFileContentAsMap(aForeignLanguage + "-en.txt", "=");
		}
		Map<String, String> foreignNEs = getExistingNE(aForeignSentenceArray);
		foreign.putAll(foreignNEs);
		
		Map<String, String> enNEs = new HashMap<String, String>();
		
		for (int i = 0; i < aEnglishSentenceArray.length; i++) {
			String englishSentence = aEnglishSentenceArray[i];
			Map<String, String> nes = util.detectNamedEntities(englishSentence);
			enNEs.putAll(nes);
		}
		english.putAll(enNEs);
		
		Map<String, Double> neLinkScored = new HashMap<String, Double>();
		
		Iterator<String> it = null;
		Iterator<String> foreignNEsIt = foreignNEs.keySet().iterator();
		while (foreignNEsIt.hasNext()) {
			String foreignNE = foreignNEsIt.next();
			String foreignNEToUse = foreignNE;
			Vector<String> foreignCharacterList = getUniGramChracter(foreignNE);
			if ("el".equalsIgnoreCase(aForeignLanguage)) {
				foreignNEToUse = getELTransliteration(elLangMap,
						foreignCharacterList);
			}
			String foreignType = foreignNEs.get(foreignNE);
			
			it = enNEs.keySet().iterator();
			while (it.hasNext()) {
				String enNE = it.next().trim();
				enNE = enNE.replaceAll("\"", "");
				enNE = enNE.replaceAll("\\.", "");
				enNE = enNE.replaceAll("„", "");
				enNE = enNE.replaceAll("“", "");
				enNE = enNE.replaceAll("«", "");
				enNE = enNE.replaceAll("!", "");
				enNE = enNE.replaceAll("\\?", "");
				enNE = enNE.replaceAll("»", "");
				enNE = enNE.replaceAll("\\)", "");
				enNE = enNE.replaceAll("\\(", "").trim();

				String enType = enNEs.get(enNE);
				if (!foreignType.equals(enType)) {
					continue;
				}
				String enValues[] = enNE.split(" ");
				String foreignValues[] = foreignNE.split(" ");
				if (enValues.length != foreignValues.length) {
					continue;
				}
				Vector<String> enCharacterList = getUniGramChracter(enNE);
				if (enNE.toLowerCase().equals(enNE)) {
					continue;
				}
				double totalScore = 0;
				if (enNE.toLowerCase().equals(foreignNE.toLowerCase())) {
					totalScore = 1;
				} else {					
					foreignCharacterList = getUniGramChracter(foreignNEToUse);
					totalScore = getTotalScore(foreignNEToUse, foreignCharacterList, enNE, enCharacterList);
				}
				
				if (totalScore > aThreshold && !neLinkScored.containsKey(enNE + "\t" + foreignNE)) {
					neLinkScored.put(enNE + "\t" + foreignNE, totalScore);
				}
				
			}
			
		}
		
		return neLinkScored;
	}

	public String getELTransliteration(Map<String, String> elLangMap, Vector<String> foreignCharacterList) {
		String foreignNEToUse;
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < foreignCharacterList.size(); i++) {
			String engCh = elLangMap.get(foreignCharacterList.get(i));
			if (engCh != null) {
				buffer.append(engCh);
			} else if (foreignCharacterList.get(i).trim().equals("")) {
				buffer.append(" ");
			}
		}
		foreignNEToUse = buffer.toString().trim();
		return foreignNEToUse;
	}

	private Map<String, Double> getNameMappingWithBothInputNETagged(String aEnglishSentenceArray[], String aForeignSentenceArray[], String aForeignLanguage, double aThreshold) throws IOException {

		
		Map<String, String> elLangMap = new HashMap<String, String>();
		if ("el".equals(aForeignLanguage)) {
			elLangMap = Util.getFileContentAsMap(aForeignLanguage + "-en.txt", "=");
		}

		Map<String, String> foreignNEs = getExistingNE(aForeignSentenceArray);
		
		Map<String, String> enNEs = getExistingNE(aEnglishSentenceArray);
		
		
		Map<String, Double> neLinkScored = new HashMap<String, Double>();
		
		Iterator<String> it = null;
		Iterator<String> foreignNEsIt = foreignNEs.keySet().iterator();
		while (foreignNEsIt.hasNext()) {
			String foreignNE = foreignNEsIt.next();
			String foreignType = foreignNEs.get(foreignNE);
			Vector<String> foreignCharacterList = getUniGramChracter(foreignNE);
			
			String foreignNEToUse = foreignNE;
			if ("el".equalsIgnoreCase(aForeignLanguage)) {
				foreignNEToUse = getELTransliteration(elLangMap,
						foreignCharacterList);
			}

			it = enNEs.keySet().iterator();
			while (it.hasNext()) {
				String enNE = it.next().trim();
				String enType = enNEs.get(enNE);
				if (!foreignType.equals(enType)) {
					continue;
				}
				String enValues[] = enNE.split(" ");
				String foreignValues[] = foreignNE.split(" ");
				if (enValues.length != foreignValues.length) {
					continue;
				}

				Vector<String> enCharacterList = getUniGramChracter(enNE);
				if (enNE.toLowerCase().equals(enNE)) {
					continue;
				}
				double totalScore = 0;
				if (enNE.toLowerCase().equals(foreignNE.toLowerCase())) {
					totalScore = 1;
				} else {					
					foreignCharacterList = getUniGramChracter(foreignNEToUse);
					totalScore = getTotalScore(foreignNEToUse, foreignCharacterList, enNE, enCharacterList);
				}
				
				if (totalScore > aThreshold && !neLinkScored.containsKey(enNE + "\t" + foreignNE)) {
					neLinkScored.put(enNE + "\t" + foreignNE, totalScore);
				}
				
			}
			
		}
		
		return neLinkScored;
	}

	
	private double getTotalScore(String aForeignNE, Vector<String> aForeignCharacterList, String anEnNE, Vector<String> anEnCharacterList) throws IOException {
		double totalScore;
		double cosineBigram = CognateSim.getCosineSim(util.getNgramNE(aForeignCharacterList, 2, true), util.getNgramNE(anEnCharacterList, 2, true));
		
		double cosineTrigram = CognateSim.getCosineSim(util.getNgramNE(aForeignCharacterList, 3, true), util.getNgramNE(anEnCharacterList, 3, true));						
		
		double levensteinDistance = CognateSim.getLevenshteinDistance(anEnNE, aForeignNE);
		double levensteinDistanceSim = (1.0/(1.0 + levensteinDistance));
		
		double lcSubstring = CognateSim.longestCommonSubstring(anEnNE, aForeignNE).length();
		double lcSubstringSim = lcSubstring/Math.max(anEnNE.length(), aForeignNE.length());
		
		double lcSubsequence = CognateSim.longestCommanSubsequence(anEnNE, aForeignNE).length();
		double lcSubsequenceSim = lcSubsequence/Math.max(anEnNE.length(), aForeignNE.length());
		
		double diceSim = 2.0 * lcSubstring / (anEnNE.length() + aForeignNE.length());
		
		double simulatedExactMatchSim = lcSubstring / (Math.min(anEnNE.length(), aForeignNE.length()));
		
		totalScore = 0.3 * diceSim + 0.2 * lcSubstringSim + 0.2 * cosineTrigram +  0.1 * simulatedExactMatchSim + 0.1 * cosineBigram + 0.05 * levensteinDistanceSim + 0.05 * lcSubsequenceSim;
		return totalScore;
	}

	
	private Vector<String> getUniGramChracter(String ngramNe) {
		char array[] = ngramNe.toCharArray();
		Vector<String> characters = new Vector<String>();
		for (int j = 0; j < array.length; j++) {
			char c = array[j];
			characters.add(c + "");
		}
		return characters;
	}

	private Map<String, String> getExistingNE(String[] aForeignSentenceArray) throws IOException {
		Map<String, String> neList = new HashMap<String, String>();
		
		
		for (int i = 0; i < aForeignSentenceArray.length; i++) {
			String sentence = aForeignSentenceArray[i];
			String nes[] = sentence.split("</ENAMEX>");
			for (int j = 0; j < nes.length; j++) {
				String ne = nes[j];
				if (ne.contains("<ENAMEX")) {
					String values[] = ne.split("<ENAMEX");
					if (values.length > 1) {
						String left = values[1];
						String value = left.replaceAll(".*>", "").trim();
						String type = left.replaceAll(">.*", "");
						type = type.replaceAll(".*TYPE=\"", "").replaceAll("\"", "").trim();
						
						value = value.replaceAll("\"", "");
						value = value.replaceAll("\\.", "");
						value = value.replaceAll("„", "");
						value = value.replaceAll("“", "");
						value = value.replaceAll("«", "");
						value = value.replaceAll("!", "");
						value = value.replaceAll("\\?", "");
						value = value.replaceAll("»", "");
						value = value.replaceAll("\\)", "");
						value = value.replaceAll("\\(", "").trim();

						neList.put(value, type);											
					}
				}
			}
		}

//		for (int i = 0; i < aForeignSentenceArray.length; i++) {
//			String sentence = aForeignSentenceArray[i];
//			String nes[] = sentence.split("</PERSON>");
//			for (int j = 0; j < nes.length; j++) {
//				String ne = nes[j];
//				if (ne.contains("<PERSON>")) {
//					String value = ne.replaceAll(".*>", "");
//					neList.put(value, "PERSON");					
//				}
//			}
//		}
//		
//		for (int i = 0; i < aForeignSentenceArray.length; i++) {
//			String sentence = aForeignSentenceArray[i];
//			String nes[] = sentence.split("</LOCATION>");
//			for (int j = 0; j < nes.length; j++) {
//				String ne = nes[j];
//				if (ne.contains("<LOCATION>")) {
//					String value = ne.replaceAll(".*>", "");
//					neList.put(value, "LOCATION");					
//				}
//			}
//		}
//
//		for (int i = 0; i < aForeignSentenceArray.length; i++) {
//			String sentence = aForeignSentenceArray[i];
//			String nes[] = sentence.split("</ORGANIZATION>");
//			for (int j = 0; j < nes.length; j++) {
//				String ne = nes[j];
//				if (ne.contains("<ORGANIZATION>")) {
//					String value = ne.replaceAll(".*>", "");
//					neList.put(value, "ORGANIZATION");					
//				}
//			}
//		}

		return neList;
	}

	
	private Map<String, String> getExistingTE(String aForeignText, boolean isToUseTranslation) throws IOException {
		Map<String, String> neList = new HashMap<String, String>();
		String terms[] = aForeignText.split("</TENAME>");
		for (int j = 0; j < terms.length && terms.length > 1; j++) {
			String term = terms[j];
			if (term.contains("<TENAME>")) {
				String values[] = term.split("<TENAME>");
				if (values.length > 1) {
					String value = values[1].trim();
					String trans = value;
					StringBuffer translation = new StringBuffer();
					if (isToUseTranslation) {
						String valuesForTrans[] = value.split("\\s");
						for (int k = 0; k < valuesForTrans.length; k++) {
							String token = valuesForTrans[k];
							token = token.replaceAll("\\.", "").replaceAll("'", "").replaceAll("\"", "");
							
			             	String candidate = Dictionary.DICTIONARY_TARGET.get(token);
		           	      if (candidate != null && !candidate.equalsIgnoreCase("nil")){
	           	    		  String str[]=candidate.split("\\ ");
	           	    		   for (int s=0;s < str.length;s++){
	            				 translation.append(str[s]).append(" ");
	           	    		   }
	           	    	  } else {
	           	    		  translation.append(token).append(" ");
	           	    	  }
						}						
						trans = translation.toString().trim().toLowerCase();
					}
					neList.put(value, trans);									
				}
			}
		}
		return neList;
	}

	
	private Vector<String> getUpperCaseNE(String[] aForeignSentenceArray) throws IOException {
		Vector<String> ngrams = new Vector<String>();
		
		for (int i = 0; i < aForeignSentenceArray.length; i++) {
			ngrams.addAll(util.getNgramNE(aForeignSentenceArray[i], 3, false));			
		}
		
		Vector<String> local = new Vector<String>();
		for (int i = 0; i < aForeignSentenceArray.length; i++) {
			local.addAll(util.getNgramNE(aForeignSentenceArray[i], 2, false));			
		}
		
		for (int i = 0; i < ngrams.size(); i++) {
			String words[] = ngrams.get(i).split("\\s");
			String ngram2 = words[0] + " " + words[1];
			if (local.contains(ngram2)) {
				local.remove(ngram2);
			}
			ngram2 = words[1] + " " + words[2];
			if (local.contains(ngram2)) {
				local.remove(ngram2);
			}
		}
		
		ngrams.addAll(local);
		local = new Vector<String>();
		
		for (int i = 0; i < aForeignSentenceArray.length; i++) {
			local.addAll(util.getNgramNEWithoutFirstWord(aForeignSentenceArray[i], 1, false));			
		}

		for (int i = 0; i < ngrams.size(); i++) {
			String words[] = ngrams.get(i).split("\\s");
			if (words.length >= 2) {
				String ngram1 = words[0];
				if (local.contains(ngram1)) {
					local.remove(ngram1);
				}
				ngram1 = words[1];
				if (local.contains(ngram1)) {
					local.remove(ngram1);
				}				
			} 
			if (words.length > 2) {
				String ngram1 = words[2];
				if (local.contains(ngram1)) {
					local.remove(ngram1);
				}
			}
		}
		ngrams.addAll(local);
		return ngrams;
	}
	
	public static void main(String[] args) throws Exception {
		
		LinkEntry entity = new LinkEntry();
        if (args.length < 7) {
        	System.out.println("Please use: java -jar MapperUSFD.jar [method] [mappingFile] [outputFile] [taggingInfo] [sourceLang] [foreignLang] [similarityThreshold]");
        	return;
        }
		
        String method = args[0];
        //System.out.println("Method: "+method);
        Vector<String> mapping = Util.getFileContentAsVector(args[1]);
        String outputFile = args[2].trim();
        String isTagged = args[3].trim();
        String sourceLang = args[4].trim();
        String foreignLang = args[5].trim();
        double threshold = Double.parseDouble(args[6].trim());
        
//        method = "NE";
//        outputFile = "testing\\output.txt";
//        isTagged = "NE2";
//        foreignLang = "LT";
        
        //System.out.println("EN DIR: "+inputDirEn);
        //System.out.println("FOREIGN DIR: "+inputDirForeign);
        Map<String, Double> links = new HashMap<String, Double>();
        int previous = 0;
        for (int k = 0; k < mapping.size(); k++) {
              String pair = mapping.get(k);
              //System.out.println("PAIR: "+pair);
              String pairValues[] = pair.split("\t");
              File exist1 = new File(pairValues[0].trim());
              //System.out.println("F1: "+inputDirEn + pairValues[0].trim());
              File exist2 = new File(pairValues[1].trim());
              //System.out.println("F2: "+inputDirForeign +  pairValues[1].trim());
              if (exist1.exists() && exist2.exists()) {
                    //System.out.println("EXISTS");
                    String englishContent = Util.getFileContentAsBuffer(pairValues[0].trim()).toString();
                    String targetContent = null;
                    try {
                        targetContent = Util.getFileContentAsBufferUTF(pairValues[1].trim()).toString();                    	
                    } catch (Exception e) {
                        targetContent = Util.getFileContentAsBuffer(pairValues[1].trim()).toString();
                    }
                    
                   if (method.equalsIgnoreCase("NE")) {
                          //System.out.println("N");
                        links.putAll(entity.alignNE(englishContent, targetContent, isTagged, foreignLang, threshold));
                   } else if (method.equalsIgnoreCase("T")) {
                          //System.out.println("T");
                          try {
                                links.putAll(entity.alignTE(englishContent, targetContent, isTagged, sourceLang, foreignLang, threshold));
                          } catch (Exception e) {
                                e.printStackTrace();
                          }
                    }
                    int change = links.size()-previous;
                    if (change>0) {
                          links = Util.sortByValue(links, true);
                          Util.doSave5UTF(outputFile, links, ":::");
                    }
                    //System.out.println(Integer.toString(k)+": "+pairValues[0].trim()+", "+pairValues[1].trim()+" - total: "+Integer.toString(links.size())+", new: "+Integer.toString(change));
                    previous = links.size();
              }
        }
        Util.doSave5UTF(outputFile, links, "\t");

	}

}
