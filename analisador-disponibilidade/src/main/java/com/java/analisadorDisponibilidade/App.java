package com.java.analisadorDisponibilidade;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;

public class App {
	public static void main(String[] args) {
		OpcService service = new OpcService();

		try {
			service.connectWithFailover();

			if (service.getClient() != null) {
				// Carrega JSON do resources/config/tags.json
				ObjectMapper mapper = new ObjectMapper();
				InputStream is = App.class.getClassLoader().getResourceAsStream("config/tags.json");

				List<TagConfig> tags = Arrays.asList(mapper.readValue(is, TagConfig[].class));

				for (TagConfig tag : tags) {
					service.subscribe(tag);
				}

				System.out.println("Monitoramento ativo. Digite 'SAIR' para encerrar.");
				Scanner scanner = new Scanner(System.in);

				/*
				 * Aguarda usuário digitar sair para terminar a aplicação. Não "case sensitive".
				 */
				while (!scanner.nextLine().equalsIgnoreCase("SAIR"))
					;
				scanner.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
		
	}
}
