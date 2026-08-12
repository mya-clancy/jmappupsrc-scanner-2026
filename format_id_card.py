import re

with open("web/index.html", "r", encoding="utf-8") as f:
    content = f.read()

# 1. Update ID Card wrapper to add a physical border
old_card = """    .id-pass-card {
      container-type: inline-size;
      width: 100%;
      max-width: 680px;
      aspect-ratio: 1.586 / 1;
      background: #FFFFFF;
      border-radius: 4cqw;
      box-shadow: 0 2cqw 5cqw rgba(0, 0, 0, 0.7);
      color: #0F172A;
      position: relative;
      overflow: hidden;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      box-sizing: border-box;
      margin: 0 auto;
      font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
    }"""
new_card = """    .id-pass-card {
      container-type: inline-size;
      width: 100%;
      max-width: 680px;
      aspect-ratio: 1.586 / 1;
      background: #FFFFFF;
      border-radius: 4cqw;
      border: 0.2cqw solid #000000;
      box-shadow: 0 2cqw 5cqw rgba(0, 0, 0, 0.7);
      color: #0F172A;
      position: relative;
      overflow: hidden;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      box-sizing: border-box;
      margin: 0 auto;
      font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
    }"""
content = content.replace(old_card, new_card)

# 2. Update Banner (Gold background, black border, navy text)
old_banner = """    .id-pass-header-banner {
      background-color: #FFFFFF;
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
      color: #64748B;
      text-transform: uppercase;
      margin-top: 0.4cqw;
    }"""
new_banner = """    .id-pass-header-banner {
      background-color: #FFC107;
      border-bottom: 0.6cqw solid #000000;
      padding: 2.5cqw 4cqw;
    }

    .id-pass-header-banner .title {
      font-size: 2.2cqw;
      font-weight: 800;
      letter-spacing: 0.02em;
      color: #0F172A;
      text-transform: uppercase;
    }
    
    .id-pass-header-banner .subtitle {
      font-size: 1.4cqw;
      font-weight: 600;
      letter-spacing: 0.05em;
      color: #0F172A;
      text-transform: uppercase;
      margin-top: 0.4cqw;
    }"""
content = content.replace(old_banner, new_banner)

# 3. Typography refinements for presentation
old_label = """    .id-pass-label {
      font-size: 1.4cqw;
      letter-spacing: 0.05em;
      color: #0F172A;
      font-weight: 500;
      text-transform: uppercase;
      margin-bottom: 0.4cqw;
    }"""
new_label = """    .id-pass-label {
      font-size: 1.3cqw;
      letter-spacing: 0.08em;
      color: #0F172A;
      font-weight: 800;
      text-transform: uppercase;
      margin-bottom: 0.4cqw;
      opacity: 0.7;
    }"""
content = content.replace(old_label, new_label)

old_val = """    .id-pass-val {
      font-size: 2.4cqw;
      font-weight: 500;
      color: #334155;
      line-height: 1.2;
    }"""
new_val = """    .id-pass-val {
      font-size: 2.6cqw;
      font-weight: 800;
      color: #0F172A;
      line-height: 1.2;
    }"""
content = content.replace(old_val, new_val)

# 4. QR Code Box (black border) and Label (navy text)
old_qr = """    .id-pass-qr-box {
      background: #FFFFFF;
      border: 0.4cqw solid #FFC107;
      border-radius: 1.5cqw;
      padding: 0.8cqw;
      box-shadow: 0 1cqw 3cqw rgba(0,0,0,0.5);
    }"""
new_qr = """    .id-pass-qr-box {
      background: #FFFFFF;
      border: 0.5cqw solid #000000;
      border-radius: 1.5cqw;
      padding: 0.8cqw;
    }"""
content = content.replace(old_qr, new_qr)

old_qr_label_css = """    .id-pass-qr-label {
      font-size: 1.4cqw;
      font-weight: 600;
      color: #FFC107;
      letter-spacing: 0.05em;
      text-transform: uppercase;
    }"""
new_qr_label_css = """    .id-pass-qr-label {
      font-size: 1.4cqw;
      font-weight: 800;
      color: #0F172A;
      letter-spacing: 0.05em;
      text-transform: uppercase;
    }"""
content = content.replace(old_qr_label_css, new_qr_label_css)

# 5. Footer (black top line)
old_footer = """    .id-pass-footer {
      border-top: 1px solid rgba(0, 0, 0, 0.1);
      padding: 1.2cqw 4cqw;
      font-size: 1.3cqw;
      color: #64748B;
      text-align: left;
      letter-spacing: 0.02em;
      margin-bottom: 0.5cqw;
    }"""
new_footer = """    .id-pass-footer {
      border-top: 0.6cqw solid #000000;
      padding: 1.5cqw 4cqw;
      font-size: 1.3cqw;
      color: #0F172A;
      font-weight: 600;
      text-align: center;
      letter-spacing: 0.05em;
      margin-bottom: 0;
      background-color: #FFFFFF;
    }"""
content = content.replace(old_footer, new_footer)

# 6. QRious init - make foreground black
old_qr_init = """      if (!qrGenerator) {
        qrGenerator = new QRious({
          element: canvas,
          size: 200,
          foreground: '#8C0000',
          background: '#FFFFFF',
          level: 'H'
        });
      }"""
new_qr_init = """      if (!qrGenerator) {
        qrGenerator = new QRious({
          element: canvas,
          size: 200,
          foreground: '#000000',
          background: '#FFFFFF',
          level: 'H'
        });
      }"""
content = content.replace(old_qr_init, new_qr_init)

with open("web/index.html", "w", encoding="utf-8") as f:
    f.write(content)

print("ID Card formatting applied.")
