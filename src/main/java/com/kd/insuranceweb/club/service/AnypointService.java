package com.kd.insuranceweb.club.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kd.insuranceweb.club.dto.AnypointTransaction;
import com.kd.insuranceweb.club.mapper.AnypointMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AnypointService {

    private final AnypointMapper anypointMapper;

    public long findBalance(Long customerId) {
        Long v = anypointMapper.selectBalance(customerId);
        return v == null ? 0L : v;   // null 방지
    }

    public List<AnypointTransaction> findTxns(Long customerId, LocalDate start, LocalDate end) {
        return anypointMapper.selectTxns(customerId, start, end);
    }
}
