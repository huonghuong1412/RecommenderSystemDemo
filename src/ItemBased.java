
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ItemBased {

    public static void main(String[] args) {
        
        /*
                        U0  U1  U2  U3 ...
                Item 0
                Item 1
                ...
        */
        
        Double ratings[][] = {
            {5.0, 5.0, 2.0, 0.0, 1.0, null, null},
            {4.0, null, null, 0.0, null, 2.0, null},
            {null, 4.0, 1.0, null, null, 1.0, 1.0},
            {2.0, 2.0, 3.0, 4.0, 4.0, null, 4.0},
            {2.0, 0.0, 4.0, null, null, null, 5.0}
        };
        Double predic_ratings[][] = {
            {5.0, 5.0, 2.0, 0.0, 1.0, null, null},
            {4.0, null, null, 0.0, null, 2.0, null},
            {null, 4.0, 1.0, null, null, 1.0, 1.0},
            {2.0, 2.0, 3.0, 4.0, 4.0, null, 4.0},
            {2.0, 0.0, 4.0, null, null, null, 5.0}
        };
        List<Double> averageUserMatrix = new ArrayList<>();
        for (int i = 0; i < ratings.length; i++) {
            double average = Utils.averageRatingEachUser(ratings[i]);
            averageUserMatrix.add(average);
        }

        // Bước 1: Chuẩn hoá ma trận.
        // Công thức: Với những giá trị null (chưa rating) tính trung bình rating mỗi user
        // Với những giá trị null để giá trị là 0 (sau chuẩn hoá)
        // VD: User 0: R_tb = (5+4+2+2) / 4 = 3.25, User 1: R_tb = (5+4+2+0) /4 = 2.75 ...
        Utils.nomalizeRatingMatrix(ratings);
        System.out.println("Ma trận sau khi chuẩn hoá: ");
        for (int i = 0; i < ratings.length; i++) {
            for (int j = 0; j < ratings[i].length; j++) {
                System.out.print(ratings[i][j] + " ");
            }
            System.out.println("");
        }

        // Bước 2: Tính độ tương tự giữa các user
        List<List<Double>> similarityUserMatrix = new ArrayList<>();
        for (int i = 0; i < ratings.length; i++) {
            List<Double> list = new ArrayList<>();
            for (int j = 0; j < ratings.length; j++) {
                list.add(Utils.cosineSimilarity(ratings[i], ratings[j]));
            }
            for (int k = 0; k < ratings[i].length; k++) {
                similarityUserMatrix.add(list);
                break;
            }
        }
        System.out.println("Ma trận độ tương tự giữa các item:");
        for (int i = 0; i < similarityUserMatrix.size(); i++) {
            System.out.println(similarityUserMatrix.get(i));
        }

        // Chọn k láng giềng = 2
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhập số láng giềng k: ");
        int k = sc.nextInt();
        System.out.print("Nhập item: ");
        int u = sc.nextInt();  // chọn user 1 để tính toán

        // Bước 3: Dự đoán rating với ma trận chuẩn hoá
        List<Double> similarityMatricByUser = new ArrayList<>();
        for (int i = 0; i < similarityUserMatrix.get(u).size(); i++) {
            similarityMatricByUser.add(similarityUserMatrix.get(u).get(i));
        }

        System.out.println("\nVector độ tương tự item " + u + " với các item #: ");
        for (int j = 0; j < similarityMatricByUser.size(); j++) {
            System.out.print(similarityMatricByUser.get(j) + " ");
        }
        System.out.println("");

        // Lấy ra k = 2 giá trị rating lớn nhất trong ds rating với mỗi item
        Map<Integer, Double> vectorRatingAndUser = new HashMap<>();
        for (int i = 0; i < ratings[u].length; i++) {
            if (ratings[u][i] == 0.0) {
                // lấy ds (vị trí) các user đã rating item này và độ tương tự với user u (ratings[u][i])
                for (int j = 0; j < ratings.length; j++) {
                    if (ratings[j][i] != 0.0) {
                        vectorRatingAndUser.put(j, similarityUserMatrix.get(u).get(j));
                    }
                }
                Map<Integer, Double> sortRatingAndUser = Utils.sortMapByValue(vectorRatingAndUser, k);
                Double t = 0.0;     // Tổng của ( độ tương tự giữa (u, ui) * các giá trị chuẩn hoá trong ma trận chuẩn hoá trong trường hợp các user đã rating )
                Double t1 = 0.0;    // Tổng trị tuyệt đối độ tương tự giữa 2 user (u, ui)
                for (Integer key : sortRatingAndUser.keySet()) {
                    t += sortRatingAndUser.get(key) * ratings[key][i];
                    t1 += Math.abs(sortRatingAndUser.get(key));
                }
                // Tính dự đoán rating trên hệ số 5
                // công thức: Trung binh rating của user + kết quả dự đoán trên ma trận chuẩn hoá
                Double predicRatingBy5 = averageUserMatrix.get(u) + t / t1;
                System.out.println("\nAverage Item " + u + " and Predict Rating / 5: ");
                System.out.println(averageUserMatrix.get(u) + " " + predicRatingBy5);

                // thay thế các giá trị chưa rating = các giá trị dự đoán đã tính
                for (int j = 0; j < predic_ratings.length; j++) {
                    if (predic_ratings[j][i] == null) {
                        predic_ratings[j][i] = predicRatingBy5;
                    }
                }
                t = 0.0;
                t1 = 0.0;
            }
        }

        System.out.println("");

        // Bước 4: Dự đoán các rating cho user
        System.out.println("");
        System.out.println("Ma trận dự đoán rating cho item " + u);
        for (int i = 0; i < predic_ratings[u].length; i++) {
            System.out.print(predic_ratings[u][i] + " ");
        }
        System.out.println("");

        System.out.println("");
        System.out.println("Danh sách sản phẩm gợi ý cho user " + u); // các dự đoán rating >= 3
        for (int i = 0; i < predic_ratings[u].length; i++) {
            if (predic_ratings[u][i] >= 3.0 && ratings[u][i] == 0.0) {
                System.out.println("Sản phẩm " + i);
            }
        }
        System.out.println("");
    }
}
