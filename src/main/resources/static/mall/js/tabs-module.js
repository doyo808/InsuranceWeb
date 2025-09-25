// js/tabs-module.js
export let tabsData = [];

export function renderTabs(container) {
  container.innerHTML = '';
  tabsData.forEach((tab, index) => {
    const div = document.createElement('div');
    div.className = 'entry-box';
    div.innerHTML = `
      <label>탭 이름</label>
      <input type="text" value="${tab.name || ''}" onchange="importTabs().then(m => m.tabsData[${index}].name = this.value)" />
      <button type="button" class="btn-outline" onclick="importTabs().then(m => { m.removeTab(${index}); })">삭제</button>
    `;
    container.appendChild(div);
  });
}

export function addTab(container) {
  tabsData.push({ name: '' });
  renderTabs(container);
}

export function removeTab(index) {
  tabsData.splice(index, 1);
  const container = document.getElementById('tabsContainer');
  renderTabs(container);
}

// helper for event inline usage
window.importTabs = async () => await import('./tabs-module.js');
