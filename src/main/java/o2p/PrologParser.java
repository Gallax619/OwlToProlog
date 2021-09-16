package o2p;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

/**
 * Classe con metodi che servono per fare il parsing da assiomi a regole e fatti prolog
 * @author Andrea Gallacci
 *
 */

public class PrologParser {

	//private Set<NormalizedIntegerAxiom> normAxioms;
	
	/**
	 * Stampa sull'output passato i fatti prolog
	 * @param normAxioms assiomi da fare il parsing
	 * @param out printwriter su cui verrà stampato il risultato
	 */
	public static void parsePrintFact(Set<NormalizedIntegerAxiom> normAxioms, PrintWriter out) {
		for(NormalizedIntegerAxiom a : normAxioms) {
			out.println(parseFact(a));
		}
	}
	
	/**
	 * Stampa sull'output passato le regole prolog complete di assioma originale
	 * @param normAxioms insieme di assiomi
	 * @param map mappa assiomi normalizzati con assiomi originali
	 * @param out printwriter su cui verrà stampato il risultato
	 */
	public static void parsePrintRule(Set<NormalizedIntegerAxiom> normAxioms, Map<NormalizedIntegerAxiom, Integer> map, PrintWriter out) {
		for(NormalizedIntegerAxiom a : normAxioms) {
			out.println(parseRule(a, map.get(a)));
		}
	}
	
	/**
	 * Stampa sull'output i fatti prolog originali 
	 * @param ca vettore degli assiomi complessi
	 * @param out printwriter su cui verrà stampato il risultato
	 */
	public static void printOriginalFacts(Vector<ComplexIntegerAxiom> ca, PrintWriter out) {
		for(int i = 0; i < ca.size(); i++) {
			if(ca.elementAt(i) != null) {
				out.println(parseOriginalFact(i));
			}
		}
	}

	/**
	 * Parsing da assioma integer based a fatto prolog
	 * @param a assioma integer based normalizzato
	 * @return stringa con fatto prolog relativo all' assioma a
	 */
	private static String parseFact(NormalizedIntegerAxiom a) {
		String result = "";
		if(a instanceof GCI0Axiom) {
			GCI0Axiom a0 = (GCI0Axiom) a;
			result = "s0(" + a0.getSubClass() + ", " + a0.getSuperClass() + ").";
		}else if(a instanceof GCI1Axiom) {
			GCI1Axiom a1 = (GCI1Axiom) a;
			result = "s1(" + a1.getLeftSubClass() + ", " + a1.getRightSubClass() 
				+ ", " + a1.getSuperClass() + ").";
		}else if(a instanceof GCI2Axiom) {
			GCI2Axiom a2 = (GCI2Axiom) a;
			result = "s2(" + a2.getSubClass() + ", " + a2.getPropertyInSuperClass() 
				+ ", " + a2.getClassInSuperClass() + ").";
		}else if(a instanceof GCI2Axiom) {
			GCI3Axiom a3 = (GCI3Axiom) a;
			result = "s3(" + a3.getPropertyInSubClass() + ", " + a3.getClassInSubClass() 
				+ ", " + a3.getSuperClass() + ").";
		}
		
		return result;
	}
	
	/**
	 * Parsing da assioma integer based a regola prolog che conserva l'assioma originale
	 * @param a assioma integer based normalizzato
	 * @param originalIndex indice dell'assioma complesso originale
	 * @return stringa con regola prolog relativa all' assioma a
	 */
	private static String parseRule(NormalizedIntegerAxiom a, int originalIndex) {
		String result = "";
		if(a instanceof GCI0Axiom) {
			GCI0Axiom a0 = (GCI0Axiom) a;
			result = "s0(" + a0.getSubClass() + ", " + a0.getSuperClass()
				+ ") :- o(" + originalIndex + ").";
		}else if(a instanceof GCI1Axiom) {
			GCI1Axiom a1 = (GCI1Axiom) a;
			result = "s1(" + a1.getLeftSubClass() + ", " + a1.getRightSubClass() 
				+ ", " + a1.getSuperClass() + ") :- o(" + originalIndex + ").";
		}else if(a instanceof GCI2Axiom) {
			GCI2Axiom a2 = (GCI2Axiom) a;
			result = "s2(" + a2.getSubClass() + ", " + a2.getPropertyInSuperClass() 
				+ ", " + a2.getClassInSuperClass() + ") :- o(" + originalIndex + ").";
		}else if(a instanceof GCI2Axiom) {
			GCI3Axiom a3 = (GCI3Axiom) a;
			result = "s3(" + a3.getPropertyInSubClass() + ", " + a3.getClassInSubClass() 
				+ ", " + a3.getSuperClass() + ") :- o(" + originalIndex + ").";
		}
		
		return result;
	}
	
	/**
	 * Parsing di un semplice fatto prolog dato un indice
	 * @param index indice dell'assioma complesso
	 * @return stringa relativa al fatto prolog di indice index
	 */
	private static String parseOriginalFact(int index) {
		return "o(" + index + ").";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
