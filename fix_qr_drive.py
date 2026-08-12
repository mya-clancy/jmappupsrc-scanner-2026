import re

with open("web/index.html", "r", encoding="utf-8") as f:
    content = f.read()

# 1. Update id-pass-qr-box CSS
old_qr_css = """    .id-pass-qr-box {
      background: #FFFFFF;
      border: 0.2cqw solid #000000;
      border-radius: 1.5cqw;
      padding: 0.8cqw;
    }"""
new_qr_css = """    .id-pass-qr-box {
      background: #FFFFFF;
      border: 0.2cqw solid #000000;
      border-radius: 1.5cqw;
      padding: 0.8cqw;
      position: relative;
      display: flex;
      align-items: center;
      justify-content: center;
    }"""
content = content.replace(old_qr_css, new_qr_css)

# 2. Update QR HTML
old_qr_html = """            <div class="id-pass-qr-box">
              <canvas id="pass-card-qr" class="w-36 h-36"></canvas>
            </div>"""
new_qr_html = """            <div class="id-pass-qr-box">
              <canvas id="pass-card-qr" class="w-36 h-36"></canvas>
              <div class="absolute inset-0 flex items-center justify-center pointer-events-none">
                <div class="bg-white p-1 flex items-center justify-center" style="width: 28%; height: 28%;">
                  <span class="font-extrabold text-[#0F172A]" style="font-size: 2cqw;">JMAP</span>
                </div>
              </div>
            </div>"""
content = content.replace(old_qr_html, new_qr_html)

# 3. Remove the broken JS canvas drawing logic
js_to_remove = """      // Draw JMAP logo in the center of the QR code
      const ctx = canvas.getContext('2d');
      const size = canvas.width;
      const center = size / 2;
      const boxSize = size * 0.25; // 25% of QR code size
      
      // White box background
      ctx.fillStyle = '#FFFFFF';
      ctx.fillRect(center - boxSize/2, center - boxSize/2, boxSize, boxSize);
      
      // Black JMAP text
      ctx.fillStyle = '#0F172A';
      ctx.font = `bold ${size * 0.08}px Arial`;
      ctx.textAlign = 'center';
      ctx.textBaseline = 'middle';
      ctx.fillText('JMAP', center, center);"""
content = content.replace(js_to_remove, "")

# 4. Restore the Drive button HTML
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

# 5. Restore Drive JS
drive_js = """
    async function savePassToDrive() {
        const card = document.getElementById('printable-id-card');
        const name = document.getElementById('pass-card-name').innerText;
        const id = document.getElementById('pass-card-id').innerText;
        const sem = document.getElementById('pass-card-sem').innerText;
        
        const filename = formatFilename(name, id, sem);

        try {
            const dataUrl = await htmlToImage.toJpeg(card, { quality: 0.95, pixelRatio: 2 });
            alert("OAuth integration in progress to save: " + filename);
        } catch (error) {
            console.error('Oops, something went wrong!', error);
            alert("Failed to process image for Drive.");
        }
    }
"""
content = content.replace("window.activeTab = 'roster';", "window.activeTab = 'roster';\n" + drive_js)

with open("web/index.html", "w", encoding="utf-8") as f:
    f.write(content)

print("Updated QR centering and added Drive button back.")
