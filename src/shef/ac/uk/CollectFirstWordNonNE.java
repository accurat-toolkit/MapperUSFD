package shef.ac.uk;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.shef.uk.util.TextProcessingUtilEnglish;
import com.shef.uk.util.Util;


public class CollectFirstWordNonNE {
	private TextProcessingUtilEnglish util = new TextProcessingUtilEnglish();

	public void collectFirstWordNonNE(String aFolder, String aLanguage) throws IOException {
		File files[] = (new File(aFolder)).listFiles();
		Map<String, String> fakedNE = new HashMap<String, String>();
		Map<String, String> lowerCaseWords = new HashMap<String, String>();
		for (int i = 0; i < files.length; i++) {
			String str = Util.getFileContentAsBuffer(files[i].getAbsolutePath()).toString();
			String sentences[] = util.detectSentences(str);
			for (int j = 0; j < sentences.length; j++) {
				String sentence = sentences[j];
				String simpleTokens[] = sentence.split("\\s");
				for (int k = 0; k < simpleTokens.length; k++) {
					String simpleToken = simpleTokens[k];
					if (k == 0) {
						if (!simpleToken.toLowerCase().equals(simpleToken)) {
							fakedNE.put(simpleToken, simpleToken);
						}
					} else {
						if (simpleToken.toLowerCase().equals(simpleToken)) {
							lowerCaseWords.put(simpleToken, simpleToken);
						}						
					}
				}
			}
		}
		Iterator<String> it = fakedNE.keySet().iterator();
		while (it.hasNext()) {
			String word = it.next();
			if (!lowerCaseWords.containsKey(word)) {
				System.out.println(word);
				it.remove();
			}
		}
		Util.doSave7("accuratResource\\" + aLanguage + "_fakedNE.txt", fakedNE);
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		CollectFirstWordNonNE obj = new CollectFirstWordNonNE();
//		obj.collectFirstWordNonNE("F:\\newCrawlData\\en-de\\de\\", "de");
		obj.collectFirstWordNonNE("F:\\newCrawlData\\en-ro\\ro\\", "ro");

	}

}
