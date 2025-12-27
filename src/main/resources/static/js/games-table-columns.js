(function(){
  const STORAGE_KEY = 'gamesTableHiddenCols';

  function setColumnVisibility(colKey, visible){
    // header cells
    document.querySelectorAll('[data-col="'+colKey+'"]').forEach(el=>{
      if(visible) el.classList.remove('hidden-col'); else el.classList.add('hidden-col');
    });
  }

  function applyState(hiddenCols){
    // find all checkboxes and apply state
    document.querySelectorAll('#games-columns-list input[type="checkbox"][data-col]').forEach(cb=>{
      const col = cb.getAttribute('data-col');
      const hidden = hiddenCols.includes(col);
      cb.checked = !hidden;
      setColumnVisibility(col, !hidden);
    });
  }

  function saveState(hiddenCols){
    localStorage.setItem(STORAGE_KEY, JSON.stringify(hiddenCols));
  }

  function loadState(){
    try{
      const raw = localStorage.getItem(STORAGE_KEY);
      return raw ? JSON.parse(raw) : [];
    }catch(e){ return []; }
  }

  document.addEventListener('DOMContentLoaded', ()=>{
    const list = document.getElementById('games-columns-list');
    if(!list) return;

    // initialize from storage
    const hiddenCols = loadState();
    applyState(hiddenCols);

    // ensure chooser visibility/aria reflects DOM (show by default)
    // if aria-hidden missing or false, show; if true, hide.
    if(list.getAttribute('aria-hidden') === 'true'){
      list.style.display = 'none';
    } else {
      list.style.display = 'flex';
    }

    // checkbox toggles
    list.addEventListener('change', (e)=>{
      const input = e.target;
      if(!input || !input.matches('input[type="checkbox"][data-col]')) return;
      const col = input.getAttribute('data-col');
      const visible = input.checked;
      setColumnVisibility(col, visible);

      // update storage
      const currentHidden = loadState();
      const idx = currentHidden.indexOf(col);
      if(!visible && idx === -1){
        currentHidden.push(col);
      }else if(visible && idx !== -1){
        currentHidden.splice(idx,1);
      }
      saveState(currentHidden);
    });

    // reset button
    const resetBtn = document.getElementById('games-columns-reset');
    if(resetBtn){
      resetBtn.addEventListener('click', ()=>{
        // unhide all
        const allCols = Array.from(document.querySelectorAll('#games-columns-list input[data-col]'))
                          .map(i=>i.getAttribute('data-col'));
        saveState([]); // clear
        applyState([]); // show all
        // set checkboxes checked
        document.querySelectorAll('#games-columns-list input[data-col]').forEach(cb=>cb.checked = true);
      });
    }

    // optional: allow toggling chooser visibility on small screens
    const chooserBtn = document.querySelector('.columns-chooser .chooser-btn');
    if(chooserBtn){
      chooserBtn.addEventListener('click', ()=> {
        const hidden = list.getAttribute('aria-hidden') === 'true';
        list.setAttribute('aria-hidden', hidden ? 'false' : 'true');
        list.style.display = hidden ? 'flex' : 'none';
        chooserBtn.setAttribute('aria-expanded', hidden ? 'true' : 'false');
      });
    }
  });
})();
