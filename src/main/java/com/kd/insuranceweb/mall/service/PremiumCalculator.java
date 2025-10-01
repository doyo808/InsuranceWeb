package com.kd.insuranceweb.mall.service;

import org.springframework.stereotype.Component;

@Deprecated
@Component
public class PremiumCalculator {

	//TODO 이 기능은 클래스를 따로 두지않고 서비스에서 사용할 것
	
    public double calculate(String gender, int age) {
        double genderWeight = switch (gender) {
            case "남성" -> 0.10;
            case "여성" -> 0.05;
            default -> 0.0;
        };

        double ageWeight = getAgeWeight(age);

        double totalWeight = 1 + genderWeight + ageWeight;

        return totalWeight;
    }

    private double getAgeWeight(int age) {
        if (age < 18) return 0.0;
        else if (age <= 25) return 0.15;
        else if (age <= 35) return 0.10;
        else if (age <= 45) return 0.20;
        else if (age <= 55) return 0.30;
        else if (age <= 65) return 0.40;
        else return 0.50;
    }
}

