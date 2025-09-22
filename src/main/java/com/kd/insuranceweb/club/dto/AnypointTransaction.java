// com.kd.insuranceweb.club.dto.AnypointTransaction
package com.kd.insuranceweb.club.dto;

import java.time.LocalDate;

// 롬복 쓰면:
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AnypointTransaction {
    private Long txnId;        // ← setTxnId 필요
    private Long customerId;   // ← setCustomerId 필요
    private String txnType;
    private Long amount;
    private String description;
    private LocalDate txnDate;
}
