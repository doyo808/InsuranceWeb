// js/sections-module.js
export let sectionsData = [];

export function renderSections(container) {
  container.innerHTML = '';
  sectionsData.forEach((section, index) => {
    const div = document.createElement('div');
    div.className = 'entry-box';
    div.innerHTML = `
      <label>제목</label>
      <input type="text" value="${section.title || ''}" onchange="importSections().then(m => m.sectionsData[${index}].title = this.value)" />
      <label>아이템</label>
      <input type="text" value="${section.items || ''}" onchange="importSections().then(m => m.sectionsData[${index}].items = this.value)" />
      <label>노트</label>
      <input type="text" value="${section.note || ''}" onchange="importSections().then(m => m.sectionsData[${index}].note = this.value)" />
      <label>이미지 URL</label>
      <input type="text" value="${section.image || ''}" onchange="importSections().then(m => m.sectionsData[${index}].image = this.value)" />
      <button type="button" class="btn-outline" onclick="importSections().then(m => { m.removeSection(${index}); })">삭제</button>
    `;
    container.appendChild(div);
  });
}

export function addSection(container) {
  sectionsData.push({ title: '', items: '', note: '', image: '' });
  renderSections(container);
}

export function removeSection(index) {
  sectionsData.splice(index, 1);
  const container = document.getElementById('sectionsContainer');
  renderSections(container);
}

window.importSections = async () => await import('./sections-module.js');
