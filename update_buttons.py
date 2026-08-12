import re

with open("web/index.html", "r", encoding="utf-8") as f:
    content = f.read()

old_buttons = """      <!-- Action Buttons -->
      <div class="flex justify-end gap-3 border-t border-slate-200 dark:border-slate-700/60 pt-4">
        <button onclick="closeModal('modal-id-card')" class="px-5 py-2.5 rounded-xl text-slate-600 dark:text-slate-300 bg-slate-100 dark:bg-pup-darkCard font-bold text-xs hover:bg-slate-200 transition">
          Close
        </button>
        <button onclick="window.print()" class="px-6 py-2.5 rounded-xl bg-pup-gold text-pup-maroon font-black text-xs shadow-md hover:bg-yellow-500 transition flex items-center gap-2">
          <i class="fa-solid fa-print"></i> Print / Save Member Pass
        </button>
      </div>"""

new_buttons = """      <!-- Action Buttons -->
      <div class="flex justify-end gap-3 border-t border-slate-200 dark:border-slate-700/60 pt-4">
        <button onclick="closeModal('modal-id-card')" class="px-4 py-2.5 rounded-xl text-slate-600 dark:text-slate-300 bg-slate-100 dark:bg-pup-darkCard font-bold text-xs hover:bg-slate-200 transition">
          Close
        </button>
        <button onclick="savePassLocal()" class="px-5 py-2.5 rounded-xl bg-slate-800 dark:bg-slate-700 text-white font-black text-xs shadow-md hover:bg-slate-900 transition flex items-center gap-2">
          <i class="fa-solid fa-download"></i> Save (JPG)
        </button>
        <button id="btn-save-drive" onclick="savePassToDrive()" class="px-5 py-2.5 rounded-xl bg-pup-gold text-pup-maroon font-black text-xs shadow-md hover:bg-yellow-500 transition flex items-center gap-2">
          <i class="fa-brands fa-google-drive"></i> Save to Drive
        </button>
      </div>"""

content = content.replace(old_buttons, new_buttons)

js_drive = """
    async function savePassToDrive() {
        const card = document.getElementById('printable-id-card');
        const name = document.getElementById('pass-card-name').innerText;
        const id = document.getElementById('pass-card-id').innerText;
        const sem = document.getElementById('pass-card-sem').innerText;
        
        const filename = formatFilename(name, id, sem);

        try {
            const dataUrl = await htmlToImage.toJpeg(card, { quality: 0.95, pixelRatio: 2 });
            // For now, prompt the user that OAuth is required
            alert("Saving " + filename + " to Google Drive via OAuth is being configured...");
        } catch (error) {
            console.error('Oops, something went wrong!', error);
            alert("Failed to process image for Drive.");
        }
    }
"""
content = content.replace("window.activeTab = 'roster';", "window.activeTab = 'roster';\n" + js_drive)

with open("web/index.html", "w", encoding="utf-8") as f:
    f.write(content)

