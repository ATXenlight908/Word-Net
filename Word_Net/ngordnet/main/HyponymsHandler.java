package ngordnet.main;

import ngordnet.browser.NgordnetQuery;
import ngordnet.browser.NgordnetQueryHandler;
import ngordnet.ngrams.NGramMap;
import ngordnet.ngrams.TimeSeries;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class HyponymsHandler extends NgordnetQueryHandler {
    private WordMap wm;
    private Graph graph;
    private NGramMap ngm;
    public HyponymsHandler(String hyponymFile, String synsetFile, NGramMap ngm) {
        super();
        this.graph = new Graph(hyponymFile, synsetFile);
        this.ngm = ngm;
    }

    @Override
    public String handle(NgordnetQuery q) {

        String word1 = q.words().get(0);
        TreeSet<String> currList = graph.getHyponyms(word1);
        for (int i = 1; i < q.words().size(); i++) {
            TreeSet<String> otherList = graph.getHyponyms(q.words().get(i));
            TreeSet<String> newList = new TreeSet<>();
            for (String s : otherList) {
                if (currList.contains(s)) {
                    newList.add(s);
                }
            }
            currList = newList;
        }
        if (q.k() == 0) {
            return currList.toString();
        }
        TreeMap<Double, ArrayList<String>> sortedList = new TreeMap<>();
        for (String s : currList) {
            TimeSeries popularity = ngm.countHistory(s, q.startYear(), q.endYear());
            double totalCount = 0;
            for (double pop : popularity.data()) {
                totalCount += pop;
            }
            if (sortedList.containsKey(totalCount)) {
                sortedList.get(totalCount).add(s);
            } else {
                sortedList.put(totalCount, new ArrayList<>());
                sortedList.get(totalCount).add(s);
            }
        }
        Set<Double> keyList = sortedList.keySet();
        Double[] keyArray = keyList.toArray(new Double[keyList.size()]);
        currList.clear();
        int index = sortedList.size() - 1;
        int k = q.k();
        while (k != currList.size() && index >= 0) {
            if (keyArray[index] == 0) {
                return currList.toString();
            }
            for (int i = 0; i < sortedList.get(keyArray[index]).size(); i++) {
                currList.add(sortedList.get(keyArray[index]).get(i));
                if (currList.size() == k) {
                    return currList.toString();
                }
            }
            index -= 1;
        }
        return currList.toString();
    }
}

