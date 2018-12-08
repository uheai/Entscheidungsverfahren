package main;

import java.io.File;
import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		
		String path = args[0];
		Parser parser = new Parser();
		try {
			parser.readFile(path);
		} catch (IOException e) {
			System.err.println("Could not read file");
			e.printStackTrace();
			System.exit(-1);
		}
		
		//System.out.println(parser.getEqSystemString());
		
		SMT_LIB_Writer writer = new SMT_LIB_Writer(parser.getEqSystem());
		File file = new File(path);
		String fileName = "smt_lib_" + file.getName();
		try {
			writer.write(fileName);
		} catch (IOException e) {
			System.err.println("Could not write file");
			e.printStackTrace();
		}
		
		System.out.println("created file " + fileName);
		
	}

}
