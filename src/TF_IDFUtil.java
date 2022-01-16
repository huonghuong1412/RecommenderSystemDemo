
import java.util.List;


public class TF_IDFUtil {

    public static double TF(List<String> doc, String term) {
        int result = 0;
        for (String word : doc) {
            if (term.equalsIgnoreCase(word)) {
                result++;
            }
        }
        return (double) result / doc.size();
    }

    public static double IDF(List<List<String>> docs, String term) {
        int n = 0;
        for (List<String> doc : docs) {
            for (String word : doc) {
                if (term.equalsIgnoreCase(word)) {
                    n++;
                    break;
                }
            }
        }
        if (docs.size() == n || n == 0) {
            return 1.0;
        } else {
            return (double) (1 + Math.log(docs.size() / n));
        }
    }
    
     public static double TF_IDF(List<String> doc, List<List<String>> docs, String term) {
        return TF(doc, term) * IDF(docs, term);
    }
}
