package com.design.communicate;

import java.io.File;

import com.ibm.watson.developer_cloud.http.HttpMediaType;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;

public class VoiceProcessing {

	SpeechToText service = new SpeechToText();
	service.setUsernameAndPassword("6ce30912-e2be-4a99-9e7a-4e712c5d0cd5", "6XZo4JOLKy6W");

	File audio = new File("src/test/resources/sample1.wav");

	SpeechResults transcript = service.recognize(audio, HttpMediaType.AUDIO_WAV);
	System.out.println(transcript);
	
	
}
