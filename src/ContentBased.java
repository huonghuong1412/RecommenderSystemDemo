
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ContentBased {

    // lấy danh sách item có các đặc tính giống nhau
    // tags: ram 8gb, mỏng, nhẹ, ssd, bluetooth, dell, asus, thời trang, cao cấp, ....
    // tương tự với các đặc tính khác tuỳ vào nghiệp vụ
    public static List<RecommendItem> similarByTags(List<String> tags, List<List<String>> documents) {
        List<Double> result = new ArrayList<>();
        double queryDistance = 0;
        for (int i = 0; i < tags.size(); i++) {
            double bp = Math.pow(TF_IDFUtil.TF_IDF(tags, documents, tags.get(i)), 2);  // binh phuong moi diem
            queryDistance += bp;
        }
        for (int i = 0; i < documents.size(); i++) {
            double dotProduct = 0, queryByDocument = 0, cosine_similarity = 0;
            for (int j = 0; j < tags.size(); j++) {
                dotProduct += TF_IDFUtil.TF_IDF(tags, documents, tags.get(j)) * TF_IDFUtil.TF_IDF(documents.get(i), documents, tags.get(j));
                queryByDocument += Math.pow(TF_IDFUtil.TF_IDF(documents.get(i), documents, tags.get(j)), 2);
            }
            cosine_similarity = dotProduct / (Math.sqrt(queryDistance) * Math.sqrt(queryByDocument));
            result.add(cosine_similarity);
        }
        List<RecommendItem> items = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            if (!Double.isNaN(result.get(i))) {
                items.add(new RecommendItem(result.get(i), i));
            }
        }
        // Sắp xếp lại theo độ tương tự lớn nhất -> nhỏ nhất
        items.sort((RecommendItem o1, RecommendItem o2) -> o2.getValue() - o1.getValue() > 0 ? 1 : -1);
        List<RecommendItem> listResult = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            // thêm những item có độ tương tự với item cần tìm
            // để tối ưu có thể chọn độ tương tự cao, khoảng >= 0.5
            if (items.get(i).getValue() >= 0.5) {
                listResult.add(new RecommendItem(items.get(i).getValue(), items.get(i).getIndex()));
            }
        }
        return listResult;
    }

    public static void main(String[] args) {
        // samsung,dienthoai,dienthoaismartphone,superamoledplus,67inch,android,fullhd,ram8gb,exynos9825,7000mah,320mp,640mp120mp50mp50mp,256gb,nanonsim
        List<String> doc0 = Arrays.asList("xiaomi", "dienthoai", "dienthoaismartphone", "android", "hd", "ram4gb", "128gb", "5mp", "13mp");
        List<String> doc1 = Arrays.asList("xiaomi", "dienthoai", "dienthoaismartphone", "android", "fullhd", "ram8gb", "128gb");
        List<String> doc2 = Arrays.asList("vivo", "dienthoai", "dienthoaismartphone", "android", "hd", "ram4gb", "64gb", "5mp", "13mp", "nanosim");
        List<String> doc3 = Arrays.asList("oppo", "dienthoai", "dienthoaismartphone", "android", "fullhd", "ram8gb", "32mp", "128gb", "nanosim");
        List<String> doc4 = Arrays.asList("asus", "laptop", "laptopgaming", "windows10home", "fullhd", "ram8gb", "ssd512gb", "vonhua");
        List<String> doc5 = Arrays.asList("acer", "laptop", "laptopgaming", "windows10home", "fullhd", "ram8gb", "ssd512gb", "3200mhz", "nguyenkhoi", "720phdcamera", "vonhom");
        List<String> doc6 = Arrays.asList("asus", "laptop", "laptoptruyenthong", "windows10pro", "fullhd", "ram16gb", "ssd512gb", "14inch", "nguyenkhoi", "3200mhz", "vonhua");
        List<String> doc7 = Arrays.asList("acer", "laptop", "laptoptruyenthong", "windows10home", "fullhd", "ram8gb", "ssd256gb", "156inch", "vonhom");
        List<String> doc8 = Arrays.asList("dell", "laptop", "laptoptruyenthong", "windows10pro", "fullhd", "ram4gb", "ssd512gb", "720phd", "vonhua");
        List<String> doc9 = Arrays.asList("samsung", "dienthoai", "dienthoaismartphone", "67inch", "fullhd", "android", "ram8gb", "320mp", "256gb", "nanonsim");
        List<List<String>> documents = Arrays.asList(doc0, doc1, doc2, doc3, doc4, doc5, doc6, doc7, doc8, doc9);

        // Tìm các sản phẩm tương tự với sản phẩm có tag: 
        // samsung,dienthoai,dienthoaismartphone,65inch,android,hd,ram4gb,5000mah,5mp,64gb,nanosim
        // dell,laptop,laptoptruyenthong,14inch,windows10home,fullhd,ram4gb,vonhua,ssd256gb
        List<RecommendItem> list = similarByTags(Arrays.asList("samsung", "dienthoai", "dienthoaismartphone", "65inch", "android", "hd", "ram4gb", "5000mah", "5mp", "64gb", "nanosim"), documents);
        List<RecommendItem> list2 = similarByTags(Arrays.asList("dell", "laptop", "laptoptruyenthong", "14inch", "windows10home", "fullhd", "ram4gb", "vonhua", "ssd256gb"), documents);
        System.out.println("List Item similariy");
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i).toString());
        }
        
        System.out.println("\nList Item similariy");
        for (int i = 0; i < list2.size(); i++) {
            System.out.println(list2.get(i).toString());
        }
    }
}
