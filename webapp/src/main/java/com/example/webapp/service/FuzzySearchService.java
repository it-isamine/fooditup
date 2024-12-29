package com.example.webapp.service;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Service;

@Service
public class FuzzySearchService {

    private final LevenshteinDistance levenshtein = new LevenshteinDistance();

    public boolean isSimilar(String keyword, String target, int threshold) {
        int distance = levenshtein.apply(keyword.toLowerCase(), target.toLowerCase());
        return distance <= threshold; // Return true if distance is within acceptable threshold
    }
}
