import re

with open("web/index.html", "r", encoding="utf-8") as f:
    content = f.read()

# Inject GSI script
old_script = '<script src="https://cdnjs.cloudflare.com/ajax/libs/html-to-image/1.11.11/html-to-image.min.js"></script>'
new_script = old_script + '\n  <script src="https://accounts.google.com/gsi/client" async defer></script>'
if "accounts.google.com/gsi/client" not in content:
    content = content.replace(old_script, new_script)

# Replace savePassToDrive
old_func_pattern = r'\s*async function savePassToDrive\(\) \{.*?\n    \}'
new_func = """
    let tokenClient;
    
    function dataURLtoBlob(dataurl) {
        var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1],
            bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
        while(n--){
            u8arr[n] = bstr.charCodeAt(n);
        }
        return new Blob([u8arr], {type:mime});
    }

    async function savePassToDrive() {
        const card = document.getElementById('printable-id-card');
        const name = document.getElementById('pass-card-name').innerText;
        const id = document.getElementById('pass-card-id').innerText;
        const sem = document.getElementById('pass-card-sem').innerText;
        
        const filename = formatFilename(name, id, sem);
        const btn = document.getElementById('btn-save-drive');
        const originalText = btn.innerHTML;
        
        const callback = async (response) => {
            if (response.error !== undefined) {
                console.error('OAuth error:', response);
                alert("Authorization failed.");
                btn.innerHTML = originalText;
                return;
            }
            
            btn.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Saving...';
            
            try {
                const dataUrl = await htmlToImage.toJpeg(card, { quality: 0.95, pixelRatio: 2 });
                const blob = dataURLtoBlob(dataUrl);
                
                const metadata = {
                  name: filename,
                  mimeType: 'image/jpeg'
                };
                const formData = new FormData();
                formData.append('metadata', new Blob([JSON.stringify(metadata)], { type: 'application/json' }));
                formData.append('file', blob);
                
                const res = await fetch('https://www.googleapis.com/upload/drive/v3/files?uploadType=multipart', {
                  method: 'POST',
                  headers: {
                    'Authorization': 'Bearer ' + response.access_token
                  },
                  body: formData
                });
                
                if (res.ok) {
                    alert("Successfully saved to Google Drive!");
                } else {
                    console.error("Drive upload error", await res.text());
                    alert("Failed to upload to Google Drive.");
                }
            } catch (error) {
                console.error('Oops, something went wrong!', error);
                alert("Failed to process image for Drive.");
            } finally {
                btn.innerHTML = originalText;
            }
        };

        if (!tokenClient) {
            tokenClient = google.accounts.oauth2.initTokenClient({
                client_id: '464926241291-buato49j1obe1ol56hh8hctpb7jks6ui.apps.googleusercontent.com',
                scope: 'https://www.googleapis.com/auth/drive.file',
                callback: callback,
            });
        }
        
        tokenClient.requestAccessToken({prompt: 'consent'});
    }
"""

content = re.sub(old_func_pattern, new_func, content, flags=re.DOTALL)

with open("web/index.html", "w", encoding="utf-8") as f:
    f.write(content)

print("Google Drive upload logic implemented.")
