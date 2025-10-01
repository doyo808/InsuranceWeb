document.addEventListener("DOMContentLoaded", function() {
    const slides = document.querySelector('.slides');
    const slideCount = document.querySelectorAll('.slide').length;
    const prev = document.querySelector('.prev');
    const next = document.querySelector('.next');
    const counter = document.getElementById('counter');
    const pauseBtn = document.getElementById('pauseBtn');

    let index = 0;
    let autoSlide;
    let isPlaying = true;

    function showSlide(i) {
        index = (i + slideCount) % slideCount;
        slides.style.transform = `translateX(${-index * 100}%)`;
        counter.textContent = `${index + 1} / ${slideCount}`;
    }

    function startAutoSlide() {
        autoSlide = setInterval(() => showSlide(index + 1), 3000);
    }

    function stopAutoSlide() {
        clearInterval(autoSlide);
    }

    startAutoSlide();

    prev.addEventListener('click', () => showSlide(index - 1));
    next.addEventListener('click', () => showSlide(index + 1));

    pauseBtn.addEventListener('click', () => {
        if (isPlaying) {
            stopAutoSlide();
            pauseBtn.textContent = "▶";
        } else {
            startAutoSlide();
            pauseBtn.textContent = "⏸";
        }
        isPlaying = !isPlaying;
    });
});
