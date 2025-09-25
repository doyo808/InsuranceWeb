// js/buttons-module.js
export let buttonsData = [];

export function renderButtons(container) {
  container.innerHTML = '';
  buttonsData.forEach((btn, index) => {
    const div = document.createElement('div');
    div.className = 'entry-box';
    div.innerHTML = `
      <label>버튼 이름</label>
      <input type="text" value="${btn.name || ''}" onchange="importButtons().then(m => m.buttonsData[${index}].name = this.value)" />
      <button type="button" class="btn-outline" onclick="importButtons().then(m => { m.removeButton(${index}); })">삭제</button>
    `;
    container.appendChild(div);
  });
}

export function addButton(container) {
  buttonsData.push({ name: '' });
  renderButtons(container);
}

export function removeButton(index) {
  buttonsData.splice(index, 1);
  const container = document.getElementById('buttonsContainer');
  renderButtons(container);
}

window.importButtons = async () => await import('./buttons-module.js');
