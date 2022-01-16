
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Utils {

    public static Double cosineSimilarity(Double[] vectorA, Double[] vectorB) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }
        double result = dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
//        if (result < 0 || Double.isNaN(result)) {
//            return 0.0;
//        } else {
        return result;
//        }
    }

    // tính độ tương đồng bằng giá trị trung bình các rating, chính xác hơn cosine similarity thông thường
    public static double correlationSimilarity(Double[] vectorA, Double[] vectorB) {
        double dotProduct = 0.0;
        double avgItem1 = averageRatingEachUser(vectorA);
        double avgItem2 = averageRatingEachUser(vectorB);

        double normA = 0.0;
        double normB = 0.0;

        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += ((vectorA[i] - avgItem1) * (vectorB[i] - avgItem2));
            normA += Math.pow(vectorA[i] - avgItem1, 2);
            normB += Math.pow(vectorB[i] - avgItem2, 2);
        }
        double result = dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
        if (result < 0 || Double.isNaN(result)) {
            return 0;
        } else {
            return result;
        }
    }

    public static double averageRatingEachUser(Double ratings[]) {
        double sum = 0;
        int size = 0;
        for (int i = 0; i < ratings.length; i++) {
            if (ratings[i] != null) {
                sum += ratings[i];
                size++;
            }
        }
        return sum / size;
    }

    public static void nomalizeRatingMatrix(Double ratings[][]) {
        for (int i = 0; i < ratings.length; i++) {
            double average = averageRatingEachUser(ratings[i]);
            for (int j = 0; j < ratings[i].length; j++) {
                if (ratings[i][j] == null) {
                    ratings[i][j] = 0.0;
                } else {
                    ratings[i][j] = ratings[i][j] - average;
                }
            }
            average = 0.0;
        }
    }

    public static Map<Integer, Double> sortMapByValue(Map<Integer, Double> map, int limit) {
        LinkedHashMap<Integer, Double> sortedMap = new LinkedHashMap<>();
        map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(limit)
                .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));

        return sortedMap;
    }
}
