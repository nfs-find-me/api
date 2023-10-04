package com.findme.api.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


public class AIService {
	
	public ResponseEntity<String> uploadImageToAI(@RequestParam("file") MultipartFile file) {
		if (file.isEmpty()) {
			return new ResponseEntity<>("Fichier vide.", HttpStatus.BAD_REQUEST);
		}
		
		return null;
	}
}
