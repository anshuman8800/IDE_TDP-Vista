package model;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Port;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;

public class Main {

	// EnglishNumberToWords
//	EnglishNumberToString	numberToString	= new EnglishNumberToString();
//	EnglishStringToNumber	stringToNumber	= new EnglishStringToNumber();

	// Logger
	private Logger logger = Logger.getLogger(getClass().getName());

	// Variables
	private String result;

	// Threads
	Thread	speechThread;
	Thread	resourcesThread;

	// LiveRecognizer
	private LiveSpeechRecognizer recognizer;
	
	public Main() {

		logger.log(Level.INFO, "Loading...\n");

		Configuration configuration = new Configuration();

		configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
		configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
		// configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin")

		// Grammar
		configuration.setGrammarPath("resource:/grammars");
		configuration.setGrammarName("grammar");
		configuration.setUseGrammar(true);

		try {
			recognizer = new LiveSpeechRecognizer(configuration);
		} catch (IOException ex) {
			//logger.log(Level.SEVERE, null, ex);
		}

		recognizer.startRecognition(true);

		startSpeechThread();
		startResourcesThread();
	}

	
	protected void startSpeechThread() {

			if (speechThread != null && speechThread.isAlive())
			return;

		// initialise
		speechThread = new Thread(() -> {
			logger.log(Level.INFO, "You can start to speak...\n");
			try {
				while (true) {
					SpeechResult speechResult = recognizer.getResult();
					if (speechResult != null) {

						result = speechResult.getHypothesis();
						System.out.println("You said:) [" + result + "]\n");
						//makeDecision(result);
						String replyResult = "";
						reply r = new reply();
						if(result.equals("hi")|| result.equals("hello") || result.equals("hey") || result.equals("what's up") || result.equals("how are you"))
							replyResult=r.commonReply(0);
						else if(result.equals("show system information"))
							replyResult = r.cmdResult("lscpu");
						else if(result.equals("show my current directory"))
							replyResult = r.cmdResult("pwd");
						else if(result.equals("show battery information"))
							replyResult = r.cmdResult("upower -i /org/freedesktop/UPower/devices/battery_BAT0");
						else if(result.equals("show hardware information"))
							replyResult = r.cmdResult("lshw");
						else if(result.equals("tell me today's date"))
							replyResult = r.commonReply(1);
						else if(result.equals("tell me a joke"))
							replyResult = r.commonReply(2);
						else if(result.equals("tell me a another joke"))
							replyResult = r.commonReply(3);
						else if(result.equals("what's your name"))
							replyResult = r.commonReply(4);
						else if(result.equals("what is your name"))
							replyResult = r.commonReply(4);
						else if(result.equals("how's the traffic"))
							replyResult = r.commonReply(5);
						else if(result.equals("tell me about my day"))
							replyResult = r.commonReply(6);
						else if(result.equals("are you happy"))
							replyResult = r.commonReply(7);
						else if(result.equals("exit the program"))
							replyResult = r.commonReply(8);
						else if(result.equals("open cmd"))
							replyResult =r.commonReply(9);
						else if(result.equals("open browser"))
							replyResult = r.commonReply(10);
						else if(result.equals("aatma"))
							replyResult="aatma";
							
							

						System.out.println(replyResult);

					} else
					System.out.println("I can't understand what you said.\n");

				}
			} catch (Exception ex) {
				logger.log(Level.WARNING, null, ex);
			}

			logger.log(Level.INFO, "SpeechThread has exited...");
		});

		// Start
		speechThread.start();

	}
	protected void startResourcesThread() {

		// alive?
		if (resourcesThread != null && resourcesThread.isAlive())
			return;

		resourcesThread = new Thread(() -> {
			try {

				while (true) {
					if (AudioSystem.isLineSupported(Port.Info.MICROPHONE)) {
					} else {

					}

					Thread.sleep(350);
				}

			} catch (InterruptedException ex) {
				logger.log(Level.WARNING, null, ex);
				resourcesThread.interrupt();
			}
		});

		// Start
		resourcesThread.start();
	}

	
//	public void makeDecision(String speech) {
//
//		String[] array = speech.split(" ");
//
//		if (array.length != 3)
//			return;
//
//		int number1 = stringToNumber.convert(array[0]);
//		int number2 = stringToNumber.convert(array[2]);
//		int calculationResult = 0;
//		String symbol = "?";
//		if ("plus".equals(array[1])) {
//			calculationResult = number1 + number2;
//			symbol = "+";
//		} else if ("minus".equals(array[1])) {
//			calculationResult = number1 - number2;
//			symbol = "-";
//		} else if ("multiply".equals(array[1])) {
//			calculationResult = number1 * number2;
//			symbol = "*";
//		} else if ("division".equals(array[1])) {
//			calculationResult = number1 / number2;
//			symbol = "/";
//		}
//
//		String res = numberToString.convert(Math.abs(calculationResult));
//
//		System.out.println("Said:[ " + speech + " ]\n\t\t which after calculation is:[ "
//				+ (calculationResult >= 0 ? "" : "minus ") + res + " ] \n");
//
//		System.out.println("Said:[ " + number1 + " " + symbol + " " + number2 + "]\n\t\t which after calculation is:[ "
//				+ calculationResult + " ]");
//
//	}
	public void endSpeech() {
		logger.log(Level.INFO, "Stop Successfully\n");
		recognizer.stopRecognition();
	}

	
	
	public static void main(String[] args) {
		new Main();

	}

}