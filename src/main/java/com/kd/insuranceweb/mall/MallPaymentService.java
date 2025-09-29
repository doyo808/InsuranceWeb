package com.kd.insuranceweb.mall;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

import jakarta.annotation.PostConstruct;

@Service
public class MallPaymentService {

    private IamportClient iamportClient;

    @Value("${portone.api-key}")
    private String apiKey;

    @Value("${portone.api-secret}")
    private String apiSecret;

    // 생성자 실행 후 API 키와 시크릿으로 IamportClient 초기화
    @PostConstruct
    public void init() {
        this.iamportClient = new IamportClient(apiKey, apiSecret);
    }

    // 결제 검증 로직
    public boolean verifyPayment(MallControllerTemp.PaymentCallbackRequest request) throws Exception {
        // 1. 포트원 API를 통해 결제 정보(Payment)를 가져옴
        // imp_uid를 사용해야 가장 정확한 정보를 얻을 수 있음
        IamportResponse<Payment> paymentResponse = iamportClient.paymentByImpUid(request.getImp_uid());
        Payment paymentInfo = paymentResponse.getResponse();

        // 2. DB에서 실제 결제되어야 할 금액을 조회 (예제에서는 하드코딩)
        // 실제로는 request.getMerchant_uid()를 사용해 DB의 주문 정보를 가져와야 함
        int expectedAmount = getAmountFromDatabase(request.getMerchant_uid());

        // 3. 포트원에서 가져온 실제 결제 금액과 우리 DB의 금액을 비교
        if (paymentInfo.getAmount().intValue() == expectedAmount) {
            System.out.println("결제 검증 성공! 빌링키 저장: " + paymentInfo.getCustomerUid());
            // TODO: 여기서 고객의 빌링키(customer_uid)를 DB에 저장하는 로직을 추가해야 함
            // 예: memberRepository.updateBillingKey(memberId, paymentInfo.getCustomerUid());
            return true; // 금액이 일치하면 true 반환
        } else {
            System.out.println("결제 금액 불일치. 위변조 의심.");
            // TODO: 금액 불일치 시 결제 자동 취소 로직 등을 추가할 수 있음
            return false; // 금액이 다르면 false 반환
        }
    }

    // DB에서 주문 정보를 가져오는 가상의 메소드
    private int getAmountFromDatabase(String merchantUid) {
        // 실제 구현에서는 DB와 연동하여 merchantUid에 해당하는 주문의 금액을 반환해야 함
        // 여기서는 예제를 위해 고정된 금액 100원을 반환
        System.out.println("DB 조회: " + merchantUid + "의 예정 금액은 100원");
        return 100;
    }
}
