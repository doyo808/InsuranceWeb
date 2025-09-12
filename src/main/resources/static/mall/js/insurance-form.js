// js/insurance-form.js
import { tabsData, renderTabs, addTab } from './tabs-module.js';
import { sectionsData, renderSections, addSection } from './sections-module.js';
import { buttonsData, renderButtons, addButton } from './buttons-module.js';

document.addEventListener('DOMContentLoaded', () => {
  // 초기 데이터 세팅 (Thymeleaf로 주입된 전역 변수 사용)
  try { Object.assign(tabsData, JSON.parse(initialTabs || '[]')); } catch {}
  try { Object.assign(sectionsData, JSON.parse(initialSections || '[]')); } catch {}
  try { Object.assign(buttonsData, JSON.parse(initialButtons || '[]')); } catch {}

  renderTabs(document.getElementById('tabsContainer'));
  renderSections(document.getElementById('sectionsContainer'));
  renderButtons(document.getElementById('buttonsContainer'));

  // 버튼 핸들링
  window.addTab = () => addTab(document.getElementById('tabsContainer'));
  window.addSection = () => addSection(document.getElementById('sectionsContainer'));
  window.addButton = () => addButton(document.getElementById('buttonsContainer'));

  // 폼 제출 시 JSON 직렬화
  document.querySelector('form').addEventListener('submit', function (e) {
    try {
      document.getElementById('tabs_json').value = JSON.stringify(tabsData);
      document.getElementById('sections_json').value = JSON.stringify(sectionsData);
      document.getElementById('bottom_buttons_json').value = JSON.stringify(buttonsData);
    } catch (err) {
      e.preventDefault();
      alert('JSON 직렬화 중 오류 발생:\n' + err.message);
    }
  });
});
