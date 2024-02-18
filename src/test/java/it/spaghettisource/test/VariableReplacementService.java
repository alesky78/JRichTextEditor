package it.spaghettisource.test;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VariableReplacementService {

	private Map<String, String> variableMap;

	public VariableReplacementService() {
		this.variableMap = new HashMap<>();
		setVariable("fornitore", "NomeFornitore");
		setVariable("codiceCon", "ABC123");
	}

	public void setVariable(String name, String value) {
		variableMap.put(name, value);
	}

	public String replaceVariables(String txt) {
		Pattern pattern = Pattern.compile("#\\{([^}]+)\\}");
		Matcher matcher = pattern.matcher(txt);

		StringBuffer result = new StringBuffer();

		while (matcher.find()) {
			String variableName = matcher.group(1);
			String variableValue = variableMap.getOrDefault(variableName, ""); // Default a stringa vuota se la variabile non è definita
			matcher.appendReplacement(result, variableValue);
		}

		matcher.appendTail(result);

		return result.toString();
	}

	public static void main(String[] args) {
		VariableReplacementService service = new VariableReplacementService();
		String input = "Il fornitore #{fornitore} si impegna a rispettare il contratto #{codiceCon}.";
		String output = service.replaceVariables(input);
		System.out.println(output);
	}
}
