document.querySelectorAll('.contract-row').forEach(row => {
    row.addEventListener('click', () => {
        const contract_id = row.dataset.contractId;
        window.location.href = `${contextPath}mypage/MPDG0080/${contract_id}`;
    });
});