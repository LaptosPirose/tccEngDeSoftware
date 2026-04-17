package com.tcc.analisador_opc;

import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		OpcService service = new OpcService();
		try {
			service.getClient();
			System.out.println("Imprimindo enquando não teclar SAIR__!");
			Scanner scanner = new Scanner(System.in);

			while (!scanner.nextLine().equalsIgnoreCase("sair"))
				;

		} catch (Exception e) {

		} finally {
			System.exit(0);
		}

	}
}
