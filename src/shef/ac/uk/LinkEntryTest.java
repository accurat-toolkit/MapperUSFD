package shef.ac.uk;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.shef.uk.util.Util;


public class LinkEntryTest {
	
	public static void main(String[] args) throws Exception {
		LinkEntry entity = new LinkEntry();
//		File inputFolder = new File("F:\\corpus\\news\\");
//		File folders[] = inputFolder.listFiles();
//		for (int i = 0; i < folders.length; i++) {
//			if (!folders[i].getName().equals("en-ro")) {
//				continue;
//			}
			Map<String, Double> neLinks = new HashMap<String, Double>();
			String eng = Util.getFileContentAsBuffer("testing\\englishTE.txt").toString();		
			String lv = Util.getFileContentAsBuffer("testing\\latvianTE.txt").toString();		
			neLinks.putAll(entity.alignTE(eng, lv, "T1-Trans", "en", "lv", 0.5));
			
//			Vector<String> mapping = Util.getFileContentAsVector(folders[i].getAbsolutePath() + "\\alignment.txt");		
//			String foreignLang = folders[i].getName().replaceAll("en-", "").trim();
//			String saveName = "-mappingWithNETaggedFiles.txt";
//			for (int k = 1; k < mapping.size(); k++) {
//				String pair = mapping.get(k);
//				String pairValues[] = pair.split("\\t");
//				File exist1 = new File(folders[i].getAbsolutePath() + "\\" + pairValues[0].trim());
//				File exist2 = new File(folders[i].getAbsolutePath() + "\\" + pairValues[1].trim().replaceAll(".*\\\\", foreignLang + "NETagged\\\\"));
//				boolean isScenario1 = true;
//				if (!exist2.exists()) {
//					exist2 = new File(folders[i].getAbsolutePath() + "\\" + pairValues[1].trim());
//					saveName = "-mapping.txt";
//					isScenario1 = false;
//				}
//				if (exist1.exists() && exist2.exists()) {
//					System.out.println(exist1 + "=" + exist2 + " " + isScenario1);
//					String englishContent = Util.getFileContentAsBuffer(exist1.getAbsolutePath()).toString();
//					String targetContent = Util.getFileContentAsBuffer(exist2.getAbsolutePath()).toString();
//					if (isScenario1) {
//						neLinks.putAll(entity.alignNE(englishContent, targetContent, "NE1", foreignLang));						
//					} else {
//						neLinks.putAll(entity.alignNE(englishContent, targetContent, "NE0", foreignLang));
//					}
//					neLinks = Util.sortByValue(neLinks, true);
//					Util.doSave5UTF("F:\\corpus\\NE-mappings\\ne-mappings-news\\" + folders[i].getName() + saveName, neLinks, "\t");						
//				}
//			}
//			System.out.println(entity.foreign.size() + " " + entity.english.size());
//			neLinks = Util.sortByValue(neLinks, true);
//			System.out.println(neLinks);
//			Util.doSave5UTF("F:\\corpus\\NE-mappings\\ne-mappings-news\\" + folders[i].getName() + saveName, neLinks, "\t");						
//			
//		}

		
//		File inputFolder = new File("F:\\corpus\\wikipedia_anchor\\");
//		File folders[] = inputFolder.listFiles();
//		for (int i = 0; i < folders.length; i++) {
//			Map<String, Double> neLinks = new HashMap<String, Double>();
//			if (!folders[i].getName().contains("-")) {
//				continue;
//			}
////			if (!folders[i].getName().equals("sl-en") || folders[i].getName().equals("ne-mappings")) {
////				continue;
////			}
//			if (!folders[i].getName().equals("ro-en")) {
//			continue;
//		}
//
//			Vector<String> mapping = Util.getFileContentAsVector(folders[i].getAbsolutePath() + "\\alignment.txt");		
//			String langValues[] = folders[i].getName().split("-");
//			
//			String firstLang = langValues[0];
//			String secondLang = langValues[1];
//			boolean isScenario2 = false;
//			if (!"en".equals(secondLang.trim())) {
//				isScenario2 = true;
//			}
//			String saveName = "-mappingWithNETaggedFiles.txt";
//			for (int k = 1; k < mapping.size(); k++) {
//				String pair = mapping.get(k);
//				System.out.println(folders[i].getName());
//				String pairValues[] = pair.split("\\t");
//				File exist1 = new File(folders[i].getAbsolutePath() + "\\" + pairValues[1].trim().replaceAll(".*" + folders[i].getName(), folders[i].getName()).replaceAll(".*\\\\", secondLang + "NETagged\\\\"));
//				File exist2 = new File(folders[i].getAbsolutePath() + "\\" + pairValues[0].trim().replaceAll(".*" + folders[i].getName(), folders[i].getName()).replaceAll(".*\\\\", firstLang + "NETagged\\\\"));
//				boolean isTaggedForeign = true;
//				boolean isTaggedSource = true;
//				if (!exist2.exists()) {
//					exist2 = new File(folders[i].getAbsolutePath() + "\\" + pairValues[0].trim().replaceAll(".*" + folders[i].getName(), ""));
//					isTaggedForeign = false;
//				}
//				if (!exist1.exists()) {
//					exist1 = new File(folders[i].getAbsolutePath() + "\\" + pairValues[1].trim().replaceAll(".*" + folders[i].getName(), ""));
//					isTaggedSource = false;
//				}
//				if (exist1.exists() && exist2.exists()) {
//					System.out.println(exist1 + "=" + exist2 + " " + isTaggedForeign);
//					String englishContent = Util.getFileContentAsBuffer(exist1.getAbsolutePath()).toString();
//					String targetContent = Util.getFileContentAsBuffer(exist2.getAbsolutePath()).toString();
//					if (isTaggedForeign && !isScenario2) {
//						neLinks.putAll(entity.alignNE(englishContent, targetContent, "NE1", firstLang));						
//					} else if (!isTaggedForeign && !isTaggedSource && !isScenario2){
//						neLinks.putAll(entity.alignNE(englishContent, targetContent, "NE0", firstLang));
//						saveName = "-mapping.txt";
//					} else if (isTaggedForeign && isTaggedSource && isScenario2){
//						neLinks.putAll(entity.alignNE(englishContent, targetContent, "NE2", firstLang));
//					}
//					neLinks = Util.sortByValue(neLinks, true);
//					Util.doSave5UTF("F:\\corpus\\NE-mappings\\ne-mappings-wiki\\" + folders[i].getName() + saveName, neLinks, "\t");						
//				}
//			}
//			System.out.println(entity.foreign.size() + " " + entity.english.size());
//			neLinks = Util.sortByValue(neLinks, true);
//			System.out.println(neLinks);
//			Util.doSave5UTF("F:\\corpus\\NE-mappings\\ne-mappings-wiki\\" + folders[i].getName() + saveName, neLinks, "\t");						
//			
//		}
		

		
		
//		inputFolder = new File("F:\\testTools\\NEEvaluation\\");
//		neLinks = new HashMap<String, Double>();
//		mapping = Util.getFileContentAsVector(inputFolder.getAbsolutePath() + "\\lv-en-mapping.txt");				
//		for (int k = 1; k < mapping.size(); k++) {
//			String pair = mapping.get(k);
//			String pairValues[] = pair.split("\\s");
//			File exist1 = new File("E:\\davidAccuratData\\EN-LV\\" + pairValues[0].trim());
//			File exist2 = new File("F:\\testTools\\NEEvaluation\\" + "\\lv\\" + pairValues[1].replaceAll(".xml", ".txt").trim());
//			if (exist1.exists() && exist2.exists()) {
//				Element root = Util.getFileContentAsDomElement(exist1.getAbsolutePath()).getRootElement().getChild("cesheader").getChild("text").getChild("body");
//				String englishContent = root.getText();
//				
//				String targetContent = Util.getFileContentAsBuffer(exist2.getAbsolutePath()).toString();
//				neLinks.putAll(entity.alignNE(englishContent, targetContent, "NE1", foreingLang));
//				System.out.println(neLinks);
//				neLinks = Util.sortByValue(neLinks, true);
//				//Util.doSave5UTF("F:\\testTools\\NEEvaluation\\en-lv.mapping", neLinks, ":::");						
//			}
//		}
//		neLinks = Util.sortByValue(neLinks, true);
//		Util.doSave5UTF("F:\\testTools\\NEEvaluation\\en-lv.mapping", neLinks, ":::");						

//		for (int k = 1; k < mapping.size(); k++) {
//		String pair = mapping.get(k);
//		String pairValues[] = pair.split("\\t");
//		File exist1 = new File(inputFolder.getAbsolutePath() + "\\en-" + foreingLang + "\\" + pairValues[0].trim());
//		File exist2 = new File("F:\\testTools\\NEEvaluation\\" + "\\" + pairValues[0].trim());
//		if (exist1.exists() && exist2.exists()) {
//			//System.out.println(exist1 + "=" + exist2);
//			String englishContent = Util.getFileContentAsBuffer(exist1.getAbsolutePath()).toString();
//			String targetContent = Util.getFileContentAsBuffer(exist2.getAbsolutePath()).toString();
//			neLinks.putAll(entity.alignNE(englishContent, targetContent, "NE1", foreingLang));
//			neLinks = Util.sortByValue(neLinks, true);
//			//Util.doSave5UTF("F:\\testTools\\NEEvaluation\\en-" + foreingLang +".mapping", neLinks, ":::");						
//		}
//	}
//	System.out.println(entity.foreign.size() + " " + entity.english.size());
//	neLinks = Util.sortByValue(neLinks, true);
	//Util.doSave5UTF("F:\\testTools\\NEEvaluation\\en-" + foreingLang +".mapping", neLinks, ":::");						

		//Vector<String> mapping = Util.getFileContentAsVector("F:\\testTools\\TEEvaluation\\" + "\\EN_LT.txt.T.mapper.lst");				
		//Map<String, Double> neLinks = new HashMap<String, Double>();

//		for (int k = 1; k < mapping.size(); k++) {
//			String pair = mapping.get(k);
//			String pairValues[] = pair.split("\\t");
//			File exist1 = new File(pairValues[0].trim());
//			File exist2 = new File(pairValues[1].trim());
//			if (true || exist1.exists() && exist2.exists()) {
//				//System.out.println(exist1 + "=" + exist2);
//				String englishContent = "<TENAME>live bacteria</TENAME> Help Create Rain, Snow and Hail, Research Finds";//Util.getFileContentAsBuffer(exist1.getAbsolutePath()).toString();
//                String targetContent = null;
//                try {
//                	targetContent = "sukelia <TENAME>gyvos bakterijos</TENAME> - Mokslas ir IT ";//Util.getFileContentAsBufferUTF(exist2.getAbsolutePath()).toString();                    	
//                } catch (Exception e) {
//                	targetContent = Util.getFileContentAsBuffer(exist2.getAbsolutePath()).toString(); 
//			}
//				neLinks.putAll(entity.alignTE(englishContent, targetContent, "T1", foreingLang));
//				neLinks = Util.sortByValue(neLinks, true);
//				System.out.println(neLinks);
//				//Util.doSave5UTF("F:\\testTools\\TEEvaluation\\en-" + foreingLang +".mapping", neLinks, ":::");						
//			}
//		}
//		System.out.println(entity.foreign.size() + " " + entity.english.size());
//		neLinks = Util.sortByValue(neLinks, true);
//		Util.doSave5UTF("F:\\testTools\\TEEvaluation\\en-" + foreingLang +".mapping", neLinks, ":::");						

	}

}
