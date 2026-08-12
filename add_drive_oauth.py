import re

with open("web/index.html", "r", encoding="utf-8") as f:
    content = f.read()

# Add button back
old_buttons = """        <button onclick="savePassLocal()" class="px-5 py-2.5 rounded-xl bg-slate-800 dark:bg-slate-700 text-white font-black text-xs shadow-md hover:bg-slate-900 transition flex items-center gap-2">
          <i class="fa-solid fa-download"></i> Save (JPG)
        </button>
      </div>"""
new_buttons = """        <button onclick="savePassLocal()" class="px-5 py-2.5 rounded-xl bg-slate-800 dark:bg-slate-700 text-white font-black text-xs shadow-md hover:bg-slate-900 transition flex items-center gap-2">
          <i class="fa-solid fa-download"></i> Save (JPG)
        </button>
        <button id="btn-save-drive" onclick="savePassToDrive()" class="px-5 py-2.5 rounded-xl bg-pup-gold text-pup-maroon font-black text-xs shadow-md hover:bg-yellow-500 transition flex items-center gap-2">
          <i class="fa-brands fa-google-drive"></i> Save to Drive
        </button>
      </div>"""
content = content.replace(old_buttons, new_buttons)

# Add js back
js_addon = """
    async function savePassToDrive() {
        const card = document.getElementById('printable-id-card');
        const name = document.getElementById('pass-card-name').innerText;
        const id = document.getElementById('pass-card-id').innerText;
        const sem = document.getElementById('pass-card-sem').innerText;
        
        const filename = formatFilename(name, id, sem);

        try {
            const dataUrl = await htmlToImage.toJpeg(card, { quality: 0.95, pixelRatio: 2 });
            alert("Saving " + filename + " to Google Drive via OAuth is being configured...");
        } catch (error) {
            console.error('Oops, something went wrong!', error);
            alert("Failed to process image for Drive.");
        }
    }
"""

content = content.replace("window.activeTab = 'roster';", "window.activeTab = 'roster';\n" + js_addon)

with open("web/index.html", "w", encoding="utf-8") as f:
    f.write(content)
