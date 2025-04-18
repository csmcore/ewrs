package app.ewarehouse.service;

import app.ewarehouse.dto.DisputeCategoryResponse;

import java.util.List;

public interface DisputeCategoryService {
    List<DisputeCategoryResponse> getAllDisputeCategories();
}
