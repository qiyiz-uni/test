package server;

import java.util.HashMap;

public class PlainFunctions implements Functions {
	
	protected HashMap<String, String> dictionary = new HashMap<String, String>();
	
	public PlainFunctions(HashMap<String, String> dictionary) {
		this.dictionary = dictionary;
	}

	@Override
	public String query(String word) {
		if (dictionary.containsKey(word) && dictionary.get(word) != null) {
			return dictionary.get(word);
		} else {
			String str = word + " is not found in the dictionary.";
			return str;
		}
	}

	@Override
	public String add(String word, String meaning) {
		if (dictionary.containsKey(word) && dictionary.get(word) != null) {
			String str = word + " has already been added in the dictionary.";
			return str;
		} else {
			dictionary.put(word, meaning);
			String str = word + " has been added to the dictionary successfully.";
			return str;
		}
	}

	@Override
	public String remove(String word) {
		if (dictionary.containsKey(word) && dictionary.get(word) != null) {
			dictionary.remove(word);
			String str = word + " has been removed from the dictionary successfully.";
			return str;
		} else {
			String str = word + " is not found in the dictionary";
			return str;
		}
	}

	@Override
	public String update(String word, String meaning) {
		if (dictionary.containsKey(word) && dictionary.get(word) != null) {
			dictionary.replace(word, dictionary.get(word), meaning);
			String str = word + " has been updated in the dictionary successfully.";
			return str;
		} else {
			String str = word + " is not found in the dictionary";
			return str;
		}
	}

}
