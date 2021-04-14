package it.academy.app.services.scraping;

import it.academy.app.models.scraping.*;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SimilarityService {

    final static LevenshteinDistance levenshteinDistance = new LevenshteinDistance();

    public ProductScratch getMaxSimilarityScratch(Object object, long shopId, ArrayList<String> receivedProductNames) {
        String productName = "";
        long productId = 0;
        switch ((int) shopId) {
            case 1:
                ProductBarbora pb = (ProductBarbora) object;
                productName = pb.getName();
                productId = pb.getProductId();
                break;
            case 2:
                ProductRimi pr = (ProductRimi) object;
                productName = pr.getName();
                productId = pr.getProductId();
                break;
            case 3:
                ProductCiaMarket p = (ProductCiaMarket) object;
                productName = p.getName();
                productId = p.getProductId();
                break;
            case 4:
                ProductGruste pg = (ProductGruste) object;
                productName = pg.getName();
                productId = pg.getProductId();
                break;
            case 5:
                ProductAibe pa = (ProductAibe) object;
                productName = pa.getName();
                productId = pa.getProductId();
                break;
            case 6:
                ProductUtenosPrekyba pu = (ProductUtenosPrekyba) object;
                productName = pu.getName();
                productId = pu.getProductId();
                break;
        }
        HashMap<Integer, Double> similarities = new HashMap<>();

        for (int i = 0; i < receivedProductNames.size(); i++) {
            similarities.put(i, similarity(productName,
                    receivedProductNames.get(i)));
        }
        Map.Entry<Integer, Double> maxEntry = Collections.max(similarities.entrySet(),
                Map.Entry.comparingByValue());
        if (maxEntry.getValue() < 0.9) {
            return new ProductScratch();
        }
        return new ProductScratch(productId, maxEntry.getKey());
    }

    public static double similarity(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) {
            longer = s2;
            shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) {
            return 1.0;
        }
        return (longerLength - levenshteinDistance.apply(longer, shorter)) / (double) longerLength;
    }
}
