package o2p;

import java.util.HashSet;

import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLLogicalEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.profiles.OWL2ELProfile;

import de.tudresden.inf.lat.jcel.coreontology.axiom.NormalizedIntegerAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.ComplexIntegerAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.IntegerOntologyObjectFactoryImpl;
import de.tudresden.inf.lat.jcel.owlapi.translator.ReverseAxiomTranslator;
import de.tudresden.inf.lat.jcel.owlapi.translator.TranslationException;
import de.tudresden.inf.lat.jcel.owlapi.translator.TranslationRepository;
import de.tudresden.inf.lat.jcel.owlapi.translator.Translator;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

/**
 * Classe che si occupa di tradurre una OWLOntology in assiomi integer based
 * @author Andrea Gallacci
 *
 */
public class MyTranslator {
	
	private Translator translator;
	
	public MyTranslator() {
		translator = new Translator(new OWLDataFactoryImpl(), new IntegerOntologyObjectFactoryImpl());
	}
	
	/**
	 * Traduttore da ontologia OWL EL ad assiomi integerBased
	 * @param ontology EL ontology
	 * @return Set di assiomi integerBased se valida, null altrimenti
	 */
	public Set<ComplexIntegerAxiom> owlToIntTranslate(OWLOntology ontology){
		if(ontologyELCheck(ontology))
			return translator.translateSA(ontology.getAxioms());
		else {
			System.out.println("Error: the ontology is not in the EL profile");
			return null;
		}
			
	}
	
	/**
	 * Traduttore inverso da assiomi (normalizzati) integerBased a assiomi OWL
	 * @param normalizedAxioms
	 * @param ontology - ontologia originale
	 * @return set di assiomi OWL
	 */
	public Set<OWLAxiom> normIntToOWLTranslate(Set<NormalizedIntegerAxiom> normalizedAxioms, OWLOntology ontology){
		ReverseAxiomTranslator intToOWLTranslator = new ReverseAxiomTranslator(translator, ontology);
		HashSet<OWLAxiom> owlAxioms = new HashSet<OWLAxiom>();
		
		for(NormalizedIntegerAxiom a : normalizedAxioms) {
			OWLAxiom owlNormalizedAxiom = a.accept(intToOWLTranslator);
			owlAxioms.add(owlNormalizedAxiom);
		}
		return owlAxioms;
	}
	
	/**
	 * Check if an ontology is in EL profile
	 * @param ontology
	 * @return true if there are no violations
	 */
	private static boolean ontologyELCheck(OWLOntology ontology) {
		OWL2ELProfile elProfile = new OWL2ELProfile(); 
		return elProfile.checkOntology(ontology).isInProfile();
	}
	
	
	/**
	 * Mapping da indice intero a OWLClass
	 * @param index
	 * @return OWLClass relativa all'indice
	 */
	public OWLLogicalEntity indexMapping(int index) {
		TranslationRepository repository = translator.getTranslationRepository();
		OWLLogicalEntity result;
		try {
			result = repository.getOWLClass(index);
		}catch(TranslationException e) {
			result = repository.getOWLObjectProperty(index);
		}
		return result;
	}
}
