package ngordnet.main;
import java.util.ArrayList;
import java.util.TreeSet;

import edu.princeton.cs.algs4.In;

public class Graph {
    private class Node {
        private ArrayList<Integer> childrenIds;
        String word;
        private Node(String word, ArrayList<Integer> childrenIds) {
            this.childrenIds = childrenIds;
            this.word = word;
        }
    }

    private WordMap wMap;
    private int V;
    private int E;
    private Node[] list;
    public Graph(String hyponyms, String synsets) {
        wMap = new WordMap(synsets);
        this.V = wMap.getEdges();
        list = new Node[V];
        this.E = 0;
        In in = new In(hyponyms);
        for (int i = 0; i < V; i++) {
            list[i] = new Node(wMap.getWord(i), new ArrayList<>());
        }
        while (in.hasNextLine() && !in.isEmpty()) {
            String[] current = in.readLine().split(",");
            int parent = Integer.parseInt(current[0]);
            for (int i = 1; i < current.length; i++) {
                list[parent].childrenIds.add(Integer.parseInt(current[i]));
            }
        }
    }
    public ArrayList<Integer> adj(int v) {
        return list[v].childrenIds;
    }
    public TreeSet<String> getChildrens(int id) {
        GetChild children = new GetChild(id);
        return children.children;
    }
    private class GetChild {
        private TreeSet<String> children;
        private GetChild(int id) {
            children = new TreeSet<>();
            helper(id);
        }
        public void helper(int id) {
            String[] nodeWord = list[id].word.split(" ");
            for (int i = 0; i < nodeWord.length; i += 1) {
                children.add(nodeWord[i]);
            }
            for (int x : adj(id)) {
                helper(x);
            }
        }
    }

    public TreeSet<String> getHyponyms(String word) {
        TreeSet<String> hyponyms = new TreeSet<>();
        if (wMap.getAppearances(word) == null) {
            return hyponyms;
        }
        for (int x : wMap.getAppearances(word)) {
            hyponyms.addAll(getChildrens(x));
        }

        return hyponyms;
    }

}
