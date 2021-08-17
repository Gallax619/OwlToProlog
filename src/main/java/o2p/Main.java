package o2p;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import de.tudresden.inf.lat.jcel.coreontology.axiom.NormalizedIntegerAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.ComplexIntegerAxiom;

public class Main {

	public static void main(String[] args) {
		
		Normalizator norm = new Normalizator();
		MyTranslator translator = new MyTranslator();
		
		
		//service scanner to let the program go on only when receives input
		Scanner sc = new Scanner(System.in);

		//load owl file
		OWLOntology ontology;
		try {
			ontology = Normalizator.loadOntology("./res/ore_ont_16626.owl");
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
			return;
		}
		System.out.println(ontology);
		sc.nextLine();
		
		
		
		
		System.out.println("Printing original axioms");
		for(OWLAxiom a : ontology.getAxioms()) {
			System.out.println(a.toString());
		}
		sc.nextLine();
		
		
		//translate OWL axioms to Jcel integer based axioms		
		Set<ComplexIntegerAxiom> integerAxioms = translator.owlToIntTranslate(ontology);
	
		//normalize the integer axioms		
		Set<NormalizedIntegerAxiom> normalizedAxioms = Normalizator.normalize(integerAxioms);
		
		
		//print normalized axioms
		/*System.out.println("Printing integer normalized axioms");
		int cont = 0;
		for(NormalizedIntegerAxiom a : normalizedAxioms) {
			System.out.println(cont + a.toString());
			cont++;
		}
		sc.nextLine();
		*/
		
		PrologParser.parsePrint(normalizedAxioms, System.out);
		
		System.out.println(translator.indexMapping(26));
		
		sc.nextLine();
		
		/*Set<OWLAxiom> owlNormalizedAxioms = translator.normIntToOWLTranslate(normalizedAxioms, ontology);
		
		System.out.println("Printing owl normalized axioms");
		for(OWLAxiom a : owlNormalizedAxioms) {
			System.out.println(a.toString());
		}
		*/
		
		System.out.println("finito");
	}
	
	public static void prova() {
		
	}

}
