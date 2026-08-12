import re

with open("web/index.html", "r", encoding="utf-8") as f:
    content = f.read()

old_id_css = """    .id-pass-card {
      container-type: inline-size;
      width: 100%;
      max-width: 680px;
      aspect-ratio: 1.586 / 1;
      background: linear-gradient(180deg, #2D0202 0%, #170101 100%);
      border-radius: 3cqw;
      box-shadow: 0 2cqw 5cqw rgba(0, 0, 0, 0.7);
      color: #FFFFFF;
      position: relative;
      overflow: hidden;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      box-sizing: border-box;
      margin: 0 auto;
    }

    .id-pass-header-banner {
      background-color: #800000;
      border-bottom: 0.5cqw solid #FFC107;
      padding: 2cqw 3.5cqw;
    }

    .id-pass-header-banner .title {
      font-size: 2.1cqw;
      font-weight: 900;
      letter-spacing: 0.05em;
      color: #FFC107;
      text-transform: uppercase;
    }
    
    .id-pass-header-banner .subtitle {
      font-size: 1.6cqw;
      font-weight: 700;
      letter-spacing: 0.08em;
      color: #FFFFFF;
      text-transform: uppercase;
      margin-top: 0.2cqw;
    }

    .id-pass-body {
      display: flex;
      align-items: center;
      justify-content: space-between;
      gap: 3.5cqw;
      flex: 1;
      padding: 2.6cqw 4cqw;
    }

    .id-pass-info {
      flex: 1;
      display: flex;
      flex-direction: column;
      justify-content: space-around;
      height: 100%;
    }

    .id-pass-label {
      font-size: 1.6cqw;
      letter-spacing: 0.08em;
      color: #D4AF37;
      font-weight: 700;
      text-transform: uppercase;
      margin-bottom: 0.3cqw;
    }

    .id-pass-val {
      font-size: 2.6cqw;
      font-weight: 700;
      color: #FFFFFF;
      line-height: 1.2;
    }

    .id-pass-badge {
      display: inline-flex;
      align-items: center;
      padding: 0.7cqw 2.3cqw;
      border-radius: 999px;
      border: 0.2cqw solid #10B981;
      background: rgba(16, 185, 129, 0.12);
      color: #34D399;
      font-size: 1.8cqw;
      font-weight: 700;
      width: fit-content;
      margin-top: 0.5cqw;
    }

    .id-pass-qr-frame {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      gap: 1.2cqw;
    }

    .id-pass-qr-box {
      background: #FFFFFF;
      border: 0.3cqw solid #FFC107;
      border-radius: 2.3cqw;
      padding: 1.7cqw;
      box-shadow: 0 1cqw 3cqw rgba(0,0,0,0.5);
    }

    .id-pass-qr-box canvas {
      width: 20cqw !important;
      height: 20cqw !important;
    }

    .id-pass-qr-label {
      font-size: 1.5cqw;
      font-weight: 800;
      color: #FFC107;
      letter-spacing: 0.08em;
      text-transform: uppercase;
    }

    .id-pass-footer {
      border-top: 1px solid rgba(255, 255, 255, 0.15);
      padding: 1.5cqw 3.5cqw;
      font-size: 1.5cqw;
      color: rgba(255, 255, 255, 0.75);
      text-align: center;
      letter-spacing: 0.02em;
    }"""

