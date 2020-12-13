package bgu.spl.mics.application;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;
import bgu.spl.mics.application.passiveObjects.Input;
import bgu.spl.mics.application.services.*;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import jdk.nashorn.internal.parser.JSONParser;

import java.io.*;


/** This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {

	private static void battleOfEndor(Input input) {
		/* Creates the characters */
		LeiaMicroservice Leia = new LeiaMicroservice(input.getAttacks());
		HanSoloMicroservice HanSolo = new HanSoloMicroservice();
		C3POMicroservice C3P0 = new C3POMicroservice();
		R2D2Microservice R2D2 = new R2D2Microservice(input.getR2D2());
		LandoMicroservice Lando = new LandoMicroservice(input.getLando());
		/* Creates the passiveObjects */
		Ewoks ewoks = Ewoks.getInstance(input.getEwoks());
		/* Threads */
		Thread threadLeia = new Thread(Leia);
		Thread threadHanSolo = new Thread(HanSolo);
		Thread threadC3P0 = new Thread(C3P0);
		Thread threadR2D2 = new Thread(R2D2);
		Thread threadLando = new Thread(Lando);

		threadLando.start();
		threadR2D2.start();
		threadHanSolo.start();
		threadC3P0.start();
		threadLeia.start();



;	}

	private static Input getInputFromJson(String filePath) throws IOException {
		Gson gson = new Gson();
		try (Reader reader = new FileReader(filePath)) {
			return gson.fromJson(reader, Input.class);
		}
	}

	private static void diaryToJson(String filePath, Diary recordDiary) throws IOException {
		Gson gson = new Gson();
		try (Writer writer = new FileWriter(filePath)) {
			gson.toJson(recordDiary, writer);
		}
	}

	public static void main(String[] args) throws IOException {


// <----------input ---------->
		if (args.length != 2) {
			System.out.println("No valid input arguments were given");
			System.out.println("Please provide valid input.json path and output.json path");
			return; //exit main
		}
		String inputFilePath = args[0]; //input file path from arguments
		String outputFilePath = args[1];//output file path from arguments
		Input input = null;
		try {
			input = getInputFromJson(inputFilePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// <----------input ---------->

		// <--------main program ---------->
		if (input != null)
			battleOfEndor(input);// - the method that runs all the threads and the program.
		// <--------main program ---------->

		// <----------output ---------->
		Diary recordDiary = Diary.getInstance();
		try {
			diaryToJson(outputFilePath, recordDiary);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// <----------output ---------->

	}


}
