package bgu.spl.mics.application;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.passiveObjects.Input;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import jdk.nashorn.internal.parser.JSONParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;


/** This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
	public static void main(String[] args) throws IOException {



		Gson gson = new Gson();
		try (JsonReader reader = new JsonReader(new FileReader("C:\\Users\\קורן\\Desktop\\SPL_Assignment2\\Assignment\\src\\main\\java\\bgu\\spl\\mics\\application\\input.json")))
		{
			{
				gson.fromJson(reader, Input.class);
			}
		}



	}
}
