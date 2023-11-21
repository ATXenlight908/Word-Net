package ngordnet.main;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.TreeMap;

public class WordMap {
    private TreeMap<String, ArrayList<Integer>> appearanceMap;
    private TreeMap<Integer, String> identify;
    private int edges;
    public WordMap(String synsets) {
        this.appearanceMap = new TreeMap<>();
        this.identify = new TreeMap<>();
        In in = new In(synsets);
        while (in.hasNextLine() && !in.isEmpty()) {
            edges += 1;
            String[] current = in.readLine().split(",");
            int id = Integer.parseInt(current[0]);
            String word = current[1];
            identify.put(id, word);
            String[] splitedWords = word.split(" ");
            for (int i = 0; i < splitedWords.length; i += 1) {
                String someWord = splitedWords[i];
                if (appearanceMap.containsKey(someWord)) {
                    appearanceMap.get(someWord).add(id);
                } else {
                    appearanceMap.put(someWord, new ArrayList<Integer>());
                    appearanceMap.get(someWord).add(id);
                }
            }

        }
    }
    public String getWord(int id) {
        return identify.get(id);
    }
    public ArrayList<Integer> getAppearances(String word) {
        return appearanceMap.get(word);
    }
    public int getEdges() {
        return edges;
    }
}