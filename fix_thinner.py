import re

with open("web/index.html", "r", encoding="utf-8") as f:
    content = f.read()

# Make card border thinner
content = content.replace("border: 0.2cqw solid #000000;", "border: 0.1cqw solid #000000;")
content = content.replace("border-bottom: 0.6cqw solid #000000;", "border-bottom: 0.3cqw solid #000000;")
content = content.replace("border: 0.5cqw solid #000000;", "border: 0.2cqw solid #000000;")
content = content.replace("border-top: 0.6cqw solid #000000;", "border-top: 0.3cqw solid #000000;")

# Update JS to draw JMAP on the QR Code
old_js = """      qrGenerator.value = JSON.stringify({ studentId: student.studentId || student.docId });

      openModal('modal-id-card');"""
      
new_js = """      qrGenerator.value = JSON.stringify({ studentId: student.studentId || student.docId });
      
      // Draw JMAP logo in the center of the QR code
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
      ctx.fillText('JMAP', center, center);

      openModal('modal-id-card');"""
      
content = content.replace(old_js, new_js)

with open("web/index.html", "w", encoding="utf-8") as f:
    f.write(content)
