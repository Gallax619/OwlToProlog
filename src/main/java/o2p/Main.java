package o2p;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import de.tudresden.inf.lat.jcel.coreontology.axiom.NormalizedIntegerAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.ComplexIntegerAxiom;

/**
 * Classe main che esegue il programma
 * @author Andrea Gallacci
 *
 */
public class Main {

	/**
	 * Programma che converte un' ontologia OWL in un programma prolog
	 * @param args[0] path dell'ontologia OWL
	 * @param args[1] (opzionale) path della cartella in cui salvare l'output
	 */
	public static void main(String[] args) {
		String ontologyPath = "";
		String outputPath = "./output";
		if(args.length == 0) {
			System.out.println("Error: ontology path is needed as first argument");
			System.out.println("First argument: ontology path");
			System.out.println("Second argument [optional]: output directory path, ./output if not specified");
			System.exit(-1);
		}else if(args.length == 1) {
			System.out.println(args[0]);
			ontologyPath = args[0];
		}else if(args.length == 2) {
			System.out.println(args[0]);
			System.out.println(args[1]);
			ontologyPath = args[0];
			outputPath = args[1];
		}else {
			System.out.println("Error: too much arguments");
			System.out.println("First argument: ontology path");
			System.out.println("Second argument [optional]: output directory path, ./output if not specified");
			System.exit(-1);
		}
		MyTranslator translator = new MyTranslator();

		//carica il file OWL
		OWLOntology ontology;
		try {
			ontology = Loader.loadOntology(ontologyPath);
		} catch (OWLOntologyCreationException e) {
			
			e.printStackTrace();
			return;
		}
		System.out.println("Ontology succesfully loaded:");
		System.out.println(ontology);
		
		
		//traduce gli assiomi OWL in assiomi Jcel integer based
		Set<ComplexIntegerAxiom> integerAxioms = translator.owlToIntTranslate(ontology);
		
		//indicizza gli integerAxioms in un vettore per mantenere un ordine
		Vector<ComplexIntegerAxiom> indexedAxioms = new Vector<ComplexIntegerAxiom>(integerAxioms);
	
		//normalizza gli assiomi interi
		Normalizator normalizator = new Normalizator();
		Set<NormalizedIntegerAxiom> normalizedAxioms = normalizator.normalizeSaveOriginal2(indexedAxioms);
		Map<NormalizedIntegerAxiom, Integer> originalMap = normalizator.getOriginalMap2();

		
		//crea i file e ci scrive il risultato
		File prologFile = new File(outputPath + "/prolog.txt");
		File originalAxiomsFile = new File(outputPath + "/originalAxioms.txt");
		File owlClassesFile = new File(outputPath + "/owlClasses.txt");
		try {
			PrintWriter prologWriter = new PrintWriter(prologFile);
			Printer.printProlog(indexedAxioms, normalizedAxioms, originalMap, prologWriter);
			System.out.println("Prolog file succesfully written");
			prologWriter.close();
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: unable to write prolog file");
			e.printStackTrace();
		}
		try {
			PrintWriter originalAxiomsWriter = new PrintWriter(originalAxiomsFile);
			Printer.printOriginalAxioms(indexedAxioms, translator, originalAxiomsWriter);
			System.out.println("Original axioms file succesfully written");
			originalAxiomsWriter.close();
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: unable to write prolog file");
			e.printStackTrace();
		}
		try {
			PrintWriter owlClassesWriter = new PrintWriter(owlClassesFile);
			Printer.printOwlClasses(normalizedAxioms, translator, owlClassesWriter);
			System.out.println("OWL classes file succesfully written");
			owlClassesWriter.close();
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: unable to write OWL classes file");
			e.printStackTrace();
		}
		
		System.out.println("Success");
	}
	
	

}
