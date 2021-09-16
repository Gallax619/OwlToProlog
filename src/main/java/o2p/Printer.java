package o2p;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import de.tudresden.inf.lat.jcel.coreontology.axiom.GCI0Axiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.GCI1Axiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.GCI2Axiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.GCI3Axiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.NormalizedIntegerAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.ComplexIntegerAxiom;
import de.tudresden.inf.lat.jcel.owlapi.translator.TranslationException;

/**
 * Classe che si occupa di scrivere gli output su file specificati
 * @author Andrea Gallacci
 *
 */
public class Printer {
	
	/**
	 * Metodo che scrive il file prolog
	 * @param ca vettore degli assiomi originali
	 * @param normAxioms assiomi normalizzati
	 * @param map mappa assiomi normalizzati con assiomi originali
	 * @param out printwriter su cui verrà stampato il risultato
	 */
	public static void printProlog(Vector<ComplexIntegerAxiom> ca, Set<NormalizedIntegerAxiom> normAxioms, Map<NormalizedIntegerAxiom, Integer> map, PrintWriter out) {
		PrologParser.printOriginalFacts(ca, out);
		out.flush();
		PrologParser.parsePrintRule(normAxioms, map, out);
		out.flush();
	}
	
	/**
	 * Metodo che scrive il file degli assiomi originali
	 * L'indice dell'assioma è il relativo numero di riga nel file
	 * @param ca vettore degli assiomi originali
	 * @param translator translator utilizzato per creare gli assiomi integer based
	 * @param out printwriter su cui verrà stampato il risultato
	 */
	public static void printOriginalAxioms(Vector<ComplexIntegerAxiom> ca, MyTranslator translator, PrintWriter out) {
		for(int i = 0; i < ca.size(); i++) {
			out.println(parseOriginalAxiom(ca.elementAt(i).toString(), translator));
		}
		out.flush();
	}
	
	/**
	 * Metodo che scrive il file delle classi OWL
	 * L'indice della classe è il relativo numero di riga nel file
	 * @param normAxioms assiomi normalizzati
	 * @param translator translator translator utilizzato per creare gli assiomi integer based
	 * @param out printwriter su cui verrà stampato il risultato
	 */
	public static void printOwlClasses(Set<NormalizedIntegerAxiom> normAxioms, MyTranslator translator, PrintWriter out) {
		//trova l'indice massimo
		int currentMax = 0;
		for(NormalizedIntegerAxiom a : normAxioms) {
			currentMax = max(currentMax, maxIndex(a));
		}
		//stampa la traduzione di tutti gli indici fino al massimo
		for(int i = 0; i < currentMax; i++) {
			try {
				out.println(translator.indexMapping(i));
			}catch(TranslationException e) {
				out.println();
			}
		}
		out.flush();
	}
	
	/**
	 * Parsing da assioma integer based a assioma OWL
	 * @param intAxiom stringa relativa all'assioma integer based
	 * @param translator translator translator translator utilizzato per creare gli assiomi integer based
	 * @return assioma owl
	 */
	private static String parseOriginalAxiom(String intAxiom, MyTranslator translator) {
		String result = intAxiom;
		int numbers = countNumbers(intAxiom);
		for(int i = 0; i < numbers; i++) {
			int current = extractNumber(intAxiom, i);
			result = result.replace("" + current, translator.indexMapping(current).toString());
		}
		
		return result;
	}
	
	/**
	 * Metodo helper che data una string a conta quanti numeri distinti ci sono
	 * @param s stringa
	 * @return quantità di numeri presenti in essa
	 */
	private static int countNumbers(String s) {
		int cont  = 0;
		boolean found = false;
		for(int i = 0; i < s.length(); i++) {
			if(Character.isDigit(s.charAt(i))) {
				found = true;
			}else if(found && !Character.isDigit(s.charAt(i))) {
				cont++;
				found = false;
			}
		}
		return cont; 
	}
	
	
	/**
	 * Metodo helper che data una stringa estrae il numero successivo a n
	 * @param s stringa 
	 * @param n quantità di numeri da ignorare
	 * @return numero estratto
	 */
	//forse è meglio estrarre solo il primo numero passando la stringa che man mano si evolve
	private static int extractNumber(String s, int n) {
		String appoggio = "";
		int cont = 0;
		for(int i = 0; i < s.length(); i++) {
			if(Character.isDigit(s.charAt(i))) {
				appoggio = appoggio + s.charAt(i);
			}else if(s.charAt(i) == ' ') {
				if(!appoggio.equals("")) {
					//ha trovato un numero
					if(cont == n){
						return Integer.parseInt(appoggio);
					}else{
						cont++;
						appoggio = "";
					}
				}
			}
		}
		if(!appoggio.equals("")) {
			//ha trovato un numero alla fine
			if(cont == n)
				return Integer.parseInt(appoggio);
		}
		
		return -1;
	}
	
	/**
	 * Metodo helper che dato un assioma normalizzato estrae l'indice massimo al suo interno
	 * @param a assioma normalizzato
	 * @return indice massimo nell'assioma
	 */
	private static int maxIndex(NormalizedIntegerAxiom a) {
		int max = 0;
		if(a instanceof GCI0Axiom) {
			GCI0Axiom a0 = (GCI0Axiom) a;
			max = max(a0.getSubClass(), a0.getSuperClass());
		}else if(a instanceof GCI1Axiom) {
			GCI1Axiom a1 = (GCI1Axiom) a;
			max = max(a1.getLeftSubClass(), a1.getRightSubClass());
			max = max(max, a1.getSuperClass());
		}else if(a instanceof GCI2Axiom) {
			GCI2Axiom a2 = (GCI2Axiom) a;
			max = max(a2.getSubClass(), a2.getPropertyInSuperClass()); 
			max = max(max, a2.getClassInSuperClass());
		}else if(a instanceof GCI2Axiom) {
			GCI3Axiom a3 = (GCI3Axiom) a;
			max = max(a3.getPropertyInSubClass(), a3.getClassInSubClass()); 
			max = max(max, a3.getSuperClass());
		}
		
		return max;
	}
	
	/**
	 * Metodo helper che calcola il massimo tra due numeri
	 * @param a
	 * @param b
	 * @return il numero maggiore
	 */
	private static int max(int a, int b) {
		if(a > b)
			return a;
		else
			return b;
	}
	
	
}
