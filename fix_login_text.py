import re

with open("web/index.html", "r", encoding="utf-8") as f:
    content = f.read()

# 1. Change text
content = content.replace("Partner Cashier", "Business Partners")

# 2. Remove Drive button
button_pattern = r'\s*<button id="btn-save-drive" onclick="savePassToDrive\(\)".*?</button>'
content = re.sub(button_pattern, '', content, flags=re.DOTALL)

# 3. Remove Drive JS
js_pattern = r'\s*async function savePassToDrive\(\) \{.*?\n    \}'
content = re.sub(js_pattern, '', content, flags=re.DOTALL)

with open("web/index.html", "w", encoding="utf-8") as f:
    f.write(content)

print("Updated login text and removed drive button")
