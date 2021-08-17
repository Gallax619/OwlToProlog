package o2p;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Set;

import de.tudresden.inf.lat.jcel.coreontology.axiom.GCI0Axiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.GCI1Axiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.GCI2Axiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.GCI3Axiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.NormalizedIntegerAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.ComplexIntegerAxiom;


public class PrologParser {

	//private Set<NormalizedIntegerAxiom> normAxioms;
	
	public static void parsePrint(Set<NormalizedIntegerAxiom> normAxioms, PrintStream out) {
		for(NormalizedIntegerAxiom a : normAxioms) {
			out.println(parseFact(a));
		}
	}
	
	public static void parseOnFile(Set<NormalizedIntegerAxiom> normAxioms, File file) throws IOException {
		FileWriter writer = new FileWriter(file);
		for(NormalizedIntegerAxiom a : normAxioms) {
			writer.append(a.getClass().getName() + "\n");
		}
		writer.close();
	}
	
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
	
	
	public static String parseRule(NormalizedIntegerAxiom na, ComplexIntegerAxiom ca) {
		String result = "";
		if(na instanceof GCI0Axiom) {
			GCI0Axiom a0 = (GCI0Axiom) na;
			result = "s0(" + a0.getSubClass() + ", " + a0.getSuperClass() + ") :- o(";
		}else if(na instanceof GCI1Axiom) {
			GCI1Axiom a1 = (GCI1Axiom) na;
			result = "s1(" + a1.getLeftSubClass() + ", " + a1.getRightSubClass() 
				+ ", " + a1.getSuperClass() + ").";
		}else if(na instanceof GCI2Axiom) {
			GCI2Axiom a2 = (GCI2Axiom) na;
			result = "s2(" + a2.getSubClass() + ", " + a2.getPropertyInSuperClass() 
				+ ", " + a2.getClassInSuperClass() + ").";
		}else if(na instanceof GCI2Axiom) {
			GCI3Axiom a3 = (GCI3Axiom) na;
			result = "s3(" + a3.getPropertyInSubClass() + ", " + a3.getClassInSubClass() 
				+ ", " + a3.getSuperClass() + ").";
		}
		
		return result;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
