import re

with open("web/index.html", "r", encoding="utf-8") as f:
    content = f.read()

old_js = """      const badgeEl = document.getElementById('pass-card-badge');
      if (student.isMembershipPaid) {
        badgeEl.className = "id-pass-badge";
        badgeEl.innerHTML = `<span id="pass-card-sem">${student.activeSemester || '2026-2027 | 1st Semester'}</span> • VALID MEMBER`;
      } else {
        badgeEl.className = "id-pass-badge border-amber-500 bg-amber-500/20 text-amber-300";
        badgeEl.innerHTML = `<span id="pass-card-sem">${student.activeSemester || '2026-2027 | 1st Semester'}</span> • PENDING DUES`;
      }"""

new_js = """      const badgeEl = document.getElementById('pass-card-badge');
      if (student.isMembershipPaid) {
        badgeEl.className = "id-pass-badge";
        badgeEl.innerHTML = `<span id="pass-card-sem">${student.activeSemester || '2026-2027 | 1st Semester'}</span> • VALID MEMBER`;
      } else {
        badgeEl.className = "id-pass-badge pending";
        badgeEl.innerHTML = `<span id="pass-card-sem">${student.activeSemester || '2026-2027 | 1st Semester'}</span> • PENDING DUES`;
      }"""

if old_js in content:
    content = content.replace(old_js, new_js)
    print("Fixed JS badge update.")
else:
    print("JS not found")

with open("web/index.html", "w", encoding="utf-8") as f:
    f.write(content)
