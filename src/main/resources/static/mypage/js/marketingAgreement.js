class MarketingAgreementManager {
	constructor() {
		this.elements = {
			/** '모두 동의하기' 체크박스 */
			consentAll: document.getElementById('consent-all'),

			/** 메인 동의 항목 (라디오 버튼 그룹)*/
			mainConsents: {
				collection: document.querySelectorAll('input[name="consent_collection"]'),
				marketing: document.querySelectorAll('input[name="consent_marketing"]'),
				sharing: document.querySelectorAll('input[name="consent_sharing"]'),
				lookup: document.querySelectorAll('input[name="consent_lookup"]'),
			},

			/** 마케팅 채널 상세 동의 항목 */
			marketingChannels: {
				all: document.querySelector('input[name="marketing-channel-all"]'),
				individuals: document.querySelectorAll('input[name^="channel_"]'),
			},
            personalInfoBtn: document.querySelector('.personal-info-using-guide'),
		};

		// this 바인딩
		this.handleConsentAllChange = this.handleConsentAllChange.bind(this);
		this.handleMarketingChannelAllChange = this.handleMarketingChannelAllChange.bind(this);
		this.handleIndividualChange = this.handleIndividualChange.bind(this);
		this.handleMarketingConsentChange = this.handleMarketingConsentChange.bind(this);
        this.openPersonalInfoGuide = this.openPersonalInfoGuide.bind(this);
	}

	// --- 헬퍼 함수들 ---

	updateRadioGroup(radioGroup, value) {
		radioGroup.forEach(radio => {
			radio.checked = (radio.value === value);
		});
	}

	updateCheckbox(checkbox, isChecked) {
		if (checkbox) {
			checkbox.checked = isChecked;
        }
	}

	// --- 메인 로직 ---
	
	/**
	 * 	각 상태값을 동기화하기
	 */
	syncConsentStatus() {
		const areAllChannelsChecked = [...this.elements.marketingChannels.individuals].every(cb => cb.checked);
		this.updateCheckbox(this.elements.marketingChannels.all, areAllChannelsChecked);

		const isAnyChannelChecked = [...this.elements.marketingChannels.individuals].some(cb => cb.checked);
		this.updateRadioGroup(this.elements.mainConsents.marketing, isAnyChannelChecked ? 'Y' : 'N');
		
		const areAllRadiosAgreed = Object.values(this.elements.mainConsents)
			.every(group => [...group].find(radio => radio.value === 'Y').checked);
		this.updateCheckbox(this.elements.consentAll, areAllRadiosAgreed);
	}


	// --- 이벤트 핸들러들 ---

	handleConsentAllChange(e) {
		const isChecked = e.target.checked;
		const targetValue = isChecked ? 'Y' : 'N';

		Object.values(this.elements.mainConsents).forEach(group => this.updateRadioGroup(group, targetValue));
		
		this.updateCheckbox(this.elements.marketingChannels.all, isChecked);
		this.elements.marketingChannels.individuals.forEach(cb => this.updateCheckbox(cb, isChecked));
	}
	
	handleMarketingConsentChange(e) {
        const selectedValue = e.target.value;

		// '동의(Y)'를 선택한 경우, 하위 모든 채널 체크박스 선택
		if (selectedValue === 'Y') {
			this.updateCheckbox(this.elements.marketingChannels.all, true);
			this.elements.marketingChannels.individuals.forEach(cb => this.updateCheckbox(cb, true));
		}
        // '비동의(N)'를 선택한 경우, 모든 하위 채널 체크박스를 해제
        if (selectedValue === 'N') {
            this.updateCheckbox(this.elements.marketingChannels.all, false);
            this.elements.marketingChannels.individuals.forEach(cb => this.updateCheckbox(cb, false));
        }

        // 작업 완료 후, 전체 상태를 최종 동기화
        this.syncConsentStatus();
    }
	
	handleMarketingChannelAllChange(e) {
		const isChecked = e.target.checked;
		this.elements.marketingChannels.individuals.forEach(cb => this.updateCheckbox(cb, isChecked));
		this.syncConsentStatus();
	}

	handleIndividualChange() {
		this.syncConsentStatus();
	}

    openPersonalInfoGuide() {
        window.open("https://www.samsungfire.com/util/P_U04_05_01_305.html", '_blank');
    }
	
	addEventListeners() {
		this.elements.consentAll.addEventListener('change', this.handleConsentAllChange);
		this.elements.marketingChannels.all.addEventListener('change', this.handleMarketingChannelAllChange);
	    this.elements.personalInfoBtn.addEventListener('click', this.openPersonalInfoGuide);

	    // 1. 일반 동의 항목들
		this.elements.mainConsents.collection.forEach(radio => radio.addEventListener('change', this.handleIndividualChange));
	    this.elements.mainConsents.sharing.forEach(radio => radio.addEventListener('change', this.handleIndividualChange));
	    this.elements.mainConsents.lookup.forEach(radio => radio.addEventListener('change', this.handleIndividualChange));

	    // 2. 광고성 동의 항목 (전용 핸들러 사용)
	    this.elements.mainConsents.marketing.forEach(radio => radio.addEventListener('change', this.handleMarketingConsentChange));

	    // 3. 개별 채널 체크박스
		this.elements.marketingChannels.individuals.forEach(checkbox => {
			checkbox.addEventListener('change', this.handleIndividualChange);
		});
	}

    init() {
        this.addEventListeners();
        this.syncConsentStatus(); 
    }
}

// --- 스크립트 실행 ---
const agreementForm = new MarketingAgreementManager();
agreementForm.init();