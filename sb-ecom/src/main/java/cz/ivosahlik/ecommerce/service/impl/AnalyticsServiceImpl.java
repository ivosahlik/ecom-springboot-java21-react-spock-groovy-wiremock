package cz.ivosahlik.ecommerce.service.impl;

import cz.ivosahlik.ecommerce.payload.AnalyticsResponse;
import cz.ivosahlik.ecommerce.repositories.OrderRepository;
import cz.ivosahlik.ecommerce.repositories.ProductRepository;
import cz.ivosahlik.ecommerce.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Override
    public AnalyticsResponse getAnalyticsData() {
        AnalyticsResponse response = new AnalyticsResponse();

        long productCount = productRepository.count();
        long totalOrders = orderRepository.count();;
        Double totalRevenue = orderRepository.getTotalRevenue();

        response.setProductCount(String.valueOf(productCount));
        response.setTotalOrders(String.valueOf(totalOrders));
        response.setTotalRevenue(String.valueOf(totalRevenue != null ? totalRevenue : 0));
        return response;
    }
}
