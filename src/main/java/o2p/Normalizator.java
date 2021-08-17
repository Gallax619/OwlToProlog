package o2p;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

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
 * Classe con metodi che servono per la normalizzazione di una OWLOntology
 * @author Andrea
 *
 */

public class Normalizator {
	
	private HashMap<NormalizedIntegerAxiom, ComplexIntegerAxiom> originalAxiomMap;
	private boolean isNormalized;
	
	
	/**
	 * Inizializza attributi di servizio
	 */
	public Normalizator(){
		///
		originalAxiomMap = new HashMap<NormalizedIntegerAxiom, ComplexIntegerAxiom>();
		isNormalized = false;
	}
	
	/**
	 * Carica un file .owl in memoria
	 * @param path - percorso relativo del file
	 * @return oggetto OWLOntology
	 * @throws OWLOntologyCreationException
	 */
	public static OWLOntology loadOntology(String path) throws OWLOntologyCreationException {
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();
		IRI ontologyIRI = IRI.create(new File(path));
		OWLOntology ontology = man.loadOntologyFromOntologyDocument(ontologyIRI);
		return ontology;
	}
	
	///
	
	/**
	 * Normalizza un Set di assiomi integerBased
	 * @param integerAxioms
	 * @return Set di assiomi normalizzati
	 */
	public static Set<NormalizedIntegerAxiom> normalize(Set<ComplexIntegerAxiom> integerAxioms){
		OntologyNormalizer normalizer = new OntologyNormalizer();		
		return normalizer.normalize(integerAxioms, new IntegerOntologyObjectFactoryImpl());
		
	}
	
	///
	
	
	/* idea: conservare l'assioma originale dopo la normalizzazione in una hashmap
	 * dove la chiave è l'assioma normalizzato e il valore restituito è l'assioma originale.
	 * Basta tenere quello intero o si deve convertire anche quello originale?
	*/
	public Set<NormalizedIntegerAxiom> normalizeSaveOriginal(Set<ComplexIntegerAxiom> integerAxioms){
		OntologyNormalizer normalizer = new OntologyNormalizer();
		HashSet<NormalizedIntegerAxiom> allNormalizedAxioms = new HashSet<NormalizedIntegerAxiom>();
		
		for(ComplexIntegerAxiom a : integerAxioms) {
			//metto a in un HashSet perché normalize vuole un set e non un singolo assioma
			HashSet<ComplexIntegerAxiom> singleAxiom = new HashSet<ComplexIntegerAxiom>();
			singleAxiom.add(a);
			
			Set<NormalizedIntegerAxiom> normalizedAxioms = 
					normalizer.normalize(singleAxiom, new IntegerOntologyObjectFactoryImpl());
			
			allNormalizedAxioms.addAll(normalizedAxioms);
			
			for(NormalizedIntegerAxiom na : normalizedAxioms) {
				originalAxiomMap.put(na, a);
			}
			
		}
		
		isNormalized = true;
		return allNormalizedAxioms;
	}
	
	public ComplexIntegerAxiom getOriginalAxiom(NormalizedIntegerAxiom a) {
		if(isNormalized)
			return originalAxiomMap.get(a);
		else
			return null;
	}
	
	public HashMap<NormalizedIntegerAxiom, ComplexIntegerAxiom> getOriginalMap(){
		return originalAxiomMap;
	}
	
	
	
	
	///
	
	
	//todo: fare metodo unico per normalizzare dall'inizio alla fine. Riempire attributi di classe
	//		e mettere dei getter
	
	
	
	
	

}
