import re

with open("web/index.html", "r", encoding="utf-8") as f:
    content = f.read()

old_save_local = """    async function savePassLocal() {
        const card = document.getElementById('printable-id-card');
        const name = document.getElementById('pass-card-name').innerText;
        const id = document.getElementById('pass-card-id').innerText;
        const sem = document.getElementById('pass-card-sem').innerText;
        
        const filename = formatFilename(name, id, sem);

        try {
            const dataUrl = await htmlToImage.toJpeg(card, { quality: 0.95, pixelRatio: 2 });
            const link = document.createElement('a');
            link.download = filename;
            link.href = dataUrl;
            link.click();
        } catch (error) {
            console.error('Oops, something went wrong!', error);
            alert("Failed to save image. Please try again.");
        }
    }"""
    
new_save_local = """    let isSaving = false;
    async function savePassLocal() {
        if (isSaving) return;
        isSaving = true;
        const card = document.getElementById('printable-id-card');
        const name = document.getElementById('pass-card-name').innerText;
        const id = document.getElementById('pass-card-id').innerText;
        const sem = document.getElementById('pass-card-sem').innerText;
        
        const filename = formatFilename(name, id, sem);

        try {
            const dataUrl = await htmlToImage.toJpeg(card, { quality: 0.95, pixelRatio: 2 });
            const link = document.createElement('a');
            link.download = filename;
            link.href = dataUrl;
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
        } catch (error) {
            console.error('Oops, something went wrong!', error);
            alert("Failed to save image. Please try again.");
        } finally {
            setTimeout(() => { isSaving = false; }, 1000);
        }
    }"""
    
content = content.replace(old_save_local, new_save_local)

with open("web/index.html", "w", encoding="utf-8") as f:
    f.write(content)

