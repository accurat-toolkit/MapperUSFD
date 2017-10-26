package com.shef.uk.util;

import java.io.IOException;
import java.util.Vector;

public class GreekNameListGenerator {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		Vector<String> list = Util.getFileContentAsVectorUTF("greek.txt");
		StringBuffer enEl = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			String line = list.get(i);
			String values[] = line.split("###");
			if (values.length == 6) {
				enEl.append(values[2]).append("\t").append(values[5]).append("\n");
			}
		}
		Util.doSaveUTF("enElNames.txt", enEl.toString());
	}

}
