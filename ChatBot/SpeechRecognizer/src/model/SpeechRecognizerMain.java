package model;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Port;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.result.WordResult;

public class SpeechRecognizerMain {
	
	private LiveSpeechRecognizer recognizer;
	private Logger logger = Logger.getLogger(getClass().getName());
	private String speechRecognitionResult;
	private boolean ignoreSpeechRecognitionResults = false;
	private boolean speechRecognizerThreadRunning = false;
	private boolean resourcesThreadRunning;
	private ExecutorService eventsExecutorService = Executors.newFixedThreadPool(2);
	

	public SpeechRecognizerMain() {
		
		System.out.println("Loading Speech Recognizer...\n");
		Configuration configuration = new Configuration();
		
		//for English
//		configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
//		configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
//		configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");
		//for Indian-English
//		configuration.setAcousticModelPath("resource:/indianEng_acoustic/");
//		configuration.setDictionaryPath("resource:/indianEng_lm/en_in.dic");
//		configuration.setLanguageModelPath("resource:/indianEng_lm/en-us.lm.bin");
//		//for Hindi
//		configuration.setAcousticModelPath("resource:/hindi_acoustic/");
//		configuration.setDictionaryPath("resource:/hindi_lm/hindi.dic");
//		 configuration.setLanguageModelPath("resource:/hindi_lm/hindi.lm");
//		//Itelic
//		configuration.setAcousticModelPath("resource:/italic_acoustic/");
//		configuration.setDictionaryPath("resource:/italic_lm/it.dic");
//		configuration.setLanguageModelPath("resource:/italic_lm/voxforge_it_sphinx.lm");
//		//German
//		configuration.setAcousticModelPath("resource:/german_acoustic/");
//		configuration.setDictionaryPath("resource:/german_lm/german.dic");
//		configuration.setLanguageModelPath("resource:/german_lm/cmusphinx-voxforge-de.lm.bin");
		try {
			recognizer = new LiveSpeechRecognizer(configuration);
		} catch (IOException ex) {
			logger.log(Level.SEVERE, null, ex);
		}
		
		// Start recognition process pruning previously cached data.
		// recognizer.startRecognition(true);
		
		//Check if needed resources are available
		startResourcesThread();
		//Start speech recognition thread
		startSpeechRecognition();
	}
	

	public synchronized void startSpeechRecognition() {
		
		if (speechRecognizerThreadRunning) {
			//logger.log(Level.INFO, "Speech Recognition Thread already running...\n");
			System.out.println("Speech Recognition Thread is already running\n");
		}
		else
			eventsExecutorService.submit(() -> {
				
				speechRecognizerThreadRunning = true;
				ignoreSpeechRecognitionResults = false;
				recognizer.startRecognition(true);			
				logger.log(Level.INFO, "You can start to speak...\n");
				
				try {
					while (speechRecognizerThreadRunning) {
						SpeechResult speechResult = recognizer.getResult();
						if (!ignoreSpeechRecognitionResults) {
							if (speechResult == null)
								System.out.println("I can't understand what you said.\n");
							else {
								speechRecognitionResult = speechResult.getHypothesis();
								
								//You said?
								System.out.println("You said****: [" + speechRecognitionResult + "]\n");
								makeDecision(speechRecognitionResult, speechResult.getWords());
								
							}
						} else
							logger.log(Level.INFO, "Ingoring Speech Recognition Results...");
						
					}
				} catch (Exception ex) {
					logger.log(Level.WARNING, null, ex);
					speechRecognizerThreadRunning = false;
				}
				
				logger.log(Level.INFO, "SpeechThread has exited...");
				
			});
	}
	
	/**
	 * Stops ignoring the results of SpeechRecognition
	 */
	public synchronized void stopIgnoreSpeechRecognitionResults() {
		
		//Stop ignoring speech recognition results
		ignoreSpeechRecognitionResults = false;
	}
	
	public synchronized void ignoreSpeechRecognitionResults() {
		ignoreSpeechRecognitionResults = true;
		
	}
	
	public void startResourcesThread() {
		
		if (resourcesThreadRunning) {
			//logger.log(Level.INFO, "Resources Thread already running...\n");
		}
		else
			eventsExecutorService.submit(() -> {
				try {
					
					//Lock
					resourcesThreadRunning = true;
					
					while (true) {
						if (!AudioSystem.isLineSupported(Port.Info.MICROPHONE))
							//logger.log(Level.INFO, "Microphone is not available.\n");
							System.out.println("Listening...");
						
						Thread.sleep(350);
					}
					
				} catch (InterruptedException ex) {
					logger.log(Level.WARNING, null, ex);
					resourcesThreadRunning = false;
				}
			});
	}
	
	
	public void makeDecision(String speech , List<WordResult> speechWords) {
		
		System.out.println(speech);
		
	}
	
	public boolean getIgnoreSpeechRecognitionResults() {
		return ignoreSpeechRecognitionResults;
	}
	
	public boolean getSpeechRecognizerThreadRunning() {
		return speechRecognizerThreadRunning;
	}
	
	public static void main(String[] args) {
		new SpeechRecognizerMain();
	}
}
