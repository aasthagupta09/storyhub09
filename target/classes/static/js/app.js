/* StoryHub front-end: live search, tiny Markdown renderer, editor toolbar. Vanilla JS, no libraries. */

// ---------- 1. Live search dropdown (calls /api/search as you type) ----------
const box = document.getElementById('searchBox');
const list = document.getElementById('suggest');
let timer;
if (box) {
  box.addEventListener('input', () => {
    clearTimeout(timer);                       // debounce: wait 250ms after the last keystroke
    timer = setTimeout(async () => {
      const q = box.value.trim();
      if (!q) { list.style.display = 'none'; return; }
      const res = await fetch('/api/search?q=' + encodeURIComponent(q));
      const hits = await res.json();
      list.innerHTML = '';
      hits.forEach(h => {
        const a = document.createElement('a');
        a.href = '/stories/' + h.id;
        a.textContent = h.title;                 // textContent = safe from HTML injection
        const small = document.createElement('small');
        small.textContent = 'by ' + h.author + ' · ' + h.category;
        a.appendChild(small);
        list.appendChild(a);
      });
      list.style.display = hits.length ? 'block' : 'none';
    }, 250);
  });
  document.addEventListener('click', e => { if (!e.target.closest('.search')) list.style.display = 'none'; });
}

// ---------- 2. Markdown-lite renderer: # heading, ## subheading, **bold**, *italic* ----------
function render(text) {
  // Escape HTML FIRST so user text can never inject scripts, then add our own tags.
  const safe = text.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
  return safe.split(/\n{2,}/).map(block => {
    block = block.trim();
    if (block.startsWith('## ')) return '<h3>' + inline(block.slice(3)) + '</h3>';
    if (block.startsWith('# '))  return '<h2>' + inline(block.slice(2)) + '</h2>';
    return '<p>' + inline(block).replace(/\n/g, '<br>') + '</p>';
  }).join('');
}
function inline(s) {
  return s.replace(/\*\*(.+?)\*\*/g, '<strong>$1</strong>').replace(/\*(.+?)\*/g, '<em>$1</em>');
}

// Story page: turn the hidden raw text into formatted reading text
const raw = document.getElementById('rawContent');
const body = document.getElementById('storyBody');
if (raw && body) body.innerHTML = render(raw.textContent);

// ---------- 3. Editor: toolbar, word count, preview ----------
const editor = document.getElementById('content');
if (editor) {
  document.querySelectorAll('[data-wrap]').forEach(btn => btn.addEventListener('click', () => {
    const [before, after] = btn.dataset.wrap.split('|');
    const s = editor.selectionStart, e = editor.selectionEnd;
    editor.setRangeText(before + editor.value.slice(s, e) + after, s, e, 'end');
    editor.focus(); count();
  }));
  const counter = document.getElementById('wordCount');
  const count = () => counter.textContent = (editor.value.trim() ? editor.value.trim().split(/\s+/).length : 0) + ' words';
  editor.addEventListener('input', count); count();

  const preview = document.getElementById('preview');
  document.getElementById('previewBtn').addEventListener('click', () => {
    const open = preview.style.display === 'block';
    preview.innerHTML = render(editor.value);
    preview.style.display = open ? 'none' : 'block';
  });
}
