import gate.Annotation;
import gate.AnnotationSet;
import gate.Corpus;
import gate.Document;
import gate.DocumentContent;
import gate.Factory;
import gate.Gate;
import gate.creole.ResourceInstantiationException;
import gate.util.GateException;
import gate.util.Out;
import gate.util.web.WebCrimeReportAnalyser.SortedAnnotationList;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class GateUtil {
		
	private static final String TOKEN = "Token";
	private static final String ANNIE = "ANNIE";
	private static final String D_DIR = "plugins";
	private static final String GATE_CONFIG_XML = "gate.xml";
	private static Document doc = null;
	private static Corpus corpus = null;
	private static StandAloneAnnie annie = null;

	public static void RunGate() {
		gateInit();
		loadAnniePlugin();				
	}
	
	@SuppressWarnings("unchecked")
	public static List getToken(String question){
		setupDocumentAndCorpus(question);
		return getTokenAndKind();		
	}

	public static void gateInit() {
		// Init Gate : set Gate Home ,Plugin Home and config file .
		Gate.setGateHome(new File(D_DIR));
		Gate.setPluginsHome(new File(D_DIR));
		Gate.setSiteConfigFile(new File(GATE_CONFIG_XML));
		try {
			Gate.init();
		} catch (GateException e) {
			e.printStackTrace();
		}
	}

	public static void loadAnniePlugin() {
		// Load ANNIE plugin
		File gateHome = Gate.getGateHome();
		File pluginsHome = new File(gateHome, "");
		Out.prln("...GATE initialised");
		try {
			Gate.getCreoleRegister().registerDirectories(
					new File(pluginsHome, ANNIE).toURL());
			annie = new StandAloneAnnie();
			// initialise ANNIE (this may take several minutes)
			annie.initAnnie();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (GateException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static void setupDocumentAndCorpus(String sentence) {
		try {
			corpus = (Corpus) Factory.createResource("gate.corpora.CorpusImpl");
			doc = (Document) Factory.newDocument(sentence);
			if (doc != null && annie != null) {
				corpus.add(doc);
				annie.setCorpus(corpus);
				// Get content and print it into console
				DocumentContent docContent = doc.getContent();
			}
			annie.execute();
		} catch (ResourceInstantiationException e) {
			e.printStackTrace();
		} catch (GateException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static List getTokenAndKind() {
		List resultToken = new ArrayList<Token>();
		Iterator iter = corpus.iterator();

		while (iter.hasNext()) {
			Document doc = (Document) iter.next();
			AnnotationSet defaultAnnotSet = doc.getAnnotations();
			Set annotTypesRequired = new HashSet();
			annotTypesRequired.add(TOKEN);
			Annotation currAnnot;
			StringBuffer editableContent = new StringBuffer(doc.getContent()
					.toString());

			Out.prln("Get all Annotation Set");
			Set<Annotation> token = new HashSet<Annotation>(defaultAnnotSet
					.get(annotTypesRequired));

			SortedAnnotationList sortedAnnotations = new SortedAnnotationList();

			Iterator it = token.iterator();
			while (it.hasNext()) {
				currAnnot = (Annotation) it.next();
				sortedAnnotations.addSortedExclusive(currAnnot);
			}

			
			Out.prln("Unsorted annotations count: " + token.size());
			Out.prln("Sorted annotations count: " + sortedAnnotations.size());
			Out.prln("=============================");
			long insertPositionEnd;
			long insertPositionStart;
			for (int i = sortedAnnotations.size() - 1; i >= 0; --i) {
				currAnnot = (Annotation) sortedAnnotations.get(i);
				insertPositionStart = currAnnot.getStartNode().getOffset()
						.longValue();
				insertPositionEnd = currAnnot.getEndNode().getOffset()
						.longValue();
				if (insertPositionEnd != -1 && insertPositionStart != -1) {
					String tmpAno = editableContent.substring(
							(int) insertPositionStart, (int) insertPositionEnd);					
					Token tempToken = new Token(tmpAno, (String) currAnnot
							.getFeatures().get("category"));
					resultToken.add(tempToken);
				}
			}
			Out.prln("=============================");
		}
		return resultToken;
	}
}
