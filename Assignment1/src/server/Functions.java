package server;

// A basic function interface
public interface Functions {
	public String query(String word);
	public String add(String word, String meaning);
	public String remove(String word);
	public String update(String word, String meaning);

}
