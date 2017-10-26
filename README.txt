The application can be run using the following command:

F:\MapperUSFD>java -jar MapperUSFD.jar method mappingFile outputFile taggingInfo sourceLang foreignLang similarityThreshold

method: NE, T
	NE: select this when you want NE mapping, please specify also
		taggingInfo: NE0, NE1, NE2
		(NE0 means both input files are not NE tagged, NE1 means the foreign files are NE tagged according to MUC-7 style, NE2 means both input files are NE tagged according to MUC-7 style).
	T: select this when you want term mapping. Both input files must be tagged with terms, please also specify
		taggingInfo: T0, T1 (T0 means the foreign files are tagged with terms, T1 means both input files are tagged with terms. Style of tags: <TENAME>value</TENAME>)

mappingFile: Path to the files (full path with name) which are comparable to each other. Structure enFile\tforeignFile\n

outputfile: fileName to write the results in

sourceLang: language code for the source language (e.g. en for English)

foreingLang: language code for the foreign language (e.g. el for Greek)

similarityThreshold: a number between 0 and 1. The tool returns mappings whose similarity is above the specified threshold.

In case of the method "T" the user can also specify whether to use a dictionary translation: T0-Trans or T1-Trans. 

If the user selects to use this option then a dictionary file must be added to the folder Mapper/dict. The file name must be sourceLangCode_targetLangCode.txt, e.g. en_de.txt for
English German dictionary. The format of the dictionaries are as in DicMetric.

e.g. F:\MapperUSFD>java -jar MapperUSFD.jar T F:\mappingFile.txt f:\output.txt T1-Trans en de 0.5

