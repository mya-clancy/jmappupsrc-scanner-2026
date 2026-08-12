import re

with open("web/index.html", "r", encoding="utf-8") as f:
    content = f.read()

old_css_bg = "background: linear-gradient(180deg, #3E0101 0%, #1A0000 100%);"
new_css_bg = "background: #FFFFFF;"
content = content.replace(old_css_bg, new_css_bg)

old_css_color = "color: #FFFFFF;"
new_css_color = "color: #0F172A;"
# Replace the first occurrence which is in .id-pass-card
content = content.replace(old_css_color, new_css_color, 1)

old_banner_bg = "background-color: #8C0000;"
new_banner_bg = "background-color: #FFFFFF;"
content = content.replace(old_banner_bg, new_banner_bg)

old_subtitle_color = "color: #FFFFFF;\n      text-transform: uppercase;\n      margin-top: 0.4cqw;"
new_subtitle_color = "color: #64748B;\n      text-transform: uppercase;\n      margin-top: 0.4cqw;"
content = content.replace(old_subtitle_color, new_subtitle_color)

old_label_color = "color: #D4AF37;"
new_label_color = "color: #0F172A;"
content = content.replace(old_label_color, new_label_color)

old_val_color = "color: #FFFFFF;\n      line-height: 1.2;"
new_val_color = "color: #334155;\n      line-height: 1.2;"
content = content.replace(old_val_color, new_val_color)

old_badge = """    .id-pass-badge {
      display: inline-flex;
      align-items: center;
      padding: 0.6cqw 1.8cqw;
      border-radius: 0.8cqw;
      border: 0.25cqw solid #10B981;
      background: transparent;
      color: #FFFFFF;"""
new_badge = """    .id-pass-badge {
      display: inline-flex;
      align-items: center;
      padding: 0.6cqw 1.8cqw;
      border-radius: 0.8cqw;
      border: 0.25cqw solid #10B981;
      background: rgba(16, 185, 129, 0.1);
      color: #10B981;"""
content = content.replace(old_badge, new_badge)

old_pending = """    .id-pass-badge.pending {
      border-color: #F59E0B;
      color: #FFFFFF;
    }"""
new_pending = """    .id-pass-badge.pending {
      border-color: #F59E0B;
      background: rgba(245, 158, 11, 0.1);
      color: #F59E0B;
    }"""
content = content.replace(old_pending, new_pending)

old_qr_size = """    .id-pass-qr-box canvas {
      width: 22cqw !important;
      height: 22cqw !important;
      display: block;
    }"""
new_qr_size = """    .id-pass-qr-box canvas {
      width: 32cqw !important;
      height: 32cqw !important;
      display: block;
    }"""
content = content.replace(old_qr_size, new_qr_size)

old_footer_css = """    .id-pass-footer {
      border-top: 1px solid rgba(255, 255, 255, 0.1);
      padding: 1.2cqw 4cqw;
      font-size: 1.3cqw;
      color: rgba(255, 255, 255, 0.6);"""
new_footer_css = """    .id-pass-footer {
      border-top: 1px solid rgba(0, 0, 0, 0.1);
      padding: 1.2cqw 4cqw;
      font-size: 1.3cqw;
      color: #64748B;"""
content = content.replace(old_footer_css, new_footer_css)

old_footer_text = "Polytechnic University of the Philippines • JMAP - PUP Santa Rosa Branch"
new_footer_text = "Junior Marketing Association of the Philippines - PUP Santa Rosa"
content = content.replace(old_footer_text, new_footer_text)

# Also update the QR code color to be Navy or keep it Maroon? The user said "line is color yellow text titles/headers are navy blue". It doesn't mention QR code color, but let's change it to Navy just in case, or leave it. I'll leave the QR color Maroon (#8C0000) as it matches the organization colors often. Wait, they said generally white, line yellow, titles navy.

with open("web/index.html", "w", encoding="utf-8") as f:
    f.write(content)

print("Updated ID styling to white theme.")
