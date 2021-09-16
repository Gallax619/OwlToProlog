package o2p;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.profiles.OWL2ELProfile;

import de.tudresden.inf.lat.jcel.coreontology.axiom.NormalizedIntegerAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.ComplexIntegerAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.IntegerOntologyObjectFactoryImpl;
import de.tudresden.inf.lat.jcel.ontology.normalization.OntologyNormalizer;
import de.tudresden.inf.lat.jcel.owlapi.translator.ReverseAxiomTranslator;
import de.tudresden.inf.lat.jcel.owlapi.translator.Translator;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

/**
 * Classe con metodi che servono per la normalizzazione di assiomi complessi
 * @author Andrea Gallacci
 *
 */

public class Normalizator {
	
	private HashMap<NormalizedIntegerAxiom, ComplexIntegerAxiom> originalAxiomMap;
	private HashMap<NormalizedIntegerAxiom, Integer> axiomMap;
	private boolean isNormalized;
	
	
	/**
	 * Inizializza attributi di servizio
	 */
	public Normalizator(){
		///
		originalAxiomMap = new HashMap<NormalizedIntegerAxiom, ComplexIntegerAxiom>();
		axiomMap = new HashMap<NormalizedIntegerAxiom, Integer>();
		isNormalized = false;
	}
	
	
	
	/**
	 * Normalizza un Set di assiomi integerBased
	 * @param integerAxioms assiomi interi complessi
	 * @return Set di assiomi normalizzati
	 */
	public static Set<NormalizedIntegerAxiom> normalize(Set<ComplexIntegerAxiom> integerAxioms){
		OntologyNormalizer normalizer = new OntologyNormalizer();		
		return normalizer.normalize(integerAxioms, new IntegerOntologyObjectFactoryImpl());
		
	}
	
	
	
	/**
	 * Normalizza salvando gli originali mettendo il mapping tra assioma normalizzato e indice di quello
	 * complesso
	 * 
	 * @param integerAxioms vettore degli assiomi complessi
	 * @return insieme degli assiomi normalizzati
	 */
	public Set<NormalizedIntegerAxiom> normalizeSaveOriginal2(Vector<ComplexIntegerAxiom> integerAxioms){
		OntologyNormalizer normalizer = new OntologyNormalizer();
		HashSet<NormalizedIntegerAxiom> allNormalizedAxioms = new HashSet<NormalizedIntegerAxiom>();
		IntegerOntologyObjectFactoryImpl ontologyFactory = new IntegerOntologyObjectFactoryImpl();
		
		for(int i = 0; i < integerAxioms.size(); i++) {
			//metto a in un HashSet perché normalize vuole un set e non un singolo assioma
			HashSet<ComplexIntegerAxiom> singleAxiom = new HashSet<ComplexIntegerAxiom>();
			singleAxiom.add(integerAxioms.elementAt(i));
			
			Set<NormalizedIntegerAxiom> normalizedAxioms = 
					normalizer.normalize(singleAxiom, ontologyFactory);
			
			allNormalizedAxioms.addAll(normalizedAxioms);
			
			for(NormalizedIntegerAxiom na : normalizedAxioms) {
				axiomMap.put(na, i);
			}
			
		}
		
		isNormalized = true;
		return allNormalizedAxioms;
	}
	
	/**
	 * Dato un assioma normalizzato ritorna l'indice del rispettivo assioma complesso originale
	 * @param a assioma normalizzato
	 * @return indice (nel vettore degli assiomi complessi) dell'assioma originale 
	 */
	public int getOriginalAxiom2(NormalizedIntegerAxiom a) {
		if(isNormalized)
			return axiomMap.get(a);
		else
			return -1;
	}
	
	/**
	 * 
	 * @return mappa che associa assioma normalizzato con indice del complesso originale
	 */
	public HashMap<NormalizedIntegerAxiom, Integer> getOriginalMap2(){
		return axiomMap;
	}
	
	
	
	

}