new_id_css = """    .id-pass-card {
      container-type: inline-size;
      width: 100%;
      max-width: 680px;
      aspect-ratio: 1.586 / 1;
      background: linear-gradient(180deg, #3E0101 0%, #1A0000 100%);
      border-radius: 4cqw;
      box-shadow: 0 2cqw 5cqw rgba(0, 0, 0, 0.7);
      color: #FFFFFF;
      position: relative;
      overflow: hidden;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      box-sizing: border-box;
      margin: 0 auto;
      font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
    }

    .id-pass-header-banner {
      background-color: #8C0000;
      border-bottom: 0.8cqw solid #FFC107;
      padding: 2.5cqw 4cqw;
    }

    .id-pass-header-banner .title {
      font-size: 2.2cqw;
      font-weight: 500;
      letter-spacing: 0.02em;
      color: #FFC107;
      text-transform: uppercase;
    }
    
    .id-pass-header-banner .subtitle {
      font-size: 1.6cqw;
      font-weight: 400;
      letter-spacing: 0.05em;
      color: #FFFFFF;
      text-transform: uppercase;
      margin-top: 0.4cqw;
    }

    .id-pass-body {
      display: flex;
      align-items: center;
      justify-content: space-between;
      gap: 3.5cqw;
      flex: 1;
      padding: 3cqw 4cqw 1cqw 4cqw;
    }

    .id-pass-info {
      flex: 1;
      display: flex;
      flex-direction: column;
      justify-content: space-around;
      height: 100%;
    }

    .id-pass-info > div {
      margin-bottom: 1.5cqw;
    }

    .id-pass-label {
      font-size: 1.4cqw;
      letter-spacing: 0.05em;
      color: #D4AF37;
      font-weight: 500;
      text-transform: uppercase;
      margin-bottom: 0.4cqw;
    }

    .id-pass-val {
      font-size: 2.4cqw;
      font-weight: 500;
      color: #FFFFFF;
      line-height: 1.2;
    }

    .id-pass-badge {
      display: inline-flex;
      align-items: center;
      padding: 0.6cqw 1.8cqw;
      border-radius: 0.8cqw;
      border: 0.25cqw solid #10B981;
      background: transparent;
      color: #FFFFFF;
      font-size: 1.8cqw;
      font-weight: 500;
      width: fit-content;
      margin-top: 0.2cqw;
    }

    .id-pass-badge.pending {
      border-color: #F59E0B;
      color: #FFFFFF;
    }

    .id-pass-qr-frame {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      gap: 1.5cqw;
      align-self: flex-start;
      margin-top: 1cqw;
    }

    .id-pass-qr-box {
      background: #FFFFFF;
      border: 0.4cqw solid #FFC107;
      border-radius: 1.5cqw;
      padding: 0.8cqw;
      box-shadow: 0 1cqw 3cqw rgba(0,0,0,0.5);
    }

    .id-pass-qr-box canvas {
      width: 22cqw !important;
      height: 22cqw !important;
      display: block;
    }

    .id-pass-qr-label {
      font-size: 1.4cqw;
      font-weight: 600;
      color: #FFC107;
      letter-spacing: 0.05em;
      text-transform: uppercase;
    }

    .id-pass-footer {
      border-top: 1px solid rgba(255, 255, 255, 0.1);
      padding: 1.2cqw 4cqw;
      font-size: 1.3cqw;
      color: rgba(255, 255, 255, 0.6);
      text-align: left;
      letter-spacing: 0.02em;
      margin-bottom: 0.5cqw;
    }"""

if old_id_css in content:
    content = content.replace(old_id_css, new_id_css)
else:
    print("CSS block not found!")

# Now update the JS that sets the badge so it removes the bg class
old_js = """    function viewPassCard(student) {
      document.getElementById('pass-card-name').innerText = student.name;
      document.getElementById('pass-card-id').innerText = student.studentId || student.docId;
      document.getElementById('pass-card-dept').innerText = student.department || 'N/A';
      
      const badgeEl = document.getElementById('pass-card-badge');
      if (student.isMembershipPaid) {
        badgeEl.className = "id-pass-badge";
        badgeEl.innerHTML = `<span id="pass-card-sem">${student.activeSemester || '2026-2027 | 1st Semester'}</span> • VALID MEMBER`;
      } else {
        badgeEl.className = "id-pass-badge border-amber-500 bg-amber-500/20 text-amber-300";
        badgeEl.innerHTML = `<span id="pass-card-sem">${student.activeSemester || '2026-2027 | 1st Semester'}</span> • PENDING DUES`;
      }"""

new_js = """    function viewPassCard(student) {
      document.getElementById('pass-card-name').innerText = student.name;
      document.getElementById('pass-card-id').innerText = student.studentId || student.docId;
      document.getElementById('pass-card-dept').innerText = student.department || 'N/A';
      
      const badgeEl = document.getElementById('pass-card-badge');
      if (student.isMembershipPaid) {
        badgeEl.className = "id-pass-badge";
        badgeEl.innerHTML = `<span id="pass-card-sem">${student.activeSemester || '2026-2027 | 1st Semester'}</span> • VALID MEMBER`;
      } else {
        badgeEl.className = "id-pass-badge pending";
        badgeEl.innerHTML = `<span id="pass-card-sem">${student.activeSemester || '2026-2027 | 1st Semester'}</span> • PENDING DUES`;
      }"""

if old_js in content:
    content = content.replace(old_js, new_js)
else:
    print("JS badge update block not found!")

# Let's adjust the QR Code foreground color (it was #800000, we'll keep it or maybe #8C0000 to match banner).
old_qr_init = """      if (!qrGenerator) {
        qrGenerator = new QRious({
          element: canvas,
          size: 200,
          foreground: '#800000',
          background: '#FFFFFF',
          level: 'H'
        });
      }"""

new_qr_init = """      if (!qrGenerator) {
        qrGenerator = new QRious({
          element: canvas,
          size: 200,
          foreground: '#8C0000',
          background: '#FFFFFF',
          level: 'H'
        });
      }"""
content = content.replace(old_qr_init, new_qr_init)

with open("web/index.html", "w", encoding="utf-8") as f:
    f.write(content)

print("Updated ID Card styling")
