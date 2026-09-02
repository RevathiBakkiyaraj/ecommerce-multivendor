package com.rev.service.impl;

import com.rev.modal.Seller;
import com.rev.modal.SellerReport;
import com.rev.repository.SellerReportRepository;
import com.rev.service.SellerReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerReportServiceImpl implements SellerReportService {
    private final SellerReportRepository sellerReportRepository;


    @Override
    public SellerReport getSellerReport(Seller seller) {
        SellerReport sr=sellerReportRepository.findBySellerId(seller.getId());

        if(sr==null){
            SellerReport newReport=new SellerReport();
            newReport.setSeller(seller);
            return sellerReportRepository.save(newReport);
        }
        return sr;
    }

    @Override
    public SellerReport updateSellerReport(SellerReport sellerReport) {
        return sellerReportRepository.save(sellerReport);
    }
}
