package o2p;

import java.io.File;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

/**
 * Classe che si occupa di caricare un ontologia OWL
 * @author Andrea Gallacci
 *
 */
public class Loader {

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
	
}
