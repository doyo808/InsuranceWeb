class MarketingAgreementManager {
	constructor() {
		this.elements = {
			/** '모두 동의하기' 체크박스 */
			consentAll: document.getElementById('consent-all'),

			/** 메인 동의 항목 (라디오 버튼 그룹)*/
			mainConsents: {
				collection: document.querySelectorAll('input[name="consent-for-collection"]'),
				marketing: document.querySelectorAll('input[name="consent-for-marketing"]'),
				sharing: document.querySelectorAll('input[name="consent-for-sharing"]'),
				lookup: document.querySelectorAll('input[name="consent-for-lookup"]'),
			},

			/** 마케팅 채널 상세 동의 항목 */
			marketingChannels: {
				all: document.querySelector('input[name="marketing-channel-all"]'),
				individuals: document.querySelectorAll('input[name^="marketing-channel-"]:not([name$="-all"])'),
			},
		};

		// this 바인딩
		this.handleConsentAllChange = this.handleConsentAllChange.bind(this);
		this.handleMarketingChannelAllChange = this.handleMarketingChannelAllChange.bind(this);
		this.handleIndividualChange = this.handleIndividualChange.bind(this);
	}

	// --- 헬퍼 함수들 ---

	/** [헬퍼] 라디오 그룹 업데이트 */
	updateRadioGroup(radioGroup, value) {
		radioGroup.forEach(radio => {
			radio.checked = (radio.value === value);
		});
	}

	/** [헬퍼] 단일 체크박스 업데이트 */
	updateCheckbox(checkbox, isChecked) {
		checkbox.checked = isChecked;
	}

	// --- 메인 로직 ---

	/**
	 * [핵심] 모든 동의 항목의 현재 상태를 기반으로 UI를 동기화하는 함수
	 */
	syncConsentStatus() {
		// 1. 마케팅 개별 채널 -> 마케팅 '전체' 체크박스 동기화
		const areAllChannelsChecked = [...this.elements.marketingChannels.individuals].every(cb => cb.checked);
		this.updateCheckbox(this.elements.marketingChannels.all, areAllChannelsChecked);

		// 2. 마케팅 개별 채널 -> 마케팅 '동의' 라디오 버튼 동기화
		const isAnyChannelChecked = [...this.elements.marketingChannels.individuals].some(cb => cb.checked);
		this.updateRadioGroup(this.elements.mainConsents.marketing, isAnyChannelChecked ? 'true' : 'false');
		
		// 3. 모든 라디오 버튼 -> '모두 동의하기' 체크박스 동기화
		const areAllRadiosAgreed = Object.values(this.elements.mainConsents)
			.every(group => [...group].find(radio => radio.value === 'true').checked);
		this.updateCheckbox(this.elements.consentAll, areAllRadiosAgreed);
	}


	// --- 이벤트 핸들러들 ---

	/**
	 * '모두 동의하기' 클릭 시: 모든 하위 항목을 제어
	 */
	handleConsentAllChange(e) {
		const isChecked = e.target.checked;
		const targetValue = isChecked ? 'true' : 'false';

		Object.values(this.elements.mainConsents).forEach(group => this.updateRadioGroup(group, targetValue));
		
		this.updateCheckbox(this.elements.marketingChannels.all, isChecked);
		this.elements.marketingChannels.individuals.forEach(cb => this.updateCheckbox(cb, isChecked));
	}
	
	/**
	 * '마케팅 채널 전체' 클릭 시: 하위 채널들과 마케팅 라디오를 제어
	 */
	handleMarketingChannelAllChange(e) {
		const isChecked = e.target.checked;
		
		this.elements.marketingChannels.individuals.forEach(cb => this.updateCheckbox(cb, isChecked));
		
		// 변경 후, 전체 상태를 다시 동기화
		this.syncConsentStatus();
	}

	/**
	 * 개별 항목 클릭 시: 전체 상태를 다시 계산하여 동기화
	 */
	handleIndividualChange() {
		this.syncConsentStatus();
	}
	
	/**
	 * 모든 이벤트 리스너를 등록하는 함수
	 */
	addEventListeners() {
		// 1. 마스터 컨트롤: '모두 동의하기' 체크박스
		this.elements.consentAll.addEventListener('change', this.handleConsentAllChange);
		
		// 2. 서브 컨트롤: '마케팅 채널 전체' 체크박스
		this.elements.marketingChannels.all.addEventListener('change', this.handleMarketingChannelAllChange);

		// 3. 개별 항목들: 라디오 버튼과 개별 채널 체크박스
		Object.values(this.elements.mainConsents).forEach(group => {
			group.forEach(radio => radio.addEventListener('change', this.handleIndividualChange));
		});

		this.elements.marketingChannels.individuals.forEach(checkbox => {
			checkbox.addEventListener('change', this.handleIndividualChange);
		});
	}
}

// --- 스크립트 실행 ---
const agreementForm = new MarketingAgreementManager();
agreementForm.addEventListeners();


// 페이지 최하단 개인정보처리방침 링크
const personalInfoUsingGuideBtn = document.querySelector('.personal-info-using-guide');
personalInfoUsingGuideBtn.addEventListener('click', () => {
	window.open("https://www.samsungfire.com/util/P_U04_05_01_305.html", '_blank');
});