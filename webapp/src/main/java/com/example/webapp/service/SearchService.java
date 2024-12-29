package com.example.webapp.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.webapp.model.Restaurant;
import com.example.webapp.repo.WebAppService;

@Service
public class SearchService {

	@Autowired
	private FuzzySearchService fuzzySearchService;

	@Autowired
	WebAppService webAppService;

	public Iterable<Restaurant> searchRestaurantsFuzzy(String keyword, int threshold) {
		List<Restaurant> allRestaurants = (List<Restaurant>) webAppService.getRestaurant();
		return allRestaurants.stream()
				.filter(restaurant -> fuzzySearchService.isSimilar(keyword, restaurant.getName(), threshold))
				.collect(Collectors.toList());
	}
}
