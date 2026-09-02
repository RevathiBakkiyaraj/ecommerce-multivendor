package com.rev.service;

import com.rev.modal.Home;
import com.rev.modal.HomeCategory;

import java.util.List;

public interface HomeService {
    public Home createHomePageData(List<HomeCategory> allCategories);
}
