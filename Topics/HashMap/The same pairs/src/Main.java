import java.util.*;


class MapFunctions {

    public static void calcTheSamePairs(Map<String, String> map1, Map<String, String> map2) {
        Map<String, String> mapToGoOver = map1.size() > map2.size() ? map2 : map1;
        Map<String, String> mapToCompare = map1.size() > map2.size() ? map1 : map2;
        int matchesCount = 0;
        for (Map.Entry entry : mapToGoOver.entrySet()) {
            if (mapToCompare.containsKey(entry.getKey()) &&
                    mapToCompare.get(entry.getKey()).equals(entry.getValue())) {
                matchesCount++;
            }
        }
        System.out.println(matchesCount);
    }
}